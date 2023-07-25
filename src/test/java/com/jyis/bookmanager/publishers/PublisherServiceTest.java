////////////////////////////////////////////////////////////////////////////////////////////////////
package com.jyis.bookmanager.publishers;
////////////////////////////////////////////////////////////////////////////////////////////////////
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.springframework.boot.test.context.SpringBootTest;
////////////////////////////////////////////////////////////////////////////////////////////////////
@SpringBootTest
public class PublisherServiceTest
{
    /** テーブルクリア用SQL */
    private static final String[] CLEAR_SQL = new String[] { "DELETE FROM publishers" };

    /** テスト対象のサービスオブジェクト */
    private static final PublisherService service = new PublisherService();
    //----------------------------------------------------------------------------------------------
    /**
     * 後始末
     * データベースに登録されたレコードを削除する
     */
    @AfterEach
    public void clearData() throws Exception
    {
        PublisherDaoMock dao = new PublisherDaoMock();
        try(Connection con = dao.open();
            Statement stmt = con.createStatement())
        {
            for(String sql : CLEAR_SQL)
            {
                stmt.execute(sql);
            }
            con.commit();
        }
    }
    //----------------------------------------------------------------------------------------------
    /**
     * 出版社オブジェクトを作成できること
     */
    @Test
    public void createPublisherTest() throws Exception
    {
        final String MESSAGE = "出版社オブジェクトを作成できること";
        Publisher publisher = service.createPublisher(createPublisher());
        Assertions.assertNotNull(publisher, MESSAGE);
        Assertions.assertEquals(publisher.getName(), "テスト出版");
        Assertions.assertNotNull(publisher.getId());
    }
    //----------------------------------------------------------------------------------------------
    /**
     * 出版社の一覧を取得できること
     */
    @Test
    public void listPublisherTest() throws Exception
    {
        final String MESSAGE = "出版社の一覧を取得できること";
        final int SIZE = 5;
        createPublishers(SIZE);
        List<Publisher> list = service.listPublisher();
        Assertions.assertEquals(SIZE, list.size(), MESSAGE);
    }
    //----------------------------------------------------------------------------------------------
    /**
     * IDと出版社名のマップを取得できること
     */
    @Test
    public void selectAllTest() throws Exception
    {
        final String MESSAGE = "IDと出版社名のマップを取得できること";
        final int SIZE = 5;
        createPublishers(SIZE);
        Map<Integer, String> map = service.selectAll(null);
        Assertions.assertEquals(SIZE, map.size(), MESSAGE);
        int number = 0;
        for(Integer i : map.keySet())
        {
            Assertions.assertLinesMatch(Arrays.asList("^出版社\\d+$"), Arrays.asList(map.get(i)));
        }
    }
    //----------------------------------------------------------------------------------------------
    /**
     * 登録済の出版社を重複して作成できないこと
     */
    @Test
    public void createPublisherWithDuplicationTest() throws Exception
    {
        final String MESSAGE = "登録済の出版社を重複して作成できないこと";
        service.createPublisher(createPublisher());
        Publisher publisher = service.createPublisher(createPublisher());
        List<String> regexpList = Arrays.asList("id=\\d+で登録済の出版社です。");
        Assertions.assertLinesMatch(regexpList, Arrays.asList(publisher.getMessage()), MESSAGE);
    }
    //----------------------------------------------------------------------------------------------
    /**
     * 出版社を更新できること
     */
    @Test
    public void updatePublisherTest() throws Exception
    {
        final String MESSAGE = "出版社を更新できること";
        Publisher publisher = service.createPublisher(createPublisher());
        int publisherId = publisher.getId();
        publisher.setName("更新後出版社");
        service.updatePublisher(publisher);
        Publisher editedPublisher = service.selectPublisherById(publisherId);
        Assertions.assertEquals("更新後出版社", editedPublisher.getName(), MESSAGE);
        Assertions.assertEquals(publisher.getEmail(), editedPublisher.getEmail(), MESSAGE);
    }
    //----------------------------------------------------------------------------------------------
    /**
     * DAOオブジェクトのモックを設定する
     */
    @BeforeAll
    public static void prepareDaoMock() throws ReflectiveOperationException
    {
        Field daoField = service.getClass().getDeclaredField("publisherDao");
        daoField.setAccessible(true);
        daoField.set(service, new PublisherDaoMock());
    }
    //----------------------------------------------------------------------------------------------
    /**
     * テスト用のPublisherオブジェクトを作成する
     * @return テスト用のPublisherオブジェクト
     */
    private Publisher createPublisher()
    {
        Publisher publisher = new Publisher("テスト出版");
        publisher.setZip("999-9999");
        publisher.setAddress1("東京都小笠原村１２３");
        publisher.setAddress2("硫黄島１２３－４");
        publisher.setPhone("000-111-9999");
        publisher.setEmail("test.publisher@example.com");
        return publisher;
    }
    //----------------------------------------------------------------------------------------------
    /**
     * 複数の出版社を作成する
     * @param count 作成する数
     */
    private void createPublishers(final int count)
    {
        for(int i = 0; i < count; i++)
        {
            Publisher publisher = createPublisher();
            publisher.setName(String.format("出版社%d", i + 1));
            service.createPublisher(publisher);
        }
    }
}
