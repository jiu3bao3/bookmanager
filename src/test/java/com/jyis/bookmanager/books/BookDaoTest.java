////////////////////////////////////////////////////////////////////////////////////////////////////
package com.jyis.bookmanager.books;
////////////////////////////////////////////////////////////////////////////////////////////////////
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.springframework.boot.test.context.SpringBootTest;

import org.sqlite.SQLiteConnection;

import com.jyis.bookmanager.IDao;
import com.jyis.bookmanager.AbstractForm;
import com.jyis.bookmanager.AbstractDaoImpl;
////////////////////////////////////////////////////////////////////////////////////////////////////
@SpringBootTest
public class BookDaoTest
{
    /** テーブルクリア用SQL */
    private static final String[] CLEAR_SQL = {
         "DELETE FROM read_books",
         "DELETE FROM extra_info",
         "DELETE FROM books",
         "DELETE FROM publishers" };
         
    /** テスト用ISBN */
    private static final String TEST_ISBN = "9784000072151";
    //----------------------------------------------------------------------------------------------
    /**
     * 後始末
     */
    @AfterEach
    public void clearData() throws Exception
    {
        BookDaoMock dao = new BookDaoMock();
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
        BookDaoMock dao = new BookDaoMock();
        try(Connection con = dao.open())
        {
            Assertions.assertTrue(con instanceof SQLiteConnection);
        }
    }
    //----------------------------------------------------------------------------------------------
    /**
     * データ登録
     */
    @Test
    public void insertTest() throws Exception
    {
        Book book = createBook();
        BookDao dao = new BookDaoMock();
        dao.insert(book);
        int count = 0;
        try(Connection con = ((BookDaoMock)dao).open();
            Statement stmt = con.createStatement())
        {
            ResultSet result = stmt.executeQuery("SELECT COUNT(*) FROM books");
            if(result.next())
            {
                count = result.getInt(1);
            }
        }
        Assertions.assertTrue(count > 0);
    }
    //----------------------------------------------------------------------------------------------
    /**
     * ISBNで検索できること
     */
    @Test
    public void selectOneWithIsbnTest() throws Exception
    {
        BookDao dao = new BookDaoMock();
        dao.insert(createBook());
        Book book = dao.selectOne(TEST_ISBN);
        Assertions.assertEquals(book.getTitle(), "吾輩は猫である");
        Assertions.assertEquals(book.getAuthor(), "夏目漱石");
        Assertions.assertEquals(book.getIsbn(), TEST_ISBN);
    }
    //----------------------------------------------------------------------------------------------
    /**
     * 更新が実行できること
     */
    @Test
    public void updateTest() throws Exception
    {
        final String NEW_TITLE = "わがはいはねこである";
        BookDao dao = new BookDaoMock();
        dao.insert(createBook());
        Book book = dao.selectOne(TEST_ISBN);
        book.setTitle(NEW_TITLE);
        dao.update(book);
        Book reselectBook = dao.selectOne(TEST_ISBN);
        Assertions.assertEquals(book.getTitle(), NEW_TITLE);
        Assertions.assertEquals(book.getIsbn(), TEST_ISBN);
    }
    //----------------------------------------------------------------------------------------------
    @Test
    public void deleteTest() throws Exception
    {
        BookDao dao = new BookDaoMock();
        dao.insert(createBook());
        Book book = dao.selectOne(TEST_ISBN);
        dao.delete(book);
        int count = 0;
        try(Connection con = ((BookDaoMock)dao).open();
            Statement stmt = con.createStatement())
        {
            ResultSet result = stmt.executeQuery("SELECT COUNT(*) FROM books");
            if(result.next())
            {
                count = result.getInt(1);
            }
        }
        Assertions.assertTrue(count == 0);
    }
    //----------------------------------------------------------------------------------------------
    /**
     * テスト用のBookオブジェクトを作成する
     * @return Bookオブジェクト
     */
    private Book createBook()
    {
        Book book = new Book("吾輩は猫である", "夏目漱石");
        book.setIsbn(TEST_ISBN);
        book.setPublishedYear(2002);
        book.setPublisherName("岩波書店");
        return book;
    }
}
////////////////////////////////////////////////////////////////////////////////////////////////////
/**
 * テスト用のBookDao
 */
class BookDaoMock extends BookDao implements IDao<Book>
{
    /** ロガー */
    private static final Logger logger = LoggerFactory.getLogger(BookDaoMock.class);
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
