package com.zsl.tcpdemo.tcpCode.utils;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

import java.util.List;

/**
 * @author ZSLONG
 * @Description 解码器
 * @Date 2021/6/20 19:32
 */
public class MyDecoder extends ReplayingDecoder<Void> {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        //获取长度
        int length = in.readInt();
        //    根据长度定义byte数组
        byte[] data = new byte[length];
        //读取数据
        in.readBytes(data);
        MyProtocol myProtocol = new MyProtocol();
        myProtocol.setLength(length);
        myProtocol.setBody(data);

        out.add(myProtocol);

    }
}
