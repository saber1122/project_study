package com.zsl.tcpdemo.tcpCode.utils;

import java.util.Arrays;

/**
 * @author ZSLONG
 * @Description 自定义协议
 * @Date 2021/6/20 19:28
 */
public class MyProtocol {
    //数据头：长度
    private Integer length;
    //数据体
    private byte[] body;

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public byte[] getBody() {
        return body;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "MyProtocol{" +
                "length=" + length +
                ", body=" + Arrays.toString(body) +
                '}';
    }
}
