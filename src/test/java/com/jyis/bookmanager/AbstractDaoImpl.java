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
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.sqlite.Function;
import org.sqlite.SQLiteConfig;
import org.sqlite.SQLiteConnection;
import org.sqlite.SQLiteDataSource;
import org.sqlite.SQLiteOpenMode;

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
                     "CREATE TABLE IF NOT EXISTS books(ID INTEGER PRIMARY KEY AUTOINCREMENT, "
                     + "ISBN string, TITLE string, AUTHORS string, PUBLISHER string, "
                     + "PUBLISHER_ID number, PUBLISHED_ON number, LANGUAGE_ID number)",
                     
                     "CREATE TABLE IF NOT EXISTS read_books(id INTEGER PRIMARY KEY AUTOINCREMENT,"
                     + " book_id number, read_on date, note string)",
                     
                     "CREATE TABLE IF NOT EXISTS PUBLISHERS(ID INTEGER PRIMARY KEY AUTOINCREMENT,"
                     + "PUBLISHER_NAME string, ZIPCODE string, ADDRESS1 string, ADDRESS2 string,"
                     + "PHONE string, EMAIL string, NOTE text)",
                     
                     "CREATE TABLE IF NOT EXISTS EXTRA_INFO(ID INTEGER PRIMARY KEY AUTOINCREMENT,"
                     + "BOOK_ID NUMBER, NOTE_TYPE STRING, NOTE STRING)",
                     
                     "CREATE TABLE IF NOT EXISTS LANGUAGES(ID, DISPLAY_NAME)"
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
            DataSource ds = AbstractDaoImpl.getDataSource();
            con = ds.getConnection();
            con.setAutoCommit(false);
            createTables(con);
            createFunctions(con);
            con.commit();
        }
        catch(SQLException e)
        {
            logger.error("テストデータベースへの接続に失敗しました。", e);
            throw new RuntimeException(e);
        }
        return con;
    }
    //----------------------------------------------------------------------------------------------
    /**
     * テスト用のデータソースを取得する
     * @return データソース
     */
    public static DataSource getDataSource()
    {
        SQLiteDataSource ds = new SQLiteDataSource();
        SQLiteConfig sqliteConfig = ds.getConfig();
        sqliteConfig.setBusyTimeout(6_000);
        sqliteConfig.setOpenMode(SQLiteOpenMode.FULLMUTEX);
        ds.setUrl(String.format("jdbc:sqlite:%s", SQLITE_FILE_PATH));
        return ds;
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
