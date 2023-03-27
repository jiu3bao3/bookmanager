////////////////////////////////////////////////////////////////////////////////////////////////////
package com.jyis.bookmanager.books;
////////////////////////////////////////////////////////////////////////////////////////////////////
import java.io.Serializable;

import com.jyis.bookmanager.publishers.Publisher;
////////////////////////////////////////////////////////////////////////////////////////////////////
/**
 * 本クラス
 * @author 久保　由仁
 */
public class Book extends AbstractBook implements Serializable
{
    /** ID */
    private Integer id;
    
    /** 出版社名 */
    private String publisherName;

    /** 出版社 */
    private Publisher publisher;
    
    /** Note */
    private String note;
    
    /** 内容 */
    private String contents;
    //----------------------------------------------------------------------------------------------
    /**
     * IDをセットする
     * @param arg ID
     */
    public void setId(final int arg)
    {
        id = arg;
    }
    //----------------------------------------------------------------------------------------------
    /**
     * IDを取得する
     * @return ID
     */
    public Integer getId()
    {
        return id;
    }
    //----------------------------------------------------------------------------------------------
    /**
     * デフォルトコンストラクタ
     */
    public Book() { }
    //----------------------------------------------------------------------------------------------
    /**
     * コンストラクタ
     * @param title タイトル
     * @param author 著者名
     */
    public Book(final String title, final String author)
    {
        this();
        this.title = title;
        this.author = author;
    }
    //----------------------------------------------------------------------------------------------
    /**
     * タイトルをセットする
     * @param arg タイトル
     */
    public void setTitle(final String arg)
    {
        title = arg;
    }
    //----------------------------------------------------------------------------------------------
    /**
     * タイトルを取得する
     * @return タイトル
     */
    public String getTitle()
    {
        return title;
    }
    //----------------------------------------------------------------------------------------------
    /**
     * 著者名をセットする
     * @param arg 著者名
     */
    public void setAuthor(final String arg)
    {
        author = arg;
    }
    //----------------------------------------------------------------------------------------------
    /**
     * 著者名を取得する
     * @return 著者名
     */
    public String getAuthor()
    {
        return author;
    }
    //----------------------------------------------------------------------------------------------
    /**
     * 出版社をセットする
     * @param arg 出版社
     */
    public void setPublisher(final Publisher arg)
    {
        publisher = arg;
    }
    //----------------------------------------------------------------------------------------------
    /**
     * 出版社を取得する
     * @return 出版社
     */
    public Publisher getPublisher()
    {
        return publisher;
    }
    //----------------------------------------------------------------------------------------------
    /**
     * Noteをセットする
     * @param arg Note
     */
    public void setNote(final String arg)
    {
        note = arg;
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
     * contentsをセットする
     * @param arg contents
     */
    public void setContents(final String arg)
    {
        contents = arg;
    }
    //----------------------------------------------------------------------------------------------
    /**
     * contentsを取得する
     * @return contents
     */
    public String getContents()
    {
        return contents;
    }
}
