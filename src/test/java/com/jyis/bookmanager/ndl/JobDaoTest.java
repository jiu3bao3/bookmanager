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
        logger.info(String.format("getSql start %s", fileName));
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
     * レコードがすべて削除されていること
     */
    @Test
    public void deleteAllTest() throws Exception
    {
        final String SQL_FOR_COUNT = "SELECT COUNT(*) AS REC_COUNT FROM BATCH_JOB_EXECUTION";
        JobDaoMock dao = new JobDaoMock();
        insertDummyData();
        dao.deleteAll();
        try(Connection con = dao.open())
        {
            if(!(con instanceof SQLiteConnection)) throw new IllegalStateException();
            long count = 0L;
            try(Statement stmt = con.createStatement();
                ResultSet result = stmt.executeQuery(SQL_FOR_COUNT))
            {
                if(result.next())
                {
                    count = result.getLong("REC_COUNT");
                }
            }
            Assertions.assertEquals(0L, count,"レコードがすべて削除されていること");
        }
    }
    //----------------------------------------------------------------------------------------------
    private void insertDummyData() throws SQLException
    {
        final String[] SQL_ARRAY = {
            "INSERT INTO BATCH_JOB_INSTANCE(JOB_INSTANCE_ID, VERSION, JOB_NAME, JOB_KEY) VALUES("
            + "0, 0, 'TEST_JOB', 'JOB_KEY')",
            "INSERT INTO BATCH_JOB_EXECUTION(JOB_EXECUTION_ID, VERSION, JOB_INSTANCE_ID, "
            + "CREATE_TIME, START_TIME, END_TIME) VALUES(0, 0, 0, CURRENT_TIMESTAMP, "
            + "CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)"
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
