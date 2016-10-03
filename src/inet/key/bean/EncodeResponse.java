/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inet.key.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 *
 * @author dainv
 */
public class EncodeResponse extends JsonBase<EncodeResponse> {
    
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("more_info")
    @Expose
    private String moreInfo;

    /**
     *
     * @return The message
     */
    public String getMessage() {
        return message;
    }

    /**
     *
     * @param message The message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     *
     * @return The id
     */
    public int getId() {
        return id;
    }

    /**
     *
     * @param id The id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     *
     * @return The moreInfo
     */
    public String getMoreInfo() {
        return moreInfo;
    }

    /**
     *
     * @param moreInfo The more_info
     */
    public void setMoreInfo(String moreInfo) {
        this.moreInfo = moreInfo;
    }
    
}
