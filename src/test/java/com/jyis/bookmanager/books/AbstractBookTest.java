////////////////////////////////////////////////////////////////////////////////////////////////////
package com.jyis.bookmanager.books;
////////////////////////////////////////////////////////////////////////////////////////////////////
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
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
    //----------------------------------------------------------------------------------------------
    /**
     * 10桁のISBNを13桁に変換できる
     */
    @Test
    public void toLongIsbnTest() throws Exception
    {
        final String MESSAGE = "10桁のISBNを13桁に変換できる";
        Assertions.assertEquals(AbstractBookImpl.toLongIsbn("4888882290"), "9784888882293");
        Assertions.assertEquals(AbstractBookImpl.toLongIsbn("4883375315"), "9784883375318");
        Assertions.assertEquals(AbstractBookImpl.toLongIsbn("4873114799"), "9784873114798");
    }
    //----------------------------------------------------------------------------------------------
    /**
     * NULLの場合にはNULLを返す
     */
    @Test
    public void toLongIsbnWithNullTest() throws Exception
    {
        final String MESSAGE = "NULLの場合にはNULLを返す";
        Assertions.assertNull(AbstractBookImpl.toLongIsbn(null), MESSAGE);
    }
    //----------------------------------------------------------------------------------------------
    /**
     * 10桁ではないISBNを不正と判定する
     */
    @ParameterizedTest
    @ValueSource(strings = {"481561549", "84815615400", "XXXXXXXXXX"})
    public void toLongIsbnWithInvalidTest(final String isbn) throws Exception
    {
        final String MESSAGE = "10桁ではないISBNを不正と判定する";
        Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            AbstractBookImpl.toLongIsbn(isbn);
        }, MESSAGE);
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////
    private class AbstractBookImpl extends AbstractBook {}
}
