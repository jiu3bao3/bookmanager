
////////////////////////////////////////////////////////////////////////////////////////////////////
package com.jyis.bookmanager.ndl;
////////////////////////////////////////////////////////////////////////////////////////////////////
import java.util.Arrays;
import java.util.Collection;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import org.springframework.boot.test.context.SpringBootTest;

import com.jyis.bookmanager.books.ExtraInfo;
////////////////////////////////////////////////////////////////////////////////////////////////////
/**
 * NdlInfoクラスのテスト
 * @author 久保　由仁
 */
@SpringBootTest
public class NdlInfoTest
{
    /** テスト用ISBN */
    private static final String VALID_ISBN = "9784121027689";
    //---------------------------------------------------------------------------------------------
    /**
     * コンストラクタでインスタンスを生成できること
     */
    @Test
    public void constructorTest() throws Exception
    {
        final String MESSAGE = "コンストラクタでインスタンスを生成できること";
        Object obj = new NdlInfo(-1, VALID_ISBN);
        Assertions.assertTrue((obj instanceof NdlInfo), MESSAGE);
    }
    //---------------------------------------------------------------------------------------------
    /**
     * getIsbn()メソッドでISBNが返されること
     */
    @Test
    public void getIsbnTest() throws Exception
    {
        final String MESSAGE = "getIsbn()メソッドでISBNが返されること";
        NdlInfo instance = new NdlInfo(-1, VALID_ISBN);
        Assertions.assertEquals(VALID_ISBN, instance.getIsbn(), MESSAGE);
    }
    //---------------------------------------------------------------------------------------------
    /**
     *  getBookId()メソッドでISBNが返されること
     */
    @Test
    public void getBookIdTest() throws Exception
    {
        final String MESSAGE = "getBookId()メソッドでISBNが返されること";
        NdlInfo instance = new NdlInfo(-1, VALID_ISBN);
        Assertions.assertEquals(-1, instance.getBookId(), MESSAGE);
    }
    //---------------------------------------------------------------------------------------------
    /**
     * 書誌情報を設定できること
     */
    @ParameterizedTest
    @ValueSource(strings = {"02", "03", "04", "23"})
    public void setTestWithValidKey(final String key) throws Exception
    {
        final String MESSAGE = "書誌情報を設定できること";
        NdlInfo instance = new NdlInfo(-1, VALID_ISBN);
        String content = String.format("書誌情報(%s)", key);
        instance.set(key, content);
        Assertions.assertEquals(content, instance.get(ExtraInfo.of(key)), MESSAGE);
        Collection<ExtraInfo> expectedKeySet = Arrays.<ExtraInfo>asList(ExtraInfo.of(key));
        Assertions.assertTrue(instance.keySet().containsAll(expectedKeySet), MESSAGE);
    }
    //---------------------------------------------------------------------------------------------
    /**
     * 不正な書誌情報タイプを設定できないこと
     */
    @ParameterizedTest
    @ValueSource(strings = {"01", "05", ""})
    public void setTestWithInvalidKey(final String key) throws Exception
    {
        final String MESSAGE = "不正な書誌情報タイプを設定できないこと";
        NdlInfo instance = new NdlInfo(-1, VALID_ISBN);
        Exception exception = Assertions.assertThrows(NoSuchElementException.class, () -> {
            instance.set(key, String.format("書誌情報(%s)", key));
        });
    }
}
