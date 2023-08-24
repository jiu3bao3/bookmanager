////////////////////////////////////////////////////////////////////////////////////////////////////
package com.jyis.bookmanager;
////////////////////////////////////////////////////////////////////////////////////////////////////
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import javax.sql.DataSource;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
////////////////////////////////////////////////////////////////////////////////////////////////////
/**
 * 抽象DAOクラス
 * @author 久保　由仁
 */
@Component
public abstract class AbstractDao<T> implements IDao<T>
{
    /** ロガー */
    private static final Logger logger = LoggerFactory.getLogger(AbstractDao.class);

    /** application設定 */
    @Autowired
    protected Environment env;
    
    /** 1ページのデフォルトデータ数 */
    protected static final int DEFALUT_PAGE_SIZE = 100;
    //----------------------------------------------------------------------------------------------
    /**
     * 検索
     * @param form 検索条件フォーム
     * @return 検索結果のList
     */
    public abstract List<T> selectAll(AbstractForm form);
    //----------------------------------------------------------------------------------------------
    /**
     * 検索
     * @param id ID
     * @return 検索結果
     */
    public abstract T selectOne(int id);
    //----------------------------------------------------------------------------------------------
    /**
     * 追加
     * @param arg 追加するオブジェクト
     */
    public abstract void insert(T arg);
    //----------------------------------------------------------------------------------------------
    /**
     * 更新
     * @param arg 更新するオブジェクト
     */
    public abstract void update(T arg);
    //----------------------------------------------------------------------------------------------
    /**
     * 削除
     * @param arg 削除するオブジェクト
     */
    public abstract void delete(T arg);
    //----------------------------------------------------------------------------------------------
    /**
     * データベースコネクションを返す
     * @return データベースコネクション
     */
    protected Connection open()
    {
        Connection con = null;
        try
        {
            DataSource dataSource = getDatasouce();
            con = dataSource.getConnection();
            con.setAutoCommit(false);
        }
        catch(SQLException e)
        {
            throw new RuntimeException(e);
        }
        return con;
    }
    //----------------------------------------------------------------------------------------------
    /**
     * データソースオブジェクトを返す
     * @return DataSourceオブジェクト
     */
    protected DataSource getDatasouce()
    {
        SQLServerDataSource ds = new SQLServerDataSource();
        ds.setURL(env.getProperty("spring.datasource.url"));
        ds.setUser(env.getProperty("spring.datasource.username"));
        ds.setPassword(env.getProperty("spring.datasource.password"));
        return ds;
    }
    //----------------------------------------------------------------------------------------------
    /**
     * 1ページのデータ数を返す
     * @return 1ページのデータ数
     */
    protected int getPageSize()
    {
        return DEFALUT_PAGE_SIZE;
    }
    //----------------------------------------------------------------------------------------------
    /**
     * offsetを計算する
     * @param pageNo pageNo ページの番号
     * @return offset
     */
    protected int calcOffset(final int pageNo)
    {
        return pageNo * DEFALUT_PAGE_SIZE;
    }
}
