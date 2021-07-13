package com.zsl.netty.handler.server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;

/**
 * @author ZSLONG
 * @Description
 * @Date 2021/6/26 21:32
 */
@Slf4j
public class ServerOutboundHandler1 extends ChannelOutboundHandlerAdapter {

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        ByteBuf buf = (ByteBuf) msg;
        log.info("OutboundHandler1----server send msg to client,msg ={}",buf.toString(StandardCharsets.UTF_8));
        super.write(ctx, msg, promise);
        ByteBuf buf1 = ctx.alloc().buffer();
        buf1.writeBytes("oh oh oh i am appended".getBytes(StandardCharsets.UTF_8));
        ctx.writeAndFlush(buf1);
    }
}