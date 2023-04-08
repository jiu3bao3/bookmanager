////////////////////////////////////////////////////////////////////////////////////////////////////
package com.jyis.bookmanager.books;
////////////////////////////////////////////////////////////////////////////////////////////////////
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import org.springframework.boot.test.context.SpringBootTest;

import com.jyis.bookmanager.IDao;
import com.jyis.bookmanager.AbstractForm;
import com.jyis.bookmanager.AbstractDaoImpl;
////////////////////////////////////////////////////////////////////////////////////////////////////
@SpringBootTest
public class BookDaoTest
{
}
class BookDaoMock implements IDao<Book>
{
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
}
