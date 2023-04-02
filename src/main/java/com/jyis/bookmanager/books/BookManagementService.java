////////////////////////////////////////////////////////////////////////////////////////////////////
package com.jyis.bookmanager.books;
////////////////////////////////////////////////////////////////////////////////////////////////////
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jyis.bookmanager.exceptions.DuplicateKeyException;
////////////////////////////////////////////////////////////////////////////////////////////////////
/**
 * 読書記録サービスクラス
 * @author 久保　由仁
 */
@Service
public class BookManagementService
{
    /** ロガー */
    private static final Logger logger = LoggerFactory.getLogger(BookManagementService.class);

    /** DAO */
    @Autowired
    private BookDao dao;
    //----------------------------------------------------------------------------------------------
    /**
     * 本のリストを取得する
     * @param form 検索フォーム
     * @return 本のリスト
     */
    public List<Book> selectBooks(SearchForm form)
    {
        return dao.selectAll(form);
    }
    //----------------------------------------------------------------------------------------------
    /**
     * 本を取得する
     * @param id BookのID
     * @return Bookオブジェクト
     */
    public Book selectBook(final int id)
    {
        return dao.selectOne(id);
    }
    //----------------------------------------------------------------------------------------------
    /**
     * 本を削除する
     * @param id 本のID
     */
    public void deleteBook(final int id)
    {
        dao.deleteById(id);
    }
    //----------------------------------------------------------------------------------------------
    /**
     * 新しい本を登録する
     * @param form 本情報フォーム
     */
    public void registerBook(final BookForm form)
    {
        if(dao.selectOne(form.getIsbn()) != null)
        {
            final String MESSAGE = String.format("登録済の本です。ISBN=%s", form.getIsbn());
            throw new DuplicateKeyException(MESSAGE);
        }
        dao.insert(form.toBook());
    }
    //----------------------------------------------------------------------------------------------
    /**
     * 読書記録を更新する
     * @param id 本のID
     * @param form 本情報フォーム
     */
    public void updateBook(final int id, final BookForm form)
    {
        form.validate();
        Book book = dao.selectOne(id);
        book.setTitle(form.getTitle());
        book.setAuthor(form.getAuthor());
        book.setPublishedYear(form.getPublishedYear());
        book.setPublisherName(form.getPublisherName());
        book.setPublisherId(form.getPublisherId());
        dao.update(book);
    }
}
