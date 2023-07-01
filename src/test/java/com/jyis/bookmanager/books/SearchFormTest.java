////////////////////////////////////////////////////////////////////////////////////////////////////
package com.jyis.bookmanager.books;
////////////////////////////////////////////////////////////////////////////////////////////////////
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import org.springframework.boot.test.context.SpringBootTest;

import com.jyis.bookmanager.exceptions.IllegalOperationException;
////////////////////////////////////////////////////////////////////////////////////////////////////
/**
 * SearchFormのテスト
 * @author 久保　由仁
 */
@SpringBootTest
public class SearchFormTest
{
    /**
     * 検索ボタン押下
     */
    @Test
    public void commandSearchTest() throws Exception
    {
        final String MESSAGE = "検索ボタン押下";
        SearchForm form = new SearchForm();
        form.setSearch("検索");
        Assertions.assertEquals(SearchForm.Command.SEARCH, form.command(), MESSAGE);
    }
    //----------------------------------------------------------------------------------------------
    /**
     * 前のページボタン押下
     */
    @Test
    public void commandPrevTest() throws Exception
    {
        final String MESSAGE = "前のページボタン押下";
        SearchForm form = new SearchForm();
        form.setPrev("前のページへ");
        Assertions.assertEquals(SearchForm.Command.PREV, form.command(), MESSAGE);
    }
    //----------------------------------------------------------------------------------------------
    /**
     * 次のページボタン押下
     */
    @Test
    public void commandNextTest() throws Exception
    {
        final String MESSAGE = "次のページボタン押下";
        SearchForm form = new SearchForm();
        form.setNext("次のページへ");
        Assertions.assertEquals(SearchForm.Command.NEXT, form.command(), MESSAGE);
    }
    //----------------------------------------------------------------------------------------------
    /**
     * 不正なリクエスト
     */
    @Test
    public void commandInvalidTest() throws Exception
    {
        final String MESSAGE = "不正なリクエスト";
        SearchForm form = new SearchForm();
        Exception exception = Assertions.assertThrows(IllegalOperationException.class, () -> {
            form.command();
        }, MESSAGE);
    }
}
