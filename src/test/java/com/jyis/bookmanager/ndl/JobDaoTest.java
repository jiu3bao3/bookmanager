////////////////////////////////////////////////////////////////////////////////////////////////////
package com.jyis.bookmanager.ndl;
////////////////////////////////////////////////////////////////////////////////////////////////////
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.boot.test.context.SpringBootTest;

import org.sqlite.SQLiteConnection;

import com.jyis.bookmanager.AbstractDaoImpl;
////////////////////////////////////////////////////////////////////////////////////////////////////
@SpringBootTest
public class JobDaoTest
{
    /** ロガー */
    private static final Logger logger = LoggerFactory.getLogger(JobDaoTest.class);
    //---------------------------------------------------------------------------------------------
    /**
     * Spring Batchのジョブ管理用テーブルを作成する
     */
    @BeforeAll
    public static void createSpringBatchMetaTables() throws Exception
    {
        executeSql(getSql("org/springframework/batch/core/schema-drop-sqlite.sql"));
        executeSql(getSql("org/springframework/batch/core/schema-sqlite.sql"));
    }
    //---------------------------------------------------------------------------------------------
    private static void executeSql(final String[] sqlArray) throws SQLException
    {
        JobDaoMock dao = new JobDaoMock();
        try(Connection con = dao.open())
        {
            if(!(con instanceof SQLiteConnection)) throw new IllegalStateException();
            for(String sql: sqlArray)
            {
                if(sql.indexOf("TABLE") < 0) break;
                try(Statement stmt = con.createStatement())
                {
                    logger.info(sql);
                    stmt.execute(sql);
                }
            }
            con.commit();
        }
    }
    //---------------------------------------------------------------------------------------------
    /**
     * Spring Batchのジョブ管理用テーブルを作成するSQLステートメントを取得する
     * @return SQLステートメント
     */
    private static String[] getSql(String fileName) throws IOException
    {
        StringBuilder sb = new StringBuilder();
        ClassLoader classLoader = JobDaoTest.class.getClassLoader();
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
        return sb.toString().split(";");
    }
    //----------------------------------------------------------------------------------------------
    /**
     * データベース接続
     */
    @Test
    public void openTest() throws Exception
    {
        JobDaoMock dao = new JobDaoMock();
        try(Connection con = dao.open())
        {
            Assertions.assertTrue(con instanceof SQLiteConnection);
        }
    }
    //----------------------------------------------------------------------------------------------
    /**
     * ジョブ実行履歴を取得できること
     */
    @Test
    public void selectAllTest() throws Exception
    {
        final String MESSAGE = "ジョブ実行履歴を取得できること";
        JobDaoMock dao = new JobDaoMock();
        insertDummyData();
        List<JobHistory> list = dao.selectAll(null);
        Assertions.assertEquals(list.size(), 1, MESSAGE);
        JobHistory jobHistory = list.get(0);
        Assertions.assertEquals(jobHistory.getJobInstanceId(), -1L, MESSAGE);
        Assertions.assertEquals(jobHistory.getStatus(), "STATUS_COMPLETED", MESSAGE);
        Assertions.assertEquals(jobHistory.getExitMessage(), "EXIT_MESSAGE_EXAMPLE", MESSAGE);
    }
    //----------------------------------------------------------------------------------------------
    private void insertDummyData() throws SQLException
    {
        final String[] SQL_ARRAY = {
            "INSERT INTO BATCH_JOB_INSTANCE(JOB_INSTANCE_ID, VERSION, JOB_NAME, JOB_KEY) VALUES("
                + "-1, 0, 'TEST_JOB', 'JOB_KEY')",
            "INSERT INTO BATCH_JOB_EXECUTION(JOB_EXECUTION_ID, VERSION, JOB_INSTANCE_ID, "
                + "CREATE_TIME, START_TIME, END_TIME, STATUS, EXIT_MESSAGE) "
                + "VALUES (0, 0, -1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, "
                + "CURRENT_TIMESTAMP, 'STATUS_COMPLETED', 'EXIT_MESSAGE_EXAMPLE')",
            "INSERT INTO BATCH_JOB_EXECUTION_PARAMS(JOB_EXECUTION_ID, PARAMETER_NAME, "
                + "PARAMETER_TYPE, IDENTIFYING) VALUES (0, 'PARAM1', 'STRING', 'C')",
            "INSERT INTO BATCH_STEP_EXECUTION(STEP_EXECUTION_ID, VERSION, STEP_NAME, "
                + "JOB_EXECUTION_ID, CREATE_TIME) VALUES (0, 0, 'STEP1', 0, CURRENT_TIMESTAMP)",
            "INSERT INTO BATCH_STEP_EXECUTION_CONTEXT(STEP_EXECUTION_ID, SHORT_CONTEXT)"
                + "VALUES (0, 'CONTEXT1')",
            "INSERT INTO BATCH_JOB_EXECUTION_CONTEXT(JOB_EXECUTION_ID, SHORT_CONTEXT)"
                + " VALUES (0, 'CONTEXT1')"
        };
        JobDaoMock dao = new JobDaoMock();
        try(Connection con = dao.open())
        {
            if(!(con instanceof SQLiteConnection)) throw new IllegalStateException();
            for(String sql: SQL_ARRAY)
            {
                try(Statement stmt = con.createStatement())
                {
                    stmt.executeUpdate(sql);
                }
            }
            con.commit();
        }
    }
}
////////////////////////////////////////////////////////////////////////////////////////////////////
class JobDaoMock extends JobDao
{
    public Connection open()
    {
        Connection con = null;
        try
        {
            AbstractDaoImpl daoImpl = new AbstractDaoImpl();
            Method method = daoImpl.getClass().getDeclaredMethod("open");
            method.setAccessible(true);
            Object obj = method.invoke(daoImpl);
            if(obj instanceof Connection)
            {
                con = (Connection)obj;
            }
        }
        catch(ReflectiveOperationException e)
        {
            throw new RuntimeException(e);
        }
        return con;
    }
}
