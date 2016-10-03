
package inet.encode.bean.info;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Tags {

    @SerializedName("creation_time")
    @Expose
    private String creationTime;
    @SerializedName("language")
    @Expose
    private String language;
    @SerializedName("handler_name")
    @Expose
    private String handlerName;

    /**
     * 
     * @return
     *     The creationTime
     */
    public String getCreationTime() {
        return creationTime;
    }

    /**
     * 
     * @param creationTime
     *     The creation_time
     */
    public void setCreationTime(String creationTime) {
        this.creationTime = creationTime;
    }

    /**
     * 
     * @return
     *     The language
     */
    public String getLanguage() {
        return language;
    }

    /**
     * 
     * @param language
     *     The language
     */
    public void setLanguage(String language) {
        this.language = language;
    }

    /**
     * 
     * @return
     *     The handlerName
     */
    public String getHandlerName() {
        return handlerName;
    }

    /**
     * 
     * @param handlerName
     *     The handler_name
     */
    public void setHandlerName(String handlerName) {
        this.handlerName = handlerName;
    }

}
