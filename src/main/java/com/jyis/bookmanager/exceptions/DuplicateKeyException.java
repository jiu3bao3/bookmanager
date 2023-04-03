////////////////////////////////////////////////////////////////////////////////////////////////////
package com.jyis.bookmanager.exceptions;
////////////////////////////////////////////////////////////////////////////////////////////////////
/**
 * キー重複例外
 * @author 久保　由仁
 */
public class DuplicateKeyException extends AbstractException
{
    /**
     * デフォルトコンストラクタ
     */
    public DuplicateKeyException()
    {
        super();
    }
    //----------------------------------------------------------------------------------------------
    /**
     * コンストラクタ
     * @param message メッセージ
     */
    public DuplicateKeyException(String message)
    {
        super(message);
    }
    //----------------------------------------------------------------------------------------------
    /**
     * コンストラクタ
     * @param message メッセージ
     * @param cause 原因
     */
    public DuplicateKeyException(String message, Throwable cause)
    {
        super(message, cause);
    }
    //----------------------------------------------------------------------------------------------
    /**
     * コンストラクタ
     * @param cause 原因
     */
    public DuplicateKeyException(Throwable cause)
    {
        super(cause);
    }
}
