////////////////////////////////////////////////////////////////////////////////////////////////////
package com.jyis.bookmanager.books;
////////////////////////////////////////////////////////////////////////////////////////////////////
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import org.springframework.boot.test.context.SpringBootTest;
////////////////////////////////////////////////////////////////////////////////////////////////////
/**
 * AbstractBookのテスト
 * @author 久保　由仁
 */
@SpringBootTest
public class AbstractBookTest
{
    /**
     * 正しいISBNを正しいと判定する
     */
    @ParameterizedTest
    @ValueSource(strings = {"9784807609260", "9784815615499"})
    public void isValidIsbn13WithValidIsbnTest(String isbn) throws Exception
    {
        final String MESSAGE = "正しいISBNを正しいと判定する";
        Assertions.assertTrue(AbstractBookImpl.isValidIsbn13(isbn), MESSAGE);
    }
    //----------------------------------------------------------------------------------------------
    /**
     * nullを不正と判定する
     */
    @Test
    public void isValidIsbn13WithValidNullTest() throws Exception
    {
        final String MESSAGE = "nullを不正と判定する";
        Assertions.assertFalse(AbstractBookImpl.isValidIsbn13(null), MESSAGE);
    }
    //----------------------------------------------------------------------------------------------
    /**
     * 不正なISBNを不正と判定する
     * @param isbn テストするISBN
     */
    @ParameterizedTest
    @ValueSource(strings = {"9784815615498", "978481561549", "97848156154991", "ABCDEFGHIJKLM"})
    public void isValidIsbn13WithInvalidIsbnTest(String isbn) throws Exception
    {
        final String MESSAGE = "不正なISBNを不正と判定する";
        Assertions.assertFalse(AbstractBookImpl.isValidIsbn13(isbn), MESSAGE);
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////
    private class AbstractBookImpl extends AbstractBook {}
}
