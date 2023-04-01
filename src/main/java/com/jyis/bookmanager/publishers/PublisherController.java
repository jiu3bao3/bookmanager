////////////////////////////////////////////////////////////////////////////////////////////////////
package com.jyis.bookmanager.publishers;
////////////////////////////////////////////////////////////////////////////////////////////////////
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
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
        return modelAndView;
    }
    //----------------------------------------------------------------------------------------------
    /**
     * 新規登録画面表示
     * @return 新規登録画面ModelAndView
     */
    @RequestMapping(value="publishers/new", method=RequestMethod.GET)
    public ModelAndView newPublisher()
    {
        return new ModelAndView("publisher", "form", new Publisher());
    }
    //----------------------------------------------------------------------------------------------
    /**
     * 出版社リストをjsonで返す
     * @return jsonのResponseEntity
     */
    @ResponseBody
    @RequestMapping(value="publishers/index", method=RequestMethod.GET)
    public ResponseEntity<String> indexPublishers()
    {
        ResponseEntity<String> response = null;
        HttpHeaders header = new HttpHeaders();
        header.set("Content-Type", "application/json");
        String json = null;
        try
        {
            Map<Integer, String> map = service.selectAll(null);
            ObjectMapper mapper = new ObjectMapper();
            json = mapper.writeValueAsString(map);
            response = new ResponseEntity<String>(json, header, HttpStatus.OK);
            logger.info(json);
        }
        catch(JsonProcessingException e)
        {
            logger.error("JSONシリアライズに失敗しました。", e);
        }
        return response;
    }
}
