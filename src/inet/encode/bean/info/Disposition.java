
package inet.encode.bean.info;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Disposition {

    @SerializedName("default")
    @Expose
    private int _default;
    @SerializedName("dub")
    @Expose
    private int dub;
    @SerializedName("original")
    @Expose
    private int original;
    @SerializedName("comment")
    @Expose
    private int comment;
    @SerializedName("lyrics")
    @Expose
    private int lyrics;
    @SerializedName("karaoke")
    @Expose
    private int karaoke;
    @SerializedName("forced")
    @Expose
    private int forced;
    @SerializedName("hearing_impaired")
    @Expose
    private int hearingImpaired;
    @SerializedName("visual_impaired")
    @Expose
    private int visualImpaired;
    @SerializedName("clean_effects")
    @Expose
    private int cleanEffects;
    @SerializedName("attached_pic")
    @Expose
    private int attachedPic;

    /**
     * 
     * @return
     *     The _default
     */
    public int getDefault() {
        return _default;
    }

    /**
     * 
     * @param _default
     *     The default
     */
    public void setDefault(int _default) {
        this._default = _default;
    }

    /**
     * 
     * @return
     *     The dub
     */
    public int getDub() {
        return dub;
    }

    /**
     * 
     * @param dub
     *     The dub
     */
    public void setDub(int dub) {
        this.dub = dub;
    }

    /**
     * 
     * @return
     *     The original
     */
    public int getOriginal() {
        return original;
    }

    /**
     * 
     * @param original
     *     The original
     */
    public void setOriginal(int original) {
        this.original = original;
    }

    /**
     * 
     * @return
     *     The comment
     */
    public int getComment() {
        return comment;
    }

    /**
     * 
     * @param comment
     *     The comment
     */
    public void setComment(int comment) {
        this.comment = comment;
    }

    /**
     * 
     * @return
     *     The lyrics
     */
    public int getLyrics() {
        return lyrics;
    }

    /**
     * 
     * @param lyrics
     *     The lyrics
     */
    public void setLyrics(int lyrics) {
        this.lyrics = lyrics;
    }

    /**
     * 
     * @return
     *     The karaoke
     */
    public int getKaraoke() {
        return karaoke;
    }

    /**
     * 
     * @param karaoke
     *     The karaoke
     */
    public void setKaraoke(int karaoke) {
        this.karaoke = karaoke;
    }

    /**
     * 
     * @return
     *     The forced
     */
    public int getForced() {
        return forced;
    }

    /**
     * 
     * @param forced
     *     The forced
     */
    public void setForced(int forced) {
        this.forced = forced;
    }

    /**
     * 
     * @return
     *     The hearingImpaired
     */
    public int getHearingImpaired() {
        return hearingImpaired;
    }

    /**
     * 
     * @param hearingImpaired
     *     The hearing_impaired
     */
    public void setHearingImpaired(int hearingImpaired) {
        this.hearingImpaired = hearingImpaired;
    }

    /**
     * 
     * @return
     *     The visualImpaired
     */
    public int getVisualImpaired() {
        return visualImpaired;
    }

    /**
     * 
     * @param visualImpaired
     *     The visual_impaired
     */
    public void setVisualImpaired(int visualImpaired) {
        this.visualImpaired = visualImpaired;
    }

    /**
     * 
     * @return
     *     The cleanEffects
     */
    public int getCleanEffects() {
        return cleanEffects;
    }

    /**
     * 
     * @param cleanEffects
     *     The clean_effects
     */
    public void setCleanEffects(int cleanEffects) {
        this.cleanEffects = cleanEffects;
    }

    /**
     * 
     * @return
     *     The attachedPic
     */
    public int getAttachedPic() {
        return attachedPic;
    }

    /**
     * 
     * @param attachedPic
     *     The attached_pic
     */
    public void setAttachedPic(int attachedPic) {
        this.attachedPic = attachedPic;
    }

}
