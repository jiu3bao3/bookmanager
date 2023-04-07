////////////////////////////////////////////////////////////////////////////////////////////////////
package com.jyis.bookmanager.books;
////////////////////////////////////////////////////////////////////////////////////////////////////
import java.util.NoSuchElementException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.springframework.boot.test.context.SpringBootTest;
////////////////////////////////////////////////////////////////////////////////////////////////////
/**
 * ExtraInfoのテスト
 * @author 久保　由仁
 */
@SpringBootTest
public class ExtraInfoTest
{
    /**
     * 正しいenumを取得できること
     */
    @Test
    public void ofTest() throws Exception
    {
        final String MESSAGE = "正しいenumを取得できること";
        Assertions.assertEquals(ExtraInfo.of("03"), ExtraInfo.NOTE, MESSAGE);
        Assertions.assertEquals(ExtraInfo.of("04"), ExtraInfo.COONTENTS, MESSAGE);
    }
    //----------------------------------------------------------------------------------------------
    /**
     * enum未定義の値でエラーがスローされること
     */
    @Test
    public void ofWithInvalidParamsTest() throws Exception
    {
        Exception exception = Assertions.assertThrows(NoSuchElementException.class, () -> {
            ExtraInfo.of("00");
        });
    }
    //----------------------------------------------------------------------------------------------
    /**
     * 指定の文字列化できること
     */
    @Test
    public void toStringTest() throws Exception
    {
        Assertions.assertEquals(ExtraInfo.NOTE.toString(), "NOTE(03)", "指定の文字列化できること");
    }
}
