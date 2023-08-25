////////////////////////////////////////////////////////////////////////////////////////////////////
package com.jyis.bookmanager.ndl;
////////////////////////////////////////////////////////////////////////////////////////////////////
import java.util.Calendar;
import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.transaction.PlatformTransactionManager;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;

import com.jyis.bookmanager.books.Book;
////////////////////////////////////////////////////////////////////////////////////////////////////
/**
 * Spring Batchジョブの設定
 * @author 久保　由仁
 */
@Configuration
public class NdlBatchConfiguration
{
    /** application設定 */
    @Autowired
    protected Environment env;
    //---------------------------------------------------------------------------------------------
    /** 
     * 書誌情報取得プロセッサー
     * @return Spring Batchのプロセッサ
     */
    @Bean
    public NdlBookItemProcessor processor()
    {
        return new NdlBookItemProcessor();
    }
    //---------------------------------------------------------------------------------------------
    /*
     * Job定義
     * @return Job
     */
    @Bean
    public Job ndlInfoObtainingJob(JobRepository jobRepository, Step step)
    {
        JobBuilder builder = new JobBuilder("NDL-JOB", jobRepository);
        Job job = builder.incrementer(new RunIdIncrementer()).flow(step).end().build();
        return job;
    }
    //---------------------------------------------------------------------------------------------
    /**
     * ジョブのステップ定義
     * @return Spring BatchのStep
     */
    @Bean
    public Step step(JobRepository jobRepository, PlatformTransactionManager transactionManager)
    {
        StepBuilder stepBuilder = new StepBuilder("step1", jobRepository);
        Step step = stepBuilder.<Book, NdlInfo> chunk(10, transactionManager)
                    .reader(reader())
                    .processor(processor())
                    .writer(writer())
                    .build();
        return step;
    }
    //---------------------------------------------------------------------------------------------
    /**
     * readerの定義
     * @return Spring Batchのreader
     */
    @Bean
    public JdbcCursorItemReader reader()
    {
        final String SQL = "SELECT ID, ISBN, TITLE, AUTHORS, PUBLISHER FROM BOOKS B "
                        + "WHERE ISBN IS NOT NULL AND NOT EXISTS(SELECT 1 FROM EXTRA_INFO E "
                        + "WHERE E.BOOK_ID = B.ID) AND EXISTS (SELECT 1 FROM READ_BOOKS R "
                        + "WHERE R.BOOK_ID = B.ID AND R.READ_ON >= ?) ";
        JdbcCursorItemReaderBuilder builder = new JdbcCursorItemReaderBuilder();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, -2);
        builder.dataSource(getDatasouce())
                .sql(SQL)
                .saveState(false)
                .queryArguments(calendar.getTime())
                .rowMapper(new BeanPropertyRowMapper<Book>(Book.class));
        return builder.build();
    }
    //---------------------------------------------------------------------------------------------
    /**
     * writerの定義
     * @return Spring Batchのwriter
     */
    @Bean
    public NdlBookItemWriter writer()
    {
        return new NdlBookItemWriter();
    }
    //---------------------------------------------------------------------------------------------
    /**
     * データソースを取得する
     * @return DataSource
     */
    private DataSource getDatasouce()
    {
        SQLServerDataSource ds = new SQLServerDataSource();
        ds.setURL(env.getProperty("spring.datasource.url"));
        ds.setUser(env.getProperty("spring.datasource.username"));
        ds.setPassword(env.getProperty("spring.datasource.password"));
        return ds;
    }
}
