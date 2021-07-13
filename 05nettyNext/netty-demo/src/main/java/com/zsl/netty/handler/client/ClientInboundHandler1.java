package com.zsl.netty.handler.client;

import com.zsl.netty.pojo.UserInfo;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;
//import org.apache.catalina.startup.ContextConfig;

import java.nio.charset.StandardCharsets;

/**
 * @author ZSLONG
 * @Description
 * @Date 2021/6/26 21:34
 */
@Slf4j
public class ClientInboundHandler1 extends ChannelInboundHandlerAdapter {
    //@Override
    //public void channelActive(ChannelHandlerContext ctx) throws Exception {
    //    //启动，通道准备就绪后向服务端发送数据
    //    String msg = "hello server,I am client!";
    //    ByteBuf buffer = ctx.alloc().buffer();
    //    buffer.writeBytes(msg.getBytes(StandardCharsets.UTF_8));
    //    //写数据
    //    //ctx.channel().writeAndFlush(buffer);
    //    ctx.writeAndFlush(buffer);
    //    super.channelActive(ctx);
    //}

    /**
     * 粘包、半包场景测试
     * @param ctx
     * @throws Exception
     */
/*    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("ClientInboundHandler1 channelActive begin send data");
        //批量发送数据
        UserInfo userInfo;
        for (int i = 0; i < 100; i++) {
            userInfo = new UserInfo(i, "name" + i, i + 1, (i % 2 == 0) ? "男" : "女", "北京");
            ctx.writeAndFlush(Unpooled.copiedBuffer(userInfo.toString().getBytes(StandardCharsets.UTF_8)));
        }
    }*/

    /**
     * 分隔符测试
     *
     * @param ctx
     * @throws Exception
     */
 /*   @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("ClientInboundHandler1 channelActive begin send data");
        //批量发送数据
        UserInfo userInfo;
        for (int i = 0; i < 100; i++) {
            userInfo = new UserInfo(i, "name" + i, i + 1, (i % 2 == 0) ? "男" : "女", "北京");
            byte[] bytes = (userInfo.toString() + "$").getBytes(StandardCharsets.UTF_8);
            ByteBuf buffer = ctx.alloc().buffer(bytes.length);
            buffer.writeBytes(bytes);
            ctx.writeAndFlush(buffer);
        }
    }*/

    /**
     * 基于消息头的解码器
     * @param ctx
     * @throws Exception
     */
/*    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("ClientInboundHandler1 channelActive begin send data");
        //批量发送数据
        UserInfo userInfo;
        for (int i = 0; i < 100; i++) {
            userInfo = new UserInfo(i, "name" + i, i + 1, (i % 2 == 0) ? "男" : "女", "北京");
            byte[] bytes = userInfo.toString().getBytes(StandardCharsets.UTF_8);
            ByteBuf buffer = ctx.alloc().buffer(bytes.length);
            buffer.writeBytes(bytes);
            ctx.writeAndFlush(buffer);
        }
    }*/

    /**
     * 二次编码 StringEncoder()
     *
     * @param ctx
     * @throws Exception
     */
   /* @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("ClientInboundHandler1 channelActive begin send data");
        //批量发送数据
        UserInfo userInfo;
        for (int i = 0; i < 100; i++) {
            userInfo = new UserInfo(i, "name" + i, i + 1, (i % 2 == 0) ? "男" : "女", "北京");

            ctx.writeAndFlush(userInfo.toString());
        }
    }*/

    /**
     * 二次编码，ProtostuffEncoder()
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("ClientInboundHandler1 channelActive begin send data");
        //批量发送数据
        UserInfo userInfo;
        for (int i = 0; i < 100; i++) {
            userInfo = new UserInfo(i, "name" + i, i + 1, (i % 2 == 0) ? "男" : "女", "北京");

            ctx.writeAndFlush(userInfo);
        }
    }

    /**
     * @param ctx
     * @param msg
     * @return void
     * @Description 通道内有无数据
     * @Author ZhangShuangLong
     * @Date 2021-6-26 11:18
     **/
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 接收服务端返回的数据
        log.info("ClientInboundHandler1 channelRead");
        ByteBuf buf = (ByteBuf) msg;
        log.info("ClientInboundHandler1:received server data = {}", buf.toString(StandardCharsets.UTF_8));
        super.channelRead(ctx, msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }
}
