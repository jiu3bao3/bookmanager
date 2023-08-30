////////////////////////////////////////////////////////////////////////////////////////////////////
package com.jyis.bookmanager.ndl;
////////////////////////////////////////////////////////////////////////////////////////////////////
import java.net.URI;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpClient.Version;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.net.ssl.SSLSession;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.boot.test.context.SpringBootTest;

import com.jyis.bookmanager.books.Book;
import com.jyis.bookmanager.books.ExtraInfo;
////////////////////////////////////////////////////////////////////////////////////////////////////
@SpringBootTest
public class NdlBookItemProcessorTest
{
    /** JSONパーサーのテストに用いるJSON */
    private static final String JSON_EXAMPLE = "{ \"result\" : { \"message\" : \"example\", "+
                                                "\"data\" : [\"A\", \"B\"]} }";
    /** 書誌情報APIレスポンスのJSON */
    private static final String EXAMPLE_RESPONSE = "[{\"onix\": { \"RecordReference\" : "
            + "\"9784334045623\", \"CollateralDetail\" : { \"TextContent\" : [ "
            + "{ \"TextType\": \"02\", \"Text\" : \"テキスト02\", \"ContentAudience\" : \"00\"},"
            + "{ \"TextType\": \"03\", \"Text\" : \"テキスト03\", \"ContentAudience\" : \"00\"}"
            + "] } } } ]";
    //---------------------------------------------------------------------------------------------
    /**
     * JSONの要素を取得できること
     */
    @Test
    public void digTest() throws Exception
    {
        final String MESSAGE = "JSONの要素を取得できること";
        ObjectMapper mapper = new ObjectMapper();
        JsonNode json = mapper.readTree(JSON_EXAMPLE);
        String message = NdlBookItemProcessor.dig(json, "result", "message").asText();
        Assertions.assertEquals(message, "example", MESSAGE);
        Assertions.assertNull(NdlBookItemProcessor.dig(json, "result", "invalid_key"), MESSAGE);
    }
    //---------------------------------------------------------------------------------------------
    /**
     * 書誌情報オブジェクトに書誌情報が格納されること
     */
    @Test
    public void processWithValidTest() throws Exception
    {
        final String MESSAGE = "書誌情報オブジェクトに書誌情報が格納されること";
        Book book = new Book();
        book.setId(0);
        book.setIsbn("9784334045623");
        NdlBookItemProcessor processor = new NdlBookItemProcessorMock(EXAMPLE_RESPONSE);
        NdlInfo result = processor.process(book);
        Assertions.assertEquals(result.get(ExtraInfo.COVER), "テキスト02", MESSAGE);
        Assertions.assertEquals(result.get(ExtraInfo.NOTE), "テキスト03", MESSAGE);
        Assertions.assertNull(result.get(ExtraInfo.COONTENTS), MESSAGE);
    }
    //---------------------------------------------------------------------------------------------
    /**
     * 書誌情報が取得できない場合はnullが返されること
     */
    @Test
    public void processWithEmptyTest() throws Exception
    {
        final String MESSAGE = "書誌情報が取得できない場合はnullが返されること";
        Book book = new Book();
        book.setId(-1);
        book.setIsbn("9784334045623");
        NdlBookItemProcessor processor = new NdlBookItemProcessorMock("{}");
        NdlInfo result = processor.process(book);
        Assertions.assertNull(result, MESSAGE);
    }
}
////////////////////////////////////////////////////////////////////////////////////////////////////
/**
 * テストのためにHTTPリクエストを模倣させるプロセッサ
 * @author 久保　由仁
 */
class NdlBookItemProcessorMock extends NdlBookItemProcessor
{
    /** レスポンスとして返すJSON */
    private String responseJson;
    //---------------------------------------------------------------------------------------------
    /**
     * コンストラクタ
     * @param json レスポンスとして返すべきjson
     */
    public NdlBookItemProcessorMock(final String json)
    {
        responseJson = json;
    }
    //---------------------------------------------------------------------------------------------
    /**
     * HTTPリクエストを模倣する
     * @param isbn ISBN
     * @return HttpResponse<String>のモックオブジェクト
     */
    @Override
    protected HttpResponse<String> sendHttpRequest(final String isbn)
    {
        return new HttpResponseMock<String>(responseJson);
    }
}
////////////////////////////////////////////////////////////////////////////////////////////////////
/**
 * HttpResponseを模倣したモック
 * @author 久保　由仁
 */
class HttpResponseMock<T> implements HttpResponse<T>
{
    /** HTTPステータス */
    private int status = 200;

    /** レスポンスボディ */
    private T content;

    /** レスポンスヘッダ */
    private Map<String, List<String>> headerList;
    //---------------------------------------------------------------------------------------------
    /**
     * ディフォルトコンストラクタ
     */
    public HttpResponseMock()
    {
        headerList = new HashMap<>();
    }
    //---------------------------------------------------------------------------------------------
    /**
     * コンストラクタ
     * @param status HTTPステータス
     * @param body レスポンスボディ
     */
    public HttpResponseMock(final int status, final T body)
    {
        this();
        this.status = status;
        content = body;
    }
    //---------------------------------------------------------------------------------------------
    /**
     * コンストラクタ
     * @param arg レスン本スボディ
     */
    public HttpResponseMock(final T arg)
    {
        content = arg;
    }
    //---------------------------------------------------------------------------------------------
    @Override
    public int statusCode()
    {
        return status;
    }
    //---------------------------------------------------------------------------------------------
    @Override
    public Version version()
    {
        return Version.HTTP_1_1;
    }
    //---------------------------------------------------------------------------------------------
    @Override
    public URI uri()
    {
        return null;
    }
    //---------------------------------------------------------------------------------------------
    @Override
    public Optional<SSLSession> sslSession()
    {
        return Optional.<SSLSession>empty();
    }
    //---------------------------------------------------------------------------------------------
    @Override
    public T body()
    {
        return content;
    }
    //---------------------------------------------------------------------------------------------
    @Override
    public HttpHeaders headers()
    {
        return HttpHeaders.of(headerList, null);
    }
    //---------------------------------------------------------------------------------------------
    @Override
    public Optional<HttpResponse<T>> previousResponse()
    {
        return Optional.<HttpResponse<T>>empty();
    }
    //---------------------------------------------------------------------------------------------
    @Override
    public HttpRequest request()
    {
        return null;
    }
}