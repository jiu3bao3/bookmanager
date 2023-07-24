////////////////////////////////////////////////////////////////////////////////////////////////////
package com.jyis.bookmanager.publishers;
////////////////////////////////////////////////////////////////////////////////////////////////////
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.springframework.boot.test.context.SpringBootTest;

import org.sqlite.SQLiteConnection;

import com.jyis.bookmanager.AbstractDaoImpl;
import com.jyis.bookmanager.IDao;
////////////////////////////////////////////////////////////////////////////////////////////////////
@SpringBootTest
public class PublisherDaoTest
{
    /** テーブルクリア用SQL */
    private static final String[] CLEAR_SQL = new String[] { "DELETE FROM publishers" };
    //----------------------------------------------------------------------------------------------
    /**
     * テストに用いたデータを削除する
     */
    @AfterEach
    public void clearData() throws Exception
    {
        PublisherDaoMock dao = new PublisherDaoMock();
        try(Connection con = dao.open();
            Statement stmt = con.createStatement())
        {
            for(String sql : CLEAR_SQL)
            {
                stmt.execute(sql);
            }
            con.commit();
        }
    }
    //----------------------------------------------------------------------------------------------
    /**
     * データベース接続
     */
    @Test
    public void openTest() throws Exception
    {
        PublisherDaoMock dao = new PublisherDaoMock();
        try(Connection con = dao.open())
        {
            Assertions.assertTrue(con instanceof SQLiteConnection);
        }
    }
    //----------------------------------------------------------------------------------------------
    /**
     * レコードを追加できること
     */
    @Test
    public void insertTest() throws Exception
    {
        PublisherDao dao = new PublisherDaoMock();
        Publisher publisher = createPublisher();
        dao.insert(publisher);
        int count = 0;
        try(Connection con = ((PublisherDaoMock)dao).open();
            Statement stmt = con.createStatement())
        {
            ResultSet result = stmt.executeQuery("SELECT COUNT(*) FROM publishers");
            if(result.next())
            {
                count = result.getInt(1);
            }
        }
        Assertions.assertTrue(count > 0, "レコードを追加できること");
    }
    //----------------------------------------------------------------------------------------------
    /**
     * IDで出版社レコードを取得できること
     */
    @Test
    public void selectOneByIdTest() throws Exception
    {
        PublisherDao dao = new PublisherDaoMock();
        Publisher publisher = createPublisher();
        dao.insert(publisher);
        Publisher selectedPublisher = dao.selectOne(publisher);
        Assertions.assertEquals(selectedPublisher.getName(), publisher.getName());
        Assertions.assertEquals(selectedPublisher.getZip(), publisher.getZip());
        Assertions.assertEquals(selectedPublisher.getAddress1(), publisher.getAddress1());
        Assertions.assertEquals(selectedPublisher.getAddress2(), publisher.getAddress2());
        Assertions.assertEquals(selectedPublisher.getPhone(), publisher.getPhone());
        Assertions.assertEquals(selectedPublisher.getEmail(), publisher.getEmail());
    }
    //----------------------------------------------------------------------------------------------
    /**
     * テスト用のPublisherオブジェクトを作成する
     * @return テスト用のPublisherオブジェクト
     */
    private Publisher createPublisher()
    {
        Publisher publisher = new Publisher("テスト出版");
        publisher.setZip("999-9999");
        publisher.setAddress1("東京都小笠原村１２３");
        publisher.setAddress2("硫黄島１２３－４");
        publisher.setPhone("000-111-9999");
        publisher.setEmail("test.publisher@example.com");
        return publisher;
    }
}
////////////////////////////////////////////////////////////////////////////////////////////////////
/**
 * テスト用のBookDao
 */
class PublisherDaoMock extends PublisherDao implements IDao<Publisher>
{
    /** ロガー */
    private static final Logger logger = LoggerFactory.getLogger(PublisherDao.class);
    //----------------------------------------------------------------------------------------------
    /**
     * テスト用のデータベースコネクションを作成する
     * @return データベースコネクション
     */
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