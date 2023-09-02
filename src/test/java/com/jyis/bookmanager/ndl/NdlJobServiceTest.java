////////////////////////////////////////////////////////////////////////////////////////////////////
package com.jyis.bookmanager.ndl;
////////////////////////////////////////////////////////////////////////////////////////////////////
import java.lang.reflect.Field;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.springframework.boot.test.context.SpringBootTest;
////////////////////////////////////////////////////////////////////////////////////////////////////
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
    public void launchTest() throws Exception
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
     * テスト対象のサービスオブジェクトを構築する
     * @return サービスクラスのインスタンス
     */
    private NdlJobService buildService() throws ReflectiveOperationException
    {
        NdlJobService service = new NdlJobService();
        Field fld = NdlJobService.class.getDeclaredField("jobDao");
        fld.setAccessible(true);
        fld.set(service, new JobDaoMock());
        return service;
    }
}
