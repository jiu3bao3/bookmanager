////////////////////////////////////////////////////////////////////////////////////////////////////
package com.jyis.bookmanager.ndl;
////////////////////////////////////////////////////////////////////////////////////////////////////
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.batch.item.ItemProcessor;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.jyis.bookmanager.books.Book;
////////////////////////////////////////////////////////////////////////////////////////////////////
/**
 * 書誌情報取得プロセッサ
 * @author 久保　由仁
 */
public class NdlBookItemProcessor implements ItemProcessor<Book, NdlInfo>
{
    /** ロガー */
    private static final Logger logger = LoggerFactory.getLogger(NdlBookItemProcessor.class);

    /** APIエンドポイント */
    private static final String NDL_API_ENDPOINT = "https://api.openbd.jp/v1/get?isbn=%s";
    //---------------------------------------------------------------------------------------------
    /**
     * プロセッサを実行する
     * @param book 取得対象のBook
     * @return 書誌情報を格納したNdlInfoオブジェクト（取得できなかった場合はnull
     */
    @Override
    public NdlInfo process(final Book book) throws Exception
    {
        return getNdlInfo(book.getIsbn(), book.getId());
    }
    //---------------------------------------------------------------------------------------------
    /**
     * 書誌取得処理を実行する
     * @param isbn ISBN
     * @param bookId BookのID
     * @return 取得した書誌情報を格納したNdlInfoオブジェクト
     */
    private NdlInfo getNdlInfo(final String isbn, final int bookId)
    {
        if(isbn == null) return null;
        NdlInfo ndlInfo = null;
        try 
        {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                            .uri(URI.create(String.format(NDL_API_ENDPOINT, isbn)))
                            .build();
            HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
            HttpResponse<String> response = client.send(request, handler);
            if(response.statusCode() == 200)
            {
                ObjectMapper mapper = new ObjectMapper();
                JsonNode jsonRoot = mapper.readTree(response.body());
                JsonNode ndlInfomations = dig(jsonRoot.get(0), "onix", "CollateralDetail");
                if(ndlInfomations != null)
                {
                    ndlInfo = packInfo(ndlInfomations, bookId, isbn);
                    if(ndlInfo != null) logger.info(ndlInfo.toString());
                }
            }
            else
            {
                final String MESSAGE = "APIアクセスエラーが発生しました。STATUS=%s, 詳細：%s";
                logger.error(String.format(MESSAGE, response.statusCode(), response.body()));
            }
            return ndlInfo;
        }
        catch(IOException | InterruptedException e)
        {
            throw new RuntimeException(e);
        }
    }
    //---------------------------------------------------------------------------------------------
    /**
     * 取得した書誌情報をNdlInfoオブジェクトに格納する
     * @param json 取得した書誌情報のJSON
     * @param bookId BookのID
     * @param isbn ISBN
     * @return 書誌情報を格納したNdlInfoオブジェクト
     */
    private NdlInfo packInfo(final JsonNode json, final int bookId, final String isbn)
    {
        JsonNode detailNode = dig(json, "TextContent");
        if(detailNode == null) return null;
        NdlInfo ndlInfo = new NdlInfo(bookId, isbn);
        for(JsonNode child : detailNode)
        {
            ndlInfo.set(child.get("TextType").asText(), child.get("Text").asText());
        }
        return ndlInfo;
    }
    //---------------------------------------------------------------------------------------------
    /**
     * JSONを探索する
     * @param node 探索対象のルートノード
     * @param keys 探索するキー
     * @return みつかったノード（みつからなかった場合はnull）
     */
    private JsonNode dig(final JsonNode node, final String... keys)
    {
        if(node == null) return null;
        JsonNode child = node;
        for(String key : keys)
        {
            child = child.get(key);
            if(child == null) break;
        }
        return child;
    }
}
