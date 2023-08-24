////////////////////////////////////////////////////////////////////////////////////////////////////
package com.jyis.bookmanager.ndl;
////////////////////////////////////////////////////////////////////////////////////////////////////
import java.util.List;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Component;

import com.jyis.bookmanager.AbstractDao;
import com.jyis.bookmanager.AbstractForm;
import com.jyis.bookmanager.books.Book;
////////////////////////////////////////////////////////////////////////////////////////////////////
@Component
public class NdlDao extends AbstractDao<Book>
{
    public void insert(Book book) {}
    public void delete(Book book) {}
    public void update(Book book) {}
    public Book selectOne(int bookId) { return null; }
    public List<Book> selectAll(AbstractForm form) { return null; }
    
    DataSource dataSource()
    {
        return super.getDatasouce();
    }
}
