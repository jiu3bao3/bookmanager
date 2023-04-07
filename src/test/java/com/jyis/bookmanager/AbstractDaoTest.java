////////////////////////////////////////////////////////////////////////////////////////////////////
package com.jyis.bookmanager;
////////////////////////////////////////////////////////////////////////////////////////////////////
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;
import org.springframework.test.context.junit.jupiter.SpringExtension;
////////////////////////////////////////////////////////////////////////////////////////////////////
/**
 * AbstractDaoのテスト
 * @author 久保　由仁
 */
@SpringBootTest
@ExtendWith(SpringExtension.class)
public class AbstractDaoTest
{
    /** テスト対象 */
    @Autowired
    private AbstractDaoImpl dao;
    //----------------------------------------------------------------------------------------------
    @Test
    public void calcOffsetTest()
    {
        Assertions.assertEquals(100, dao.calcOffset(1), "calcOffset()でオフセットを取得できること");
    }
    //----------------------------------------------------------------------------------------------
    @Test
    public void openTest() throws SQLException
    {
        try(Connection con = dao.open())
        {
            Assertions.assertFalse(con.getAutoCommit(), "自動コミットが無効であること");
        }
    }
}
////////////////////////////////////////////////////////////////////////////////////////////////////
/**
 * AbstractDaoのテスト用実装
 */
@Component
class AbstractDaoImpl extends AbstractDao<Object>
{
    public List<Object> selectAll(AbstractForm form)
    {
        return new ArrayList<Object>();
    }
    public Object selectOne(int id)
    {
        return null;
    }
    public void insert(Object arg) {}
    public void update(Object arg) {}
    public void delete(Object arg) {}
}
