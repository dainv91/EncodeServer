/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inet.key.bean;

import com.google.gson.annotations.Expose;

/**
 *
 * @author dainv
 */
public class VideoEncodeInfo extends JsonBase<VideoEncodeInfo> {

    private int id;
    private int currentFrame;
    private int totalFrames;
    private int currentTime;
    private int duration;
    private String name;
    private double currentPercent;

    @Expose
    private transient String logFile;
    @Expose
    private transient String infoLogFile;

    public VideoEncodeInfo() {
    }

    public VideoEncodeInfo(int id, int currentFrame, int totalFrames, int currentTime, int duration, String name, double currentPercent, String logFile, String infoLogFile) {
        this.id = id;
        this.currentFrame = currentFrame;
        this.totalFrames = totalFrames;
        this.currentTime = currentTime;
        this.duration = duration;
        this.name = name;
        this.currentPercent = currentPercent;
        this.logFile = logFile;
        this.infoLogFile = infoLogFile;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCurrentFrame() {
        return currentFrame;
    }

    public void setCurrentFrame(int currentFrame) {
        this.currentFrame = currentFrame;
    }

    public int getTotalFrames() {
        return totalFrames;
    }

    public void setTotalFrames(int totalFrames) {
        this.totalFrames = totalFrames;
    }

    public int getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(int currentTime) {
        this.currentTime = currentTime;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getCurrentPercent() {
        return currentPercent;
    }

    public void setCurrentPercent(double currentPercent) {
        this.currentPercent = currentPercent;
    }

    public String getLogFile() {
        return logFile;
    }

    public void setLogFile(String logFile) {
        this.logFile = logFile;
    }

    public String getInfoLogFile() {
        return infoLogFile;
    }

    public void setInfoLogFile(String infoLogFile) {
        this.infoLogFile = infoLogFile;
    }

    public VideoEncodeInfo copyInstance() {
        VideoEncodeInfo obj = new VideoEncodeInfo(getId(), getCurrentFrame(), getTotalFrames(), getCurrentTime(), getDuration(), getName(), getCurrentPercent(), getLogFile(), getInfoLogFile());
        return obj;
    }

    public static void main(String[] args) {
        VideoEncodeInfo obj = new VideoEncodeInfo();
        obj.setLogFile("idd");
        System.out.println(obj.toJson());
    }

}
