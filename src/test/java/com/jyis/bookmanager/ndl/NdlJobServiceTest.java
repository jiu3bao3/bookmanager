////////////////////////////////////////////////////////////////////////////////////////////////////
package com.jyis.bookmanager.ndl;
////////////////////////////////////////////////////////////////////////////////////////////////////
import java.lang.reflect.Field;
import java.util.regex.Pattern;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.launch.support.TaskExecutorJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.dao.DefaultExecutionContextSerializer;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.support.DefaultDataFieldMaxValueIncrementerFactory;
import org.springframework.batch.support.DatabaseType;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.scheduling.concurrent.DefaultManagedTaskExecutor;

import com.jyis.bookmanager.AbstractDaoImpl;
////////////////////////////////////////////////////////////////////////////////////////////////////
/**
 * NdlJobServiceのテスト
 * @author 久保　由仁
 */
@SpringBootTest
public class NdlJobServiceTest
{
    /** ロガー */
    private static final Logger logger = LoggerFactory.getLogger(NdlJobServiceTest.class);
    //----------------------------------------------------------------------------------------------
    /**
     * ジョブ履歴削除を実行できること
     */
    @Test
    public void deleteJobHistoriesTest() throws Exception
    {
        final String MESSAGE = "ジョブ履歴削除を実行できること";
        NdlJobService service = buildService();
        String body = service.deleteJobHistories();
        final String PATTERN = "\\{\\s*\"message\"\\s*:\\s*\"ジョブ実行履歴を削除しました。\"\\s*\\}";
        Assertions.assertTrue(Pattern.matches(PATTERN, body), MESSAGE);
        Assertions.assertEquals(service.getJobHistories(null).size(), 0, MESSAGE);
    }
    //----------------------------------------------------------------------------------------------
    /**
     * ジョブを起動できること
     */
    @Test
    public void launchTest() throws Exception
    {
        final String MESSAGE = "ジョブを起動できること";
        NdlJobService service = buildService();
        service.launch();
        Assertions.assertEquals(service.getJobHistories(null).size(), 1, MESSAGE);
    }
    //----------------------------------------------------------------------------------------------
    /**
     * テスト対象のサービスオブジェクトを構築する
     * @return サービスクラスのインスタンス
     */
    private NdlJobService buildService() throws Exception
    {
        NdlTestBatchConfiguration config = new NdlTestBatchConfiguration();
        JobRepository jobRepository = config.jobRepository();
        NdlJobService service = new NdlJobService();
        Field daoFld = getPrivateField(NdlJobService.class, "jobDao");
        daoFld.set(service, config.getDao());
        Field launcherFld = getPrivateField(NdlJobService.class, "jobLauncher");
        TaskExecutorJobLauncher launcher = new TaskExecutorJobLauncher();
        launcher.setJobRepository(jobRepository);
        launcher.setTaskExecutor(new DefaultManagedTaskExecutor());
        launcherFld.set(service, launcher);
        Field jobFld = getPrivateField(NdlJobService.class, "job");
        Step step = config.step(jobRepository, new JdbcTransactionManager(config.getDatasouce()));
        Job job = config.ndlInfoObtainingJob(jobRepository, step);
        jobFld.set(service, job);
        return service;
    }
    //----------------------------------------------------------------------------------------------
    /**
     * privateフィールドを取得する
     */
    private Field getPrivateField(Class clazz, final String fieldName)
                                                             throws ReflectiveOperationException
    {
        Field field = clazz.getDeclaredField(fieldName);
        field.setAccessible(true);
        return field;
    }
}
////////////////////////////////////////////////////////////////////////////////////////////////////
/**
 * テスト用のSpring Batch設定
 */
class NdlTestBatchConfiguration extends NdlBatchConfiguration
{
    /** テストデータベースへ接続するDAO */
    private JobDaoMock jobDaoMock = null;
    //---------------------------------------------------------------------------------------------
    /**
     * ディフォルトコンストラクタ
     */
    public NdlTestBatchConfiguration()
    {
        jobDaoMock  = new JobDaoMock();
    }
    //---------------------------------------------------------------------------------------------
    /**
     * データソースを取得する
     * @return DataSource
     */
    @Override
    protected DataSource getDatasouce()
    {
        return AbstractDaoImpl.getDataSource();
    }
    //---------------------------------------------------------------------------------------------
    /**
     * Spring Batchのジョブを管理するJobRepositoryを構築する
     * @return JobRepository
     */
    public JobRepository jobRepository() throws Exception
    {
        JobRepositoryFactoryBean factory = new JobRepositoryFactoryBean();
        DataSource ds = AbstractDaoImpl.getDataSource();
        factory.setDataSource(ds);
        factory.setDatabaseType(DatabaseType.SQLITE.getProductName());
        factory.setTransactionManager(new JdbcTransactionManager(ds));
        factory.setJdbcOperations(new JdbcTemplate(ds));
        factory.setIncrementerFactory(new DefaultDataFieldMaxValueIncrementerFactory(ds));
        factory.setConversionService(new DefaultConversionService());
        factory.setSerializer(new DefaultExecutionContextSerializer());
        return factory.getObject();
    }
    //---------------------------------------------------------------------------------------------
    /**
     * DAOオブジェクトを取得する
     * @return テスト用DAO
     */
    public JobDaoMock getDao()
    {
        return jobDaoMock;
    }
}
