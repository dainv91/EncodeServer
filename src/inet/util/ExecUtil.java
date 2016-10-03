/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inet.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 *
 * @author dainv
 */
public class ExecUtil {

    public static interface Listener {

        void onResponse(String response);
    }

    public static boolean execShellCmd(String cmd) {
        boolean result = false;
        if (!OSUtil.isLinux()) {
            Logger.log("Only implemented on Linux...");
            return false;
        }
        try {
            Runtime runtime = Runtime.getRuntime();
            Process process = runtime.exec(new String[]{"/bin/bash", "-c", cmd});
            int exitValue = process.waitFor();
            Logger.log("exit value: " + exitValue);
            if (exitValue == 0) {
                result = true;
            }
            BufferedReader buf = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = buf.readLine()) != null) {
                Logger.log("exec response: " + line);
            }
        } catch (Exception ex) {
            Logger.log(ex);
        }
        return result;
    }

    public static boolean execShellCmdOnly(String cmd) {
        boolean result = false;
        if (!OSUtil.isLinux()) {
            Logger.log("Only implemented on Linux...");
            return false;
        }
        try {
            Runtime runtime = Runtime.getRuntime();
            Process process = runtime.exec(cmd);
            int exitValue = process.waitFor();
            Logger.log("exit value: " + exitValue);
            if (exitValue == 0) {
                result = true;
            }
            BufferedReader buf = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = buf.readLine()) != null) {
                Logger.log("exec response: " + line);
            }
        } catch (Exception ex) {
            Logger.log(ex);
        }
        return result;
    }

    public static String execShellCmdReturnResult(String cmd) {
        String result = null;
        if (!OSUtil.isLinux()) {
            Logger.log("Only implemented on Linux...");
            return null;
        }
        try {
            Runtime runtime = Runtime.getRuntime();
            Process process = runtime.exec(new String[]{"/bin/bash", "-c", cmd});
            int exitValue = process.waitFor();
            Logger.log("exit value: " + exitValue);
            if (exitValue == 0) {
                //result = true;
            }
            BufferedReader buf = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            StringBuilder sb = new StringBuilder();
            while ((line = buf.readLine()) != null) {
                //Logger.log("exec response: " + line);
                sb.append(line).append("\n");
            }
            result = sb.toString();
        } catch (Exception ex) {
            Logger.log(ex);
        }
        return result;
    }

    public static void execShellCmdAsync(final String cmd, final Listener listener, final Object obj) {
        Thread sendThread = new Thread(new Runnable() {
            @Override
            public void run() {
                if (!OSUtil.isLinux()) {
                    String response = "Only implemented on Linux...";
                    Logger.log(response);
                    if (listener != null) {
                        listener.onResponse(response);
                    }
                    return;
                }
                try {
                    Runtime runtime = Runtime.getRuntime();
                    Process process = runtime.exec(new String[]{"/bin/bash", "-c", cmd});
                    int exitValue = process.waitFor();
                    Logger.log("exit value: " + exitValue + "---cmd: " + cmd);
                    if (exitValue == 0) {

                    }
                    BufferedReader buf = new BufferedReader(new InputStreamReader(process.getInputStream()));
                    String line;
                    StringBuilder sb = new StringBuilder();
                    while ((line = buf.readLine()) != null) {
                        Logger.log("exec response: " + line);
                        sb.append(line).append("\n");
                    }
                    if (listener != null) {
                        listener.onResponse(sb.toString());
                    }
                } catch (Exception ex) {
                    Logger.log(ex);
                }
            }
        });
        sendThread.start();

    }
}
