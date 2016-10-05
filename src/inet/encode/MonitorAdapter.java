/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inet.encode;

import inet.util.StringUtil;

/**
 *
 * @author dainv
 */
public class MonitorAdapter {
    
    public static void startMonitorWithKeyServer(String monitorPortStr) {
        if (StringUtil.isEmpty(Encoder.KEY_STORE_PATH)) {
            // Start http
            Monitor.startMonitorServer(monitorPortStr);
        } else {
            // Start https
            SecureMonitor.startMonitorServer(monitorPortStr);
        }
    }

    public static void main(String[] args) {
        startMonitorWithKeyServer("6790");
    }
}
