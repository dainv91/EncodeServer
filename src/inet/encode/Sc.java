/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inet.encode;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author dainv
 */
public class Sc {

    static {
        //init();
    }

    private static void init() {
        System.out.println("Start init");
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException ex) {
            Logger.getLogger(Sc.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("End init");
    }

    public static void myCall() {
        System.out.println("Call......");
    }

    public static void main(String[] args) {
        duration();
    }

    static void duration() {
        String str = "413.50000";
        try {
            //java.text.NumberFormat df = new java.text.DecimalFormat("#.###");
            //df.setRoundingMode(RoundingMode.CEILING);

            int num = (int) Math.round(Double.parseDouble(str));
            System.out.println(num);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
