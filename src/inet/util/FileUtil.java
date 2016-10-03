/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inet.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Map;
import java.util.Properties;

/**
 *
 * @author dainv
 */
public class FileUtil {

    public static boolean isValidFile(String filePath) {
        File f = new File(filePath);
        return !(!f.exists() || f.isDirectory());
    }

    public static String readTextFile(String filePath) {
        if (!isValidFile(filePath)) {
            Logger.log("Invalid file.." + filePath);
            return null;
        }
        String result = null;
        try {
            BufferedReader br = new BufferedReader(new FileReader(filePath));
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();
            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
            result = sb.toString();
        } catch (Exception ex) {
            Logger.log(ex);
        }
        return result;
    }

    public static String readLastLine(String filePath) {
        if (!isValidFile(filePath)) {
            Logger.log("Invalid file.." + filePath);
            return null;
        }
        String lastLine = "";
        try {
            BufferedReader br = new BufferedReader(new FileReader(filePath));
            String line;
            while ((line = br.readLine()) != null) {
                lastLine = line;
            }
        } catch (Exception ex) {
            Logger.log(ex);
        }
        return lastLine;
    }

    public static Map<String, String> readConfigFile(String fileName) {
        Map<String, String> result = new java.util.HashMap<String, String>();
        Properties prop = new Properties();
        InputStream input;
        try {
            input = FileUtil.class.getClassLoader().getResourceAsStream(fileName);
            prop.load(input);
            try {
                input.close();
            } catch (Exception e) {
                //e.printStackTrace();
            }
            Enumeration keys = prop.keys();
            while (keys.hasMoreElements()) {
                String key = (String) keys.nextElement();
                String value = prop.getProperty(key);
                if (!StringUtil.isEmpty(key)) {
                    result.put(key, value);
                }
            }
        } catch (Exception ex) {
            Logger.log(ex);
        }
        return result;
    }

    public static void main(String[] args) {
        arr();
        String pattern = "frame= 3716 fps= 38 q=-1.0 Lq=-1.0 q=-1.0 q=-1.0 size= 14659kB time=123.80 bitrate= 970.0kbits/s";
        String[] arr = pattern.split("=");
        String[] arr2 = arr[1].trim().split(" ");
        System.out.println(arr[1]);
        System.out.println(arr2[0]);
    }

    static void arr() {
        String[] arr = new String[]{"frame=1", "frame = 2", "iadd = 1", "iadd = 2"};
        for (int i = arr.length - 1; i >= 0; i--) {
            System.out.println(i + "===" + arr[i]);
        }
        System.exit(0);
    }
}
