////////////////////////////////////////////////////////////////////////////////////////////////////
package com.jyis.bookmanager.books;
////////////////////////////////////////////////////////////////////////////////////////////////////
import java.util.Calendar;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import org.springframework.boot.test.context.SpringBootTest;

import com.jyis.bookmanager.exceptions.ValidationException;
import com.jyis.bookmanager.publishers.Publisher;
////////////////////////////////////////////////////////////////////////////////////////////////////
/**
 * BookFormのテスト
 * @author 久保　由仁
 */
@SpringBootTest
public class BookFormTest
{
    /** テスト用ISBNコード */
    private static final String ISBN = "978-481561549-9";
    //----------------------------------------------------------------------------------------------
    /**
     * BookオブジェクトからBookFormオブジェクトを構築できること
     */
    @Test
    public void constructTest() throws Exception
    {
        Book book = createBook();
        BookForm form = new BookForm(book);
        Assertions.assertEquals(form.getTitle(), "書誌名");
        Assertions.assertEquals(form.getAuthor(), "著者名");
        Assertions.assertEquals(form.getIsbn(), "9784815615499");
        Assertions.assertEquals(form.getContents(), "CONTENTS");
        Assertions.assertEquals(form.getNote(), "NOTE");
        Assertions.assertEquals(form.getPublisherName(), "ＳＢクリエイティブ");
        Assertions.assertEquals(form.getLanguage(), Language.JAPANESE);
        Assertions.assertEquals(form.getPublishedYear(), Calendar.getInstance().get(Calendar.YEAR));
    }
    //----------------------------------------------------------------------------------------------
    /**
     * Bookオブジェクトを作成できること
     */
    @Test
    public void toBookTest() throws Exception
    {
        BookForm form = createForm();
        Assertions.assertTrue(form.toBook() instanceof Book);
        Book book = form.toBook();
        Assertions.assertEquals(book.getTitle(), "吾輩は猫である");
        Assertions.assertEquals(book.getAuthor(), "夏目漱石");
        Assertions.assertEquals(book.getPublishedYear(), 1900);
        Assertions.assertEquals(book.getLanguage(), Language.CHINESE);
    }
    //----------------------------------------------------------------------------------------------
    /**
     * 不正なISBNをセットした場合にValidationExceptionがスローされること
     */
    @Test
    public void validateIsbnForInvalidTest() throws Exception
    {
        final String MESSAGE = "不正なISBNをセットした場合にValidationExceptionがスローされること";
        BookForm form = createForm();
        form.setIsbn("1234567890123");
        Exception exception = Assertions.assertThrows(ValidationException.class, () -> {
            form.validate();
        });
        final String EXPECT_MESSAGE = "isbn : 1234567890123\nisbn : 不正なISBNです。\n";
        Assertions.assertEquals(exception.getMessage(), EXPECT_MESSAGE, MESSAGE);
    }
    //----------------------------------------------------------------------------------------------
    /**
     * 不正なISBNをセットした場合にValidationExceptionがスローされること
     */
    @Test
    public void validateIsbnTest() throws Exception
    {
        final String MESSAGE = "正しいISBNをセットした場合にValidationExceptionがスローされないこと";
        BookForm form = createForm();
        form.setIsbn(ISBN);
        Assertions.assertDoesNotThrow(() -> form.validate(), MESSAGE);
    }
    //----------------------------------------------------------------------------------------------
    /**
     * 出版年が未来の年の場合にValidationExceptionがスローされること
     */
    @Test
    public void validateYearForInvalidTest() throws Exception
    {
        BookForm form = createForm();
        form.setPublishedYear(Calendar.getInstance().get(Calendar.YEAR) + 1);
        Exception exception = Assertions.assertThrows(ValidationException.class, () -> {
             form.validate();
        });
    }
    //----------------------------------------------------------------------------------------------
    /**
     * 正しい出版年でValidationExceptionがスローされないこと
     */
    @Test
    public void validateYearTest() throws Exception
    {
        final String MESSAGE = "正しい出版年でValidationExceptionがスローされないこと";
        BookForm form = createForm();
        form.setPublishedYear(Calendar.getInstance().get(Calendar.YEAR));
        Assertions.assertDoesNotThrow(() -> form.validate(), MESSAGE);
    }
    //----------------------------------------------------------------------------------------------
    /**
     * タイトルがブランクの場合にValidationExceptionがスローされること
     */
    @Test
    public void validateTitleWithBlankTest() throws Exception
    {
        final String MESSAGE = "タイトルがブランクの場合にValidationExceptionがスローされること";
        BookForm form = createForm();
        form.setTitle("");
        Exception exception = Assertions.assertThrows(ValidationException.class, () -> {
             form.validate();
        });
    }
    //----------------------------------------------------------------------------------------------
    /**
     * テスト用のBookオブジェクトを作成する
     * @return Bookオブジェクト
     */
    private Book createBook()
    {
        Book book = new Book("書誌名", "著者名");
        book.setNote("NOTE");
        book.setContents("CONTENTS");
        book.setIsbn(ISBN);
        book.setPublisherName("ＳＢクリエイティブ");
        book.setPublisher(new Publisher(book.getPublisherName()));
        book.setPublishedYear(Calendar.getInstance().get(Calendar.YEAR));
        book.setLanguage(Language.JAPANESE);
        return book;
    }
    //----------------------------------------------------------------------------------------------
    /**
     * BookFormオブジェクトを作成する
     * @return BookFormオブジェクト
     */
    private BookForm createForm()
    {
        BookForm form = new BookForm();
        form.setTitle("吾輩は猫である");
        form.setAuthor("夏目漱石");
        form.setPublishedYear(1900);
        form.setLanguage(Language.CHINESE);
        return form;
    }
}
