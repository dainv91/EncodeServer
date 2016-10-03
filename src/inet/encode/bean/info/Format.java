
package inet.encode.bean.info;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Format {

    @SerializedName("filename")
    @Expose
    private String filename;
    @SerializedName("nb_streams")
    @Expose
    private int nbStreams;
    @SerializedName("nb_programs")
    @Expose
    private int nbPrograms;
    @SerializedName("format_name")
    @Expose
    private String formatName;
    @SerializedName("format_long_name")
    @Expose
    private String formatLongName;
    @SerializedName("start_time")
    @Expose
    private String startTime;
    @SerializedName("duration")
    @Expose
    private String duration;
    @SerializedName("size")
    @Expose
    private String size;
    @SerializedName("bit_rate")
    @Expose
    private String bitRate;
    @SerializedName("probe_score")
    @Expose
    private int probeScore;
    @SerializedName("tags")
    @Expose
    private Tags_ tags;

    /**
     * 
     * @return
     *     The filename
     */
    public String getFilename() {
        return filename;
    }

    /**
     * 
     * @param filename
     *     The filename
     */
    public void setFilename(String filename) {
        this.filename = filename;
    }

    /**
     * 
     * @return
     *     The nbStreams
     */
    public int getNbStreams() {
        return nbStreams;
    }

    /**
     * 
     * @param nbStreams
     *     The nb_streams
     */
    public void setNbStreams(int nbStreams) {
        this.nbStreams = nbStreams;
    }

    /**
     * 
     * @return
     *     The nbPrograms
     */
    public int getNbPrograms() {
        return nbPrograms;
    }

    /**
     * 
     * @param nbPrograms
     *     The nb_programs
     */
    public void setNbPrograms(int nbPrograms) {
        this.nbPrograms = nbPrograms;
    }

    /**
     * 
     * @return
     *     The formatName
     */
    public String getFormatName() {
        return formatName;
    }

    /**
     * 
     * @param formatName
     *     The format_name
     */
    public void setFormatName(String formatName) {
        this.formatName = formatName;
    }

    /**
     * 
     * @return
     *     The formatLongName
     */
    public String getFormatLongName() {
        return formatLongName;
    }

    /**
     * 
     * @param formatLongName
     *     The format_long_name
     */
    public void setFormatLongName(String formatLongName) {
        this.formatLongName = formatLongName;
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
     *     The size
     */
    public String getSize() {
        return size;
    }

    /**
     * 
     * @param size
     *     The size
     */
    public void setSize(String size) {
        this.size = size;
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
     *     The probeScore
     */
    public int getProbeScore() {
        return probeScore;
    }

    /**
     * 
     * @param probeScore
     *     The probe_score
     */
    public void setProbeScore(int probeScore) {
        this.probeScore = probeScore;
    }

    /**
     * 
     * @return
     *     The tags
     */
    public Tags_ getTags() {
        return tags;
    }

    /**
     * 
     * @param tags
     *     The tags
     */
    public void setTags(Tags_ tags) {
        this.tags = tags;
    }

}
