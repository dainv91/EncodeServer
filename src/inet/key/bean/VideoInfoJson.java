/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inet.key.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import inet.util.JsonUtil;
import java.lang.reflect.Type;

/**
 *
 * @author dainv
 */
public class VideoInfoJson extends JsonBase<VideoInfoJson> {
    
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("height")
    @Expose
    private int height;
    @SerializedName("in_file")
    @Expose
    private String inFile;
    @SerializedName("videos")
    @Expose
    private String videos;
    @SerializedName("out_folder")
    @Expose
    private String outFolder;
    @SerializedName("thumb_count")
    @Expose
    private int thumbCount;
    @SerializedName("priority")
    @Expose
    private String priority;
    @SerializedName("callback")
    @Expose
    private String callback;
    @SerializedName("bit_rate")
    @Expose
    private int bitRate;
    @SerializedName("width")
    @Expose
    private int width;
    @SerializedName("thumbs")
    @Expose
    private String thumbs;
    @SerializedName("thumb_size")
    @Expose
    private String thumbSize;
    @SerializedName("duration")
    @Expose
    private int duration;
    @SerializedName("thumb_folder")
    @Expose
    private String thumbFolder;
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("size")
    @Expose
    private int size;

    /**
     *
     * @return The status
     */
    public String getStatus() {
        return status;
    }

    /**
     *
     * @param status The status
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     *
     * @return The height
     */
    public int getHeight() {
        return height;
    }

    /**
     *
     * @param height The height
     */
    public void setHeight(int height) {
        this.height = height;
    }

    /**
     *
     * @return The inFile
     */
    public String getInFile() {
        return inFile;
    }

    /**
     *
     * @param inFile The in_file
     */
    public void setInFile(String inFile) {
        this.inFile = inFile;
    }

    /**
     *
     * @return The videos
     */
    public String getVideos() {
        return videos;
    }

    /**
     *
     * @param videos The videos
     */
    public void setVideos(String videos) {
        this.videos = videos;
    }

    /**
     *
     * @return The outFolder
     */
    public String getOutFolder() {
        return outFolder;
    }

    /**
     *
     * @param outFolder The out_folder
     */
    public void setOutFolder(String outFolder) {
        this.outFolder = outFolder;
    }

    /**
     *
     * @return The thumbCount
     */
    public int getThumbCount() {
        return thumbCount;
    }

    /**
     *
     * @param thumbCount The thumb_count
     */
    public void setThumbCount(int thumbCount) {
        this.thumbCount = thumbCount;
    }

    /**
     *
     * @return The priority
     */
    public String getPriority() {
        return priority;
    }

    /**
     *
     * @param priority The priority
     */
    public void setPriority(String priority) {
        this.priority = priority;
    }

    /**
     *
     * @return The callback
     */
    public String getCallback() {
        return callback;
    }

    /**
     *
     * @param callback The callback
     */
    public void setCallback(String callback) {
        this.callback = callback;
    }

    /**
     *
     * @return The bitRate
     */
    public int getBitRate() {
        return bitRate;
    }

    /**
     *
     * @param bitRate The bit_rate
     */
    public void setBitRate(int bitRate) {
        this.bitRate = bitRate;
    }

    /**
     *
     * @return The width
     */
    public int getWidth() {
        return width;
    }

    /**
     *
     * @param width The width
     */
    public void setWidth(int width) {
        this.width = width;
    }

    /**
     *
     * @return The thumbs
     */
    public String getThumbs() {
        return thumbs;
    }

    /**
     *
     * @param thumbs The thumbs
     */
    public void setThumbs(String thumbs) {
        this.thumbs = thumbs;
    }

    /**
     *
     * @return The thumbSize
     */
    public String getThumbSize() {
        return thumbSize;
    }

    /**
     *
     * @param thumbSize The thumb_size
     */
    public void setThumbSize(String thumbSize) {
        this.thumbSize = thumbSize;
    }

    /**
     *
     * @return The duration
     */
    public int getDuration() {
        return duration;
    }

    /**
     *
     * @param duration The duration
     */
    public void setDuration(int duration) {
        this.duration = duration;
    }

    /**
     *
     * @return The thumbFolder
     */
    public String getThumbFolder() {
        return thumbFolder;
    }

    /**
     *
     * @param thumbFolder The thumb_folder
     */
    public void setThumbFolder(String thumbFolder) {
        this.thumbFolder = thumbFolder;
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
     * @return The size
     */
    public int getSize() {
        return size;
    }

    /**
     *
     * @param size The size
     */
    public void setSize(int size) {
        this.size = size;
    }
    
    public static VideoInfoJson parseJsonFromChild(String json) {
        Type type = new TypeToken<VideoInfoJson>() {
        }.getType();
        VideoInfoJson info;
        info = JsonUtil.fromJson(json, type);
        return info;
    }
    
}
