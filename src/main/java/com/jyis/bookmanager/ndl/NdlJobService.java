////////////////////////////////////////////////////////////////////////////////////////////////////
package com.jyis.bookmanager.ndl;
////////////////////////////////////////////////////////////////////////////////////////////////////
import java.util.List;
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

    /** DAO */
    @Autowired
    private JobDao jobDao;
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
    //----------------------------------------------------------------------------------------------
    /**
     * ジョブ履歴の一覧を取得する
     * @param form ダミー
     * @return ジョブ履歴のList
     */
    public List<JobHistory> getJobHistories(NdlJobForm form)
    {
        return jobDao.selectAll(form);
    }
    //----------------------------------------------------------------------------------------------
    /**
     * ジョブ履歴をクリアする
     * @return 処理結果のJSON文字列
     */
    public String deleteJobHistories()
    {
        jobDao.deleteAll();
        return "{ \"message\": \"ジョブ実行履歴を削除しました。\"}";
    }
}
