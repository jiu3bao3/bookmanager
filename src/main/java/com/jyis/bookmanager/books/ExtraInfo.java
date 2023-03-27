////////////////////////////////////////////////////////////////////////////////////////////////////
package com.jyis.bookmanager.books;
////////////////////////////////////////////////////////////////////////////////////////////////////
import java.io.Serializable;
import java.util.stream.Stream;
////////////////////////////////////////////////////////////////////////////////////////////////////
/**
 * ExtraInfo enum
 * @author 久保　由仁
 */
public enum ExtraInfo
{
    NOTE("03"),
    COONTENTS("04");
    
    /** DBに格納されている値 */
    private String typeCode;
    //----------------------------------------------------------------------------------------------
    /**
     * コンストラクタ
     */
    private ExtraInfo(String arg)
    {
        this.typeCode = arg;
    }
    //----------------------------------------------------------------------------------------------
    /**
     * DBに格納されている値を取得する
     * @return DB(NOTE_TYPE)に格納されている値
     */
    public String getTypeCode()
    {
        return typeCode;
    }
    //----------------------------------------------------------------------------------------------
    /**
     * このオブジェクトの文字列表現を返す
     * @return 文字列表現
     */
    @Override
    public String toString()
    {
        return String.format("%s(%s)", name(), typeCode);
    }
    //----------------------------------------------------------------------------------------------
    /**
     * DBに格納されている値からこのEnumへ変換する
     * @param arg DBに格納されている値
     * @return ExtraInfo
     */
    public static ExtraInfo of(final String arg)
    {
        return Stream.of(ExtraInfo.values())
                .filter((i) -> i.getTypeCode().equals(arg))
                .findFirst()
                .get();
    }
}
