/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inet.encode;

import inet.encode.bean.VideoInfoUsingFFProbe;
import inet.util.ExecUtil;
import inet.util.FileUtil;
import inet.util.Logger;
import inet.util.StringUtil;
import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

/**
 *
 * @author dainv
 */
public class Test {

    static String SHELL_COMMAND_GET_INFO_VIDEO = "~/script/v2/ffmpeg_get_info.sh";

    private static final String CONFIG_FILE_PATH = "conf/encoder.conf";

    static String INPUT_FILE = "";
    static String OUTPUT_FILE = "";

    private static void readConfigFile() {
        Map<String, String> configs = FileUtil.readConfigFile(CONFIG_FILE_PATH);

        String shellCmdCreateKeyFile = configs.get("SHELL_COMMAND_GET_INFO_VIDEO");
        if (!StringUtil.isEmpty(shellCmdCreateKeyFile)) {
            SHELL_COMMAND_GET_INFO_VIDEO = shellCmdCreateKeyFile;
        }

        String inputFile = configs.get("INPUT_FILE");
        if (!StringUtil.isEmpty(inputFile)) {
            INPUT_FILE = inputFile;
        }

        String outputFile = configs.get("OUTPUT_FILE");
        if (!StringUtil.isEmpty(outputFile)) {
            OUTPUT_FILE = outputFile;
        }
    }

    public static void main(String[] args) {
        //readConfigFile();
        md5();
//        String str = "\\/unica/83/8299/";
//        System.out.println(str.startsWith(File.separator));
//        System.out.println(str.substring(1));

        //System.out.println("Start now");
        //Sc.myCall();
//        String fileName = "conf/iadd.conf";
//        Map<String, String> result = FileUtil.readConfigFile(fileName);
//        System.out.println("Props: " + result);
//        if (args.length > 0) {
//            testExec(args[0]);
//        }
        //testSymlink();
    }

    static void testSymlink() {
        String oldFile = INPUT_FILE;
        String movedFile = OUTPUT_FILE;
        File f = new File(oldFile);
        try {
            boolean r = f.renameTo(new File(movedFile));
            Logger.log("IN: " + INPUT_FILE);
            Logger.log(r + "_OUT: " + OUTPUT_FILE);
        } catch (Exception ex) {
            Logger.log(ex);
        }
    }

    static void testExec(String inputFile) {
        final String SPACE = " ";
        String cmd = SHELL_COMMAND_GET_INFO_VIDEO + SPACE + inputFile;
        String result = ExecUtil.execShellCmdReturnResult(cmd);
        Logger.log("Result of : " + cmd);

        VideoInfoUsingFFProbe obj = VideoInfoUsingFFProbe.parseJson(result);
        Logger.log(obj.toJson());
        Logger.log("===========================================");
    }

    static void md5() {
        String str = "đói quá";
        System.out.println(md5(str));
    }

    static String md5(String input) {
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
