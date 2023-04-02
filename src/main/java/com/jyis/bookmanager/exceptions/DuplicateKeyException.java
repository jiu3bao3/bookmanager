////////////////////////////////////////////////////////////////////////////////////////////////////
package com.jyis.bookmanager.exceptions;
////////////////////////////////////////////////////////////////////////////////////////////////////
/**
 * キー重複例外
 * @author 久保　由仁
 */
public class DuplicateKeyException extends AbstractException
{
    public DuplicateKeyException()
    {
        super();
    }
    //----------------------------------------------------------------------------------------------
    public DuplicateKeyException(String message)
    {
        super(message);
    }
    //----------------------------------------------------------------------------------------------
    public DuplicateKeyException(String message, Throwable cause)
    {
        super(message, cause);
    }
    //----------------------------------------------------------------------------------------------
    public DuplicateKeyException(Throwable cause)
    {
        super(cause);
    }
}
