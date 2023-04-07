////////////////////////////////////////////////////////////////////////////////////////////////////
package com.jyis.bookmanager;
////////////////////////////////////////////////////////////////////////////////////////////////////
import java.io.Serializable;

import org.springframework.stereotype.Component;
////////////////////////////////////////////////////////////////////////////////////////////////////
/**
 * 抽象フォームクラス
 * @author 久保　由仁
 */
public abstract class AbstractForm implements Serializable
{
    /** メッセージ */
    protected String message;
    //----------------------------------------------------------------------------------------------
    /**
     * メッセージをセットする
     * @param arg メッセージ
     */
    public void setMessage(final String arg)
    {
        message = arg;
    }
    //----------------------------------------------------------------------------------------------
    /**
     * メッセージを取得する
     * @return メッセージ
     */
    public String getMessage()
    {
        return message;
    }
}
