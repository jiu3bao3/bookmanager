////////////////////////////////////////////////////////////////////////////////////////////////////
package com.jyis.bookmanager.books;
////////////////////////////////////////////////////////////////////////////////////////////////////
import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jyis.bookmanager.AbstractForm;
////////////////////////////////////////////////////////////////////////////////////////////////////
/**
 * 抽象本クラス
 * @author 久保　由仁
 */
public abstract class AbstractBook extends AbstractForm implements Serializable
{
    /** ISBN（13桁）の桁数 */
    public static final int ISBN13_LENGTH = 13;

    /** ISBN(10桁)の桁数 */
    public static final int ISBN10_LENGTH = 10;

    /** ロガー */
    private static final Logger logger = LoggerFactory.getLogger(AbstractBook.class);
    
    /** タイトル */
    protected String title;
    
    /** 著者名 */
    protected String author;
    
    /** 出版年 */
    protected Integer publishedYear;
    
    /** ISBN */
    protected String isbn;
    
    /** 出版社名 */
    protected String publisherName;
    
    /** 出版社ID */
    protected Integer publisherId;
    
    /** 言語 */
    protected Language language;
    //----------------------------------------------------------------------------------------------
    /**
     * ISBN（13桁）の妥当性を検査する
     * @param isbn ISBNコード
     * @return 検査結果
     */
    public static boolean isValidIsbn13(final String isbn)
    {
        if(isbn == null || isbn.length() != ISBN13_LENGTH)
        {
            return false;
        }
        int summary = 0;
        int lastDigit = 0;
        try
        {
            for(int i = 0; i < (ISBN13_LENGTH - 1); i++)
            {
                int digit = Integer.parseInt(String.valueOf(isbn.charAt(i)));
                summary += digit * ((i % 2 == 0) ? 1 : 3);
            }
            lastDigit = Integer.parseInt(String.valueOf(isbn.charAt(ISBN13_LENGTH - 1)));
        }
        catch(NumberFormatException e)
        {
            return false;
        }
        return (10 - (summary % 10)) % 10 == lastDigit;
    }
    //----------------------------------------------------------------------------------------------
    /**
     * 短い形式のISBNを13桁のコードに変換する
     * @param shortIsbn 10桁のISBNコード
     * @return 13桁のISBNコード
     */
    public static synchronized String toLongIsbn(final String shortIsbn)
    {
        if(shortIsbn == null) return null;
        if(shortIsbn.length() != ISBN10_LENGTH) throw new IllegalArgumentException();

        StringBuffer sb = new StringBuffer("978");
        int summary = 9 + 3 * 7 + 8;
        for(int i = 0; i < (ISBN10_LENGTH - 1) ; i++)
        {
            String digit = shortIsbn.substring(i, i + 1);
            sb.append(digit);
            summary += ((i % 2 == 1) ? 1 : 3 ) * Integer.parseInt(digit);
        }
        sb.append(String.valueOf((10 - (summary % 10)) % 10));
        return sb.toString();
    }
    //----------------------------------------------------------------------------------------------
    /**
     * オブジェクトの文字列表現を返す
     * @return オブジェクトの文字列表現
     */
    @Override
    public String toString()
    {
        final String FORMAT = "%s, AUTHOR: %s, TITLE: %s (%d)";
        String className = this.getClass().getName();
        return String.format(FORMAT, className,  author, title, publishedYear);
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
     * 出版年をセットする
     * @param arg 出版年
     */
    public void setPublishedYear(final Integer arg)
    {
        publishedYear = arg;
    }
    //----------------------------------------------------------------------------------------------
    /**
     * 出版年を取得する
     * @return 出版年
     */
    public Integer getPublishedYear()
    {
        return publishedYear;
    }
    //----------------------------------------------------------------------------------------------
    /**
     * ISBNをセットする
     * @param arg ISBN
     */
    public void setIsbn(final String arg)
    {
        isbn = (arg != null) ? arg.replaceAll("-", "") : null;
    }
    //----------------------------------------------------------------------------------------------
    /**
     * ISBNを取得する
     * @return ISBN
     */
    public String getIsbn()
    {
        return isbn;
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
     * 出版社IDをセットする
     * @param arg 出版社ID
     */
    public void setPublisherId(final Integer arg)
    {
        publisherId = arg;
    }
    //----------------------------------------------------------------------------------------------
    /**
     * 出版社IDを取得する
     * @return 出版社ID
     */
    public Integer getPublisherId()
    {
        return publisherId;
    }
    //----------------------------------------------------------------------------------------------
    /**
     * 使用言語をセットする
     * @param arg 言語
     */
    public void setLanguage(final Language arg)
    {
        language = arg;
    }
    //----------------------------------------------------------------------------------------------
    /**
     * 使用言語をセットする
     * @param arg 言語
     */
    public void setLanguage(final int arg)
    {
        language = (arg == 0) ? null : Language.of(arg);
    }
    //----------------------------------------------------------------------------------------------
    /**
     * 使用言語をセットする
     * @param arg 言語
     */
    public void setLanguage(final String arg)
    {
        language = (arg == null) ? null : Language.valueOf(arg);
    }
    //----------------------------------------------------------------------------------------------
    /**
     * 使用言語を取得する
     * @return 言語
     */
    public Language getLanguage()
    {
        return language;
    }
}
