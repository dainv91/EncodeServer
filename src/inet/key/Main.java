/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inet.key;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import inet.key.bean.EncodeResponse;
import inet.key.bean.VideoEncodeInfo;
import inet.key.bean.VideoInfoJson;
import inet.key.bean.VideoQueue;
import inet.util.ExecUtil;
import inet.util.FileUtil;
import inet.util.HttpRequestUtil;
import inet.util.JsonUtil;
import inet.util.Logger;
import inet.util.StringUtil;
import inet.util.VideoUtil;
import inet.util.request.HttpRequest;
import inet.util.request.IRequest;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.Collection;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author dainv
 */
public class Main {

    static final int PORT = 6789;

    static HttpServer server = null;

    static boolean IS_VALID = true;
    static String KEY = "CBD2133A519B098ED9EAD88BCA45AEFA";
    static final String DEFAULT_APP = "iphim.vn";

    static final String PARAM_KEY = "key";
    static final String PARAM_APP = "app";

    static final String PARAM_URL = "url";
    static final String PARAM_VIDEO_FILE = "video_code";

    static final String PARAM_VIDEO_FILE_TO_TRANSCODE = "video_file";
    static final String PARAM_VIDEO_INFO = "encode";

    static final String METHOD_GET = "GET";
    static final String METHOD_POST = "POST";

    static String SHELL_COMMAND_CREATE_KEY_FILE = "~/script/create_key_file.sh";
    static String SHELL_COMMAND_TRANSCODE_VIDEO = "~/script/ffmpeg_transcode.sh";
    static String SHELL_COMMAND_GET_INFO_VIDEO = "~/script/ffmpeg_get_info.sh";

    static String PATH_ROOT_CMS = "/var/www/html/vscms2/backend/web/uploads";

    // app - key
    static final Map<String, String> LIST_KEY_BY_APP = new ConcurrentHashMap();

    static int NUMBER_OF_CONCURRENT_FILES = 4;
    static final Queue<VideoQueue> VIDEO_QUEUE;

    // Video id - info
    static final Map<Integer, VideoEncodeInfo> MAP_ENCODING_STATUS;

    //static final Map<
    static {
        //LIST_KEY_BY_APP.put(DEFAULT_APP, KEY);
        VIDEO_QUEUE = new java.util.LinkedList();
        MAP_ENCODING_STATUS = new java.util.concurrent.ConcurrentHashMap();
    }

    static void addEncodeRequestToQueue(VideoQueue video) {
        VIDEO_QUEUE.add(video);
    }

    public static void main(String[] args) {
        try {
            if (args.length == 0) {
                String options = " <SCRIPT_CREATE_KEY_FILE> <SCRIPT_TRANSCODE_VIDEO> <SCRIPT_GET_INFO_VIDEO> <PATH_ROOT_CMS>";
                Logger.log("Using java " + Main.class.getName() + options);
            } else {
                Logger.log("Nums of params: " + args.length);
                for (String str : args) {
                    Logger.log("\tParam: " + str);
                }
            }
            if (args.length > 0) {
                SHELL_COMMAND_CREATE_KEY_FILE = args[0];
            }
            if (args.length > 1) {
                SHELL_COMMAND_TRANSCODE_VIDEO = args[1];
            }
            if (args.length > 2) {
                SHELL_COMMAND_GET_INFO_VIDEO = args[2];
            }
            if (args.length > 3) {
                PATH_ROOT_CMS = args[3];
            }
            server = HttpServer.create(new InetSocketAddress(PORT), 0);
            createContext();

            server.setExecutor(null);
            server.start();
            Logger.log("Server key started...");
        } catch (IOException ex) {
            Logger.log(ex);
        }
    }

    private static void createContext() {
        server.createContext("/handler", new Handler());
        server.createContext("/stop", new HttpHandler() {
            @Override
            public void handle(HttpExchange he) throws IOException {
                try {
                    if (server != null) {
                        addResponseHeader(he);
                        String response = "Server stopping...";
                        he.sendResponseHeaders(200, response.getBytes().length);
                        try (OutputStream os = he.getResponseBody()) {
                            os.write(response.getBytes());
                        }
                        server.stop(0);
                        Logger.log("Server key stopped...");

                    }
                } catch (Exception ex) {
                    Logger.log(ex);
                }
            }
        });
        server.createContext("/toggle", new HttpHandler() {
            @Override
            public void handle(HttpExchange he) throws IOException {
                IS_VALID = !IS_VALID;
                Logger.log("IS_VALID: " + IS_VALID);
                String response = "OK";
                addResponseHeader(he);
                he.sendResponseHeaders(200, response.getBytes().length);
                try (OutputStream outs = he.getResponseBody()) {
                    outs.write(response.getBytes());
                    outs.flush();
                }
            }
        });
        server.createContext("/change_key", new HttpHandler() {
            @Override
            public void handle(HttpExchange he) throws IOException {
                String method = he.getRequestMethod();
                Logger.log("METHOD: " + method);
                if (method.equalsIgnoreCase(METHOD_GET)) {
                    String query = he.getRequestURI().getQuery();
                    java.util.Map<String, String> queries = StringUtil.queryToMap(query);
                    String key = queries.get(PARAM_KEY);
                    if (!StringUtil.isEmpty(key)) {
                        KEY = key;

                        String app = queries.get(PARAM_APP);
                        if (StringUtil.isEmpty(app)) {
                            app = DEFAULT_APP;
                        }
                        LIST_KEY_BY_APP.put(app, key);
                    }
                } else if (method.equalsIgnoreCase(METHOD_POST)) {
                    InputStream is = he.getRequestBody();
                    Map<String, String> queries = HttpRequestUtil.readParamFromInputStream(is);
                }
                String response = "OK";
                addResponseHeader(he);
                he.sendResponseHeaders(200, response.getBytes().length);
                try (OutputStream outs = he.getResponseBody()) {
                    outs.write(response.getBytes());
                    outs.flush();
                }
            }
        });

        server.createContext("/upload", new UploadVideoHandler());

        server.createContext("/encode", new EncodeVideoHandler());

        server.createContext("/monitor", new MonitorHandler());
    }

    static class Handler implements HttpHandler {

        @Override
        public void handle(HttpExchange he) throws IOException {
            boolean isValid = IS_VALID;
            if (!isValid) {
                addResponseHeader(he);
                he.sendResponseHeaders(403, 0);
                return;
            }
            Headers headers = he.getResponseHeaders();
            headers.add("Content-Type", "binary/octet-stream");
            headers.add("Pragma", "no-cache");
            //String keyStr = "DE51A7254739C0EDF1DCE13BBB308FF0";

            String query = he.getRequestURI().getQuery();
            java.util.Map<String, String> queries = StringUtil.queryToMap(query);

            String app = queries.get(PARAM_APP);
            if (StringUtil.isEmpty(app)) {
                app = DEFAULT_APP;
            }
            String key = LIST_KEY_BY_APP.get(app);
            if (key == null) {
                key = KEY;
            }

            String keyStr = key;
            int len = keyStr.length() / 2;
            byte[] keyBuffer = new byte[len];

            for (int i = 0; i < len; i++) {
                try {
                    keyBuffer[i] = (byte) Integer.parseInt(keyStr.substring(i * 2, (i * 2) + 2), 16);
                } catch (Exception ex) {
                    Logger.log(ex);
                }
            }
//            int pos = 0;
//            for (byte b : keyBuffer) {
//                System.out.println("b[" + pos++ + "]=" + b);
//            }
            Logger.log("Key response:_" + keyStr + "----" + new String(keyBuffer));
            addResponseHeader(he);
            he.sendResponseHeaders(200, keyBuffer.length);
            try (OutputStream outs = he.getResponseBody()) {
                outs.write(keyBuffer);
                outs.flush();
            }
        }

    }

    static class UploadVideoHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange he) throws IOException {
            boolean result = false;
            String method = he.getRequestMethod();
            if (method.equalsIgnoreCase(METHOD_GET)) {
                String query = he.getRequestURI().getQuery();
                java.util.Map<String, String> queries = StringUtil.queryToMap(query);

                String videoPathNotIncludeApplication = queries.get(PARAM_VIDEO_FILE);
                if (StringUtil.isEmpty(videoPathNotIncludeApplication)) {
                    Logger.log("Video file is empty...");

                    String response = "Video file is empty...";
                    addResponseHeader(he);
                    he.sendResponseHeaders(404, response.getBytes().length);
                    try (OutputStream outs = he.getResponseBody()) {
                        outs.write(response.getBytes());
                        outs.flush();
                    }
                    return;
                }

                String app = queries.get(PARAM_APP);
                if (StringUtil.isEmpty(app)) {
                    app = DEFAULT_APP;
                }
                String key = LIST_KEY_BY_APP.get(app);
                if (StringUtil.isEmpty(key)) {
                    key = "";
                }

                String url = queries.get(PARAM_URL);
                if (StringUtil.isEmpty(url)) {
                    url = "";
                }
                final String SPACE = " ";
                String cmd = SHELL_COMMAND_CREATE_KEY_FILE + SPACE + videoPathNotIncludeApplication + SPACE + key + SPACE + url;
                Logger.log("COMMAND: " + cmd);
                result = ExecUtil.execShellCmd(cmd);
            } else if (method.equalsIgnoreCase(METHOD_POST)) {
                //InputStream is = he.getRequestBody();
                //Map<String, String> queries = HttpRequestUtil.readParamFromInputStream(is);
            }
            String response = "OK";
            if (!result) {
                response = "FAILED";
            }
            addResponseHeader(he);
            he.sendResponseHeaders(200, response.getBytes().length);
            try (OutputStream outs = he.getResponseBody()) {
                outs.write(response.getBytes());
                outs.flush();
            }
        }

    }

    static class EncodeVideoHandler implements HttpHandler {

        private VideoQueue extractInfo(HttpExchange he) throws IOException {
            VideoQueue videoQueue = null;
            //boolean result = false;
            String method = he.getRequestMethod();

            VideoInfoJson videoInfo = null;
            if (method.equalsIgnoreCase(METHOD_GET)) {
                //String query = he.getRequestURI().getQuery();
                //java.util.Map<String, String> queries = StringUtil.queryToMap(query);

                //String videoInfoStr = queries.get(PARAM_VIDEO_INFO);
                //videoInfoStr = java.net.URLDecoder.decode(videoInfoStr, "UTF-8");
                String response = "Dữ liệu dài lắm, dùng post đi cưng. Moa moa chụt chụt =)))";
                Logger.log(response);
                addResponseMessage(he, 403, response);
                return null;

            } else if (method.equalsIgnoreCase(METHOD_POST)) {
                InputStream is = he.getRequestBody();
                Map<String, String> queries = HttpRequestUtil.readParamFromInputStream(is);

                String videoInfoStr = queries.get(PARAM_VIDEO_INFO);
                videoInfoStr = java.net.URLDecoder.decode(videoInfoStr, "UTF-8");
                if (StringUtil.isEmpty(videoInfoStr)) {
                    String response = "json video info is empty...";
                    Logger.log(response);
                    addResponseMessage(he, 404, response);
                    return null;
                }

                videoInfo = VideoInfoJson.parseJsonFromChild(videoInfoStr);
                if (videoInfo == null) {
                    String response = "Invalid json video info...";
                    Logger.log(response + ": " + videoInfoStr);
                    addResponseMessage(he, 404, response);
                    return null;
                }

                //String videoPath = queries.get(PARAM_VIDEO_FILE_TO_TRANSCODE);
                String videoPath = videoInfo.getInFile();
                String outputFolder = videoInfo.getOutFolder();
                if (StringUtil.isEmpty(videoPath)) {
                    String response = "Video file is empty...";
                    Logger.log(response);
                    addResponseMessage(he, 404, response);
                    return null;
                }

                String app = queries.get(PARAM_APP);
                if (StringUtil.isEmpty(app)) {
                    app = DEFAULT_APP;
                }
                String key = LIST_KEY_BY_APP.get(app);
                if (StringUtil.isEmpty(key)) {
                    key = "";
                }

                String url = queries.get(PARAM_URL);
                if (StringUtil.isEmpty(url)) {
                    url = "";
                }
                //videoQueue = new VideoQueue(videoInfo.getId(), videoPath, outputFolder, null);
                videoQueue = new VideoQueue(videoInfo.getId(), videoPath, outputFolder, videoInfo.getId() + ".log", videoInfo);
//                final String SPACE = " ";
//                String cmd = SHELL_COMMAND_TRANSCODE_VIDEO + SPACE + videoPath + SPACE + outputFolder + SPACE + " &>> shell_cmd.log";
//                Logger.log("COMMAND: " + cmd);
//
//                final Object obj = videoInfo;
//                ExecUtil.execShellCmdAsync(cmd, new ExecUtil.Listener() {
//                    void processVideo(VideoInfo obj) {
//                        obj.setStatus("DONE");
//                        Map<String, String> mapQuality = new java.util.HashMap();
//                        String fileName = VideoUtil.getFileNameWithoutExt(obj.getInFile()) + ".mp4";
//
//                        VideoUtil.addQuality(mapQuality, "720p", obj.getOutFolder() + "720_" + fileName);
//                        VideoUtil.addQuality(mapQuality, "480p", obj.getOutFolder() + "480_" + fileName);
//                        VideoUtil.addQuality(mapQuality, "360p", obj.getOutFolder() + "360_" + fileName);
//                        VideoUtil.addQuality(mapQuality, "240p", obj.getOutFolder() + "240_" + fileName);
//
//                        String lstVideoStr = VideoUtil.getListVideoJson(mapQuality);
//                        obj.setVideos(lstVideoStr);
//                    }
//
//                    @Override
//                    public void onResponse(String response) {
//                        Logger.log(response);
//                        Logger.log("Start call callback from here...");
//                        if (obj instanceof VideoInfo) {
//                            //((VideoInfo) obj).setStatus("DONE");
//                            processVideo((VideoInfo) obj);
//                            String callbackLink = ((VideoInfo) obj).getCallback();
//                            Logger.log("Callback link: " + callbackLink);
//                            Logger.log("Video info: " + ((VideoInfo) obj).toJson());
//                            if (!StringUtil.isEmpty(callbackLink)) {
//                                IRequest request = new HttpRequest();
//                                request.setMethod(IRequest.Method.POST);
//                                request.setUrl(callbackLink);
//                                request.addParam(PARAM_VIDEO_INFO, ((VideoInfo) obj).toJson());
//                                request.send();
//                            }
//                        }
//
//                    }
//                }, obj);
            }
            // fix result
//            result = true;
            String response = "OK";
//            if (!result) {
//                response = "FAILED";
//            } else {
//                // Call callback
//                String callbackLink = videoInfo.getCallback();
//                Logger.log("Must call callback from here..." + callbackLink);
            int id = videoInfo.getId();
            EncodeResponse er = new EncodeResponse();
            er.setId(id);
            er.setMessage("Encoding request created");
            er.setMoreInfo("Thằng Tam âm thầm đi vào ngõ cụt.");
            response = er.toJson();
//            }
            addResponseHeader(he);
            he.sendResponseHeaders(200, response.getBytes().length);
            try (OutputStream outs = he.getResponseBody()) {
                outs.write(response.getBytes());
                outs.flush();
            }
            return videoQueue;
        }

        private void processEncodeRequest(VideoQueue videoQueue) throws IOException {
            if (videoQueue.getId() == 0) {
                return;
            }
            String videoPath = videoQueue.getInputFile();
            String outputFolder = videoQueue.getOutputFolderWithSlash();
            String logFile = videoQueue.getLogFile();
            final String SPACE = " ";
            String cmd = SHELL_COMMAND_TRANSCODE_VIDEO + SPACE + videoPath + SPACE + outputFolder + SPACE + " &>> " + logFile;
            Logger.log("COMMAND: " + cmd);

            final Object obj = videoQueue.getVideoInfo();
            ExecUtil.execShellCmdAsync(cmd, new ExecUtil.Listener() {

                void processVideo(VideoInfoJson obj) {
                    obj.setStatus("DONE");
                    Map<String, String> mapQuality = new java.util.HashMap();
                    String fileName = VideoUtil.getFileNameWithoutExt(obj.getInFile()) + ".mp4";

                    VideoUtil.addQuality(mapQuality, "720p", obj.getOutFolder() + "720_" + fileName);
                    VideoUtil.addQuality(mapQuality, "480p", obj.getOutFolder() + "480_" + fileName);
                    VideoUtil.addQuality(mapQuality, "360p", obj.getOutFolder() + "360_" + fileName);
                    VideoUtil.addQuality(mapQuality, "240p", obj.getOutFolder() + "240_" + fileName);

                    String lstVideoStr = VideoUtil.getListVideoJson(mapQuality);
                    obj.setVideos(lstVideoStr);
                }

                @Override
                public void onResponse(String response) {
                    Logger.log(response);
                    Logger.log("Start call callback from here...");
                    if (obj instanceof VideoInfoJson) {
                        //((VideoInfo) obj).setStatus("DONE");
                        processVideo((VideoInfoJson) obj);
                        String callbackLink = ((VideoInfoJson) obj).getCallback();
                        Logger.log("Callback link: " + callbackLink);
                        Logger.log("Video info: " + ((VideoInfoJson) obj).toJson());
                        if (!StringUtil.isEmpty(callbackLink)) {
                            IRequest request = new HttpRequest();
                            request.setMethod(IRequest.Method.POST);
                            request.setUrl(callbackLink);
                            request.addParam(PARAM_VIDEO_INFO, ((VideoInfoJson) obj).toJson());
                            String result = request.send();
                            Logger.log("Callback result: " + result);
                        }

                        // Remove video from encoding status map.
                        int videoId = ((VideoInfoJson) obj).getId();
                        MAP_ENCODING_STATUS.remove(videoId);
                    }

                }
            }, obj);
        }

        public static String getLineHasFrameInfo(String info) {
            if (StringUtil.isEmpty(info)) {
                return null;
            }

            String frameLine = null;
            String[] lines = info.split("\n");
            for (int i = lines.length - 1; i >= 0; i--) {
                String line = lines[i];
                if (!line.contains("frame=")) {
                    continue;
                }
                frameLine = line;
                break;
            }
            return frameLine;
        }

        private VideoEncodeInfo getVideoInfo(VideoQueue videoQueue) {
            int videoId = videoQueue.getId();
            if (videoId == 0) {
                return null;
            }
            String inputFileAbsolutePath = "";
            if (videoQueue.getInputFile() != null) {
                if (!videoQueue.getInputFile().contains(PATH_ROOT_CMS)) {
//                    videoQueue.setInputFile(PATH_ROOT_CMS + videoQueue.getInputFile());
                    inputFileAbsolutePath = PATH_ROOT_CMS + videoQueue.getInputFile();
                }
            }
            VideoEncodeInfo video = new VideoEncodeInfo(videoId, 0, 0, 0, 0, videoQueue.getInputFile(), 0, videoQueue.getLogFile(), videoQueue.getLogFile() + ".info");
            // Must call script to get video info from here...
            String cmd = "ffmpeg -i " + inputFileAbsolutePath + " -f null /dev/null > " + video.getInfoLogFile() + " 2>&1";
            boolean result = ExecUtil.execShellCmd(cmd);
//            try {
//                //Thread.sleep(5000);
//            } catch (InterruptedException ex) {
//                Logger.log(ex);
//            }
            if (result) {
//                String info = FileUtil.readTextFile(video.getInfoLogFile());
//                Logger.log("VIDEO INFO: ");
//                Logger.log(info);
//                String frameLine = getLineHasFrameInfo(info);
//                int totalFrame = MonitorHandler.getFrameInLine(frameLine);
//                video.setTotalFrames(totalFrame);
            } else {
                Logger.log("Get video info failed...");
            }
            return video;
        }

        @Override
        public void handle(HttpExchange he) throws IOException {
            VideoQueue videoToEncode = extractInfo(he);
            if (videoToEncode == null) {
                return;
            }
            Logger.log("Video info: " + videoToEncode.toString());
            addEncodeRequestToQueue(videoToEncode);
            if (MAP_ENCODING_STATUS.size() >= NUMBER_OF_CONCURRENT_FILES) {
                //addEncodeRequestToQueue(videoToEncode);
                videoToEncode = VIDEO_QUEUE.poll();

                // Add to current processing
                VideoEncodeInfo videoInfo = getVideoInfo(videoToEncode);
                MAP_ENCODING_STATUS.put(videoToEncode.getId(), videoInfo);

                processEncodeRequest(videoToEncode);
            } else {
                videoToEncode = VIDEO_QUEUE.poll();

                // Add to current processing
                VideoEncodeInfo videoInfo = getVideoInfo(videoToEncode);
                MAP_ENCODING_STATUS.put(videoToEncode.getId(), videoInfo);

                processEncodeRequest(videoToEncode);
            }
        }

    }

    static class MonitorHandler implements HttpHandler {

        public static int getFrameInLine(String lastLine) {
            if (StringUtil.isEmpty(lastLine)) {
                return 0;
            }
            int frameInt = 0;
            String frame = "";
            String[] arr = lastLine.split("=");

            if (arr != null && arr.length > 0) {
                try {
                    String frames = arr[1].trim();
                    String[] arr2 = frames.split(" ");
                    frame = arr2[0];
                } catch (Exception ex) {
                    Logger.log(ex);
                }
            }
            try {
                frameInt = Integer.parseInt(frame);
            } catch (Exception ex) {

            }
            return frameInt;
        }

        /**
         * Get video info from log file and add to map
         *
         */
        private void getVideoInfoFromLogFiles() {
            Collection<Integer> videoIds = MAP_ENCODING_STATUS.keySet();
            for (int videoId : videoIds) {
                VideoEncodeInfo oldInfo = MAP_ENCODING_STATUS.get(videoId);
                VideoEncodeInfo newInfo = oldInfo.copyInstance();

                //newInfo.setCurrentPercent(50);
                getTotalFrames(newInfo);
                // Read from log file
                //String log = FileUtil.readTextFile(oldInfo.getLogFile());
                String log = FileUtil.readLastLine(oldInfo.getLogFile());
                int curretFrame = getFrameInLine(log);
                newInfo.setCurrentFrame(curretFrame);

                double currentPercent = (newInfo.getCurrentFrame() * 1.0 / newInfo.getTotalFrames()) * 100;
                java.text.DecimalFormat df = new java.text.DecimalFormat("#.##");
                currentPercent = Double.valueOf(df.format(currentPercent));
                newInfo.setCurrentPercent(currentPercent);
                MAP_ENCODING_STATUS.put(videoId, newInfo);
            }
        }

        private void getTotalFrames(VideoEncodeInfo info) {
            String frameInfo = FileUtil.readTextFile(info.getInfoLogFile());
            Logger.log("VIDEO INFO: \n----------------------------");
            //Logger.log(frameInfo);
            String frameLine = EncodeVideoHandler.getLineHasFrameInfo(frameInfo);
            Logger.log(frameLine);
            Logger.log("------------------------------------------");
            int totalFrame = MonitorHandler.getFrameInLine(frameLine);
            if (totalFrame > info.getTotalFrames()) {
                info.setTotalFrames(totalFrame);
            }

        }

        @Override
        public void handle(HttpExchange he) throws IOException {
            //boolean result = false;
            String method = he.getRequestMethod();
            if (method.equalsIgnoreCase(METHOD_GET)) {
                //String query = he.getRequestURI().getQuery();
                //java.util.Map<String, String> queries = StringUtil.queryToMap(query);

                //String videoInfoStr = queries.get(PARAM_VIDEO_INFO);
                //videoInfoStr = java.net.URLDecoder.decode(videoInfoStr, "UTF-8");
//                String response = "Dữ liệu dài lắm, dùng post đi cưng. Moa moa chụt chụt =)))";
//                Logger.log(response);
//                addResponseMessage(he, 403, response);
//                return;
                // Get video info from log file.
                getVideoInfoFromLogFiles();
                Collection<Integer> videoIds = MAP_ENCODING_STATUS.keySet();
                for (int videoId : videoIds) {
                    VideoEncodeInfo info = MAP_ENCODING_STATUS.get(videoId);
                }

            } else if (method.equalsIgnoreCase(METHOD_POST)) {

            }
            // fix result
            //result = true;
            String response = JsonUtil.toJson(MAP_ENCODING_STATUS);
            addResponseHeader(he);
            he.sendResponseHeaders(200, response.getBytes().length);
            try (OutputStream outs = he.getResponseBody()) {
                outs.write(response.getBytes());
                outs.flush();
            }
        }

    }

    public static void addResponseHeader(HttpExchange http) {
        Headers responseHeaders = http.getResponseHeaders();
        responseHeaders.set("Server", "Microsoft-IIS/7.5");
        responseHeaders.set("X-Powered-By", "PHP/7.0.1");
        responseHeaders.add("X-Powered-By", "ASP.NET");
        responseHeaders.add("Access-Control-Allow-Origin", "*");
    }

    public static void addResponseMessage(HttpExchange http, int httpCode, String msg) {
        try {
            addResponseHeader(http);
            http.sendResponseHeaders(httpCode, msg.getBytes().length);
            try (OutputStream outs = http.getResponseBody()) {
                outs.write(msg.getBytes());
                outs.flush();
            }
        } catch (IOException ex) {
            Logger.log(ex);
        }
    }
}
