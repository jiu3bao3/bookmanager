////////////////////////////////////////////////////////////////////////////////////////////////////
package com.jyis.bookmanager.exceptions;
////////////////////////////////////////////////////////////////////////////////////////////////////
/**
 * カスタム例外基底クラス
 * @author 久保　由仁
 */
public abstract class AbstractException extends RuntimeException
{
    /**
     * 詳細メッセージにnullを使用して、新しい実行時例外を構築します。
     */
    protected AbstractException()
    {
        super();
    }
    //----------------------------------------------------------------------------------------------
    /**
     * 指定された詳細メッセージを使用して、新規例外を構築します。
     * @param message メッセージ
     */
    protected AbstractException(String message)
    {
        super(message);
    }
    //----------------------------------------------------------------------------------------------
    /**
     * 指定された詳細メッセージおよび原因を使用して新しい実行時例外を構築します。
     * @param message メッセージ
     * @param cause 原因
     */
    protected AbstractException(String message, Throwable cause)
    {
        super(message, cause);
    }
    //----------------------------------------------------------------------------------------------
    /**
     * 指定された原因と詳細メッセージを持つ新しい実行時例外を構築します。
     * @param cause 原因
     */
    protected AbstractException(Throwable cause)
    {
        super(cause);
    }
}
