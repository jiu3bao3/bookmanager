////////////////////////////////////////////////////////////////////////////////////////////////////
package com.jyis.bookmanager.publishers;
////////////////////////////////////////////////////////////////////////////////////////////////////
import java.util.List;
import java.util.Map;
import java.util.Locale;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.jyis.bookmanager.exceptions.PersistenceException;
////////////////////////////////////////////////////////////////////////////////////////////////////
/**
 * 出版社コントローラ
 * @author 久保　由仁
 */
@Controller
public class PublisherController
{
    /** ロガー */
    private static final Logger logger = LoggerFactory.getLogger(PublisherController.class);

    /** PublisherのDAO */
    @Autowired
    private PublisherService service;
    //----------------------------------------------------------------------------------------------
    /**
     * 出版社情報表示
     * @param id 出版社ID
     * @return ModelAndView
     */
    @RequestMapping(value="publisher/{id}", method=RequestMethod.GET)
    public ModelAndView showPublisher(@PathVariable("id") Integer id)
    {
        Publisher publisher = service.selectPublisherById(id);
        logger.info(publisher.toString());
        ModelAndView modelAndView = new ModelAndView("publisher", "form", publisher);
        modelAndView.addObject("action", String.format("/publisher/%d", publisher.getId()));
        return modelAndView;
    }
    //----------------------------------------------------------------------------------------------
    /**
     * 新規登録画面表示
     * @return 新規登録画面ModelAndView
     */
    @RequestMapping(value="publisher/new", method=RequestMethod.GET)
    public ModelAndView newPublisher()
    {
        ModelAndView modelAndView = new ModelAndView("publisher", "form", new Publisher());
        modelAndView.addObject("action", "/publisher/new");
        return modelAndView;
    }
    //----------------------------------------------------------------------------------------------
    /**
     * 出版社を登録する
     * @param form 出版社フォーム
     * @return ModelAndView
     */
    @RequestMapping(value="publisher/new", method=RequestMethod.POST)
    public ModelAndView createPublisher(@ModelAttribute Publisher form)
    {
        ModelAndView modelAndView = new ModelAndView("publisher");
        try
        {
            Publisher publisher = service.createPublisher(form);
            logger.info(publisher.toString());
            modelAndView.addObject("form", publisher);
            modelAndView.addObject("action", String.format("/publisher/%d", publisher.getId()));
        }
        catch(PersistenceException e)
        {
            logger.error(e.getMessage());
            form.setMessage(e.getMessage());
            modelAndView.addObject("form", form);
        }
        return modelAndView;
    }
    //----------------------------------------------------------------------------------------------
    /**
     * 出版社を更新する
     * @param publisher 出版社フォーム
     * @return 出版社ModelAndView
     */
    @RequestMapping(value="publisher/{id}", method={ RequestMethod.PATCH, RequestMethod.POST })
    public ModelAndView updatePublisher(@ModelAttribute Publisher publisher)
    {
        ModelAndView modelAndView = new ModelAndView("publisher", "form", publisher);
        modelAndView.addObject("action", String.format("/publisher/%d", publisher.getId()));
        try
        {
            service.updatePublisher(publisher);
        }
        catch(PersistenceException e)
        {
            logger.error(e.getMessage());
            publisher.setMessage(e.getMessage());
        }
        return modelAndView;
    }
    //----------------------------------------------------------------------------------------------
    /**
     * 出版社リストをjsonで返す
     * @param searchWord 検索ワード
     * @return jsonのResponseEntity
     */
    @RequestMapping(value="publisher/index", method=RequestMethod.GET)
    public ResponseEntity<String> indexPublishers(@RequestParam(name="searchWord", required=false)
                                                                                 String searchWord)
    {
        logger.info(String.format("indexPublishers() called with %s", searchWord));
        ResponseEntity<String> response = null;
        HttpHeaders header = createResponseHeader();
        String json = null;
        try
        {
            Map<Integer, String> map = service.selectAll(new SearchForm(searchWord));
            ObjectMapper mapper = new ObjectMapper();
            json = mapper.writeValueAsString(map);
            response = new ResponseEntity<String>(json, header, HttpStatus.OK);
        }
        catch(JsonProcessingException e)
        {
            logger.error("JSONシリアライズに失敗しました。", e);
            json = String.format("{ \"error\" : \"%s\" }", e.getMessage());
            response = new ResponseEntity<String>(json, header, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return response;
    }
    //----------------------------------------------------------------------------------------------
    /**
     * 出版社一覧画面を表示する
     * @return 出版社一覧画面
     */
    @RequestMapping(value="publisher/list", method=RequestMethod.GET)
    public ModelAndView listPublishers()
    {
        List<Publisher> list = service.listPublisher();
        return new ModelAndView("list_publisher", "publisherList", list);
    }
    //----------------------------------------------------------------------------------------------
    /**
     * 出版社情報を削除する
     * @param id 出版社情報のID
     * @return 処理結果のJSON
     */
    @RequestMapping(value="publisher/{id}", method=RequestMethod.DELETE)
    public ResponseEntity<String> deletePublisher(@PathVariable("id") Integer id)
    {
        logger.info(String.format("deletePublishers() called %d", id));
        ResponseEntity<String> response = null;
        service.deletePublisher(id);
        String json = String.format("{ \"message\" : \"%dの出版社を削除しました。\"}", id);
        return new ResponseEntity<String>(json, createResponseHeader(), HttpStatus.OK);
    }
    //----------------------------------------------------------------------------------------------
    /**
     * JSON用のレスポンスヘッダを作成する
     * @return JSON用のレスポンスヘッダ
     */
    private HttpHeaders createResponseHeader()
    {
        HttpHeaders header = new HttpHeaders();
        header.set(HttpHeaders.CONTENT_TYPE, "application/json");
        header.setContentLanguage(Locale.JAPANESE);
        return header;
    }
}
