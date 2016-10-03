/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inet.util;

import java.util.concurrent.TimeUnit;

/**
 *
 * @author dainv
 */
public class ThreadUtil {

    public static void sleep(int second) {
        try {
            TimeUnit.SECONDS.sleep(second);
        } catch (InterruptedException ex) {
            Logger.log(ex);
        }
    }
}
