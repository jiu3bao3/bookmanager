////////////////////////////////////////////////////////////////////////////////////////////////////
package com.jyis.bookmanager.publishers;
////////////////////////////////////////////////////////////////////////////////////////////////////
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import org.springframework.boot.test.context.SpringBootTest;

import org.sqlite.SQLiteConnection;

import com.jyis.bookmanager.AbstractDaoImpl;
import com.jyis.bookmanager.IDao;
import com.jyis.bookmanager.exceptions.PersistenceException;
////////////////////////////////////////////////////////////////////////////////////////////////////
@SpringBootTest
@Execution(ExecutionMode.SAME_THREAD)
public class PublisherDaoTest
{
    /** DBがロックされていた場合にリトライする回数の上限 */
    private static final int RETRY_COUNT = 5;

    /** テーブルクリア用SQL */
    private static final String[] CLEAR_SQL = new String[] { "DELETE FROM publishers" };

    /** ロック（処理が競合しないようロックする） */
    private static final Object lock = new Object();
    //----------------------------------------------------------------------------------------------
    /**
     * テストに用いたデータを削除する
     */
    @AfterEach
    public void clearData() throws Exception
    {
        synchronized(lock)
        {
            PublisherDaoMock dao = new PublisherDaoMock();
            try(Connection con = dao.open();
                Statement stmt = con.createStatement())
            {
                for(String sql : CLEAR_SQL)
                {
                    for(int i = 0; i < RETRY_COUNT; i++)
                    {
                        try
                        {
                            stmt.execute(sql);
                            break;
                        }
                        catch(PersistenceException e)
                        {
                            Thread.sleep(200 * i * i);
                            continue;
                        }
                    }
                }
                con.commit();
            }
        }
    }
    //----------------------------------------------------------------------------------------------
    /**
     * データベース接続
     */
    @Test
    public void openTest() throws Exception
    {
        synchronized(lock)
        {
            PublisherDaoMock dao = new PublisherDaoMock();
            try(Connection con = dao.open())
            {
                Assertions.assertTrue(con instanceof SQLiteConnection);
            }
        }
    }
    //----------------------------------------------------------------------------------------------
    /**
     * レコードを追加できること
     */
    @Test
    public void insertTest() throws Exception
    {
        synchronized(lock)
        {
            Publisher publisher = createPublisher();
            PublisherDao dao = new PublisherDaoMock();
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
    }
    //----------------------------------------------------------------------------------------------
    /**
     * IDで出版社レコードを取得できること
     */
    @Test
    public void selectOneByNameTest() throws Exception
    {
        synchronized(lock)
        {
            PublisherDao dao = new PublisherDaoMock();
            Publisher publisher = createPublisher();
            dao.insert(publisher);
            Publisher selectedPublisher = dao.selectOne(new Publisher(publisher.getName()));
            Assertions.assertEquals(selectedPublisher.getName(), publisher.getName());
            Assertions.assertEquals(selectedPublisher.getZip(), publisher.getZip());
            Assertions.assertEquals(selectedPublisher.getAddress1(), publisher.getAddress1());
            Assertions.assertEquals(selectedPublisher.getAddress2(), publisher.getAddress2());
            Assertions.assertEquals(selectedPublisher.getPhone(), publisher.getPhone());
            Assertions.assertEquals(selectedPublisher.getEmail(), publisher.getEmail());
        }
    }
    //----------------------------------------------------------------------------------------------
    /**
     * 条件を指定せずに全件を取得できること
     */
    @Test
    public void selectAllTestByEmpty() throws Exception
    {
        synchronized(lock)
        {
            final int size = 5;
            PublisherDao dao = new PublisherDaoMock();
            for(int i = 0; i < size; i++)
            {
                Publisher publisher = createPublisher();
                publisher.setName(String.format("テスト出版%d", i + 1));
                dao.insert(publisher);
            }
            List<Publisher> list = dao.selectAll(null);
            Assertions.assertEquals(list.size(), size);
        }
    }
    //----------------------------------------------------------------------------------------------
    /**
     * 出版社名が一部一致するレコードのみ抽出できること
     */
    @Test
    public void selectAllTestByName() throws Exception
    {
        synchronized(lock)
        {
            String[] publisher_names = new String[] { "テスト出版１", "テスト出版２", "TEST PUBLISHING 1", "テストパブリッシング" };
            PublisherDao dao = new PublisherDaoMock();
            for(String name : publisher_names)
            {
                Publisher publisher = createPublisher();
                publisher.setName(name);
                dao.insert(publisher);
            }
            List<Publisher> list = dao.selectAll(new SearchForm("テスト出版"));
            Assertions.assertEquals(list.size(), 2);
        }
    }
    //----------------------------------------------------------------------------------------------
    /**
     * 出版社の更新ができること
     */
    @Test
    public void updateTest() throws Exception
    {
        synchronized(lock)
        {
            final String newAddress = "引っ越しました";
            PublisherDao dao = new PublisherDaoMock();
            dao.insert(createPublisher());
            List<Publisher> list = dao.selectAll(null);
            Integer id = null;
            for(Publisher publisher: list)
            {
                id = publisher.getId();
                publisher.setAddress1(newAddress);
                dao.update(publisher);
            }
            Publisher selectedPublisher = dao.selectOne(id);
            Assertions.assertEquals(newAddress, selectedPublisher.getAddress1());
        }
    }
    //----------------------------------------------------------------------------------------------
    /**
     * 出版社のレコードを削除できること
     */
    @Test
    public void deleteTest() throws Exception
    {
        synchronized(lock)
        {
            PublisherDao dao = new PublisherDaoMock();
            dao.insert(createPublisher());
            List<Publisher> list = dao.selectAll(null);
            for(Publisher publisher : list)
            {
                dao.deleteById(publisher.getId());
                Publisher result = dao.selectOne(publisher);
                Assertions.assertEquals(null, result);
            }
        }
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
    /** リトライ数 */
    private static final int RETRY_COUNT = 10;

    /** 次のリトライまで待機する時間（ミリ秒） */
    private static final int WAIT_TIME = 500;

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
            Object obj = null;
            for(int i = 0; i < RETRY_COUNT; i++)
            {
                try
                {
                    obj = method.invoke(daoImpl);
                    break;
                }
                catch(InvocationTargetException e)
                {
                    Thread.sleep(1000 * i * i);
                    continue;
                }
            }
            if(obj instanceof Connection)
            {
                con = (Connection)obj;
            }
        }
        catch(ReflectiveOperationException | InterruptedException e)
        {
            throw new RuntimeException(e);
        }
        return con;
    }
    //----------------------------------------------------------------------------------------------
    /**
     * スーパークラスのinsert()をリトライするラップ
     * @param arg 出版社オブジェクト
     */
    @Override
    public void insert(Publisher arg)
    {
        try
        {
            for(int i = 0; i < RETRY_COUNT; i++)
            {
                try
                {
                    super.insert(arg);
                    return;
                }
                catch(PersistenceException e)
                {
                    Thread.sleep(WAIT_TIME * i * i);
                    continue;
                }
            }
        }
        catch(InterruptedException ie)
        {
            throw new RuntimeException(ie);
        }
    }
    //----------------------------------------------------------------------------------------------
    /**
     * スーパークラスのupdateをリトライするラップ
     * @param arg 出版社オブジェクト
     */
    @Override
    public void update(Publisher arg)
    {
        try
        {
            for(int i = 0; i < RETRY_COUNT; i++)
            {
                try
                {
                    super.update(arg);
                    return;
                }
                catch(PersistenceException e)
                {
                    Thread.sleep(WAIT_TIME * i * i);
                    continue;
                }
            }
        }
        catch(InterruptedException ie)
        {
            throw new RuntimeException(ie);
        }
    }
    //----------------------------------------------------------------------------------------------
    /**
     * スーパークラスのdelete()をリトライするラップ
     * @param arg 出版社オブジェクト
     */
    @Override
    public void delete(Publisher arg)
    {
        try
        {
            for(int i = 0; i < RETRY_COUNT; i++)
            {
                try
                {
                    super.delete(arg);
                    return;
                }
                catch(PersistenceException e)
                {
                    Thread.sleep(WAIT_TIME * i * i);
                    continue;
                }
            }
        }
        catch(InterruptedException ie)
        {
            throw new RuntimeException(ie);
        }
    }
}
