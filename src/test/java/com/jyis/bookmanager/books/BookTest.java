////////////////////////////////////////////////////////////////////////////////////////////////////
package com.jyis.bookmanager.books;
////////////////////////////////////////////////////////////////////////////////////////////////////
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.springframework.boot.test.context.SpringBootTest;
////////////////////////////////////////////////////////////////////////////////////////////////////
/**
 * Bookクラスのテスト
 * @author 久保　由仁
 */
@SpringBootTest
public class BookTest
{
    /**
     * コンストラクタのテスト
     */
    @Test
    public void constructTest() throws Exception
    {
        final String MESSAGE = "コンストラクタのテスト";
        Book book = new Book("吾輩は猫である", "夏目漱石");
        Assertions.assertEquals(book.getTitle(), "吾輩は猫である", MESSAGE);
        Assertions.assertEquals(book.getAuthor(), "夏目漱石", MESSAGE);
    }
}
