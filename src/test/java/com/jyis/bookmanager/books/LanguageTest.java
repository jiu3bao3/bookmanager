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
}
