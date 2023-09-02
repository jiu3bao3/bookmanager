////////////////////////////////////////////////////////////////////////////////////////////////////
package com.jyis.bookmanager.ndl;
////////////////////////////////////////////////////////////////////////////////////////////////////
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.sql.DataSource;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Component;

import com.jyis.bookmanager.AbstractDao;
import com.jyis.bookmanager.AbstractForm;
import com.jyis.bookmanager.books.Book;
////////////////////////////////////////////////////////////////////////////////////////////////////
/**
 * JobのDAO
 * @author 久保　由仁
 */
@Component
public class JobDao extends AbstractDao<JobHistory>
{
    /** ロガー */
    private static final Logger logger = LoggerFactory.getLogger(JobDao.class);

    /** SQL Server用のSpring Batchのジョブ管理テーブルスキーマ定義 */
    private static final String CREATE_SQL = "org/springframework/batch/core/schema-sqlserver.sql";

    /** SQL Server用のSpring Batchのジョブ管理テーブルスキーマ定義削除スクリプト */
    private static final String DROP_SQL = 
                                    "org/springframework/batch/core/schema-drop-sqlserver.sql";
    //---------------------------------------------------------------------------------------------
    public void insert(JobHistory arg) { throw new UnsupportedOperationException(); }
    public void delete(JobHistory arg) { throw new UnsupportedOperationException(); }
    public void update(JobHistory arg) { throw new UnsupportedOperationException(); }
    public JobHistory selectOne(int arg) { throw new UnsupportedOperationException(); }
    //---------------------------------------------------------------------------------------------
    /**
     * ジョブ一覧取得
     * @param form ダミー
     * @return ジョブ履歴のList
     */
    public List<JobHistory> selectAll(AbstractForm form)
    { 
        final String SQL = "SELECT JOB_INSTANCE_ID, CREATE_TIME, START_TIME, END_TIME, STATUS, "
                        + "EXIT_MESSAGE FROM BATCH_JOB_EXECUTION ORDER BY CREATE_TIME DESC";
        List<JobHistory> list = new ArrayList<>();
        try(Connection con = open();
            Statement stmt = con.createStatement();
            ResultSet result = stmt.executeQuery(SQL))
        {
            while(result.next())
            {
                JobHistory jobHistory = new JobHistory();
                jobHistory.setJobInstanceId(result.getLong("JOB_INSTANCE_ID"));
                jobHistory.setCreateTime(result.getTimestamp("CREATE_TIME"));
                jobHistory.setStartTime(result.getTimestamp("START_TIME"));
                jobHistory.setEndTime(result.getTimestamp("END_TIME"));
                jobHistory.setStatus(result.getString("STATUS"));
                jobHistory.setExitMessage(result.getString("EXIT_MESSAGE"));
                list.add(jobHistory);
            }
        }
        catch(SQLException e)
        {
            throw new RuntimeException(e);
        }
        return list;
    }
    //---------------------------------------------------------------------------------------------
    /**
     * Spring Batchのジョブ管理用テーブルを（再）作成する
     */
    public void deleteAll()
    {
        try
        {
            List<String> editSqlList = correctSql(getSql(getDropSQL()));
            executeSql(editSqlList);
            executeSql(getSql(getCreateSQL()));
        }
        catch(IOException | SQLException e)
        {
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }
    //---------------------------------------------------------------------------------------------
    /**
     * SQLを修正する
     * オブジェクトが存在しない場合に重ねてDROPを試みてもエラーを発生させないように修正する
     * @param sqlList SQLステートメントのList
     * @return 修正したSQLのList
     */
    protected List<String> correctSql(List<String> sqlList)
    {
        if(!(getDatasouce() instanceof SQLServerDataSource))
        {
            return sqlList;
        }
        Pattern pattern = Pattern.compile("IF\\s+EXISTS", Pattern.CASE_INSENSITIVE);
        Pattern editPtn = Pattern.compile("DROP\\s+(.*)\\s+(.*)", Pattern.CASE_INSENSITIVE);
        List<String> editSqlList = sqlList.stream().map(sql -> {
            if(!pattern.matcher(sql).matches())
            {
                Matcher matcher = editPtn.matcher(sql.trim());
                if(matcher.matches())
                {
                    String objectType = matcher.group(2).endsWith("_SEQ") ? "SEQUENCE" : "TABLE";
                    String edittedSql = matcher.replaceFirst("DROP %s IF EXISTS $2");
                    sql = String.format(edittedSql, objectType);
                }
            }
            return sql;
        }).collect(Collectors.toList());
        return editSqlList;
    }
    //---------------------------------------------------------------------------------------------
    /**
     * Spring Batchの管理テーブルを作成するSQLファイルのパスを返す
     * @return Spring Batchの管理テーブルを作成するSQLファイルのパス
     */
    protected String getCreateSQL()
    {
        return JobDao.CREATE_SQL;
    }
    //---------------------------------------------------------------------------------------------
    /**
     * Spring Batchの管理テーブルを削除するSQLファイルのパスを返す
     * @return Spring Batchの管理テーブルを削除するSQLファイルのパス
     */
    protected String getDropSQL()
    {
        return JobDao.DROP_SQL;
    }
    //---------------------------------------------------------------------------------------------
    /**
     * ファイルに記述されたSQLステートメントをListに格納して返す
     * @param fileName fileName SQLステートメントが記述されたファイルのパス
     * @return SQLステートメントのList
     */
    private List<String> getSql(final String fileName) throws IOException
    {
        StringBuilder sb = new StringBuilder();
        ClassLoader classLoader = this.getClass().getClassLoader();
        try(InputStream stream = classLoader.getResourceAsStream(fileName);
            InputStreamReader streamReader = new InputStreamReader(stream, StandardCharsets.UTF_8);
            BufferedReader buffer = new BufferedReader(streamReader))
        {
            String rec = null;
            while((rec = buffer.readLine()) != null)
            {
                sb.append(rec);
                sb.append("\n");
            }
        }
        List<String> sqlList = Arrays.stream( sb.toString().split(";"))
                                .filter(sql -> sql.trim().length() > 0)
                                .collect(Collectors.toList());
        return sqlList;
    }
}
