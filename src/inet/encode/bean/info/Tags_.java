
package inet.encode.bean.info;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Tags_ {

    @SerializedName("major_brand")
    @Expose
    private String majorBrand;
    @SerializedName("minor_version")
    @Expose
    private String minorVersion;
    @SerializedName("compatible_brands")
    @Expose
    private String compatibleBrands;
    @SerializedName("creation_time")
    @Expose
    private String creationTime;

    /**
     * 
     * @return
     *     The majorBrand
     */
    public String getMajorBrand() {
        return majorBrand;
    }

    /**
     * 
     * @param majorBrand
     *     The major_brand
     */
    public void setMajorBrand(String majorBrand) {
        this.majorBrand = majorBrand;
    }

    /**
     * 
     * @return
     *     The minorVersion
     */
    public String getMinorVersion() {
        return minorVersion;
    }

    /**
     * 
     * @param minorVersion
     *     The minor_version
     */
    public void setMinorVersion(String minorVersion) {
        this.minorVersion = minorVersion;
    }

    /**
     * 
     * @return
     *     The compatibleBrands
     */
    public String getCompatibleBrands() {
        return compatibleBrands;
    }

    /**
     * 
     * @param compatibleBrands
     *     The compatible_brands
     */
    public void setCompatibleBrands(String compatibleBrands) {
        this.compatibleBrands = compatibleBrands;
    }

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

}
