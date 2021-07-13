package com.zsl.tcpdemo.nettyCode;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author ZSLONG
 * @Description
 * @Date 2021/6/20 21:14
 */
public class ServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Integer integer = (Integer) msg;
        System.out.println("服务端接收到的消息为：" + integer);
        super.channelRead(ctx, msg);
    }


}
