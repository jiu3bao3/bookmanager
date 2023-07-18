////////////////////////////////////////////////////////////////////////////////////////////////////
package com.jyis.bookmanager.books;
////////////////////////////////////////////////////////////////////////////////////////////////////
import java.io.Serializable;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jyis.bookmanager.exceptions.ValidationException;
////////////////////////////////////////////////////////////////////////////////////////////////////
/**
 * 本フォーム(追加／更新用)
 * @author 久保　由仁
 */
public class BookForm extends AbstractBook implements Serializable
{
    /** NOTE(ReadOnly) */
    private String note;
    
    /** CONTENTS(ReadOnly) */
    private String contents;
    //----------------------------------------------------------------------------------------------
    /**
     * デフォルトコンストラクタ
     */
    public BookForm() {}
    //----------------------------------------------------------------------------------------------
    /**
     * コンストラクタ
     * @param book Bookオブジェクト
     */
    public BookForm(final Book book)
    {
        this();
        title = book.getTitle();
        author = book.getAuthor();
        publishedYear = book.getPublishedYear();
        publisherName = book.getPublisherName();
        publisherId = book.getPublisherId();
        note = book.getNote();
        isbn = book.getIsbn();
        contents = book.getContents();
    }
    //----------------------------------------------------------------------------------------------
    /**
     * Bookオブジェクトへ変換する
     * @return Bookオブジェクト
     */
    public Book toBook()
    {
        validate();
        Book book = new Book(getTitle(), getAuthor());
        book.setPublisherName(getPublisherName());
        book.setIsbn(getIsbn());
        book.setPublishedYear(getPublishedYear());
        book.setPublisherId(getPublisherId());
        return book;
    }
    //----------------------------------------------------------------------------------------------
    /**
     * バリデーションを行う
     * @throws ValidationException バリデーションエラーを検出
     */
    public void validate()
    {
        Map<String, List<String>> errors = new HashMap<>();
        if(publishedYear != null)
        {
            Calendar calendar = Calendar.getInstance();
            if(publishedYear < 0 || publishedYear > calendar.get(Calendar.YEAR))
            {
                errors.put("publishdYear", new ArrayList(Arrays.asList("不正な年です。")));
            }
        }
        String isbnString = getIsbn();
        if(isbnString != null && isbnString.length() > 0)
        {
            if(!isValidIsbn13(getIsbn()))
            {
                errors.put("isbn", new ArrayList(Arrays.asList(isbn, "不正なISBNです。")));
            }
        }
        if(getTitle().length() == 0)
        {
            errors.put("title", new ArrayList(Arrays.asList("ブランクです。")));
        }
        if(!errors.isEmpty())
        {
            throw new  ValidationException(errors);
        }
    }
    //----------------------------------------------------------------------------------------------
    /**
     * Noteを取得する
     * @return Note
     */
    public String getNote()
    {
        return note;
    }
    //----------------------------------------------------------------------------------------------
    /**
     * Contentsを取得する
     * @return contents
     */
    public String getContents()
    {
        return contents;
    }
}
