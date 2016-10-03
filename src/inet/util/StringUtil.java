/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inet.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

/**
 *
 * @author dainv
 */
public class StringUtil {

    /**
     * Return map of query
     *
     * @param query
     * @return
     */
    public static Map<String, String> queryToMap(String query) {
        Map<String, String> map = new java.util.HashMap<String, String>();
        if (query != null && !query.trim().isEmpty()) {
            String[] entries = query.split("&");
            if (entries != null) {
                for (String entry : entries) {
                    String[] pair = entry.split("=");
                    String key = "", value = "";
                    try {
                        key = java.net.URLDecoder.decode(pair[0], "UTF-8");
                        if (pair.length > 1) {
                            value = java.net.URLDecoder.decode(pair[1], "UTF-8");
                        }
                    } catch (UnsupportedEncodingException e) {
                    }
                    map.put(key, value);
                }
            }
        }
        return map;
    }

    public static boolean isEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    public static String md5(String input) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException ex) {
            Logger.log(ex);
        }
        if (md == null) {
            return null;
        }
        byte[] dateBytes = input.getBytes();
        byte[] outputBytes = md.digest(dateBytes);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < outputBytes.length; i++) {
            sb.append(Integer.toHexString((outputBytes[i] & 0xFF) | 0x100).substring(1, 3));
        }
        return sb.toString();
    }
}
