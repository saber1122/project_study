package com.itheima.ts_01_visibility;

import com.itheima.util.ThreadUtil;

/**
 * final 字段能保证可见性，编译器也可以随便优化(比如指令重排)
 * Java 编译器在 1.5 以前的版本的确优化得很努力，以至于都优化错了
 * 在 1.5 以后 Java 内存模型对 final 类型变量的重排进行了约束。现在只要我们提供正确构造函数没有“逸出”，就不会出问题了。
 *  ->（this逃逸）
 */
public class C01_FinalField {

    final int x ;
    int y;

    static C01_FinalField f;

    // 正确的构造函数
    /*public C01_FinalField() {
        x = 3;
        y = 4;
        // this逸出
    }*/

    // 不正确的构造函数
    public C01_FinalField() {
        x = 3;
        y = 4;
        C02_FinalThisEscape.obj = this;
    }


    static void writer() {
        f = new C01_FinalField();
    }

    static void reader() {
        if (f != null) {
            int i = f.x;
            int j = f.y;
            System.out.println("x="+i+",y="+j);
        }
    }


    public static void main(String[] args) {
        Thread t1 = new Thread(()->{
            // t1 thread read
            C01_FinalField.reader();
            //C02_FinalThisEscape.readX();
        });
        // main thread write
        C01_FinalField.writer();

        t1.start();

        ThreadUtil.sleepSeconds(5);
    }
}
