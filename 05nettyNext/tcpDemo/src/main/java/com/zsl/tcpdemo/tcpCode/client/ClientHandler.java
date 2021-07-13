package com.zsl.tcpdemo.tcpCode.client;

import com.zsl.tcpdemo.tcpCode.utils.MyProtocol;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

import java.nio.charset.StandardCharsets;

/**
 * @author ZSLONG
 * @Description
 * @Date 2021/6/20 19:36
 */
public class ClientHandler extends SimpleChannelInboundHandler<MyProtocol> {
    private int count;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MyProtocol msg) throws Exception {
        System.out.println("收到的服务端消息：" + new String(msg.getBody(), CharsetUtil.UTF_8));
        System.out.println("接收到服务端的消息数量：" + (++count));

    }

    /**
     * 建立连接
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        for (int i = 0; i < 10; i++) {
            byte[] data = "from client msg!".getBytes(StandardCharsets.UTF_8);
            MyProtocol myProtocol = new MyProtocol();
            myProtocol.setLength(data.length);
            myProtocol.setBody(data);

            ctx.writeAndFlush(myProtocol);
        }
        super.channelActive(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("client抛出异常：");
        cause.printStackTrace();
        ctx.close();
        super.exceptionCaught(ctx, cause);
    }
}
