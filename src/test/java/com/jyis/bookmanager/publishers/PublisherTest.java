////////////////////////////////////////////////////////////////////////////////////////////////////
package com.jyis.bookmanager.publishers;
////////////////////////////////////////////////////////////////////////////////////////////////////
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.springframework.boot.test.context.SpringBootTest;
////////////////////////////////////////////////////////////////////////////////////////////////////
/**
 * Publisherクラスのテスト
 * @author 久保　由仁
 */
@SpringBootTest
public class PublisherTest
{
    //----------------------------------------------------------------------------------------------
    /**
     * コンストラクタのテスト
     */
    @Test
    public void constructorTest() throws Exception
    {
        final String MESSAGE = "コンストラクタのテスト";
        final String PUBLISHER_NAME = "出版社Ａ";
        Publisher publisher = new Publisher(PUBLISHER_NAME);
        Assertions.assertEquals(publisher.getName(), PUBLISHER_NAME, MESSAGE);
    }
}
