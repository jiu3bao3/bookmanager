////////////////////////////////////////////////////////////////////////////////////////////////////
package com.jyis.bookmanager.books;
////////////////////////////////////////////////////////////////////////////////////////////////////
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.jyis.bookmanager.AbstractController;
////////////////////////////////////////////////////////////////////////////////////////////////////
/**
 * 言語マスターコントローラ
 * @author 久保　由仁
 */
@Controller
public final class LanguageController extends AbstractController
{
    /** ロガー */
    private static final Logger logger = LoggerFactory.getLogger(LanguageController.class);

    /** サービス */
    @Autowired
    private BookManagementService service;
    //----------------------------------------------------------------------------------------------
    /**
     * 言語一覧を取得する
     * @return 言語一覧
     * ex: {"CHINESE":"中国語","ENGLISH":"英語","JAPANESE":"日本語"}
     */
    @RequestMapping(value="books/languages", method=RequestMethod.GET)
    public ResponseEntity<String> indexLanguages()
    {
        ResponseEntity<String> response = null;
        HttpHeaders header = createResponseHeader();
        String json = null;
        try
        {
            Map<Language, String> map = service.listLanguages();
            ObjectMapper mapper = new ObjectMapper();
            json = mapper.writeValueAsString(map);
            response = new ResponseEntity<String>(json, header, HttpStatus.OK);
        }
        catch(JsonProcessingException e)
        {
            logger.error("JSONシリアライズに失敗しました。", e);
            json = String.format("{ \"error\" : \"%s\" }", e.getMessage());
            response = new ResponseEntity<String>(json, header, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return response;
    }
}
