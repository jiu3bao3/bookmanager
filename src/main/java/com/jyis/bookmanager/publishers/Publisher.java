////////////////////////////////////////////////////////////////////////////////////////////////////
package com.jyis.bookmanager.publishers;
////////////////////////////////////////////////////////////////////////////////////////////////////
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.jyis.bookmanager.AbstractForm;
////////////////////////////////////////////////////////////////////////////////////////////////////
/**
 * 出版社クラス
 * @author 久保　由仁
 */
public class Publisher extends AbstractForm implements Serializable
{
    /** ID */
    @JsonProperty("id")
    private Integer id;

    /** 出版社名 */
    @JsonProperty("name")
    private String name;
    
    /** 郵便番号 */
    private String zip;
    
    /** 住所１ */
    private String address1;
    
    /** 住所２ */
    private String address2;
    
    /** 電話番号 */
    private String phone;
    
    /** メールアドレス */
    private String email;
    //----------------------------------------------------------------------------------------------
    /**
     * デフォルトコンストラクタ
     */
    public Publisher() {}
    //----------------------------------------------------------------------------------------------
    /**
     * コンストラクタ
     * @param arg 出版社名
     */
    public Publisher(String arg)
    {
        name = arg;
    }
    //----------------------------------------------------------------------------------------------
    /**
     * このクラスの文字列表現を返す
     * @return 文字列表現
     */
    @Override
    public String toString()
    {
        return String.format("[%s] %s(%d)", this.getClass().getName(), name, id);
    }
    //----------------------------------------------------------------------------------------------
    /**
     * IDをセットする
     * @param arg ID
     */
    public void setId(final Integer arg)
    {
        id = arg;
    }
    //----------------------------------------------------------------------------------------------
    /**
     * IDを取得する
     * @return ID
     */
    public Integer getId()
    {
        return id;
    }
    //----------------------------------------------------------------------------------------------
    /**
     * 出版社名を取得する
     * @return 出版社名
     */
    public String getName()
    {
        return name;
    }
    //----------------------------------------------------------------------------------------------
    /**
     * 出版社名をセットする
     * @param arg 出版社名
     */
    public void setName(final String arg)
    {
        name = arg;
    }
    //----------------------------------------------------------------------------------------------
    /**
     * zip を取得する
     * @return zip
     */
    public String getZip()
    {
        return zip;
    }
    //----------------------------------------------------------------------------------------------
    /**
     * zip を設定する
     * @param arg zip
     */
    public void setZip(final String arg)
    {
        zip = arg;
    }
    //----------------------------------------------------------------------------------------------
    /**
     * address1 を取得する
     * @return address1
     */
    public String getAddress1()
    {
        return address1;
    }
    //----------------------------------------------------------------------------------------------
    /**
     * address1 を設定する
     * @param arg address1
     */
    public void setAddress1(final String arg)
    {
        address1 = arg;
    }
    //----------------------------------------------------------------------------------------------
    /**
     * address2 を取得する
     * @return address2
     */
    public String getAddress2()
    {
        return address2;
    }
    //----------------------------------------------------------------------------------------------
    /**
     * address2 を設定する
     * @param arg address2
     */
    public void setAddress2(final String arg)
    {
        address2 = arg;
    }
    //----------------------------------------------------------------------------------------------
    /**
     * phone を取得する
     * @return phone
     */
    public String getPhone()
    {
        return phone;
    }
    //----------------------------------------------------------------------------------------------
    /**
     * phone を設定する
     * @param arg phone
     */
    public void setPhone(final String arg)
    {
        phone = arg;
    }
    //----------------------------------------------------------------------------------------------
    /**
     * email を取得する
     * @return email
     */
    public String getEmail()
    {
        return email;
    }
    //----------------------------------------------------------------------------------------------
    /**
     * email を設定する
     * @param arg email
     */
    public void setEmail(final String arg)
    {
        email = arg;
    }
}
