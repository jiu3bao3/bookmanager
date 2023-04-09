////////////////////////////////////////////////////////////////////////////////////////////////////
package com.jyis.bookmanager;
////////////////////////////////////////////////////////////////////////////////////////////////////
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import  org.sqlite.Function;


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

    /** テーブル定義SQL */
    private static final String[] SQL_SCHEMAS = {
                       "CREATE TABLE IF NOT EXISTS books(ID number, ISBN string, "
                       + "TITLE string, AUTHORS string, PUBLISHER string, PUBLISHER_ID number,"
                       + "PUBLISHED_ON number, LANGUAGE_ID number, "
                       + "CONSTRAINT PK_BOOKS PRIMARY KEY(ID))",
                       
                       "CREATE TABLE IF NOT EXISTS read_books(id number, book_id number, "
                       + "read_on date, note string, CONSTRAINT PK_READ_BOOKS PRIMARY KEY(ID))",
                       "CREATE TABLE IF NOT EXISTS PUBLISHERS (ID number, PUBLISHER_NAME string, "
                       
                       + "ZIPCODE string, ADDRESS1 string, ADDRESS2 string,PHONE string,"
                       + " EMAIL string, NOTE text, CONSTRAINT PK_PUBLISHERS PRIMARY KEY(ID))"
    };

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
            createTables(con);
            createFunctions(con);
            con.commit();
        }
        catch(SQLException |ClassNotFoundException e)
        {
            logger.error("テストデータベースへの接続に失敗しました。", e);
            throw new RuntimeException(e);
        }
        return con;
    }
    //----------------------------------------------------------------------------------------------
    /**
     * テスト用のSQL関数を定義する
     * @param con データベースコネクション
     */
    protected void createFunctions(Connection con) throws SQLException
    {
        Function.create(con, "GETDATE", new Function() {
          @Override
          protected void xFunc() throws SQLException {
              SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
              result(formatter.format(Calendar.getInstance().getTime()));
          }
      });
    }
    //----------------------------------------------------------------------------------------------
    /**
     * テスト用のテーブルを作成する
     * @param con データベースコネクション
     */
    protected void createTables(Connection con) throws SQLException
    {
        for(String sql : SQL_SCHEMAS)
        {
            try(Statement stmt = con.createStatement())
            {
                logger.info(sql);
                stmt.executeUpdate(sql);
            }
        }
    }
}
