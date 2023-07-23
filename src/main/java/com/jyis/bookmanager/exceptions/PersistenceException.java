////////////////////////////////////////////////////////////////////////////////////////////////////
package com.jyis.bookmanager.exceptions;
////////////////////////////////////////////////////////////////////////////////////////////////////
/**
 * 永続化失敗例外
 * @author 久保　由仁
 */
public class PersistenceException extends AbstractException
{
    /**
     * ディフォルトコンストラクタ
     */
    public PersistenceException()
    {
        super();
    }
    //----------------------------------------------------------------------------------------------
    /**
     * コンストラクタ
     * @param message メッセージ
     */
    public PersistenceException(String message)
    {
        super(message);
    }
    //----------------------------------------------------------------------------------------------
    /**
     * コンストラクタ
     * @param message メッセージ
     * @param cause 原因
     */
    public PersistenceException(String message, Throwable cause)
    {
        super(message, cause);
    }
    //----------------------------------------------------------------------------------------------
    /**
     * コンストラクタ
     * @param cause 原因
     */
    public PersistenceException(Throwable cause)
    {
        super(cause);
    }
}
