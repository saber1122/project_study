package com.itheima.ts_03_ordering;

/**
 * 创建对象的中间状态：
 *
 * 0 new #3 <com/itheima/ts_03_ordering/C01_NewObject>
 * 3 dup
 * 4 invokespecial #4 <com/itheima/ts_03_ordering/C01_NewObject.<init> : ()V>
 * 7 astore_1
 * 8 return
 */
public class C01_NewObject {

    int m = 8;

    public static void main(String[] args) {
        C01_NewObject c = new C01_NewObject();
    }


}
