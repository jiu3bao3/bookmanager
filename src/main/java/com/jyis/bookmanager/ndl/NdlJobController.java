////////////////////////////////////////////////////////////////////////////////////////////////////
package com.jyis.bookmanager.ndl;
////////////////////////////////////////////////////////////////////////////////////////////////////
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.jyis.bookmanager.books.Book;
////////////////////////////////////////////////////////////////////////////////////////////////////
/**
 * ジョブ制御画面コントローラ
 * @author 久保　由仁
 */
@Controller
public class NdlJobController
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
        NdlJobForm form = new NdlJobForm();
        ModelAndView modelAndView = new ModelAndView("jobs", "form", form);
        List<JobHistory> jobList = service.getJobHistories(form);
        modelAndView.addObject("jobHistories", jobList);
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
        return modelAndView;
    }
}
