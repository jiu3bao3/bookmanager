////////////////////////////////////////////////////////////////////////////////////////////////////
package com.jyis.bookmanager.ndl;
////////////////////////////////////////////////////////////////////////////////////////////////////
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.batch.item.ItemProcessor;

import com.jyis.bookmanager.books.Book;
////////////////////////////////////////////////////////////////////////////////////////////////////
/**
 * 書誌情報取得プロセッサ
 * @author 久保　由仁
 */
public class NdlBookItemProcessor implements ItemProcessor<Book, NdlInfo>
{
    /** ロガー */
    private static final Logger logger = LoggerFactory.getLogger(NdlBookItemProcessor.class);
    //---------------------------------------------------------------------------------------------
    @Override
    public NdlInfo process(final Book book) throws Exception
    {
        logger.info(book.toString());
        return new NdlInfo();
    }
}
