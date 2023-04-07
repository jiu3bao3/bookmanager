////////////////////////////////////////////////////////////////////////////////////////////////////
package com.jyis.bookmanager.books;
////////////////////////////////////////////////////////////////////////////////////////////////////
import java.io.Serializable;

import com.jyis.bookmanager.AbstractForm;
import com.jyis.bookmanager.exceptions.IllegalOperationException;
////////////////////////////////////////////////////////////////////////////////////////////////////
/**
 * 検索条件フォーム
 * @author 久保　由仁
 */
public final class SearchForm extends AbstractForm implements Serializable
{
    /**
     * 検索コマンド定義
     */
    enum Command
    {
        PREV,
        NEXT,
        SEARCH
    }
    //----------------------------------------------------------------------------------------------
    /** タイトル */
    private String title;
    
    /** 著者名 */
    protected String author;
    
    /** 出版社名 */
    private String publisherName;
    
    /** 出版年（自） */
    private String publishedYearFrom;
    
    /** 出版年（至） */
    private String publishedYearTo;
    
    /** ページ番号 */
    private Integer page;
    
    /** 前頁リクエスト */
    private String prev;
    
    /** 後頁リクエスト */
    private String next;
    
    /** 検索リクエスト */
    private String search;
    //----------------------------------------------------------------------------------------------
    /**
     * このオブジェクトの文字列表現を返す
     * @return このオブジェクトの文字列表現
     */
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("検索条件");
        sb.append("タイトル：");
        sb.append(title);
        sb.append(", 著者名：");
        sb.append(author);
        sb.append(",出版社名");
        sb.append(publisherName);
        sb.append(",出版年");
        sb.append(publishedYearFrom);
        sb.append("～");
        sb.append(publishedYearTo);
        sb.append(",ページ番号：");
        sb.append(page);
        sb.append(", command:");
        sb.append(command().toString());
        return sb.toString();
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
        return (title == null) ? "" : title.trim();
    }
    //----------------------------------------------------------------------------------------------
    /**
     * 出版社名をセットする
     * @param arg 出版社名
     */
    public void setPublisherName(final String arg)
    {
        publisherName = arg;
    }
    //----------------------------------------------------------------------------------------------
    /**
     * 出版社名を取得する
     * @return 出版社名
     */
    public String getPublisherName()
    {
        return publisherName;
    }
    //----------------------------------------------------------------------------------------------
    /**
     * 出版年（From)をセットする
     * @param arg 出版年（From)
     */
    public void setPublishedYearFrom(final String arg)
    {
        publishedYearFrom = arg;
    }
    //----------------------------------------------------------------------------------------------
    /**
     * 出版年（From)を取得する
     * @return 出版年（From)
     */
    public String getPublishedYearFrom()
    {
        return publishedYearFrom;
    }
    //----------------------------------------------------------------------------------------------
    /**
     * 出版年（To)をセットする
     * @param arg 出版年（To)
     */
    public void setPublishedYearTo(final String arg)
    {
        publishedYearTo = arg;
    }
    //----------------------------------------------------------------------------------------------
    /**
     * 出版年（To)を取得する
     * @return 出版年（To)
     */
    public String getPublishedYearTo()
    {
        return publishedYearTo;
    }
    //----------------------------------------------------------------------------------------------
    /**
     * ページ番号をセットする
     * @param arg ページ番号
     */
    public void setPage(final Integer arg)
    {
        if(arg != null)
        {
            page = (arg < 0) ? 0 : arg;
        }
    }
    //----------------------------------------------------------------------------------------------
    /**
     * ページ番号を取得する
     * @return ページ番号
     */
    public Integer getPage()
    {
        return (page == null) ? 0 : page;
    }
    //----------------------------------------------------------------------------------------------
    /**
     * 検索ボタンをセットする
     * @param arg 検索ボタン
     */
    public void setSearch(final String arg)
    {
        search = arg;
    }
    //----------------------------------------------------------------------------------------------
    /**
     * 検索ボタンを取得する
     * @return 検索ボタン
     */
    public String getSearch()
    {
        return search;
    }
    //----------------------------------------------------------------------------------------------
    /**
     * 前のページボタンをセットする
     * @param arg 前のページボタン
     */
    public void setPrev(final String arg)
    {
        prev = arg;
    }
    //----------------------------------------------------------------------------------------------
    /**
     * 前のページボタンを取得する
     * @return 前のページボタン
     */
    public String getPrev()
    {
        return prev;
    }
    //----------------------------------------------------------------------------------------------
    /**
     * 次のページボタンをセットする
     * @param arg 次のページボタン
     */
    public void setNext(final String arg)
    {
        next = arg;
    }
    //----------------------------------------------------------------------------------------------
    /**
     * 次のページボタンを取得する
     * @return 次のページボタン
     */
    public String getNext()
    {
        return next;
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
        return (author == null) ? "" : author.trim();
    }
    //----------------------------------------------------------------------------------------------
    /**
     * 検索の指示を判定する
     * @return 検索指示enum
     */
    public Command command()
    {
        if(search != null)
        {
            return Command.SEARCH;
        }
        else if(prev != null)
        {
            return Command.PREV;
        }
        else if(next != null)
        {
            return Command.NEXT;
        }
        else
        {
            throw new IllegalOperationException("不正なコマンドです");
        }
    }
}
