////////////////////////////////////////////////////////////////////////////////////////////////////
package com.jyis.bookmanager.stats;
////////////////////////////////////////////////////////////////////////////////////////////////////
import java.util.List;

import com.jyis.bookmanager.AbstractForm;
////////////////////////////////////////////////////////////////////////////////////////////////////
/**
 * 集計データクラス
 * @author 久保　由仁
 */
public final class MonthlyCount extends AbstractForm
{
    /** 年月 */
    private String year;
    
    /** 冊数 */
    private List<Integer> count;
    
    /** 合計冊数 */
    private Integer total;
    //----------------------------------------------------------------------------------------------
    /** デフォルトコンストラクタ */
    public MonthlyCount() {}
    //----------------------------------------------------------------------------------------------
    /**
     * コンストラクタ
     * @param year 年月
     * @param count 冊数
     */
    public MonthlyCount(final String year, final List<Integer> count)
    {
        this.year = year;
        this.count = count;
    }
    //----------------------------------------------------------------------------------------------
    /**
     * コンストラクタ
     * @param year 年月
     * @param count 冊数
     * @param total 合計冊数
     */
    public MonthlyCount(final String year, final List<Integer> count, Integer total)
    {
        this(year, count);
        this.total = total;
    }
    //----------------------------------------------------------------------------------------------
    /**
     * year を取得する
     * @return year
     */
    public String getYear()
    {
        return year;
    }
    //----------------------------------------------------------------------------------------------
    /**
     * yearMonth を設定する
     * @param arg yearMonth
     */
    public void setYear(final String arg)
    {
        year = arg;
    }
    //----------------------------------------------------------------------------------------------
    /**
     * count を取得する
     * @return count
     */
    public List<Integer> getCount()
    {
        return count;
    }
    //----------------------------------------------------------------------------------------------
    /**
     * count を設定する
     * @param arg count
     */
    public void setCount(final List<Integer> arg)
    {
        count = arg;
    }
    //----------------------------------------------------------------------------------------------
    /**
     * 合計を取得する
     * @return 合計
     */
    public Integer getTotal()
    {
        return total;
    }
    //----------------------------------------------------------------------------------------------
    /**
     * 合計をセットする
     * @param arg 合計
     */
    public void setTotal(final Integer arg)
    {
        total = arg;
    }
}
