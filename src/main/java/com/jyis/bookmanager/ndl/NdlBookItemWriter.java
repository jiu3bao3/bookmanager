////////////////////////////////////////////////////////////////////////////////////////////////////
package com.jyis.bookmanager.ndl;
////////////////////////////////////////////////////////////////////////////////////////////////////
import java.util.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.ItemWriterException;
import org.springframework.batch.item.Chunk;

import com.jyis.bookmanager.books.Book;
import com.jyis.bookmanager.books.ExtraInfo;
////////////////////////////////////////////////////////////////////////////////////////////////////
public class NdlBookItemWriter implements ItemWriter<NdlInfo>
{
    /** ロガー */
    private static final Logger logger = LoggerFactory.getLogger(NdlBookItemWriter.class);

    /** データソース */
    private DataSource dataSource;
    //---------------------------------------------------------------------------------------------
    public NdlBookItemWriter(DataSource ds)
    {
        dataSource = ds;
    }
    //---------------------------------------------------------------------------------------------
    @Override
    public void write(Chunk<? extends NdlInfo> chunk) throws ItemWriterException
    {
        List<? extends NdlInfo> list = chunk.getItems();
        for(NdlInfo ndlInfo : list)
        {
            for(ExtraInfo type : ndlInfo.keySet())
            {
                register(ndlInfo.getBookId(), type, ndlInfo.get(type));
            }
        }
    }
    //---------------------------------------------------------------------------------------------
    private void register(final int bookId, ExtraInfo type, final String content)
    {
        logger.info(String.format("%d----%s----%s", bookId, type, content));
        final String SQL = "INSERT INTO EXTRA_INFO(book_id, note_type, note) VALUES(?, ?, ?)";
        try(Connection con = dataSource.getConnection();
            PreparedStatement stmt = con.prepareStatement(SQL))
        {
            stmt.setInt(1, bookId);
            stmt.setString(2, type.getTypeCode());
            stmt.setString(3, content);
            int result = stmt.executeUpdate();
        }
        catch(SQLException e)
        {
            throw new RuntimeException(e);
        }
    }
}
