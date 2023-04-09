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
    /** テスト用ISBN */
    private static final String TEST_ISBN = "9784000072151";
    //----------------------------------------------------------------------------------------------
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
class BookDaoMock extends BookDao implements IDao<Book>
{
    private static final Logger logger = LoggerFactory.getLogger(BookDaoMock.class);
    @Override
    public List<Book> selectAll(AbstractForm arg)
    {
        return null;
    }
    public void delete(final Book book)
    {
    }
    public void update(final Book book)
    {
    }
    @Override
    public Book selectOne(final int id)
    {
        return null;
    }
    protected Book selectOne(final Integer id, final String isbn)
    {
        return null;
    }
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
