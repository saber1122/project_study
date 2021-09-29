package com.itheima.util;

import java.util.concurrent.TimeUnit;

public class ThreadUtil {

    public static void sleepSeconds(long seconds) {
        try {
            TimeUnit.SECONDS.sleep(seconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void sleepMillisSeconds(long millisSeconds) {
        try {
            TimeUnit.MILLISECONDS.sleep(millisSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
