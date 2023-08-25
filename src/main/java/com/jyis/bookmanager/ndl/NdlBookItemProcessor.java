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

    private static final String NDL_API_ENDPOINT = "https://api.openbd.jp/v1/get?isbn=%s";
    //---------------------------------------------------------------------------------------------
    @Override
    public NdlInfo process(final Book book) throws Exception
    {
        getNdlInfo(book.getIsbn());
        return new NdlInfo();
    }
    private void getNdlInfo(final String isbn)
    {
        if(isbn == null) return;
        try 
        {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                            .uri(URI.create(String.format(NDL_API_ENDPOINT, isbn)))
                            .build();
            HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
            HttpResponse<String> response = client.send(request, handler);
            logger.info("STATUS=" + response.statusCode());
            if(response.statusCode() == 200)
            {
                HttpHeaders headers = response.headers();
                Optional<String> contentType = headers.firstValue("Content-Type");
                contentType.ifPresent(c -> logger.info(c));
                String body = response.body();
                logger.info(body);
                ObjectMapper mapper = new ObjectMapper();
                JsonNode jsonRoot = mapper.readTree(body);
                JsonNode firstNode = jsonRoot.get(0);
                if(firstNode != null)
                {
                    JsonNode onixNode = firstNode.get("onix");
                    if(onixNode != null)
                    {
                        logger.info(onixNode.get("CollateralDetail").toString());
                    }
                }
                
            }
            Thread.sleep(100);
        }
        catch(IOException | InterruptedException e)
        {
            throw new RuntimeException(e);
        }
    }
}
