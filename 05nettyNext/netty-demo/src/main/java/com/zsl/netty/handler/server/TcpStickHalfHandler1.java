package com.zsl.netty.handler.server;

import com.zsl.netty.pojo.UserInfo;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;

/**
 * @author ZSLONG
 * @Description
 * @Date 2021/6/26 22:12
 */
@Slf4j
public class TcpStickHalfHandler1  extends ChannelInboundHandlerAdapter {
    int count =0;
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
    }

    /**
     * 粘包、半包场景测试
     * @param ctx
     * @param msg
     * @throws Exception
     */
 /*   @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf) msg;
        count++;
        log.info("---服务端收到的第{}个数据:{}",count,buf.toString(StandardCharsets.UTF_8));
        super.channelRead(ctx, msg);

    }*/

    /**
     * 二次编解码测试  StringDecoder
     * @param ctx
     * @param msg
     * @throws Exception
     */
 /*   @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String buf = (String) msg;
        count++;
        log.info("---服务端收到的第{}个数据:{}",count,buf);
        super.channelRead(ctx, buf);

    }*/

    /**
     * 二次编码 ProtostuffDecoder() 测试
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        UserInfo userInfo = (UserInfo) msg;
        count++;
        log.info("---服务端收到的第{}个数据:{}",count,userInfo);
        super.channelRead(ctx, userInfo);

    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        super.channelReadComplete(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }
}
