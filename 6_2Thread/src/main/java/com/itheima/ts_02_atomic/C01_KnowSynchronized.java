package com.itheima.ts_02_atomic;

/**
 * 认识 synchronized 关键字的用法
 */
public class C01_KnowSynchronized {

    static int i = 0;

    // 1、修饰非静态方法  锁定的是当前类的实例对象this
    public synchronized void foo() {
        i++;
    }

    // 2、修饰静态方法 锁定的是当前类的Class对象
    public static synchronized void bar() {
        i--;
    }


    Object obj = new Object();

    public void car() {
        //3、修饰代码块 锁定的是指定的对象    查看字节码可看出隐式的加锁和解锁
        synchronized (obj) {
            i+=2;
        }
    }


    public static void main(String[] args) {

    }
}
