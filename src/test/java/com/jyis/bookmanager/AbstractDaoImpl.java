////////////////////////////////////////////////////////////////////////////////////////////////////
package com.jyis.bookmanager;
////////////////////////////////////////////////////////////////////////////////////////////////////
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
////////////////////////////////////////////////////////////////////////////////////////////////////
/**
 * AbstractDaoのテスト用実装
 * @author 久保　由仁
 */
@Component
public final class AbstractDaoImpl<T> extends AbstractDao<T> implements IDao<T>
{
    /** テストデータベースファイルのパス */
    public static final String SQLITE_FILE_PATH = "target/test.db";

    /** ロガー */
    private static final Logger logger = LoggerFactory.getLogger(AbstractDaoImpl.class);
    //----------------------------------------------------------------------------------------------
    @Override
    public List<T> selectAll(AbstractForm form)
    {
        return new ArrayList<T>();
    }
    //----------------------------------------------------------------------------------------------
    @Override
    public T selectOne(int id)
    {
        return null;
    }
    //----------------------------------------------------------------------------------------------
    @Override
    public void insert(T arg) {}
    //----------------------------------------------------------------------------------------------
   @Override
    public void update(T arg) {}
    //----------------------------------------------------------------------------------------------
    @Override
    public void delete(T arg) {}
    //----------------------------------------------------------------------------------------------
    /**
     * テスト用のデータベースコネクションを作成する
     * @return データベースコネクション
     */
    protected Connection open()
    {
        Connection con = null;
        try
        {
            Class.forName("org.sqlite.JDBC");
            con = DriverManager.getConnection(String.format("jdbc:sqlite:%s", SQLITE_FILE_PATH));
            con.setAutoCommit(false);
        }
        catch(SQLException |ClassNotFoundException e)
        {
            logger.error("テストデータベースへの接続に失敗しました。", e);
        }
        return con;
    }
}
