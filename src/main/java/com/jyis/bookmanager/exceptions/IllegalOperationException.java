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
    public IllegalOperationException()
    {
        super();
    }
    //----------------------------------------------------------------------------------------------
    public IllegalOperationException(String message)
    {
        super(message);
    }
    //----------------------------------------------------------------------------------------------
    public IllegalOperationException(String message, Throwable cause)
    {
        super(message, cause);
    }
    //----------------------------------------------------------------------------------------------
    public IllegalOperationException(Throwable cause)
    {
        super(cause);
    }
}
