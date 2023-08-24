////////////////////////////////////////////////////////////////////////////////////////////////////
package com.jyis.bookmanager.ndl;
////////////////////////////////////////////////////////////////////////////////////////////////////
import javax.sql.DataSource;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionException;
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
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.support.DefaultDataFieldMaxValueIncrementerFactory;
import org.springframework.batch.support.DatabaseType;
import org.springframework.batch.support.transaction.ResourcelessTransactionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
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

    /** application設定 */
    @Autowired
    protected Environment env;
    //----------------------------------------------------------------------------------------------
    public void launch() throws Exception
    {
            JobRepository jobRepository = createJobRepository();
            SimpleJob job = new SimpleJob("NDL Job");
            job.addStep(createStep(jobRepository));
            job.setJobRepository(jobRepository);
            TaskExecutorJobLauncher jobLauncher = new TaskExecutorJobLauncher();
            jobLauncher.setJobRepository(jobRepository);
            jobLauncher.setTaskExecutor(new DefaultManagedTaskExecutor());
            JobExecution jobExecution = jobLauncher.run(job, new JobParameters());
    }
    //----------------------------------------------------------------------------------------------
    protected JobRepository createJobRepository() throws Exception
    {
        JobRepositoryFactoryBean factory = new JobRepositoryFactoryBean();
        DataSource dataSource = getDatasouce();
        factory.setDataSource(dataSource);
        factory.setIsolationLevelForCreate("ISOLATION_SERIALIZABLE");
        factory.setTablePrefix("BATCH_");
        factory.setMaxVarCharLength(1000);
        factory.setIncrementerFactory(new DefaultDataFieldMaxValueIncrementerFactory(dataSource) {
            @Override
            public DataFieldMaxValueIncrementer getIncrementer(String incrementerType, String incrementerName) {
                return new SqlServerSequenceMaxValueIncrementer(dataSource, incrementerName);
            }
        });
        factory.setDatabaseType(DatabaseType.SQLSERVER.toString());
        factory.setTransactionManager(new ResourcelessTransactionManager());
        factory.setJdbcOperations(new JdbcTemplate(dataSource));
        factory.afterPropertiesSet();
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
    protected Step createStep(JobRepository jobRepository)
    {
        StepBuilder stepBuilder = new StepBuilder("step", jobRepository);
        SimpleStepBuilder simpleStepBuilder = stepBuilder
                            .<Book, Book>chunk(1, new ResourcelessTransactionManager())
                            .reader(new ItemReader<Book>() {
                                @Override
                                public Book read() { return null; }
                            })
                            .writer(new ItemWriter<Book>() {
                                @Override
                                public void write(Chunk<? extends Book> chunk) { }
                            });
        return simpleStepBuilder.build();
    }
}
