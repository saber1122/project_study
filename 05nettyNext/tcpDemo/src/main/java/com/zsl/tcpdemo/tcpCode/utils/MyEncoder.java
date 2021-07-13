package com.zsl.tcpdemo.tcpCode.utils;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @author ZSLONG
 * @Description 编码器
 * @Date 2021/6/20 19:29
 */
public class MyEncoder extends MessageToByteEncoder<MyProtocol> {
    @Override
    protected void encode(ChannelHandlerContext ctx, MyProtocol msg, ByteBuf out) throws Exception {
        out.writeInt(msg.getLength());
        out.writeBytes(msg.getBody());
    }
}
