////////////////////////////////////////////////////////////////////////////////////////////////////
package com.jyis.bookmanager.exceptions;
////////////////////////////////////////////////////////////////////////////////////////////////////
/**
 * 不正オペレーション例外
 * @author 久保　由仁
 */
public class IllegalOperationException extends AbstractException
{
    //----------------------------------------------------------------------------------------------
    /** 
     * デフォルトコンストラクタ
     */
    public IllegalOperationException()
    {
        super();
    }
    //----------------------------------------------------------------------------------------------
    /**
     * コンストラクタ
     * @param message メッセージ
     */
    public IllegalOperationException(String message)
    {
        super(message);
    }
    //----------------------------------------------------------------------------------------------
    /**
     * コンストラクタ
     * @param message メッセージ
     * @param cause 原因
     */
    public IllegalOperationException(String message, Throwable cause)
    {
        super(message, cause);
    }
    //----------------------------------------------------------------------------------------------
    /**
     * コンストラクタ
     * @param cause 原因
     */
    public IllegalOperationException(Throwable cause)
    {
        super(cause);
    }
}
