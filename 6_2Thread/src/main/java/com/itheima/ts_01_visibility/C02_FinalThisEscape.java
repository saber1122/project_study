package com.itheima.ts_01_visibility;
// this逃逸
public class C02_FinalThisEscape {
    public static C01_FinalField obj;

    public static void readX() {
        System.out.println("obj.x="+obj.x);
    }
}
