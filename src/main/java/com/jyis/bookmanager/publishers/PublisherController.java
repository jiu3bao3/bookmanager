////////////////////////////////////////////////////////////////////////////////////////////////////
package com.jyis.bookmanager.publishers;
////////////////////////////////////////////////////////////////////////////////////////////////////
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
    private PublisherDao publisherDao;
    //----------------------------------------------------------------------------------------------
    /**
     * 出版社情報表示
     * @param id 出版社ID
     * @return ModelAndView
     */
    @RequestMapping(value="publisher/{id}", method=RequestMethod.GET)
    public ModelAndView showPublisher(@PathVariable("id") Integer id)
    {
        Publisher publisher = publisherDao.selectOne(id);
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
}
