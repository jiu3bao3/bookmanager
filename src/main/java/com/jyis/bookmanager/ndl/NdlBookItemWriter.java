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
import org.springframework.batch.item.WriteFailedException;
import org.springframework.batch.item.Chunk;

import com.jyis.bookmanager.books.Book;
import com.jyis.bookmanager.books.ExtraInfo;
////////////////////////////////////////////////////////////////////////////////////////////////////
/**
 * Spring BatchのWriterクラス
 * @author 久保　由仁
 */
public class NdlBookItemWriter implements ItemWriter<NdlInfo>
{
    /** ロガー */
    private static final Logger logger = LoggerFactory.getLogger(NdlBookItemWriter.class);

    /** データソース */
    private DataSource dataSource;
    //---------------------------------------------------------------------------------------------
    /**
     * コンストラクタ
     * @param ds データソース
     */
    public NdlBookItemWriter(DataSource ds)
    {
        dataSource = ds;
    }
    //---------------------------------------------------------------------------------------------
    /**
     * 書誌情報をデータベースに書き込む
     * @param chunk 登録対象の書誌情報群
     * @throws ItemWriterException 書込エラー
     */
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
    /**
     * 書誌情報をテーブルに書き込む
     * @param bookId BookのID
     * @param type 書誌情報のタイプ
     * @param content 書誌情報本文
     * @throws ItemWriterException ＤＢ書込エラー
     */
    private void register(final int bookId, ExtraInfo type, final String content)
                                                                throws ItemWriterException
    {
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
            throw new WriteFailedException("ＤＢ登録に失敗しました。", e);
        }
    }
}
