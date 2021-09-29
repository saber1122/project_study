package com.itheima.ts_10_reference_threadlocal;

public class User {
    private int id;
    private String name;

    public User(){};
    public User(int id,String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    protected void finalize() throws Throwable {
        System.out.println("user finalize");
    }
}
