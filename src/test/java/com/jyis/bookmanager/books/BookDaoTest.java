////////////////////////////////////////////////////////////////////////////////////////////////////
package com.jyis.bookmanager.books;
////////////////////////////////////////////////////////////////////////////////////////////////////
import java.lang.reflect.Method;
import java.sql.Connection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.springframework.boot.test.context.SpringBootTest;

import com.jyis.bookmanager.IDao;
import com.jyis.bookmanager.AbstractForm;
import com.jyis.bookmanager.AbstractDaoImpl;
////////////////////////////////////////////////////////////////////////////////////////////////////
@SpringBootTest
public class BookDaoTest
{
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
    public void insert(final Book book)
    {
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
    protected Connection open()
    {
        Connection con = null;
        try
        {
            AbstractDaoImpl daoImpl = new AbstractDaoImpl();
            Method method = daoImpl.getClass().getDeclaredMethod("open");
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
