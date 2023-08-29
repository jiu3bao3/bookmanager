////////////////////////////////////////////////////////////////////////////////////////////////////
package com.jyis.bookmanager.ndl;
////////////////////////////////////////////////////////////////////////////////////////////////////
import java.util.List;
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

import com.jyis.bookmanager.books.Book;
import com.jyis.bookmanager.AbstractController;
////////////////////////////////////////////////////////////////////////////////////////////////////
/**
 * ジョブ制御画面コントローラ
 * @author 久保　由仁
 */
@Controller
public final class NdlJobController extends AbstractController
{
    /** ロガー */
    private static final Logger logger = LoggerFactory.getLogger(NdlJobController.class);
    
    /** サービス */
    @Autowired
    private NdlJobService service;
    //----------------------------------------------------------------------------------------------
    /**
     * ジョブの起動画面を表示させる
     * @return ジョブの起動画面のFormAndView
     */
    @RequestMapping(value="/jobs", method=RequestMethod.GET)
    public ModelAndView index()
    {
        logger.info("ジョブ実行履歴一覧 START");
        NdlJobForm form = new NdlJobForm();
        ModelAndView modelAndView = new ModelAndView("jobs", "form", form);
        List<JobHistory> jobList = service.getJobHistories(form);
        modelAndView.addObject("jobHistories", jobList);
        logger.info("ジョブ実行履歴一覧 END");
        return modelAndView;
    }
    //----------------------------------------------------------------------------------------------
    /**
     * ジョブを実行する
     * @param form フォーム
     * @return ジョブの起動画面のFormAndView
     */
    @RequestMapping(value="/jobs", method=RequestMethod.POST)
    public ModelAndView launch(@ModelAttribute NdlJobForm form)
    {
        logger.info("ジョブ実行 START");
        try
        {
            service.launch();
            form.setMessage("ジョブを実行しました。");
        }
        catch(Exception ex)
        {
            logger.error("ジョブ実行に失敗しました。", ex);
            form.setMessage("ジョブ実行に失敗しました。");
        }
        ModelAndView modelAndView = new ModelAndView("jobs", "form", form);
        List<JobHistory> jobList = service.getJobHistories(form);
        modelAndView.addObject("jobHistories", jobList);
        logger.info("ジョブ実行 END");
        return modelAndView;
    }
    //----------------------------------------------------------------------------------------------
    /**
     * ジョブ実行履歴をすべてクリア（削除）する
     * @pram form フォーム
     * @return ジョブの起動画面のFormAndView
     */
    @RequestMapping(value="/jobs", method=RequestMethod.DELETE)
    public ResponseEntity<String> clearJobHistories()
    {
        logger.info("ジョブ実行履歴削除 START");
        String json = "{ \"message\": \"ジョブ実行履歴を削除しました。\"}";
        logger.info("ジョブ実行履歴削除 END");
        return new ResponseEntity<String>(json, createResponseHeader(), HttpStatus.OK);
    }
}
