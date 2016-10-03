/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inet.encode;

import inet.encode.bean.VideoInfoUsingFFProbe;
import inet.key.bean.VideoEncodeInfo;
import inet.key.bean.VideoInfoJson;
import inet.util.ExecUtil;
import inet.util.FileUtil;
import inet.util.Logger;
import inet.util.StringUtil;
import inet.util.VideoUtil;
import inet.util.request.HttpRequest;
import inet.util.request.IRequest;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author dainv
 */
public class Encoder {

    private static final String CONFIG_FILE_PATH = "conf/encoder.conf";
    private static String MONITOR_SERVER_PORT_STR = "6790";

    private static String API_CMS_CALLBACK = "http://210.211.99.29:6061/index.php/video/api?auth=8B6DCA2F";
    private static final String PARAM_VIDEO_INFO = "encode";

    public static String INPUT_FOLDER_TO_SCAN = "/var/www/html/vscms2/video/videos/input/";
    static final String FOLDER_PROCESSING = "process";

    public static String OUTPUT_FOLDER_ROOT_WITH_SLASH = "/var/www/html/vscms2/video/videos/output/";

    public static boolean RUNNING = true;
    public static int TIME_CYCLE_SCAN_IN_SEC = 10;

    //public static int num = 1;
    private static ScheduledFuture<?> future = null;

    static String SHELL_COMMAND_CREATE_KEY_FILE = "~/script/v2/create_key_file.sh";
    static String SHELL_COMMAND_TRANSCODE_VIDEO = "~/script/v2/ffmpeg_transcode.sh";
    static String SHELL_COMMAND_GET_INFO_VIDEO = "~/script/v2/ffmpeg_get_info.sh";

    static int MAX_FILES_TRANSCODING = 4;
    static transient int CURRENT_FILES_TRANSCODING = 0;

    // Video id - info
    public static final Map<Integer, VideoEncodeInfo> MAP_ENCODING_STATUS;

    static {
        MAP_ENCODING_STATUS = new java.util.concurrent.ConcurrentHashMap();
    }

    private static void stop() {
        if (future != null) {
            future.cancel(true);
        }
    }

    private static VideoInfoJson createVideoInfo(String idStr, String outputFolderAbsolute) {
        int id = 0;
        String outputFolder = outputFolderAbsolute.replace(OUTPUT_FOLDER_ROOT_WITH_SLASH, "");
        try {
            id = Integer.parseInt(idStr);
        } catch (Exception ex) {

        }
        VideoInfoJson obj = new VideoInfoJson();
        obj.setId(id);
        obj.setOutFolder(outputFolder);
        return obj;
    }

    private static void processOutputVideoPath(VideoInfoJson obj, int videoQuality) {
        obj.setStatus("START");
        Map<String, String> mapQuality = new java.util.HashMap();
        final String ext = ".mp4";
        String seperate = "";
        if (!obj.getOutFolder().endsWith("/")) {
            //seperate = File.separator;
            seperate = "/";
        }
        if (videoQuality >= 1080) {
            VideoUtil.addQuality(mapQuality, "1080p", obj.getOutFolder() + seperate + "1080" + ext);
        }
        if (videoQuality >= 720) {
            VideoUtil.addQuality(mapQuality, "720p", obj.getOutFolder() + seperate + "720" + ext);
        }
        if (videoQuality >= 480) {
            VideoUtil.addQuality(mapQuality, "480p", obj.getOutFolder() + seperate + "480" + ext);
        }
        if (videoQuality >= 360) {
            VideoUtil.addQuality(mapQuality, "360p", obj.getOutFolder() + seperate + "360" + ext);
        }
        if (videoQuality >= 240) {
            VideoUtil.addQuality(mapQuality, "240p", obj.getOutFolder() + seperate + "240" + ext);
        }
//        VideoUtil.addQuality(mapQuality, "720p", obj.getOutFolder() + seperate + "720" + ext);
//        VideoUtil.addQuality(mapQuality, "480p", obj.getOutFolder() + seperate + "480" + ext);
//        VideoUtil.addQuality(mapQuality, "360p", obj.getOutFolder() + seperate + "360" + ext);
//        VideoUtil.addQuality(mapQuality, "240p", obj.getOutFolder() + seperate + "240" + ext);

        String lstVideoStr = VideoUtil.getListVideoJson(mapQuality);
        obj.setVideos(lstVideoStr);
    }

    private static void createProcessingFolder() {
        String folder = INPUT_FOLDER_TO_SCAN + File.separatorChar + FOLDER_PROCESSING;
        Path path = Paths.get(folder);
        if (!Files.exists(path)) {
            try {
                Files.createDirectories(path);
            } catch (IOException ex) {
                Logger.log(ex);
            }
        }
    }

    private static boolean moveToProcessingFolder(String inputFile) {
        String prefix = INPUT_FOLDER_TO_SCAN + File.separatorChar;
        if (INPUT_FOLDER_TO_SCAN.endsWith("/")) {
            prefix = INPUT_FOLDER_TO_SCAN;
        }
        String processingFile = prefix + FOLDER_PROCESSING + File.separatorChar + inputFile;
        String oldFile = prefix + inputFile;
        boolean result = false;
        File f = new File(oldFile);
        try {
            result = f.renameTo(new File(processingFile));
        } catch (Exception ex) {
            Logger.log(ex);
        }
        Logger.log("OLD_FILE: " + oldFile);
        Logger.log(result + "_MOVING_TO: " + processingFile);
        return result;
    }

    private static void readConfigFile() {
        Map<String, String> configs = FileUtil.readConfigFile(CONFIG_FILE_PATH);

        String shellCmdCreateKeyFile = configs.get("SHELL_COMMAND_CREATE_KEY_FILE");
        if (!StringUtil.isEmpty(shellCmdCreateKeyFile)) {
            SHELL_COMMAND_CREATE_KEY_FILE = shellCmdCreateKeyFile;
        }
        String shellCmdTranscodeVideo = configs.get("SHELL_COMMAND_TRANSCODE_VIDEO");
        if (!StringUtil.isEmpty(shellCmdTranscodeVideo)) {
            SHELL_COMMAND_TRANSCODE_VIDEO = shellCmdTranscodeVideo;
        }
        String apiCmsCallback = configs.get(("API_CMS_CALLBACK"));
        if (!StringUtil.isEmpty(apiCmsCallback)) {
            API_CMS_CALLBACK = apiCmsCallback;
        }

        String inputFolderToScan = configs.get("INPUT_FOLDER_TO_SCAN");
        if (!StringUtil.isEmpty(inputFolderToScan)) {
            INPUT_FOLDER_TO_SCAN = inputFolderToScan;
        }

        String outputFolderRootWithSlash = configs.get("OUTPUT_FOLDER_ROOT_WITH_SLASH");
        if (!StringUtil.isEmpty(outputFolderRootWithSlash)) {
            OUTPUT_FOLDER_ROOT_WITH_SLASH = outputFolderRootWithSlash;
        }

        String timeCycleScanInSecStr = configs.get("TIME_CYCLE_SCAN_IN_SEC");
        if (!StringUtil.isEmpty(timeCycleScanInSecStr)) {
            try {
                TIME_CYCLE_SCAN_IN_SEC = Integer.parseInt(timeCycleScanInSecStr);
            } catch (Exception ex) {

            }

        }

        String maxFilesTranscoding = configs.get("MAX_FILES_TRANSCODING");
        if (!StringUtil.isEmpty(maxFilesTranscoding)) {
            try {
                MAX_FILES_TRANSCODING = Integer.parseInt(maxFilesTranscoding);
            } catch (Exception ex) {

            }
        }

        String monitorServerPortStr = configs.get("MONITOR_SERVER_PORT_STR");
        if (!StringUtil.isEmpty(monitorServerPortStr)) {
            MONITOR_SERVER_PORT_STR = monitorServerPortStr;
        }

    }

    private static void processArgs(String[] args) {
        readConfigFile();
        if (args.length == 0) {
            //String options = " <SCRIPT_CREATE_KEY_FILE> <SCRIPT_TRANSCODE_VIDEO> <SCRIPT_GET_INFO_VIDEO> <PATH_ROOT_CMS>";
            String options = " <INPUT_FOLDER_TO_SCAN> <OUTPUT_FOLDER_ROOT_WITH_SLASH> <SHELL_COMMAND_TRANSCODE_VIDEO> <MONITOR_SERVER_PORT>";
            Logger.log("Using java " + Encoder.class.getName() + options);
        } else {
            Logger.log("Nums of params: " + args.length);
            for (String str : args) {
                Logger.log("\tParam: " + str);
            }
            if (args.length > 0) {
                INPUT_FOLDER_TO_SCAN = args[0];
            }
            if (args.length > 1) {
                OUTPUT_FOLDER_ROOT_WITH_SLASH = args[1];
            }
            if (args.length > 2) {
                SHELL_COMMAND_TRANSCODE_VIDEO = args[2];
            }
            if (args.length > 3) {
                MONITOR_SERVER_PORT_STR = args[3];
            }
        }
    }

    private static void handleWhenEncodeError(String idStr, String channel) {
        Logger.log("CANNOT_GET_VIDEO_INFO_" + idStr + "----" + channel);
    }

    private static VideoEncodeInfo getVideoEncodeInfo(String idStr, String relativeFileNameWithExtMoved) {
        int id = 0;
        try {
            id = Integer.parseInt(idStr);
        } catch (Exception ex) {
            Logger.log(ex);
        }
        if (id == 0) {
            return null;
        }
        String relativeFilePath = relativeFileNameWithExtMoved;
        String logFile = idStr + ".log";
        String infoLogFile = logFile + ".info";
        VideoEncodeInfo obj = new VideoEncodeInfo(id, 0, 0, 0, 0, relativeFilePath, 0, logFile, infoLogFile);
        return obj;
    }

    private static String getAbsoluteFilePathMoved(String relativeFileNameWithExtMoved) {
        String absolutePath = INPUT_FOLDER_TO_SCAN + relativeFileNameWithExtMoved;
        final String slash = "/";
        if (!INPUT_FOLDER_TO_SCAN.endsWith(slash)) {
            absolutePath = INPUT_FOLDER_TO_SCAN + slash + relativeFileNameWithExtMoved;
        }
        return absolutePath;
    }

    private static boolean addInfoVideoEncoding(String idStr, String relativeFileNameWithExtMoved) {
        boolean result = false;
        int id = 0;
        try {
            id = Integer.parseInt(idStr);
        } catch (Exception ex) {
            Logger.log(ex);
        }
        if (id == 0) {
            return result;
        }
        VideoEncodeInfo info = getVideoEncodeInfo(idStr, relativeFileNameWithExtMoved);
        if (info == null) {
            Logger.log("Invalid video info");
        } else {
            MAP_ENCODING_STATUS.put(id, info);
            String absoluteFilePath = getAbsoluteFilePathMoved(relativeFileNameWithExtMoved);
            result = execGetVideoInfo(absoluteFilePath, info.getInfoLogFile());
        }
        return result;
    }

    private static VideoInfoUsingFFProbe getVideoInfoUsingFFProbe(String absoluteFilePath) {
        final String SPACE = " ";
        String cmd = SHELL_COMMAND_GET_INFO_VIDEO + SPACE + absoluteFilePath;
        String result = ExecUtil.execShellCmdReturnResult(cmd);
        Logger.log("Result of : " + cmd);

        VideoInfoUsingFFProbe obj = VideoInfoUsingFFProbe.parseJson(result);
        Logger.log(obj + "");
        Logger.log("===========================================");
        return obj;
    }

    private static int getVideoQuality(VideoInfoUsingFFProbe info) {
        final int f1080 = 1080;
        final int f720 = 720;
        final int f480 = 480;
        final int f360 = 360;
        final int f240 = 240;

        int videoQuality = 720;
        if (info == null) {
            return videoQuality;
        }
        inet.encode.bean.info.Stream videoStream = info.getVideoStreamInFile(true);
        if (videoStream != null) {
            int videoHeight = videoStream.getHeight();
            int bitRate = 0;
            try {
                bitRate = Integer.parseInt(videoStream.getBitRate()) / 1000;
                Logger.log("BIT_RATE: " + bitRate);
            } catch (Exception ex) {

            }

            if (videoHeight >= f1080) {
                videoQuality = f1080;
            } else if (videoHeight >= f720) {
                videoQuality = f720;
            } else if (videoHeight >= f480) {
                videoQuality = f480;
            } else if (videoHeight >= f360) {
                videoQuality = f360;
            } else if (videoHeight >= f240) {
                videoQuality = f240;
            }
        }
        return videoQuality;
    }

    private static int getDuration(VideoInfoUsingFFProbe info) {
        int duration = 0;
        String durationStr = 0 + "";
        if (info != null) {
            inet.encode.bean.info.Stream videoStream = info.getVideoStreamInFile(true);
            if (videoStream != null) {
                durationStr = videoStream.getDuration();
            }
        }
        try {
            duration = (int) Math.round(Double.parseDouble(durationStr));
        } catch (Exception ex) {

        }
        return duration;
    }

    private static void finishEncoding(int id) {
        if (id > 0) {
            MAP_ENCODING_STATUS.remove(id);
        }
    }

    private static void createKeyFile(String videoPathNotIncludeApplication, String key, String url) {
        if (StringUtil.isEmpty(key)) {
            key = "";
        }
        if (StringUtil.isEmpty(url)) {
            url = "";
        }
        final String SPACE = " ";
        String cmd = SHELL_COMMAND_CREATE_KEY_FILE + SPACE + videoPathNotIncludeApplication + SPACE + key + SPACE + url;
        boolean result = ExecUtil.execShellCmd(cmd);
        Logger.log("COMMAND: " + cmd + " EXEC_RESPONSE: " + result);

    }

    public static void main(String[] args) {
        //INPUT_FOLDER_TO_SCAN = "D:\\tmps\\scan\\input\\";
        //OUTPUT_FOLDER_ROOT_WITH_SLASH = "D:\\tmps\\scan\\output\\";
        INPUT_FOLDER_TO_SCAN = "/var/www/html/vscms2/upload/";
        OUTPUT_FOLDER_ROOT_WITH_SLASH = "/var/www/html/vscms2/upload/output/";
        processArgs(args);
        Monitor.startMonitorServer(MONITOR_SERVER_PORT_STR);
        //test();
        final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        future = executorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                try {
                    myTask();
                } catch (Exception ex) {
                    stop();
                    Logger.log(ex);
                    System.exit(0);
                }
            }
        }, 0, TIME_CYCLE_SCAN_IN_SEC, TimeUnit.SECONDS);
    }

    public static String getOutputFolderAbsoluteFromIdAndChannel(String idStr, String channel) {

        //String outputFolderAbsolute = OUTPUT_FOLDER_ROOT_WITH_SLASH + channel + File.separatorChar + n + File.separatorChar + idStr + File.separatorChar;
        String outputFolderAbsolute = OUTPUT_FOLDER_ROOT_WITH_SLASH + getOutputFolderRelativeFromIdAndChannel(idStr, channel);
        return outputFolderAbsolute;
    }

    public static String getOutputFolderRelativeFromIdAndChannel(String idStr, String channel) {
        int n = 0;
        try {
            int num = Integer.parseInt(idStr);
            n = 1 + num / 100;
        } catch (Exception ex) {
            idStr += "_" + channel;
            channel = "";
        }
        String outputFolderRelative = channel + File.separatorChar + n + File.separatorChar + idStr + File.separatorChar;
        if (outputFolderRelative.startsWith("/") || outputFolderRelative.startsWith(File.separator)) {
            outputFolderRelative = outputFolderRelative.substring(1);
        }
        return outputFolderRelative;
    }

    static void myTask() {
        //num++;
        createProcessingFolder();
        getListToEncode();
        //Logger.log("Running..." + new java.util.Date().toString());
    }

    static String[] parseFileName(String fileNameWithExtensions) {
        String fileName = "";
        try {
            fileName = VideoUtil.getFileNameWithoutExt(fileNameWithExtensions);
        } catch (Exception ex) {

        }
        String[] arr = new String[]{"", ""};
        try {
            String[] r = fileName.split("_");
            arr[0] = r[0];
            arr[1] = r[1];
        } catch (Exception ex) {
            //Logger.log(ex);
        }
        return arr;
    }

    static void getListToEncode() {
        final File folder = new File(INPUT_FOLDER_TO_SCAN);
        for (final File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
                //listFilesForFolder(fileEntry);
                Logger.log("Folder..." + CURRENT_FILES_TRANSCODING);
            } else {
                // is file
                String fileNameWithExt = fileEntry.getName();
                if (!VideoUtil.isVideoFile(fileNameWithExt)) {
                    Logger.log("Invalid video: " + fileNameWithExt);
                    continue;
                }
                if (CURRENT_FILES_TRANSCODING >= MAX_FILES_TRANSCODING) {
                    Logger.log("CURRENT_FILES_TRANSCODING: " + CURRENT_FILES_TRANSCODING);
                    return;
                }
                Logger.log("Process file: " + fileNameWithExt);
                String info[] = parseFileName(fileNameWithExt);
                if (!StringUtil.isEmpty(info[0]) && !StringUtil.isEmpty(info[1])) {
                    // Valid
                    String idStr = info[0];
                    String usernameStr = info[1];
                    //callApi(idStr, "start");
                    boolean isMoved = moveToProcessingFolder(fileNameWithExt);
                    if (!isMoved) {
                        return;
                    }
                    //fileNameWithExt = FOLDER_PROCESSING + File.separatorChar + fileNameWithExt;
                    String relativeFileNameWithExtMoved = FOLDER_PROCESSING + File.separatorChar + fileNameWithExt;
                    String outputFolderAbsolute = createOutputFolderFromId(idStr, usernameStr);

                    boolean getVideoInfo = addInfoVideoEncoding(idStr, relativeFileNameWithExtMoved);
                    if (!getVideoInfo) {
                        handleWhenEncodeError(idStr, usernameStr);
                        return;
                    }
                    CURRENT_FILES_TRANSCODING++;

                    String inputVideoAbsolutePath = getAbsoluteFilePathMoved(relativeFileNameWithExtMoved);
                    VideoInfoUsingFFProbe videoInfoUsingFFProbe = getVideoInfoUsingFFProbe(inputVideoAbsolutePath);
                    if (videoInfoUsingFFProbe == null) {
                        Logger.log("***Cannot get info of: " + inputVideoAbsolutePath);
                        //continue;
                        Logger.log("Default quality is now 720p");
                    }
                    int videoQuality = getVideoQuality(videoInfoUsingFFProbe);
                    int duration = getDuration(videoInfoUsingFFProbe);
                    VideoInfoJson obj = startEncode(idStr, relativeFileNameWithExtMoved, outputFolderAbsolute, videoQuality);
                    //callApi(idStr, "START"); // call api with output object

                    // Set duration
                    obj.setDuration(duration);
                    Logger.log(duration + "===============" + obj.toJson());
                    callApi(idStr, obj);
                    //callApi(null, null);
                } else {
                    Logger.log("Video file name is invalid..." + fileNameWithExt);
                }
            }
        }
    }

    static String createOutputFolderFromId(String idStr, String channel) {
        String outputFolderAbsolute;

        //String outputFolder = OUTPUT_FOLDER_ROOT_WITH_SLASH + channel + File.separatorChar + n + File.separatorChar + idStr + File.separatorChar;
        String outputFolder = getOutputFolderAbsoluteFromIdAndChannel(idStr, channel);
        outputFolderAbsolute = outputFolder;
        Path path = Paths.get(outputFolder);
        if (!Files.exists(path)) {
            try {
                Files.createDirectories(path);
            } catch (IOException ex) {
                outputFolderAbsolute = "";
                Logger.log(ex);
            }
        } else {
            Logger.log("Existed: " + outputFolder);
        }

        return outputFolderAbsolute;
    }

    static boolean execGetVideoInfo(String inputFileAbsolutePath, String infoLogFile) {
        boolean result;
        String cmd = "ffmpeg -i " + inputFileAbsolutePath + " -f null /dev/null > " + infoLogFile + " 2>&1";
        result = ExecUtil.execShellCmd(cmd);
        Logger.log(inputFileAbsolutePath + "===GET_VIDEO_INFO__" + result);
        return result;
    }

    static VideoInfoJson startEncode(String idStr, String videoPath, String outputFolderAbsolute, int videoQuality) {
        final String SPACE = " ";
        String logFile = idStr + ".log";
        String outputFolder = outputFolderAbsolute;
        String cmd = SHELL_COMMAND_TRANSCODE_VIDEO + SPACE + videoPath + SPACE + outputFolder + SPACE + videoQuality + " &>> " + logFile;
        Logger.log("COMMAND: " + cmd);

        //final Object obj = videoQueue.getVideoInfo();
        final VideoInfoJson obj = createVideoInfo(idStr, outputFolderAbsolute);
        processOutputVideoPath(obj, videoQuality);
        //Logger.log("===============" + obj.toJson());

        ExecUtil.execShellCmdAsync(cmd, new ExecUtil.Listener() {
            // Do not use
            void processOutputVideoPath(VideoInfoJson obj) {
                obj.setStatus("DONE");
                Map<String, String> mapQuality = new java.util.HashMap();
                final String ext = ".mp4";

//                VideoUtil.addQuality(mapQuality, "720p", obj.getOutFolder() + "720_" + fileName);
//                VideoUtil.addQuality(mapQuality, "480p", obj.getOutFolder() + "480_" + fileName);
//                VideoUtil.addQuality(mapQuality, "360p", obj.getOutFolder() + "360_" + fileName);
//                VideoUtil.addQuality(mapQuality, "240p", obj.getOutFolder() + "240_" + fileName);
                VideoUtil.addQuality(mapQuality, "720p", obj.getOutFolder() + "720" + ext);
                VideoUtil.addQuality(mapQuality, "480p", obj.getOutFolder() + "480" + ext);
                VideoUtil.addQuality(mapQuality, "360p", obj.getOutFolder() + "360" + ext);
                VideoUtil.addQuality(mapQuality, "240p", obj.getOutFolder() + "240" + ext);

                String lstVideoStr = VideoUtil.getListVideoJson(mapQuality);
                obj.setVideos(lstVideoStr);
            }

            @Override
            public void onResponse(String response) {
                Logger.log(response);
                Logger.log("Start call callback from here...");
                if (obj instanceof VideoInfoJson) {
                    CURRENT_FILES_TRANSCODING--;
                    createKeyFile(obj.getOutFolder(), null, null);
                    finishEncoding(((VideoInfoJson) obj).getId());
                    callApi(((VideoInfoJson) obj).getId() + "", "FINISH");
                }
            }
        }, obj);
        //*/
        return obj;
    }

    static void callApi(String idStr, Object info) {
        if (info instanceof VideoInfoJson) {
            //Logger.log("-------Process here");
            sendInfo((VideoInfoJson) info);
        } else if (info instanceof String) {
            Logger.log("----Str---" + info + "");
            VideoInfoJson obj = new VideoInfoJson();
            int id = 0;
            try {
                id = Integer.parseInt(idStr);
            } catch (Exception ex) {

            }
            if (id == 0) {
                Logger.log("**************Invalid id************");
            }
            obj.setId(id);
            obj.setStatus(info + "");
            sendInfo(obj);
        } else {
            Logger.log(".....Do not implemented.....");
        }
    }

    static void sendInfo(VideoInfoJson obj) {
        String url = API_CMS_CALLBACK + "&id=" + obj.getId();

        IRequest request = new HttpRequest();
        request.setMethod(IRequest.Method.POST);
        request.setUrl(url);
        request.addParam(PARAM_VIDEO_INFO, ((VideoInfoJson) obj).toJson());
        String result = request.send();
        Logger.log("URL callback: " + url);
        Logger.log("Callback param: " + ((VideoInfoJson) obj).toJson());
        Logger.log("Callback result: " + result);
    }

    //====================================================//
    static void test() {
        String str = "<ID>_<username>.mp4";

        //System.exit(0);
    }
}
