////////////////////////////////////////////////////////////////////////////////////////////////////
package com.jyis.bookmanager.ndl;
////////////////////////////////////////////////////////////////////////////////////////////////////
import java.util.List;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionException;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.job.SimpleJob;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.TaskExecutorJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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

    /** application設定 */
    @Autowired
    protected Environment env;
    //----------------------------------------------------------------------------------------------
    /**
     * ジョブの起動画面を表示させる
     * @return ジョブの起動画面のFormAndView
     */
    @RequestMapping(value="/jobs", method=RequestMethod.GET)
    public ModelAndView index()
    {
        return new ModelAndView("jobs", "form", new NdlJobForm());
    }
    //----------------------------------------------------------------------------------------------
    /**
     * ジョブを実行する
     * @return ジョブの起動画面のFormAndView
     */
    @RequestMapping(value="/jobs", method=RequestMethod.POST)
    public ModelAndView launch(@ModelAttribute NdlJobForm form)
    {
        form.setMessage("ジョブを実行します。");
        try
        {
            Job job = new SimpleJob("NDL Job");
            TaskExecutorJobLauncher jobLauncher = new TaskExecutorJobLauncher();
            jobLauncher.setJobRepository(createJobRepository());
            JobExecution jobExecution = jobLauncher.run(job, new JobParameters());
        }
        catch(Exception ex)
        {
            logger.error("ジョブ実行に失敗しました。", ex);
            form.setMessage("ジョブ実行に失敗しました。");
        }
        return new ModelAndView("jobs", "form", form);
    }
    //----------------------------------------------------------------------------------------------
    protected JobRepository createJobRepository() throws Exception
    {
        JobRepositoryFactoryBean factory = new JobRepositoryFactoryBean();
        factory.setDataSource(getDatasouce());
        factory.setIsolationLevelForCreate("ISOLATION_SERIALIZABLE");
        factory.setTablePrefix("BATCH_");
        factory.setMaxVarCharLength(1000);
        return factory.getObject();
    }
    //----------------------------------------------------------------------------------------------
    protected DataSource getDatasouce()
    {
        SQLServerDataSource ds = new SQLServerDataSource();
        ds.setURL(env.getProperty("spring.datasource.url"));
        ds.setUser(env.getProperty("spring.datasource.username"));
        ds.setPassword(env.getProperty("spring.datasource.password"));
        return ds;
    }
}
