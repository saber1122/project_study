package com.zsl.tcpdemo.nettyCode.utils;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

import java.util.List;

/**
 * @author ZSLONG
 * @Description
 * @Date 2021/6/20 21:30
 */
public class IntegerHeaderFrameDecoderByRe  extends ReplayingDecoder<Void> {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        out.add(in.readBytes(in.readInt()));
    }
}
