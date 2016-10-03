/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inet.util;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

/**
 *
 * @author dainv
 */
public class VideoUtil {

    static final List<String> LIST_VIDEO_EXTENSION;

    static {
        LIST_VIDEO_EXTENSION = new java.util.ArrayList();
        LIST_VIDEO_EXTENSION.add("3gp");
        LIST_VIDEO_EXTENSION.add("avi");
        LIST_VIDEO_EXTENSION.add("flv");
        LIST_VIDEO_EXTENSION.add("mkv");
        LIST_VIDEO_EXTENSION.add("mov");
        LIST_VIDEO_EXTENSION.add("mp4");
        LIST_VIDEO_EXTENSION.add("ts");
        LIST_VIDEO_EXTENSION.add("webm");
        LIST_VIDEO_EXTENSION.add("wmv");

    }

    public static boolean isVideoFile(String fileName) {
        String ext = getExtension(fileName);
        return LIST_VIDEO_EXTENSION.contains(ext);
    }

    public static String getFileNameWithoutExt(String filePath) {
        if (StringUtil.isEmpty(filePath)) {
            return "";
        }
        Path path = Paths.get(filePath);
        String name = path.getFileName().toString();
        String ext = getExtension(name);
        String fileName = name.replace("." + ext, "");
        return fileName;
    }

    public static void addQuality(Map<String, String> list, String qual, String link) {
        if (list == null) {
            list = new java.util.HashMap();
        }
        list.put(qual, link);
    }

    public static String getListVideoJson(Map<String, String> links) {
        if (links == null) {
            return "";
        }
        return JsonUtil.toJson(links);
    }

    public static void main(String[] args) {
        //Path path = Paths.get("/usr/local/iadd.java");
        //System.out.println("path: " + getExtension(path.toString()));

        //test();
        fileName();
    }

    static void fileName() {
        Path path = Paths.get("/home/iadd/java.txt");
        String name = path.getFileName().toString();
        String ext = getExtension(name);
        System.out.println(name.replace("." + ext, ""));
    }

    static void test() {
        Map<String, String> map = new java.util.HashMap();
        addQuality(map, "720", "link 720");
        addQuality(map, "480", "link 480");
        addQuality(map, "360", "link 360");
        addQuality(map, "240", "link 240");
        System.out.println(getListVideoJson(map));
    }

    private static String getExtension(String fileName) {
        if (fileName == null) {
            return null;
        }
        int extensionPos = fileName.lastIndexOf('.');
        int lastUnixPos = fileName.lastIndexOf('/');
        int lastWindowsPos = fileName.lastIndexOf('\\');
        int lastSeparator = Math.max(lastUnixPos, lastWindowsPos);
        int index = lastSeparator > extensionPos ? -1 : extensionPos;
        if (index == -1) {
            return "";
        } else {
            return fileName.substring(index + 1).toLowerCase();
        }
    }
}
