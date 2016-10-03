/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inet.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

/**
 *
 * @author dainv
 */
public class HttpRequestUtil {

    /**
     * Read input stream to one line.
     *
     * @param is
     * @return
     */
    public static Map<String, String> readParamFromInputStream(InputStream is) {
        Map<String, String> result = new java.util.HashMap<>();
        StringBuilder sb = new StringBuilder();
        String line;
        java.io.BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                sb.append(line.trim());
            }
            line = sb.toString();
            java.util.Iterator<java.util.Map.Entry<String, String>> it = StringUtil.queryToMap(line).entrySet()
                    .iterator();
            while (it.hasNext()) {
                java.util.Map.Entry<String, String> entry = it.next();
                result.put(entry.getKey(), java.net.URLDecoder.decode(entry.getValue(), "UTF-8"));
            }
        } catch (IOException ex) {
            System.out.println(ex);
        } finally {
            if (br != null) {
                try {
                    br.close();
                    is.close();
                } catch (Exception ex) {

                }
            }
        }
        return result;
    }

}
