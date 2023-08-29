////////////////////////////////////////////////////////////////////////////////////////////////////
package com.jyis.bookmanager.ndl;
////////////////////////////////////////////////////////////////////////////////////////////////////
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;

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
     * ジョブ実行履歴をすべて削除する
     */
    public void deleteAll()
    {
        final String[] TABLES = { "BATCH_STEP_EXECUTION_CONTEXT", "BATCH_STEP_EXECUTION",
            "BATCH_JOB_EXECUTION_CONTEXT", "BATCH_JOB_EXECUTION_PARAMS", "BATCH_JOB_EXECUTION" };
        try(Connection con = open();
            Statement stmt = con.createStatement())
        {
            for(String table : TABLES)
            {
                String sql = String.format("DELETE FROM %s", table);
                logger.info(sql);
                stmt.execute(sql);
            }
            con.commit();
        }
        catch(SQLException e)
        {
            throw new RuntimeException(e);
        }
    }
}
