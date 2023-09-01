////////////////////////////////////////////////////////////////////////////////////////////////////
package com.jyis.bookmanager.ndl;
////////////////////////////////////////////////////////////////////////////////////////////////////
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.springframework.batch.item.Chunk;
import org.springframework.boot.test.context.SpringBootTest;

import org.sqlite.SQLiteDataSource;

import com.jyis.bookmanager.AbstractDaoImpl;
import com.jyis.bookmanager.books.ExtraInfo;
////////////////////////////////////////////////////////////////////////////////////////////////////
/**
 * Spring Batch Writerのテスト
 */
@SpringBootTest
public class NdlBookItemWriterTest 
{
    /** ロガー */
    private static final Logger logger = LoggerFactory.getLogger(NdlBookItemWriterTest.class);
    //---------------------------------------------------------------------------------------------
    /**
     * 書誌情報テーブルにデータが書き込まれていること
     */
    @Test
    public void writeTest() throws Exception
    {
        final String MESSAGE = "書誌情報テーブルにデータが書き込まれていること";
        final int BOOK_COUNT = 3;
        JobDaoMock dao = new JobDaoMock();
        NdlBookItemWriter writer = new NdlBookItemWriter(dao.getTestDataSource());
        Chunk<NdlInfo> chunk = createChunk(BOOK_COUNT);
        writer.write(chunk);
        List<Map<String, Object>> results = getExtraInfoTable(dao.getTestDataSource());
        Assertions.assertEquals(results.size(), BOOK_COUNT * 2, MESSAGE);
        for(Map<String, Object> record : results)
        {
            int bookId = (int)record.get("book_id");
            Assertions.assertTrue(bookId >= 1 && bookId <= BOOK_COUNT, MESSAGE);
            String noteType = (String)record.get("note_type");
            String regexp = String.format("^(NOTE|COVER)\\s\\[%d\\]$", bookId);
            Assertions.assertTrue(Pattern.matches(regexp, (String)record.get("note")), MESSAGE);
        }
    }
    //---------------------------------------------------------------------------------------------
    /**
     * 検証のために書誌情報のテーブルを読み込む
     * @param ds DataSourceオブジェクト
     * @return テーブルのデータ各レコードをフィールド名をキーにしたMapにし，Listに格納して返す
     */
    private List<Map<String, Object>> getExtraInfoTable(DataSource ds) throws SQLException
    {
        final String SQL = "SELECT book_id, note_type, note FROM extra_info ORDER BY book_id";
        List<Map<String, Object>> list = new ArrayList<>();
        try(Connection con = ds.getConnection();
            Statement stmt = con.createStatement();
            ResultSet result = stmt.executeQuery(SQL))
        {
            while(result.next())
            {
                Map<String, Object> record = new HashMap<>();
                record.put("book_id", result.getInt("book_id"));
                record.put("note_type", result.getString("note_type"));
                record.put("note", result.getString("note"));
                list.add(record);
            }
        }
        return list;
    }
    //---------------------------------------------------------------------------------------------
    /**
     * テスト用のChunkを構築する
     * @param size Chunkの大きさ
     * @return Chunk
     */
    private Chunk<NdlInfo> createChunk(int size)
    {
        Chunk<NdlInfo> chunk = new Chunk<>();
        for(int i = 0; i < size; i++)
        {
            NdlInfo ndlInfo = new NdlInfo(i + 1, "0123456789012");
            ndlInfo.set(ExtraInfo.COVER.getTypeCode(), String.format("COVER [%d]", i + 1));
            ndlInfo.set(ExtraInfo.NOTE.getTypeCode(), String.format("NOTE [%d]", i + 1));
            chunk.add(ndlInfo);
        }
        return chunk;
    }
}
