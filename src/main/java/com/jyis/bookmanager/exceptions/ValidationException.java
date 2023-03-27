////////////////////////////////////////////////////////////////////////////////////////////////////
package com.jyis.bookmanager.exceptions;
////////////////////////////////////////////////////////////////////////////////////////////////////
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
////////////////////////////////////////////////////////////////////////////////////////////////////
/**
 * バリデーションエラー
 * @author 久保　由仁
 */
public class ValidationException extends AbstractException
{
    /** エラーのあった項目と内容を保持するMap */
    private Map<String, List<String>> errors;
    //----------------------------------------------------------------------------------------------
    /**
     * コンストラクタ
     */
    public ValidationException()
    {
        errors = new HashMap<String, List<String>>();
    }
    //----------------------------------------------------------------------------------------------
    /**
     * コンストラクタ
     * @param errorMap エラーのあった項目と内容ListのMap
     */
    public ValidationException(final Map<String, List<String>> errorMap)
    {
        errors = (errorMap != null) ? errorMap : new HashMap<String, List<String>>();
    }
    //----------------------------------------------------------------------------------------------
    /**
     * エラーを追加する
     * @param field エラーのあった項目
     * @param message エラーメッセージ
     */
    public void addError(String field, String message)
    {
        List<String> messageList = errors.getOrDefault(field, new ArrayList<String>());
        messageList.add(message);
        errors.put(field, messageList);
    }
    //----------------------------------------------------------------------------------------------
    /**
     * エラーメッセージを返す
     * @return エラーメッセージ
     */
    @Override
    public String getMessage()
    {
        StringBuilder sb = new StringBuilder();
        for(Map.Entry<String, List<String>> error : errors.entrySet())
        {
            for(String message : error.getValue())
            {
                sb.append(String.format("%s : %s\n", error.getKey(), message));
            }
        }
        return sb.toString();
    }
}
