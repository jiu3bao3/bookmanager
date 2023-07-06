////////////////////////////////////////////////////////////////////////////////////////////////////
package com.jyis.bookmanager.publishers;
////////////////////////////////////////////////////////////////////////////////////////////////////
import java.io.Serializable;

import com.jyis.bookmanager.AbstractForm;
////////////////////////////////////////////////////////////////////////////////////////////////////
/**
 * 出版社検索フォーム
 * @author 久保　由仁
 */
public class SearchForm extends AbstractForm implements Serializable
{
    /** 検索文字列 */
    private String searchWord;
    //----------------------------------------------------------------------------------------------
    /**
     * デフォルトコンストラクタ
     */
    public SearchForm() {}
    //----------------------------------------------------------------------------------------------
    /**
     * コンストラクタ
     * @param arg 検索文字列
     */
    public SearchForm(final String arg)
    {
        searchWord = arg;
    }
    //----------------------------------------------------------------------------------------------
    /**
     * searchWord を取得する
     * @return searchWord
     */
    public String getSearchWord()
    {
        return searchWord;
    }
    //----------------------------------------------------------------------------------------------
    /**
     * searchWord を設定する
     * @param arg searchWord
     */
    public void setSearchWord(final String arg)
    {
        searchWord = arg;
    }
    //----------------------------------------------------------------------------------------------
    /**
     * このオブジェクトの文字列表現を返す
     * @return このオブジェクトの文字列表現
     */
    @Override
    public String toString()
    {
        return searchWord;
    }
}
