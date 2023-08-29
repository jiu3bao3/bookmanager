////////////////////////////////////////////////////////////////////////////////////////////////////
package com.jyis.bookmanager;
////////////////////////////////////////////////////////////////////////////////////////////////////
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
////////////////////////////////////////////////////////////////////////////////////////////////////
/**
 * 抽象コントローラクラス
 * @author 久保　由仁
 */
@Controller
public abstract class AbstractController
{
    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractController.class);

    /** JSONのMIMEタイプ */
    public static final String MIME_JSON_TYPE = "application/json";
    //----------------------------------------------------------------------------------------------
    /**
     * JSON用のレスポンスヘッダを作成する
     * @return JSON用のレスポンスヘッダ
     */
    protected HttpHeaders createResponseHeader()
    {
        HttpHeaders header = new HttpHeaders();
        header.set(HttpHeaders.CONTENT_TYPE, MIME_JSON_TYPE);
        header.setContentLanguage(Locale.JAPANESE);
        return header;
    }
}
