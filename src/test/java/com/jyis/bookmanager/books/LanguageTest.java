////////////////////////////////////////////////////////////////////////////////////////////////////
package com.jyis.bookmanager.books;
////////////////////////////////////////////////////////////////////////////////////////////////////
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.springframework.boot.test.context.SpringBootTest;
////////////////////////////////////////////////////////////////////////////////////////////////////
/**
 * Languageのテスト
 * @author 久保　由仁
 */
@SpringBootTest
public class LanguageTest
{
    /**
     * DBの値からenumを取得できること
     */
    @Test
    public void ofTest() throws Exception
    {
       Assertions.assertEquals(Language.of(1), Language.JAPANESE);
       Assertions.assertEquals(Language.of(2), Language.ENGLISH);
       Assertions.assertEquals(Language.of(3), Language.CHINESE);
    }
    //---------------------------------------------------------------------------------------------
    /**
     * 未設定の場合はNULLをof()が返すこと
     */
    @Test
    public void ofTestWithNull() throws Exception
    {
        Assertions.assertNull(Language.of(0));
        Assertions.assertNull(Language.of(null));
    }
    //---------------------------------------------------------------------------------------------
    /**
     * valueOfにより文字列からenumを取得できること
     */
    @Test
    public void valueOfTest() throws Exception
    {
        Assertions.assertEquals(Language.valueOf("JAPANESE"), Language.JAPANESE);
        Assertions.assertEquals(Language.valueOf("ENGLISH"), Language.ENGLISH);
        Assertions.assertEquals(Language.valueOf("CHINESE"), Language.CHINESE);
    }
}
