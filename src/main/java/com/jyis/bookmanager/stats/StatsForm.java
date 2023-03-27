////////////////////////////////////////////////////////////////////////////////////////////////////
package com.jyis.bookmanager.stats;
////////////////////////////////////////////////////////////////////////////////////////////////////
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import com.jyis.bookmanager.AbstractForm;
import com.jyis.bookmanager.exceptions.ValidationException;
////////////////////////////////////////////////////////////////////////////////////////////////////
/**
 * 集計フォーム
 * @author 久保　由仁
 */
public final class StatsForm extends AbstractForm
{
    /** 年月のパターン */
    private static final String YEARMONTH_PATTERN = "\\d{4}[01]\\d";
    
    /** 年月の範囲の開始 */
    private String from;
    
    /** 年月の範囲の終了 */
    private String to;
    
    /** 検索結果のList */
    private List<MonthlyCount> summary;
    //----------------------------------------------------------------------------------------------
    /** デフォルトコンストラクタ */
    public StatsForm() {}
    //----------------------------------------------------------------------------------------------
    /**
     * コピーコンストラクタ
     * @param another コピー元オブジェクト
     */
    public StatsForm(final StatsForm another)
    {
        if(another != null)
        {
            from = another.getFrom();
            to = another.getTo();
            summary = another.getSummary();
        }
    }
    //----------------------------------------------------------------------------------------------
    /**
     * このオブジェクトの文字列表現を返す
     * @return このオブジェクトの文字列表現
     */
    @Override
    public String toString()
    {
        return String.format("%s -- %s", from, to);
    }
    //----------------------------------------------------------------------------------------------
    /**
     * バリデーションを行う
     * @throws ValidationException エラーがあった場合
     */
    public void validate()
    {
        Map<String, List<String>> errors = new HashMap<>();
        if(from != null && from.length() > 0)
        {
            if(!Pattern.matches(YEARMONTH_PATTERN, from))
            {
                errors.put("from", new ArrayList(Arrays.asList("YYYYMM形式で入力してください")));
            }
        }
        if(to != null && to.length() > 0)
        {
            if(!Pattern.matches(YEARMONTH_PATTERN, to))
            {
                errors.put("to", new ArrayList(Arrays.asList("YYYYMM形式で入力してください")));
            }
        }
        if(!errors.isEmpty())
        {
            throw new ValidationException(errors);
        }
    }
    //----------------------------------------------------------------------------------------------
    /**
     * 検索結果のListをセットする
     * @param arg 検索結果のList
     */
    public void setSummary(List<MonthlyCount> arg)
    {
        summary = arg;
    }
    //----------------------------------------------------------------------------------------------
    /**
     * 検索結果のListを取得する
     * @return 検索結果のList
     */
    public List<MonthlyCount> getSummary()
    {
        return summary;
    }
    //----------------------------------------------------------------------------------------------
    /**
     * from を取得する
     * @return from
     */
    public String getFrom()
    {
        return (from != null && from.length() > 0) ? from : null;
    }
    //----------------------------------------------------------------------------------------------
    /**
     * from を設定する
     * @param arg from
     */
    public void setFrom(final String arg)
    {
        from = arg;
    }
    //----------------------------------------------------------------------------------------------
    /**
     * to を取得する
     * @return to
     */
    public String getTo()
    {
        return (to != null && to.length() > 0) ? to : null;
    }
    //----------------------------------------------------------------------------------------------
    /**
     * to を設定する
     * @param arg to
     */
    public void setTo(final String arg)
    {
        to = arg;
    }
}
