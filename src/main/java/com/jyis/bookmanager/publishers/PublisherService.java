////////////////////////////////////////////////////////////////////////////////////////////////////
package com.jyis.bookmanager.publishers;
////////////////////////////////////////////////////////////////////////////////////////////////////
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jyis.bookmanager.AbstractForm;
////////////////////////////////////////////////////////////////////////////////////////////////////
/**
 * 出版社サービス
 * @author 久保　由仁
 */
@Service
public class PublisherService
{
    /** ロガー */
    private static final Logger logger = LoggerFactory.getLogger(PublisherService.class);
    
    /** PublisherのDAO */
    @Autowired
    private PublisherDao publisherDao;
    //----------------------------------------------------------------------------------------------
    /**
     * 出版社情報（詳細）を取得する
     * @param id 出版社ID
     * @return 出版社情報
     */
    public Publisher selectPublisherById(final int id)
    {
        return publisherDao.selectOne(id);
    }
    //----------------------------------------------------------------------------------------------
    /**
     * 出版社のIDと名称のMapを取得する
     * @param form フォーム（未使用）
     * @return 出版社のIDと名称のMap
     */
    public Map<Integer, String> selectAll(AbstractForm form)
    {
        return publisherDao.selectAll(form).stream()
                           .collect(Collectors.toMap(p -> p.getId(), p -> p.getName()));
    }
    //----------------------------------------------------------------------------------------------
    /**
     * 出版社情報をすべて取得してListを返す
     * @return 出版社情報のList
     */
    public List<Publisher> listPublisher()
    {
        return publisherDao.selectAll(null);
    }
    //----------------------------------------------------------------------------------------------
    /**
     * 出版社情報を作成する
     * @param arg 作成するPublisher情報
     * @return 作成されたPublisher情報
     */
    public Publisher createPublisher(Publisher arg)
    {
        Publisher publisher = null;
        Publisher existingPublisher = publisherDao.selectOne(arg);
        if(existingPublisher == null)
        {
            try
            {
                publisherDao.insert(arg);
            }
            catch(Exception e)
            {
                throw new RuntimeException(e);
            }
            publisher = publisherDao.selectOne(arg);
        }
        else
        {
            final String MESSAGE = "id=%dで登録済の出版社です。";
            publisher = arg;
            publisher.setMessage(String.format(MESSAGE, existingPublisher.getId()));
        }
        return publisher;
    }
    //----------------------------------------------------------------------------------------------
    /**
     * 出版社情報を更新する
     * @param arg 出版社情報オブジェクト
     */
    public void updatePublisher(Publisher arg)
    {
        publisherDao.update(arg);
    }
    //----------------------------------------------------------------------------------------------
    /**
     * 出版社情報を削除する
     * @param id 出版社のID
     */
    public void deletePublisher(final Integer id)
    {
        publisherDao.deleteById(id);
    }
}
