////////////////////////////////////////////////////////////////////////////////////////////////////
package com.jyis.bookmanager.ndl;
////////////////////////////////////////////////////////////////////////////////////////////////////
import java.lang.reflect.Field;
import javax.sql.DataSource;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.boot.test.context.SpringBootTest;

import org.sqlite.SQLiteDataSource;

import com.jyis.bookmanager.AbstractDaoImpl;
////////////////////////////////////////////////////////////////////////////////////////////////////
@SpringBootTest
public class NdlBatchConfigurationTest
{
    /**
     * readerが想定通りに構築されていること
     */
    @Test
    public void readerTest() throws Exception
    {
        final String MESSAGE = "readerが想定通りに構築されていること";
        NdlBatchConfiguration config = new NdlBatchConfigurationImpl();
        JdbcCursorItemReader reader = config.reader();
        Assertions.assertTrue(reader.getDataSource() instanceof SQLiteDataSource, MESSAGE);
        Assertions.assertFalse(reader.isSaveState(), MESSAGE);
        Assertions.assertEquals(reader.getSql(), NdlBatchConfiguration.READER_SQL, MESSAGE);
    }
    //---------------------------------------------------------------------------------------------
    /**
     * writerが想定通りに構築されていること
     */
    @Test
    public void writerTest() throws Exception
    {
        final String MESSAGE = "writerが想定通りに構築されていること";
        NdlBatchConfiguration config = new NdlBatchConfigurationImpl();
        NdlBookItemWriter writer = config.writer();
        Field fld = NdlBookItemWriter.class.getDeclaredField("dataSource");
        fld.setAccessible(true);
        Assertions.assertTrue(fld.get(writer) instanceof SQLiteDataSource, MESSAGE);
    }
}
////////////////////////////////////////////////////////////////////////////////////////////////////
class NdlBatchConfigurationImpl extends NdlBatchConfiguration
{
    @Override
    protected DataSource getDatasouce()
    {
        SQLiteDataSource ds = new SQLiteDataSource();
        ds.setUrl(AbstractDaoImpl.SQLITE_FILE_PATH);
        return ds;
    }
}
