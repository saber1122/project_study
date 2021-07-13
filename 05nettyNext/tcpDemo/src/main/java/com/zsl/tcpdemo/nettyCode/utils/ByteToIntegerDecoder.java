package com.zsl.tcpdemo.nettyCode.utils;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * @author ZSLONG
 * @Description 将字节流转为 Interger类型
 * @Date 2021/6/20 21:07
 */
public class ByteToIntegerDecoder extends ByteToMessageDecoder {
    /**
     * @param ctx 上下文
     * @param in  输入的ByteBuf 消息数据
     * @param out 转化后输出的容器
     * @throws Exception
     */
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        //int 类型占用是个字节，所以需要判断是否存在四个字节，在进行读取
        if (in.readableBytes() >= 4) {
            //读取到int类型数据，放入到输出，完成数据类型的转化
            out.add(in.readInt());
        }
    }
}
