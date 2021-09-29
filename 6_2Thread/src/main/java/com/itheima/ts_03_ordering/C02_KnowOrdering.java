package com.itheima.ts_03_ordering;

/**
 * 指令优化导致的有序性问题
 */
public class C02_KnowOrdering {

    private C02_KnowOrdering(){}

    // 使用volatile禁止指令重排序
    static volatile C02_KnowOrdering instance;

    public static C02_KnowOrdering getInstance() {
        if (instance == null) {
            synchronized (C02_KnowOrdering.class) { // 保证并发线程A/B的有序
                if (instance == null) { // 保证并发线程A/B，B不会修改A创建的对象
                    instance = new C02_KnowOrdering();
                }
            }
        }
        return instance;
    }


    public static void main(String[] args) {
        C02_KnowOrdering instance = getInstance();
    }

}
