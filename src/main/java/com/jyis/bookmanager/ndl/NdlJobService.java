////////////////////////////////////////////////////////////////////////////////////////////////////
package com.jyis.bookmanager.ndl;
////////////////////////////////////////////////////////////////////////////////////////////////////
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.SimpleJob;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.TaskExecutorJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.builder.SimpleStepBuilder;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.database.support.DefaultDataFieldMaxValueIncrementerFactory;
import org.springframework.batch.support.DatabaseType;
import org.springframework.batch.support.transaction.ResourcelessTransactionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.incrementer.DataFieldMaxValueIncrementer;
import org.springframework.jdbc.support.incrementer.SqlServerMaxValueIncrementer;
import org.springframework.jdbc.support.incrementer.SqlServerSequenceMaxValueIncrementer;
import org.springframework.scheduling.concurrent.DefaultManagedTaskExecutor;
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
    
    /** DAO */
    @Autowired
    private NdlDao dao;
    //----------------------------------------------------------------------------------------------
    /**
     * ジョブを起動する
     */
    public void launch() throws Exception
    {
        logger.info("ジョブ開始");
        JobRepository jobRepository = createJobRepository();
        SimpleJob job = new SimpleJob("NDL Job");
        job.addStep(createStep(jobRepository));
        job.setJobRepository(jobRepository);
        TaskExecutorJobLauncher jobLauncher = new TaskExecutorJobLauncher();
        jobLauncher.setJobRepository(jobRepository);
        jobLauncher.setTaskExecutor(new DefaultManagedTaskExecutor());
        JobExecution jobExecution = jobLauncher.run(job, new JobParameters());
        logger.info("ジョブ終了");
    }

    //----------------------------------------------------------------------------------------------
    protected Step createStep(JobRepository jobRepository)
    {
        StepBuilder stepBuilder = new StepBuilder("step", jobRepository);
        SimpleStepBuilder simpleStepBuilder = stepBuilder
                            .<Book, Book>chunk(1, new ResourcelessTransactionManager())
                            .reader(new NdlBookReader())
                            .writer(new NdlBookWriter());
        return simpleStepBuilder.build();
    }
    //----------------------------------------------------------------------------------------------
    /**
     * JobRepositoryを構築する
     * @return JobRepository
     */
    protected JobRepository createJobRepository() throws Exception
    {
        DataSource dataSource = dao.dataSource();
        JobRepositoryFactoryBean factory = new JobRepositoryFactoryBean();
        factory.setDataSource(dataSource);
        factory.setIsolationLevelForCreate("ISOLATION_SERIALIZABLE");
        factory.setTablePrefix("BATCH_");
        factory.setMaxVarCharLength(1000);
        factory.setIncrementerFactory(new DefaultDataFieldMaxValueIncrementerFactory(dataSource) {
            @Override
            public DataFieldMaxValueIncrementer getIncrementer(String type, String name) {
                return new SqlServerSequenceMaxValueIncrementer(dataSource, name);
            }
        });
        factory.setDatabaseType(DatabaseType.SQLSERVER.toString());
        factory.setTransactionManager(new ResourcelessTransactionManager());
        factory.setJdbcOperations(new JdbcTemplate(dataSource));
        factory.afterPropertiesSet();
        return factory.getObject();
    }
}
