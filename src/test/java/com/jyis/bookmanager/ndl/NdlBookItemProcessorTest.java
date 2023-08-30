////////////////////////////////////////////////////////////////////////////////////////////////////
package com.jyis.bookmanager.ndl;
////////////////////////////////////////////////////////////////////////////////////////////////////
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.boot.test.context.SpringBootTest;
////////////////////////////////////////////////////////////////////////////////////////////////////
@SpringBootTest
public class NdlBookItemProcessorTest
{
    /** テストに用いるJSON */
    private static final String JSON_EXAMPLE = "{ \"result\" : { \"message\" : \"example\", "+
                                                "\"data\" : [\"A\", \"B\"]} }";
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
}
