////////////////////////////////////////////////////////////////////////////////////////////////////
package com.jyis.bookmanager.ndl;
////////////////////////////////////////////////////////////////////////////////////////////////////
import java.io.Serializable;
import java.util.Map;
import java.util.HashMap;

import com.jyis.bookmanager.books.ExtraInfo;
////////////////////////////////////////////////////////////////////////////////////////////////////
/**
 * 取得した書誌情報を保持するクラス
 * @author 久保　由仁
 */
public class NdlInfo implements Serializable
{
    /** BookのID */
    private int bookId;

    /** ISBN */
    private String isbn;

    /** 書誌情報のマップ */
    private Map<ExtraInfo, String> informations;
    //---------------------------------------------------------------------------------------------
    /**
     * ディフォルトコンストラクタ
     */
    public NdlInfo()
    {
        informations = new HashMap<>();
    }
    //---------------------------------------------------------------------------------------------
    /**
     * コンストラクタ
     * @param bookId BookのID
     * @param isbn ISBN
     */
    public NdlInfo(int bookId, String isbn)
    {
        this();
        this.bookId = bookId;
        this.isbn = isbn;
    }
    //---------------------------------------------------------------------------------------------
    /**
     * このオブジェクトの文字列表現を返す
     * @return このオブジェクトの文字列表現
     */
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("[書誌情報] book_id: %d, ISBN: %s\n", bookId, isbn));
        for(ExtraInfo key : informations.keySet())
        {
            sb.append(String.format("%s: %s\n", key.toString(), informations.get(key)));
        }
        return sb.toString();
    }
    //---------------------------------------------------------------------------------------------
    /**
     * BookのIDを取得する
     * @return BookのID
     */
    public int getBookId()
    {
        return bookId;
    }
    //---------------------------------------------------------------------------------------------
    /**
     * BookのIDを設定する
     * @param arg BookのID
     */
    public void setBookId(final int arg)
    {
        bookId = arg;
    }
    //---------------------------------------------------------------------------------------------
    public String getIsbn()
    {
        return isbn;
    }
    //---------------------------------------------------------------------------------------------
    /**
     * ISBNを設定する
     * @param arg ISBN
     */
    public void setIsbn(final String arg)
    {
        isbn = arg;
    }
    //---------------------------------------------------------------------------------------------
    /**
     * 書誌情報をセットする
     * @param key 書誌情報のTextType("02" "03", "04", "23")
     * @param content 本文Text
     */
    public void set(final String key, final String content)
    {
        informations.put(ExtraInfo.of(key), content);
    }
    //---------------------------------------------------------------------------------------------
    /**
     * 書誌情報を取得する
     * @param 書誌情報のタイプ
     * @return 書誌情報
     */
    public String get(final ExtraInfo type)
    {
        return informations.get(type);
    }
}
