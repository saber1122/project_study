package com.itheima.ts_10_reference_threadlocal;

import com.itheima.util.ThreadUtil;

/**
 * 强引用：即正常引用
 */
public class C00_NormalReference {
    public static void main(String[] args) {
        User user = new User();
        user=null;
        System.gc();
        ThreadUtil.sleepSeconds(3);
    }
}
