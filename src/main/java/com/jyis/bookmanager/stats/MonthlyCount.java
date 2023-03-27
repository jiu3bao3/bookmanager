////////////////////////////////////////////////////////////////////////////////////////////////////
package com.jyis.bookmanager.stats;
////////////////////////////////////////////////////////////////////////////////////////////////////
import com.jyis.bookmanager.AbstractForm;
////////////////////////////////////////////////////////////////////////////////////////////////////
/**
 * 集計データクラス
 * @author 久保　由仁
 */
public final class MonthlyCount extends AbstractForm
{
    /** 年月 */
    private String yearMonth;
    
    /** 冊数 */
    private Integer count;
    //----------------------------------------------------------------------------------------------
    /** デフォルトコンストラクタ */
    public MonthlyCount() {}
    //----------------------------------------------------------------------------------------------
    /**
     * コンストラクタ
     * @param yearMonth 年月
     * @param count 冊数
     */
    public MonthlyCount(final String yearMonth, final Integer count)
    {
        this.yearMonth = yearMonth;
        this.count = count;
    }
    //----------------------------------------------------------------------------------------------
    /**
     * yearMonth を取得する
     * @return yearMonth
     */
    public String getYearMonth()
    {
        return yearMonth;
    }
    //----------------------------------------------------------------------------------------------
    /**
     * yearMonth を設定する
     * @param arg yearMonth
     */
    public void setYearMonth(final String arg)
    {
        yearMonth = arg;
    }
    //----------------------------------------------------------------------------------------------
    /**
     * count を取得する
     * @return count
     */
    public Integer getCount()
    {
        return count;
    }
    //----------------------------------------------------------------------------------------------
    /**
     * count を設定する
     * @param arg count
     */
    public void setCount(final Integer arg)
    {
        count = arg;
    }
}
