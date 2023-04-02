////////////////////////////////////////////////////////////////////////////////////////////////////
package com.jyis.bookmanager.books;
////////////////////////////////////////////////////////////////////////////////////////////////////
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import jakarta.servlet.ServletContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.jyis.bookmanager.exceptions.ValidationException;
////////////////////////////////////////////////////////////////////////////////////////////////////
/**
 * コントローラ
 * @author 久保　由仁
 */
@Controller
public final class BookController
{
    /** ロガー */
    private static final Logger logger = LoggerFactory.getLogger(BookController.class);

    /** サービス */
    @Autowired
    private BookManagementService service;
    
    /** サーブレットコンテキスト */
    @Autowired
    private ServletContext context;
    //----------------------------------------------------------------------------------------------
    /** 
     * ルート
     */
    @RequestMapping(value="/", method=RequestMethod.GET)
    public ModelAndView welcome()
    {
        return new ModelAndView("welcome");
    }
    //----------------------------------------------------------------------------------------------
    @RequestMapping(value="/index", method=RequestMethod.GET)
    public ModelAndView index()
    {
        ModelAndView modelAndView = new ModelAndView("index", "bookList", Arrays.asList());
        modelAndView.addObject("form", new SearchForm());
        return modelAndView;
    }
    //----------------------------------------------------------------------------------------------
    /**
     * 読書記録を検索する
     * @param form 検索フォーム
     * @return 検索結果ModelAndViewオブジェクト
     */
    @RequestMapping(value="/index", method=RequestMethod.POST)
    public ModelAndView search(@ModelAttribute SearchForm form)
    {
        switch(form.command())
        {
            case PREV: // 前のページ
                form.setPage(form.getPage() - 1);
                break;
            case NEXT: // 次のページ
                form.setPage(form.getPage() + 1);
                break;
            default:
                form.setPage(0);
        }
        logger.debug(form.toString());
        List<Book> list = service.selectBooks(form);
        ModelAndView modelAndView = new ModelAndView("index", "bookList", list);
        modelAndView.addObject("form", form);
        return modelAndView;
    }
    //----------------------------------------------------------------------------------------------
    /**
     * 新規登録画面を表示する
     * @return 新規登録ModelAndViewオブジェクト
     */
    @RequestMapping(value="/new", method=RequestMethod.GET)
    public ModelAndView newBook()
    {
        ModelAndView modelAndView = new ModelAndView("edit", "form", new BookForm());
        modelAndView.addObject("action", "new");
        return modelAndView;
    }
    //----------------------------------------------------------------------------------------------
    @RequestMapping(value="/new", method=RequestMethod.POST)
    public ModelAndView createBook(@ModelAttribute BookForm form)
    {
        logger.info("create:" + form.toString());
        try
        {
            service.registerBook(form);
            form.setMessage("登録しました。");
        }
        catch(Exception e)
        {
            logger.warn(e.getMessage());
            form.setMessage(e.getMessage());
        }
        ModelAndView modelAndView = new ModelAndView("edit", "form", form);
        return modelAndView;
    }
    //----------------------------------------------------------------------------------------------
    @RequestMapping(value="/{id}/edit", method=RequestMethod.GET)
    public ModelAndView editBook(@PathVariable("id") Integer id)
    {
        Book book = service.selectBook(id);
        BookForm form = (book != null) ? new BookForm(book) : new BookForm();
        ModelAndView modelAndView = new ModelAndView("edit", "form", form);
        modelAndView.addObject("_method", "PATCH");
        modelAndView.addObject("action", String.format("/%d/update", id));
        return modelAndView;
    }
    //----------------------------------------------------------------------------------------------
    /**
     * 読書情報を更新する
     * @param id 本のID
     * @param form リクエストForm
     * @return 画面表示用ModelAndViews
     */
    @RequestMapping(value="/{id}/update", method={RequestMethod.PATCH, RequestMethod.POST})
    public ModelAndView updateBook(@PathVariable("id") Integer id, @ModelAttribute BookForm form)
    {
        try
        {
            service.updateBook(id, form);
            form.setMessage("更新しました。");
        }
        catch(Exception e)
        {
            logger.warn("", e);
            e.printStackTrace();
            form.setMessage(e.getMessage());
        }
        ModelAndView modelAndView = new ModelAndView("edit", "form", form);
        return modelAndView;
    }
    //----------------------------------------------------------------------------------------------
    /**
     * 読書記録を削除する
     * @param id 削除対象のID
     */
    @RequestMapping(value="/{id}/delete", method={RequestMethod.DELETE, RequestMethod.POST})
    public ModelAndView deleteBook(@PathVariable("id") Integer id)
    {
        logger.info(String.format("delete:%d", id));
        service.deleteBook(id);
        SearchForm form = new SearchForm();
        List<Book> list = service.selectBooks(form);
        form.setMessage("削除しました。");
        ModelAndView modelAndView = new ModelAndView("index", "bookList", list);
        modelAndView.addObject("form", form);
        return modelAndView;
    }
}
