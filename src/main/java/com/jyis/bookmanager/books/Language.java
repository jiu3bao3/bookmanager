////////////////////////////////////////////////////////////////////////////////////////////////////
package com.jyis.bookmanager.books;
////////////////////////////////////////////////////////////////////////////////////////////////////
import java.util.stream.Stream;
////////////////////////////////////////////////////////////////////////////////////////////////////
/**
 * 言語
 * @author 久保　由仁
 */
public enum Language
{
    JAPANESE(1),
    
    ENGLISH(2),
    
    CHINESE(3);
    
    /** 言語コード */
    private final int languageCode;
    //---------------------------------------------------------------------------------------------
    /**
     * コンストラクタ
     * @param value 言語コード
     */
    private Language(final int value)
    {
        this.languageCode = value;
    }
    //---------------------------------------------------------------------------------------------
    /**
     * ＤＢに格納されている値からenumを取得する
     * @param arg ＤＢに格納されている値
     * @return 言語のenum値
     */
    public static Language of(final Integer arg)
    {
        if(arg == null || arg == 0) return null;
        return Stream.<Language>of(Language.values()).filter(item -> item.languageCode == arg)
                                                    .findFirst().get();
    }
}
