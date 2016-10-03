/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inet.util;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 *
 * @author dainv
 */
public class Logger {

    static boolean IS_DEBUG = true;

    public static void log(String msg) {
        if (IS_DEBUG) {
            String curr = getCurrentDateAsString();
            System.out.println(curr + "_" + msg);
        }
    }

    public static void log(Throwable ex) {
        String curr = getCurrentDateAsString();
        String err = null;
        try {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            ex.printStackTrace(pw);
            err = sw.toString();
            sw.close();
            pw.close();
        } catch (Exception ex1) {

        }
        System.out.println(curr + "_" + err);
    }

    public static void main(String[] args) {
        //Logger.log("hehe");
        try {
            String obj = null;
            obj.isEmpty();
        } catch (Exception ex) {
            //ex.printStackTrace();
            Logger.log(ex);
        }
    }

    private static String getCurrentDateAsString() {
        java.text.DateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String curr = df.format(new java.util.Date());
        return curr;
    }
}
