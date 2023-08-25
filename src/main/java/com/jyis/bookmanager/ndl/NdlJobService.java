////////////////////////////////////////////////////////////////////////////////////////////////////
package com.jyis.bookmanager.ndl;
////////////////////////////////////////////////////////////////////////////////////////////////////
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jyis.bookmanager.books.Book;
////////////////////////////////////////////////////////////////////////////////////////////////////
/**
 * 書誌情報ジョブ管理サービス
 * @author 久保　由仁
 */
@Service
public class NdlJobService
{
    /** ロガー */
    private static final Logger logger = LoggerFactory.getLogger(NdlJobService.class);
    
    /** JobLauncher */
    @Autowired
    private JobLauncher jobLauncher;

    /** ジョブ */
    @Autowired
    private Job job;
    //----------------------------------------------------------------------------------------------
    /**
     * ジョブを起動する
     */
    public void launch() throws Exception
    {
        logger.info("ジョブ開始");
        jobLauncher.run(job, new JobParameters());
        logger.info("ジョブ終了");
    }
}
