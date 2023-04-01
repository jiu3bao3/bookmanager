////////////////////////////////////////////////////////////////////////////////////////////////////
package com.jyis.bookmanager.publishers;
////////////////////////////////////////////////////////////////////////////////////////////////////
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
}