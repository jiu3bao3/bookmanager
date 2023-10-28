////////////////////////////////////////////////////////////////////////////////////////////////////
package com.jyis.bookmanager.books;
////////////////////////////////////////////////////////////////////////////////////////////////////
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Component;

import com.jyis.bookmanager.AbstractDao;
import com.jyis.bookmanager.AbstractForm;
////////////////////////////////////////////////////////////////////////////////////////////////////
/**
 * Bookに対するDAO
 * @author 久保　由仁
 */
@Component
public class BookDao extends AbstractDao<Book>
{
    /** ロガー */
    private static final Logger logger = LoggerFactory.getLogger(BookDao.class);
    //----------------------------------------------------------------------------------------------
    /**
     * Bookの一覧を取得する
     * @param arg 検索フォーム
     * @return 検索結果のList
     */
    @Override
    public List<Book> selectAll(AbstractForm arg)
    {
        if(!(arg instanceof SearchForm))
        {
            throw new IllegalArgumentException();
        }
        SearchForm form = (SearchForm)arg;
        List<Book> bookList = new ArrayList<>();
        String sql = "SELECT B.id, B.title, B.authors, B.publisher, B.publisher_id, "
                    + "B.published_on, RB.read_on "
                    + "FROM books B INNER JOIN read_books RB ON RB.BOOK_ID = B.ID "
                    + "WHERE (B.title LIKE ? OR 1 = ? ) "
                    + "AND (B.authors LIKE ? OR 1 = ? ) "
                    + "ORDER BY RB.READ_ON DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        try(Connection con = open();
            PreparedStatement stmt = con.prepareStatement(sql))
        {
            stmt.setString(1, String.format("%%%s%%", form.getTitle()));
            stmt.setInt(2, form.getTitle().equals("") ? 1 : 0);
            stmt.setString(3, String.format("%%%s%%", form.getAuthor()));
            stmt.setInt(4, form.getAuthor().equals("") ? 1 : 0);
            stmt.setInt(5, calcOffset(form.getPage()));
            stmt.setInt(6, getPageSize());
            try(ResultSet results = stmt.executeQuery())
            {
                while(results.next())
                {
                    Book book = new Book(results.getString("title"), results.getString("authors"));
                    book.setId(results.getInt("id"));
                    book.setPublisherName(results.getString("publisher"));
                    book.setPublishedYear(results.getInt("published_on"));
                    book.setReadDate(results.getDate("read_on"));
                    bookList.add(book);
                }
            }
        }
        catch(SQLException e)
        {
            throw new RuntimeException(e);
        }
        return bookList;
    }
    //----------------------------------------------------------------------------------------------
    /**
     * IDで本を検索する
     * @param id 本のID
     * @return Bookオブジェクト
     */
    public Book selectOne(final int id)
    {
        return selectOne(id, null);
    }
    //----------------------------------------------------------------------------------------------
    /**
     * ISBNで本を検索する
     * @param isbn 本のISBNコード
     * @return Bookオブジェクト
     */
    public Book selectOne(final String isbn)
    {
        return selectOne(null, isbn);
    }
    //----------------------------------------------------------------------------------------------
    /**
     * idとISBNで本を検索する
     * @param id 本のID
     * @param isbn 本のISBNコード
     * @return Bookオブジェクト
     */
    protected Book selectOne(final Integer id, final String isbn)
    {
        Book book = null;
        String sql = "SELECT B.id, B.title, B.authors, B.publisher, B.publisher_id, "
                   + "B.published_on, B.isbn, B.language_id FROM books B "
                   + "WHERE ((B.id = ? or 1 = ?) AND (B.isbn = ? or 1 = ?))";
        try(Connection con = open();
            PreparedStatement stmt = con.prepareStatement(sql))
        {
            setParameters(stmt, id, isbn);
            try(ResultSet results = stmt.executeQuery())
            {
                if(results.next())
                {
                    book = new Book(results.getString("title"), results.getString("authors"));
                    book.setId(results.getInt("id"));
                    book.setPublishedYear(results.getInt("published_on"));
                    book.setIsbn(results.getString("isbn"));
                    book.setPublisherName(results.getString("publisher"));
                    book.setPublisherId(results.getInt("publisher_id"));
                    book.setLanguage(results.getInt("language_id"));
                }
            }
            Map<ExtraInfo, String> extraInfo = retrieveExtraInfo(con, id);
            for(Map.Entry<ExtraInfo, String> entry : extraInfo.entrySet())
            {
                switch(entry.getKey())
                {
                    case NOTE:
                        book.setNote(entry.getValue());
                        break;
                    case COONTENTS:
                        book.setContents(entry.getValue());
                        break;
                }
            }
        }
        catch(SQLException e)
        {
            throw new RuntimeException(e);
        }
        return book;
    }
    //----------------------------------------------------------------------------------------------
    /**
     * selectOneで用いるSQLステートメントにパラメータをセットする
     * @param stmt 設定対象のPreparedstatementオブジェクト
     * @param id BookのID
     * @param isbn BookのISBNコード
     */
    protected void setParameters(PreparedStatement stmt, Integer id, String isbn) throws SQLException
    {
        if(id != null)
        {
            stmt.setInt(1, id);
            stmt.setInt(2, 0);
        }
        else
        {
            stmt.setNull(1, Types.INTEGER);
            stmt.setInt(2, 1);
        }
        if(isbn != null)
        {
            stmt.setString(3, isbn);
            stmt.setInt(4, 0);
        }
        else
        {
            stmt.setNull(3, Types.VARCHAR);
            stmt.setInt(4, 1);
        }
    }
    //----------------------------------------------------------------------------------------------
    /**
     * 国会図書館書誌情報データ取得
     * @param con データベースコネクション
     * @param id bookのID
     * @return 書誌情報文字列のMap(キー：レコード種別のenum，値：データ文字列）
     * @throws SQLException データベースエラー
     */
    private Map<ExtraInfo, String> retrieveExtraInfo(final Connection con, final Integer id)
                                                                           throws SQLException
    {
        Map<ExtraInfo, String> map = new HashMap<>();
        if(id == null)
        {
            return map;
        }
        final String SQL = "SELECT note_type, note FROM extra_info EI WHERE EI.book_id = ?";
        try(PreparedStatement stmt = con.prepareStatement(SQL))
        {
            stmt.setInt(1, id);
            try(ResultSet results = stmt.executeQuery())
            {
                while(results.next())
                {
                    String type = results.getString(1);
                    try
                    {
                        map.put(ExtraInfo.of(type), results.getString("note"));
                    }
                    catch(NoSuchElementException e)
                    {
                        logger.warn("extra_infoの不正なレコードです。" + type, e);
                        continue;
                    }
                }
            }
        }
        return map;
    }
    //----------------------------------------------------------------------------------------------
    /**
     * 読書記録を登録する
     * @param book Bookオブジェクト
     */
    public void insert(final Book book)
    {
        String sql1 = "INSERT INTO BOOKS (isbn, title, authors, publisher, "
                   + "publisher_id, published_on) VALUES (?, ?, ?, ?, ?, ?)";
        String sql2 = "INSERT INTO READ_BOOKS(book_id, read_on) "
                   + "SELECT MAX(B.id), GETDATE() FROM books B";
        try(Connection con = open())
        {
            try(PreparedStatement stmt = con.prepareStatement(sql1))
            {
                if(book.getIsbn() == null || book.getIsbn().equals(""))
                {
                    stmt.setNull(1, Types.CHAR);
                }
                else
                {
                    stmt.setString(1, book.getIsbn());
                }
                stmt.setString(2, book.getTitle());
                stmt.setString(3, book.getAuthor());
                stmt.setString(4, book.getPublisherName());
                if(book.getPublisherId() == null)
                {
                    stmt.setNull(5, Types.INTEGER);
                }
                else
                {
                    stmt.setInt(5, book.getPublisherId());
                }
                if(book.getPublishedYear() == null)
                {
                    stmt.setNull(6, Types.INTEGER);
                }
                else
                {
                    stmt.setInt(6, book.getPublishedYear());
                }
                stmt.executeUpdate();
            }
            try(Statement stmt = con.createStatement())
            {
                stmt.execute(sql2);
            }
            con.commit();
        }
        catch(SQLException e)
        {
            throw new RuntimeException(e);
        }
    }
    //----------------------------------------------------------------------------------------------
    /**
     * 読書記録を更新する
     * @param book Bookオブジェクト
     */
    public void update(final Book book)
    {
        logger.info(book.toString());
        if(book.getId() == null)
        {
            throw new IllegalArgumentException("BookのIDが設定されていません");
        }
        String sql = "UPDATE books SET title= ?, authors = ?, isbn= ?, published_on = ?, "
                   + "publisher_id = ? WHERE id = ?";
        try(Connection con = open();
            PreparedStatement stmt = con.prepareStatement(sql))
        {
            stmt.setString(1, book.getTitle());
            stmt.setString(2, book.getAuthor());
            stmt.setString(3, book.getIsbn());
            if(book.getPublishedYear() == null)
            {
                stmt.setNull(4, Types.INTEGER);
            }
            else
            {
                 stmt.setInt(4, book.getPublishedYear());
            }
            if(book.getPublisherId() == null)
            {
                stmt.setNull(5, Types.INTEGER);
            }
            else
            {
                stmt.setInt(5, book.getPublisherId());
            }
            stmt.setInt(6, book.getId());
            stmt.executeUpdate();
            con.commit();
        }
        catch(SQLException e)
        {
            throw new RuntimeException(e);
        }
    }
    //----------------------------------------------------------------------------------------------
    /**
     * 読書記録を削除する
     * @param book Bookオブジェクト
     */
    @Override
    public void delete(final Book book)
    {
        deleteById(book.getId());
    }
    //----------------------------------------------------------------------------------------------
    /**
     * 読書記録を削除する
     * @param bookId Bookオブジェクトのid
     */
    public void deleteById(final int bookId)
    {
        String[] sqls = 
        {
            "DELETE FROM EXTRA_INFO WHERE book_id = ?",
            "DELETE FROM READ_BOOKS WHERE book_id = ?",
            "DELETE FROM BOOKS WHERE id = ?"
        };
        try(Connection con = open())
        {
            for(String sql : sqls)
            {
                try(PreparedStatement stmt = con.prepareStatement(sql))
                {
                    stmt.setInt(1, bookId);
                    stmt.executeUpdate();
                    stmt.clearParameters();
                }
            }
            con.commit();
        }
        catch(SQLException e)
        {
            throw new RuntimeException(e);
        }
    }
}
