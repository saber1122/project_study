package com.zsl.tcpdemo.tcpCode.server;

import com.zsl.tcpdemo.tcpCode.utils.MyProtocol;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

import java.nio.charset.StandardCharsets;

/**
 * @author ZSLONG
 * @Description
 * @Date 2021/6/20 19:56
 */
public class ServerHandler extends SimpleChannelInboundHandler<MyProtocol> {
    private int count;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MyProtocol msg) throws Exception {
        System.out.println("服务端接收到的消息：" + new String(msg.getBody(), CharsetUtil.UTF_8));
        System.out.println("服务端接收到的消息数量：" + (++count));
        byte[] date="ok".getBytes(StandardCharsets.UTF_8);
        MyProtocol myProtocol = new MyProtocol();
        myProtocol.setLength(date.length);
        myProtocol.setBody(date);

        ctx.writeAndFlush(myProtocol);
    }

}
