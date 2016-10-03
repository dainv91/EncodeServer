/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inet.key.bean;

/**
 *
 * @author dainv
 */
public class VideoQueue {

    private int id;
    private String inputFile;
    private String outputFolderWithSlash;
    private String logFile;

    private VideoInfoJson videoInfo;

    public VideoQueue() {
    }

    public VideoQueue(int id, String inputFile, String outputFolderWithSlash, String logFile, VideoInfoJson videoInfo) {
        this.id = id;
        this.inputFile = inputFile;
        this.outputFolderWithSlash = outputFolderWithSlash;
        this.logFile = logFile;
        this.videoInfo = videoInfo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getInputFile() {
        return inputFile;
    }

    public void setInputFile(String inputFile) {
        this.inputFile = inputFile;
    }

    public String getOutputFolderWithSlash() {
        return outputFolderWithSlash;
    }

    public void setOutputFolderWithSlash(String outputFolderWithSlash) {
        this.outputFolderWithSlash = outputFolderWithSlash;
    }

    public String getLogFile() {
        return logFile;
    }

    public void setLogFile(String logFile) {
        this.logFile = logFile;
    }

    public VideoInfoJson getVideoInfo() {
        return videoInfo;
    }

    public void setVideoInfo(VideoInfoJson videoInfo) {
        this.videoInfo = videoInfo;
    }

    @Override
    public String toString() {
        return "VideoQueue{" + "id=" + id + ", inputFile=" + inputFile + ", outputFolderWithSlash=" + outputFolderWithSlash + ", logFile=" + logFile + ", videoInfo=" + videoInfo + '}';
    }

}
