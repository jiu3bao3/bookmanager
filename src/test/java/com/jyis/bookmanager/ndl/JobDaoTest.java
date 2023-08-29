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
    @BeforeAll
    public static void createSpringBatchMetaTables() throws Exception
    {
        String[] sqlArray = getSql();
        JobDaoMock dao = new JobDaoMock();
        try(Connection con = dao.open())
        {
            if(!(con instanceof SQLiteConnection)) throw new IllegalStateException();
            for(String sql: sqlArray)
            {
                if(sql.indexOf("CREATE") < 0) break;
                try(Statement stmt = con.createStatement())
                {
                    logger.info(sql);
                    stmt.execute(sql);
                }
            }
        }
    }
    //---------------------------------------------------------------------------------------------
    /**
     * Spring Batchのジョブ管理用テーブルを作成するSQLステートメントを取得する
     * @return SQLステートメント
     */
    private static String[] getSql() throws IOException
    {
        StringBuilder sb = new StringBuilder();
        ClassLoader classLoader = JobDaoTest.class.getClassLoader();
        try(InputStream stream = classLoader.getResourceAsStream("schema-sqlite.sql");
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
