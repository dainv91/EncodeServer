
package inet.encode.bean.info;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Stream {

    @SerializedName("index")
    @Expose
    private int index;
    @SerializedName("codec_name")
    @Expose
    private String codecName;
    @SerializedName("codec_long_name")
    @Expose
    private String codecLongName;
    @SerializedName("profile")
    @Expose
    private String profile;
    @SerializedName("codec_type")
    @Expose
    private String codecType;
    @SerializedName("codec_time_base")
    @Expose
    private String codecTimeBase;
    @SerializedName("codec_tag_string")
    @Expose
    private String codecTagString;
    @SerializedName("codec_tag")
    @Expose
    private String codecTag;
    @SerializedName("width")
    @Expose
    private int width;
    @SerializedName("height")
    @Expose
    private int height;
    @SerializedName("has_b_frames")
    @Expose
    private int hasBFrames;
    @SerializedName("sample_aspect_ratio")
    @Expose
    private String sampleAspectRatio;
    @SerializedName("display_aspect_ratio")
    @Expose
    private String displayAspectRatio;
    @SerializedName("pix_fmt")
    @Expose
    private String pixFmt;
    @SerializedName("level")
    @Expose
    private int level;
    @SerializedName("r_frame_rate")
    @Expose
    private String rFrameRate;
    @SerializedName("avg_frame_rate")
    @Expose
    private String avgFrameRate;
    @SerializedName("time_base")
    @Expose
    private String timeBase;
    @SerializedName("start_pts")
    @Expose
    private int startPts;
    @SerializedName("start_time")
    @Expose
    private String startTime;
    @SerializedName("duration_ts")
    @Expose
    private int durationTs;
    @SerializedName("duration")
    @Expose
    private String duration;
    @SerializedName("bit_rate")
    @Expose
    private String bitRate;
    @SerializedName("nb_frames")
    @Expose
    private String nbFrames;
    @SerializedName("disposition")
    @Expose
    private Disposition disposition;
    @SerializedName("tags")
    @Expose
    private Tags tags;
    @SerializedName("sample_fmt")
    @Expose
    private String sampleFmt;
    @SerializedName("sample_rate")
    @Expose
    private String sampleRate;
    @SerializedName("channels")
    @Expose
    private int channels;
    @SerializedName("channel_layout")
    @Expose
    private String channelLayout;
    @SerializedName("bits_per_sample")
    @Expose
    private int bitsPerSample;

    /**
     * 
     * @return
     *     The index
     */
    public int getIndex() {
        return index;
    }

    /**
     * 
     * @param index
     *     The index
     */
    public void setIndex(int index) {
        this.index = index;
    }

    /**
     * 
     * @return
     *     The codecName
     */
    public String getCodecName() {
        return codecName;
    }

    /**
     * 
     * @param codecName
     *     The codec_name
     */
    public void setCodecName(String codecName) {
        this.codecName = codecName;
    }

    /**
     * 
     * @return
     *     The codecLongName
     */
    public String getCodecLongName() {
        return codecLongName;
    }

    /**
     * 
     * @param codecLongName
     *     The codec_long_name
     */
    public void setCodecLongName(String codecLongName) {
        this.codecLongName = codecLongName;
    }

    /**
     * 
     * @return
     *     The profile
     */
    public String getProfile() {
        return profile;
    }

    /**
     * 
     * @param profile
     *     The profile
     */
    public void setProfile(String profile) {
        this.profile = profile;
    }

    /**
     * 
     * @return
     *     The codecType
     */
    public String getCodecType() {
        return codecType;
    }

    /**
     * 
     * @param codecType
     *     The codec_type
     */
    public void setCodecType(String codecType) {
        this.codecType = codecType;
    }

    /**
     * 
     * @return
     *     The codecTimeBase
     */
    public String getCodecTimeBase() {
        return codecTimeBase;
    }

    /**
     * 
     * @param codecTimeBase
     *     The codec_time_base
     */
    public void setCodecTimeBase(String codecTimeBase) {
        this.codecTimeBase = codecTimeBase;
    }

    /**
     * 
     * @return
     *     The codecTagString
     */
    public String getCodecTagString() {
        return codecTagString;
    }

    /**
     * 
     * @param codecTagString
     *     The codec_tag_string
     */
    public void setCodecTagString(String codecTagString) {
        this.codecTagString = codecTagString;
    }

    /**
     * 
     * @return
     *     The codecTag
     */
    public String getCodecTag() {
        return codecTag;
    }

    /**
     * 
     * @param codecTag
     *     The codec_tag
     */
    public void setCodecTag(String codecTag) {
        this.codecTag = codecTag;
    }

    /**
     * 
     * @return
     *     The width
     */
    public int getWidth() {
        return width;
    }

    /**
     * 
     * @param width
     *     The width
     */
    public void setWidth(int width) {
        this.width = width;
    }

    /**
     * 
     * @return
     *     The height
     */
    public int getHeight() {
        return height;
    }

    /**
     * 
     * @param height
     *     The height
     */
    public void setHeight(int height) {
        this.height = height;
    }

    /**
     * 
     * @return
     *     The hasBFrames
     */
    public int getHasBFrames() {
        return hasBFrames;
    }

    /**
     * 
     * @param hasBFrames
     *     The has_b_frames
     */
    public void setHasBFrames(int hasBFrames) {
        this.hasBFrames = hasBFrames;
    }

    /**
     * 
     * @return
     *     The sampleAspectRatio
     */
    public String getSampleAspectRatio() {
        return sampleAspectRatio;
    }

    /**
     * 
     * @param sampleAspectRatio
     *     The sample_aspect_ratio
     */
    public void setSampleAspectRatio(String sampleAspectRatio) {
        this.sampleAspectRatio = sampleAspectRatio;
    }

    /**
     * 
     * @return
     *     The displayAspectRatio
     */
    public String getDisplayAspectRatio() {
        return displayAspectRatio;
    }

    /**
     * 
     * @param displayAspectRatio
     *     The display_aspect_ratio
     */
    public void setDisplayAspectRatio(String displayAspectRatio) {
        this.displayAspectRatio = displayAspectRatio;
    }

    /**
     * 
     * @return
     *     The pixFmt
     */
    public String getPixFmt() {
        return pixFmt;
    }

    /**
     * 
     * @param pixFmt
     *     The pix_fmt
     */
    public void setPixFmt(String pixFmt) {
        this.pixFmt = pixFmt;
    }

    /**
     * 
     * @return
     *     The level
     */
    public int getLevel() {
        return level;
    }

    /**
     * 
     * @param level
     *     The level
     */
    public void setLevel(int level) {
        this.level = level;
    }

    /**
     * 
     * @return
     *     The rFrameRate
     */
    public String getRFrameRate() {
        return rFrameRate;
    }

    /**
     * 
     * @param rFrameRate
     *     The r_frame_rate
     */
    public void setRFrameRate(String rFrameRate) {
        this.rFrameRate = rFrameRate;
    }

    /**
     * 
     * @return
     *     The avgFrameRate
     */
    public String getAvgFrameRate() {
        return avgFrameRate;
    }

    /**
     * 
     * @param avgFrameRate
     *     The avg_frame_rate
     */
    public void setAvgFrameRate(String avgFrameRate) {
        this.avgFrameRate = avgFrameRate;
    }

    /**
     * 
     * @return
     *     The timeBase
     */
    public String getTimeBase() {
        return timeBase;
    }

    /**
     * 
     * @param timeBase
     *     The time_base
     */
    public void setTimeBase(String timeBase) {
        this.timeBase = timeBase;
    }

    /**
     * 
     * @return
     *     The startPts
     */
    public int getStartPts() {
        return startPts;
    }

    /**
     * 
     * @param startPts
     *     The start_pts
     */
    public void setStartPts(int startPts) {
        this.startPts = startPts;
    }

    /**
     * 
     * @return
     *     The startTime
     */
    public String getStartTime() {
        return startTime;
    }

    /**
     * 
     * @param startTime
     *     The start_time
     */
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    /**
     * 
     * @return
     *     The durationTs
     */
    public int getDurationTs() {
        return durationTs;
    }

    /**
     * 
     * @param durationTs
     *     The duration_ts
     */
    public void setDurationTs(int durationTs) {
        this.durationTs = durationTs;
    }

    /**
     * 
     * @return
     *     The duration
     */
    public String getDuration() {
        return duration;
    }

    /**
     * 
     * @param duration
     *     The duration
     */
    public void setDuration(String duration) {
        this.duration = duration;
    }

    /**
     * 
     * @return
     *     The bitRate
     */
    public String getBitRate() {
        return bitRate;
    }

    /**
     * 
     * @param bitRate
     *     The bit_rate
     */
    public void setBitRate(String bitRate) {
        this.bitRate = bitRate;
    }

    /**
     * 
     * @return
     *     The nbFrames
     */
    public String getNbFrames() {
        return nbFrames;
    }

    /**
     * 
     * @param nbFrames
     *     The nb_frames
     */
    public void setNbFrames(String nbFrames) {
        this.nbFrames = nbFrames;
    }

    /**
     * 
     * @return
     *     The disposition
     */
    public Disposition getDisposition() {
        return disposition;
    }

    /**
     * 
     * @param disposition
     *     The disposition
     */
    public void setDisposition(Disposition disposition) {
        this.disposition = disposition;
    }

    /**
     * 
     * @return
     *     The tags
     */
    public Tags getTags() {
        return tags;
    }

    /**
     * 
     * @param tags
     *     The tags
     */
    public void setTags(Tags tags) {
        this.tags = tags;
    }

    /**
     * 
     * @return
     *     The sampleFmt
     */
    public String getSampleFmt() {
        return sampleFmt;
    }

    /**
     * 
     * @param sampleFmt
     *     The sample_fmt
     */
    public void setSampleFmt(String sampleFmt) {
        this.sampleFmt = sampleFmt;
    }

    /**
     * 
     * @return
     *     The sampleRate
     */
    public String getSampleRate() {
        return sampleRate;
    }

    /**
     * 
     * @param sampleRate
     *     The sample_rate
     */
    public void setSampleRate(String sampleRate) {
        this.sampleRate = sampleRate;
    }

    /**
     * 
     * @return
     *     The channels
     */
    public int getChannels() {
        return channels;
    }

    /**
     * 
     * @param channels
     *     The channels
     */
    public void setChannels(int channels) {
        this.channels = channels;
    }

    /**
     * 
     * @return
     *     The channelLayout
     */
    public String getChannelLayout() {
        return channelLayout;
    }

    /**
     * 
     * @param channelLayout
     *     The channel_layout
     */
    public void setChannelLayout(String channelLayout) {
        this.channelLayout = channelLayout;
    }

    /**
     * 
     * @return
     *     The bitsPerSample
     */
    public int getBitsPerSample() {
        return bitsPerSample;
    }

    /**
     * 
     * @param bitsPerSample
     *     The bits_per_sample
     */
    public void setBitsPerSample(int bitsPerSample) {
        this.bitsPerSample = bitsPerSample;
    }

}
