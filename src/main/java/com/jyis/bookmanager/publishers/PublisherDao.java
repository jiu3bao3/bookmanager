////////////////////////////////////////////////////////////////////////////////////////////////////
package com.jyis.bookmanager.publishers;
////////////////////////////////////////////////////////////////////////////////////////////////////
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Component;

import com.jyis.bookmanager.AbstractDao;
import com.jyis.bookmanager.AbstractForm;
////////////////////////////////////////////////////////////////////////////////////////////////////
/**
 * 出版社DAOクラス
 * @author 久保　由仁
 */
@Component
public final class PublisherDao extends AbstractDao<Publisher>
{
    /** ロガー */
    private static final Logger logger = LoggerFactory.getLogger(PublisherDao.class);
    //----------------------------------------------------------------------------------------------
    /**
     * 検索
     * @param id ID
     * @return 検索結果
     */
    @Override
    public Publisher selectOne(final int id)
    {
        Publisher publisher = null;
        String SQL = "SELECT id, publisher_name, zipcode, address1, address2, phone, email "
                   + "FROM publishers P WHERE id = ?";
        try(Connection con = open();
            PreparedStatement stmt = con.prepareStatement(SQL))
        {
            stmt.setInt(1, id);
            try(ResultSet results = stmt.executeQuery())
            {
                if(results.next())
                {
                    publisher = new Publisher(results.getString("publisher_name"));
                    publisher.setId(results.getInt("id"));
                    publisher.setZip(results.getString("zipcode"));
                    publisher.setAddress1(results.getString("address1"));
                    publisher.setAddress2(results.getString("address2"));
                    publisher.setPhone(results.getString("phone"));
                    publisher.setEmail(results.getString("email"));
                }
            }
        }
        catch(SQLException e)
        {
            throw new RuntimeException(e);
        }
        return publisher;
    }
    //----------------------------------------------------------------------------------------------
    /**
     * 名前が一致する出版社を取得する
     * @param arg 出版社オブジェクト
     * @return 名前が一致した出版社オブジェクト
     */
    public Publisher selectOne(final Publisher arg)
    {
        String SQL = "SELECT id, publisher_name, zipcode, address1, address2, phone, email "
                   + "FROM publishers P WHERE 1 = 1 "
                   + "AND (P.publisher_name = ? OR 1 = ?)";
        Publisher publisher = null;
        try(Connection con = open();
            PreparedStatement stmt = con.prepareStatement(SQL))
        {
            if(arg.getName() != null)
            {
                stmt.setString(1, arg.getName());
                stmt.setInt(2, 0);
            }
            else
            {
                stmt.setNull(1, Types.NVARCHAR);
                stmt.setInt(2, 1);
            }
            try(ResultSet results = stmt.executeQuery())
            {
                if(results.next())
                {
                    publisher = new Publisher(results.getString("publisher_name"));
                    publisher.setId(results.getInt("id"));
                    publisher.setZip(results.getString("zipcode"));
                    publisher.setAddress1(results.getString("address1"));
                    publisher.setAddress2(results.getString("address2"));
                    publisher.setPhone(results.getString("phone"));
                    publisher.setEmail(results.getString("email"));
                }
            }
        }
        catch(SQLException e)
        {
            throw new RuntimeException(e);
        }
        return publisher;
    }
    //----------------------------------------------------------------------------------------------
    /**
     * 検索
     * @param form 検索条件フォーム
     * @return 検索結果のList
     */
    @Override
    public List<Publisher> selectAll(AbstractForm form)
    {
        String SQL = "SELECT id, publisher_name, zipcode, address1, address2, phone, email "
                   + "FROM publishers P WHERE 1 = 1";
        List<Publisher> list = new ArrayList<>();
        try(Connection con = open();
            PreparedStatement stmt = con.prepareStatement(SQL);
            ResultSet results = stmt.executeQuery())
        {
            while(results.next())
            {
                Publisher publisher = new Publisher(results.getString("publisher_name"));
                publisher.setId(results.getInt("id"));
                publisher.setZip(results.getString("zipcode"));
                publisher.setAddress1(results.getString("address1"));
                publisher.setAddress2(results.getString("address2"));
                publisher.setPhone(results.getString("phone"));
                publisher.setEmail(results.getString("email"));
                list.add(publisher);
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
     * 追加
     * @param arg 追加するオブジェクト
     */
    @Override
    public void insert(Publisher arg)
    {
        final String SQL = "INSERT INTO publishers "
                         + "(publisher_name, zipcode, address1, address2, phone, email) "
                         + " VALUES (?, ?, ?, ?, ?, ?)";
        try(Connection con = open();
            PreparedStatement stmt = con.prepareStatement(SQL))
        {
            stmt.setString(1, arg.getName());
            stmt.setString(2, arg.getZip());
            stmt.setString(3, arg.getAddress1());
            stmt.setString(4, arg.getAddress2());
            stmt.setString(5, arg.getPhone());
            stmt.setString(6, arg.getEmail());
            stmt.execute();
            con.commit();
        }
        catch(SQLException e)
        {
            throw new RuntimeException(e);
        }
    }
    //----------------------------------------------------------------------------------------------
    /**
     * 更新
     * @param arg 更新するオブジェクト
     */
    @Override
    public void update(Publisher arg)
    {
        final String SQL = "UPDATE publishers SET "
                         + "publisher_name = ?, zipcode = ?, address1 = ?, "
                         +  "address2 = ?, phone = ?, email = ? "
                         + "WHERE id = ?";
        try(Connection con = open();
            PreparedStatement stmt = con.prepareStatement(SQL))
        {
            stmt.setString(1, arg.getName());
            stmt.setString(2, arg.getZip());
            stmt.setString(3, arg.getAddress1());
            stmt.setString(4, arg.getAddress2());
            stmt.setString(5, arg.getPhone());
            stmt.setString(6, arg.getEmail());
            stmt.setInt(7, arg.getId());
            stmt.execute();
            con.commit();
        }
        catch(SQLException e)
        {
            throw new RuntimeException(e);
        }
    }
    //----------------------------------------------------------------------------------------------
    /**
     * 削除
     * @param arg 削除するオブジェクト
     */
    @Override
    public void delete(Publisher arg)
    {
        deleteById((arg != null) ? arg.getId() : null);
    }
    //----------------------------------------------------------------------------------------------
    /**
     * publishersのレコードをidを使って削除する
     * @param id publishsersのid
     * @throws IllegalArgumentException idがnull
     */
    public void deleteById(final Integer id)
    {
        final String SQL = "DELETE FROM publishers WHERE id = ?";
        if(id == null)
        {
            throw new IllegalArgumentException("idがnullです。");
        }
        try(Connection con = open();
            PreparedStatement stmt = con.prepareStatement(SQL))
        {
            stmt.setInt(1, id);
            stmt.execute();
            con.commit();
        }
        catch(SQLException e)
        {
            throw new RuntimeException(e);
        }
    }
}
