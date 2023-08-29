////////////////////////////////////////////////////////////////////////////////////////////////////
package com.jyis.bookmanager.stats;
////////////////////////////////////////////////////////////////////////////////////////////////////
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.jyis.bookmanager.AbstractController;
////////////////////////////////////////////////////////////////////////////////////////////////////
/**
 * 集計コントローラ
 * @author 久保　由仁
 */
@Controller
public final class StatsController extends AbstractController
{
    /** DAO */
    @Autowired
    private StatsDao statsDao;

    /** ロガー */
    private static final Logger logger = LoggerFactory.getLogger(StatsController.class);
    //----------------------------------------------------------------------------------------------
    /**
     * 集計結果を表示する
     * @param form 検索フォーム
     * @return 集計結果のModelAndView
     */
    @RequestMapping(value="stats", method=RequestMethod.GET)
    public ModelAndView show(@ModelAttribute StatsForm form)
    {
        try
        {
            logger.info(String.format("show stats %s", form.toString()));
            form.validate();
            form = new StatsForm(form);
            form.setSummary(statsDao.selectAll(form));
            summarize(form);
        }
        catch(Exception e)
        {
            logger.error("バリデーションエラー", e);
            form.setMessage(e.getMessage());
        }
        return new ModelAndView("stats", "form", form);
    }
    //----------------------------------------------------------------------------------------------
    /**
     * 総計の計算を行う
     * @param form 検索フォーム
     */
    private void summarize(StatsForm form)
    {
        if(form == null) return;
        int total = form.getSummary().stream()
                                    .collect(Collectors.summingInt(MonthlyCount::getTotal));
        form.setMessage(String.format("総計：%d", total));
    }
}
