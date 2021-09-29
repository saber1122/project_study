package com.itheima.ts_08_jol;

import org.openjdk.jol.info.ClassLayout;

/**
 * 认识 JOL
 *  https://mvnrepository.com/artifact/org.openjdk.jol/jol-core
 *         <dependency>
 *             <groupId>org.openjdk.jol</groupId>
 *             <artifactId>jol-core</artifactId>
 *             <version>0.9</version>
 *         </dependency>
 *
 */
public class C00_KnowJOL {

    public static void main(String[] args) {
        Object o = new Object();
        //打印对象的内存布局
        System.out.println(ClassLayout.parseInstance(o).toPrintable());


        User user = new User(18,"ts");
        System.out.println(ClassLayout.parseInstance(user).toPrintable());


        Coupons c = new Coupons(100L);
        System.out.println(ClassLayout.parseInstance(c).toPrintable());


        int[] arr = new int[]{1,2,3};
        System.out.println(ClassLayout.parseInstance(arr).toPrintable());

        User[] users = new User[3];
        System.out.println(ClassLayout.parseInstance(users).toPrintable());
    }

    static class User{
        private int age;
        private String name;
        public User(){};
        public User(int age,String name) {
            this.age = age;
            this.name = name;
        }
    }

    static class Coupons{
        private long id;
        public Coupons(){};
        public Coupons(long id) {
            this.id = id;
        }
    }
}
