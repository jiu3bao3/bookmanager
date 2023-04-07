////////////////////////////////////////////////////////////////////////////////////////////////////
package com.jyis.bookmanager;
////////////////////////////////////////////////////////////////////////////////////////////////////
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;
import org.springframework.test.context.junit.jupiter.SpringExtension;
////////////////////////////////////////////////////////////////////////////////////////////////////
/**
 * AbstractFormのテスト
 * @author 久保　由仁
 */
@SpringBootTest
@ExtendWith(SpringExtension.class)
public class AbstractFormTest
{
    /**
     * メッセージのテスト
     */
    @Test
    public void setAndGetMessageTest() throws Exception
    {
        final String MESSAGE = "Hello World";
        AbstractForm form = new AbstractFormImpl(MESSAGE);
        Assertions.assertEquals(form.getMessage(), MESSAGE, "設定したメッセージを取得できること");
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * AbstractFormのテスト用実装クラス
     */
    private final class AbstractFormImpl extends AbstractForm
    {
        public AbstractFormImpl(final String arg)
        {
            setMessage(arg);
        }
    }
}
