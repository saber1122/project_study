package com.zsl.netty.handler.server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

/**
 * @author ZSLONG
 * @Description
 * @Date 2021/6/26 21:15
 */
@Slf4j
public class ServerInboundHandler1 extends ChannelInboundHandlerAdapter {
    /**
     * @param ctx
     * @return void
     * @Description 连接已经建立好，开始进行初始化
     * @Author ZhangShuangLong
     * @Date 2021-6-26 10:48
     **/
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("ServerInboundHandler1 channelActive-----");
        TimeUnit.SECONDS.sleep(3);
        //向后传播
        //ctx.fireChannelActive();
        super.channelActive(ctx);
    }

    /**
     * @param ctx
     * @param msg
     * @return void
     * @Description 从通道有数据可读时
     * @Author ZhangShuangLong
     * @Date 2021-6-26 10:52
     **/
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        log.info("ServerInboundHandler1 channelRead----,remoteAddress={}", ctx.channel().remoteAddress());
        ByteBuf buf = (ByteBuf) msg;
        log.info("ServerInboundHandler1:received client data = {}", buf.toString(StandardCharsets.UTF_8));
        super.channelRead(ctx, msg);
    }

    /**
     * @param ctx
     * @return void
     * @Description 通道内数据读取完成
     * @Author ZhangShuangLong
     * @Date 2021-6-26 11:15
     **/
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        log.info("ServerInboundHandler1 channelReadComplete 向客户端写数据----");
        //向客户端写数据
        String msg = "hello client,I am server!";
        ByteBuf buffer = ctx.alloc().buffer();
        buffer.writeBytes(msg.getBytes(StandardCharsets.UTF_8));
        //添加监听
/*     ChannelFuture future = ctx.writeAndFlush(buffer);
        future.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {

            }
        });*/
        ctx.writeAndFlush(buffer);
        super.channelReadComplete(ctx);
    }

    /**
     * @param ctx
     * @param cause
     * @return void
     * @Description 异常回调
     * @Author ZhangShuangLong
     * @Date 2021-6-26 10:53
     **/
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.info("ServerInboundHandler1 exceptionCaught----,cause={}", cause.getMessage());
        super.exceptionCaught(ctx, cause);
    }
}