////////////////////////////////////////////////////////////////////////////////////////////////////
package com.jyis.bookmanager.stats;
////////////////////////////////////////////////////////////////////////////////////////////////////
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Component;

import com.jyis.bookmanager.AbstractDao;
import com.jyis.bookmanager.AbstractForm;
////////////////////////////////////////////////////////////////////////////////////////////////////
/**
 * 集計結果DAO
 * @author 久保　由仁
 */
@Component
public class StatsDao extends AbstractDao<MonthlyCount>
{
    /** ロガー */
    private static final Logger logger = LoggerFactory.getLogger(StatsDao.class);
    //----------------------------------------------------------------------------------------------
    /**
     * 検索
     * @param form 検索条件フォーム
     * @return 検索結果のList
     */
    @Override
    public List<MonthlyCount> selectAll(AbstractForm form)
    {
        StatsForm statsForm = (form instanceof StatsForm) ? (StatsForm)form : new StatsForm();
        String from = (statsForm.getFrom() != null) ? statsForm.getFrom() : "000001";
        String to = (statsForm.getTo() != null) ? statsForm.getTo() : "999912";
        List<MonthlyCount> list = new ArrayList<>();
        final String SQL = "SELECT * FROM ( SELECT SUBSTRING(V.YEAR_MONTH, 1,4) AS YEAR, "
                         + "SUBSTRING(V.YEAR_MONTH, 5,2)AS MON, V.CNT FROM V_MONTHLY_TOTAL V "
                         + "WHERE V.YEAR_MONTH BETWEEN ? AND ? ) M PIVOT"
                         + " (SUM(CNT) FOR MON IN ([01],[02],[03],[04],[05],[06],[07],"
                         + "[08],[09],[10],[11],[12])) AS pv ORDER BY YEAR DESC";
        try(Connection con = open();
            PreparedStatement stmt = con.prepareStatement(SQL))
        {
            stmt.setString(1, from);
            stmt.setString(2, to);
            try(ResultSet results = stmt.executeQuery())
            {
                while(results.next())
                {
                    String year = results.getString("year");
                    List<Integer> countList = new ArrayList<>();
                    int total = 0;
                    for(int month = 0; month < 12; month++)
                    {
                        int value = results.getInt(month + 2);
                        total += value;
                        countList.add((value == 0 && results.wasNull()) ? null : value);
                    }
                    list.add(new MonthlyCount(year, countList, total));
                }
            }
        }
        catch(SQLException e)
        {
            throw new RuntimeException(e);
        }
        return list;
    }
    //----------------------------------------------------------------------------------------------
    /**
     * 検索
     * @param id ID
     * @return 検索結果
     */
    public MonthlyCount selectOne(int id)
    {
        throw new UnsupportedOperationException();
    }
    //----------------------------------------------------------------------------------------------
    /**
     * 追加
     * @param arg 追加するオブジェクト
     */
    public void insert(MonthlyCount arg)
    {
        throw new UnsupportedOperationException();
    }
    //----------------------------------------------------------------------------------------------
    /**
     * 更新
     * @param arg 更新するオブジェクト
     */
    public void update(MonthlyCount arg)
    {
        throw new UnsupportedOperationException();
    }
    //----------------------------------------------------------------------------------------------
    /**
     * 削除
     * @param arg 削除するオブジェクト
     */
    public void delete(MonthlyCount arg)
    {
        throw new UnsupportedOperationException();
    }
}
