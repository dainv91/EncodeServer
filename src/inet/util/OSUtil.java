/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inet.util;

/**
 *
 * Get os type.
 *
 *
 * @author dainv
 *
 */
public class OSUtil {

    private static final boolean OS_IS_MAC_OSX;
    private static final boolean OS_IS_WINDOWS;
    private static final boolean OS_IS_LINUX;

    static {
        String os = System.getProperty("os.name");
        if (os != null) {
            os = os.toLowerCase();
        }

        OS_IS_MAC_OSX = "mac os x".equals(os);
        OS_IS_WINDOWS = os != null && os.contains("windows");
        OS_IS_LINUX = os != null && os.contains("linux");
    }

    public static boolean isMacOSX() {
        return OS_IS_MAC_OSX;
    }

    public static boolean isWindows() {
        return OS_IS_WINDOWS;
    }

    public static boolean isLinux() {
        return OS_IS_LINUX;
    }

}
