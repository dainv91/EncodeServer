/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inet.encode;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import inet.encode.bean.MessageResponse;
import inet.key.bean.VideoEncodeInfo;
import inet.util.FileUtil;
import inet.util.JsonUtil;
import inet.util.Logger;
import inet.util.StringUtil;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author dainv
 */
public class Monitor {

    static int MONITOR_SERVER_PORT = 6790;
    static HttpServer server = null;

    static final String METHOD_GET = "GET";
    static final String METHOD_POST = "POST";

    static final String PARAM_ID = "id";
    static final String PARAM_CHANNEL = "channel";
    static final String PARAM_AUTH = "auth";

    public static final String SALT = "fu*k thang tam";
    public static final String FOLDER_DELETED = "deleted";
    public static String DELETED_FOLDER_ROOT_WITH_SLASH = Encoder.OUTPUT_FOLDER_ROOT_WITH_SLASH + FOLDER_DELETED + "/";

    private static void processArgs(String... args) {
        if (args.length == 0) {
            //String options = " <SCRIPT_CREATE_KEY_FILE> <SCRIPT_TRANSCODE_VIDEO> <SCRIPT_GET_INFO_VIDEO> <PATH_ROOT_CMS>";
            //String options = " <OUTPUT_FOLDER_ROOT_WITH_SLASH> <SHELL_COMMAND_TRANSCODE_VIDEO>";
            String options = " <MONITOR_SERVER_PORT>";
            Logger.log("Using java " + Monitor.class.getName() + options);
            // Add from web
        } else {
            Logger.log("Nums of params: " + args.length);
            for (String str : args) {
                Logger.log("\tParam: " + str);
            }
            if (args.length > 0) {
                try {
                    MONITOR_SERVER_PORT = Integer.parseInt(args[0]);
                } catch (Exception ex) {
                    Logger.log(ex);
                }
            }
        }
    }

    private static void createContext() {
        server.createContext("/monitor", new MonitorHandler());
        server.createContext("/delete", new DeleteVideoHandler());
        
        server.createContext("/handler", new KeyHandler());
        server.createContext("/change_key", new ChangeKeyHandler());
    }

    public static void main(String[] args) {
        startMonitorServer(args);
    }

    public static void startMonitorServer(String... args) {
        processArgs(args);
        try {
            server = HttpServer.create(new InetSocketAddress(MONITOR_SERVER_PORT), 0);
        } catch (IOException ex) {
            Logger.log(ex);
        }
        createContext();

        //server.setExecutor(null);
        server.setExecutor(java.util.concurrent.Executors.newCachedThreadPool());
        server.start();
        Logger.log("Server monitor started...");
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

    // Do not use
    public static boolean deleteVideo(String idStr, String channel) {
        boolean result = true;
        String outputFolderAbsolute = Encoder.getOutputFolderAbsoluteFromIdAndChannel(idStr, channel);
        try {
            FileUtils.deleteDirectory(new File(outputFolderAbsolute));
        } catch (IOException ex) {
            result = false;
            Logger.log(ex);
        }
        return result;
    }

    public static boolean moveToDeletedFolder(String idStr, String channel) {
        boolean result = true;
        String outputFolderRelative = Encoder.getOutputFolderRelativeFromIdAndChannel(idStr, channel);
        String outputFolderAbsolute = Encoder.getOutputFolderAbsoluteFromIdAndChannel(idStr, channel);

        //String processingFile = INPUT_FOLDER_TO_SCAN + File.separatorChar + FOLDER_PROCESSING + File.separatorChar + inputFile;
        String deletedFile = DELETED_FOLDER_ROOT_WITH_SLASH + outputFolderRelative;
        Logger.log(outputFolderAbsolute + " will be deleted: " + deletedFile);
        try {
            File f = new File(outputFolderAbsolute);
            File df = new File(deletedFile);
            FileUtils.moveDirectory(f, df);
        } catch (Exception ex) {
            Logger.log(ex);
            result = false;
        }

        return result;
    }

    public static void addResponseHeader(HttpExchange http) {
        Headers responseHeaders = http.getResponseHeaders();
        responseHeaders.set("Server", "Microsoft-IIS/7.5");
        responseHeaders.set("X-Powered-By", "PHP/7.0.1");
        responseHeaders.add("X-Powered-By", "ASP.NET");
        responseHeaders.add("Access-Control-Allow-Origin", "*");
    }

    public static void addResponseMessage(HttpExchange http, int httpCode, String msg) {
        String resMsg = "{\"error\": \"Data empty...\"}";
        if (!StringUtil.isEmpty(msg)) {
            resMsg = msg;
        }
        try {
            addResponseHeader(http);
            http.sendResponseHeaders(httpCode, resMsg.getBytes().length);
            try (OutputStream outs = http.getResponseBody()) {
                outs.write(resMsg.getBytes());
                outs.flush();
            }
        } catch (IOException ex) {
            Logger.log(ex);
        }
    }

    public static String generateAuthenticated(String idStr) {
        String plain = idStr + SALT + idStr;
        String encrypted = StringUtil.md5(plain);
        return encrypted;
    }

    static class MonitorHandler implements HttpHandler {

        /**
         * Get video info from log file and add to map
         *
         */
        private void getVideoInfoFromLogFiles() {
            Collection<Integer> videoIds = Encoder.MAP_ENCODING_STATUS.keySet();
            for (int videoId : videoIds) {
                VideoEncodeInfo oldInfo = Encoder.MAP_ENCODING_STATUS.get(videoId);
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
                try {
                    currentPercent = Double.valueOf(df.format(currentPercent));
                } catch (NumberFormatException ex) {
                    Logger.log(ex);
                }
                newInfo.setCurrentPercent(currentPercent);
                Encoder.MAP_ENCODING_STATUS.put(videoId, newInfo);
            }
        }

        private void getTotalFrames(VideoEncodeInfo info) {
            String frameInfo = FileUtil.readTextFile(info.getInfoLogFile());
            Logger.log("VIDEO INFO: \n----------------------------");
            //Logger.log(frameInfo);
            String frameLine = getLineHasFrameInfo(frameInfo);
            Logger.log(frameLine);
            Logger.log("------------------------------------------");
            int totalFrame = getFrameInLine(frameLine);
            if (totalFrame > info.getTotalFrames()) {
                info.setTotalFrames(totalFrame);
            }

        }

        @Override
        public void handle(HttpExchange he) throws IOException {
            String method = he.getRequestMethod();
            if (method.equalsIgnoreCase(METHOD_GET)) {
                getVideoInfoFromLogFiles();
                //Collection<Integer> videoIds = Encoder.MAP_ENCODING_STATUS.keySet();
//                for (int videoId : videoIds) {
//                    VideoEncodeInfo info = Encoder.MAP_ENCODING_STATUS.get(videoId);
//                }

            } else if (method.equalsIgnoreCase(METHOD_POST)) {

            }
            // fix result
            //result = true;
            String response = JsonUtil.toJson(Encoder.MAP_ENCODING_STATUS);
            addResponseHeader(he);
            addResponseMessage(he, 200, response);
            he.close();
        }

    }

    static class DeleteVideoHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange he) throws IOException {
            String method = he.getRequestMethod();
            MessageResponse objResponse = new MessageResponse();

            if (method.equalsIgnoreCase(METHOD_GET)) {
                String query = he.getRequestURI().getQuery();
                java.util.Map<String, String> queries = StringUtil.queryToMap(query);
                String idStr = queries.get(PARAM_ID);
                String channel = queries.get(PARAM_CHANNEL);
                String auth = queries.get(PARAM_AUTH);
                String myAuth = generateAuthenticated(idStr);
                if (StringUtil.isEmpty(auth) || !myAuth.equalsIgnoreCase(auth)) {
                    objResponse.setStatus(1);
                    objResponse.setMsg("Invalid authenticate code...");
                } else {
                    objResponse.setStatus(0);
                    objResponse.setMsg("Video deleted " + idStr);
                    boolean isDeleted = moveToDeletedFolder(idStr, channel);
                    if (!isDeleted) {
                        objResponse.setStatus(2);
                        objResponse.setMsg("Cannot delete video " + idStr);
                    }
                }

            } else if (method.equalsIgnoreCase(METHOD_POST)) {
                objResponse.setStatus(3);
                objResponse.setMsg("Method not allowed.");
            }
            String response = objResponse.toJson();
            addResponseHeader(he);
            if (objResponse.getStatus() == 0) {
                addResponseMessage(he, 200, response);
            } else {
                addResponseMessage(he, 403, response);
            }
            /*
            he.sendResponseHeaders(200, response.getBytes().length);
            try (OutputStream outs = he.getResponseBody()) {
                outs.write(response.getBytes());
                outs.flush();
            }
             */
            he.close();
        }

    }

    static class KeyHandler extends MyHandler {

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
            Logger.log("Key response:_" + keyStr + "----" + new String(keyBuffer));
            addResponseHeader(he);
            he.sendResponseHeaders(200, keyBuffer.length);
            try (OutputStream outs = he.getResponseBody()) {
                outs.write(keyBuffer);
                outs.flush();
            }
        }

    }

    static class ChangeKeyHandler extends MyHandler {

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
                //InputStream is = he.getRequestBody();
                //Map<String, String> queries = HttpRequestUtil.readParamFromInputStream(is);
            }
            String response = "OK";
            addResponseHeader(he);
            he.sendResponseHeaders(200, response.getBytes().length);
            try (OutputStream outs = he.getResponseBody()) {
                outs.write(response.getBytes());
                outs.flush();
            }
        }

    }

    static abstract class MyHandler implements HttpHandler {

        static boolean IS_VALID = true;
        static String KEY = "CBD2133A519B098ED9EAD88BCA45AEFA";
        static final String DEFAULT_APP = "iphim.vn";

        static final String PARAM_KEY = "key";
        static final String PARAM_APP = "app";

        // app - key
        static final Map<String, String> LIST_KEY_BY_APP = new ConcurrentHashMap();
    }
}
