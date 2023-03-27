////////////////////////////////////////////////////////////////////////////////////////////////////
package com.jyis.bookmanager.publishers;
////////////////////////////////////////////////////////////////////////////////////////////////////
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
     * 検索
     * @param form 検索条件フォーム
     * @return 検索結果のList
     */
    @Override
    public List<Publisher> selectAll(AbstractForm form)
    {
        throw new UnsupportedOperationException();
    }
    //----------------------------------------------------------------------------------------------
    /**
     * 追加
     * @param arg 追加するオブジェクト
     */
    @Override
    public void insert(Publisher arg)
    {
        throw new UnsupportedOperationException();
    }
    //----------------------------------------------------------------------------------------------
    /**
     * 更新
     * @param arg 更新するオブジェクト
     */
    @Override
    public void update(Publisher arg)
    {
        throw new UnsupportedOperationException();
    }
    //----------------------------------------------------------------------------------------------
    /**
     * 削除
     * @param arg 削除するオブジェクト
     */
    @Override
    public void delete(Publisher arg)
    {
        throw new UnsupportedOperationException();
    }
}
