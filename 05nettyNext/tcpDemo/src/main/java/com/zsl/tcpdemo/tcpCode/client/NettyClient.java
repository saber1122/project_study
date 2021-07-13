package com.zsl.tcpdemo.tcpCode.client;

import com.zsl.tcpdemo.tcpCode.utils.MyDecoder;
import com.zsl.tcpdemo.tcpCode.utils.MyEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @author ZSLONG
 * @Description 客户端实现
 * @Date 2021/6/20 19:47
 */
public class NettyClient {
    public static void main(String[] args) {
        NioEventLoopGroup worker = new NioEventLoopGroup();

        try {
            //    服务启动类
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(worker);
            bootstrap.channel(NioSocketChannel.class);
            bootstrap.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(new MyEncoder());
                    ch.pipeline().addLast(new MyDecoder());
                    ch.pipeline().addLast(new ClientHandler());
                }
            });
            ChannelFuture future = bootstrap.connect("127.0.0.1", 7989).sync();
            future.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            worker.shutdownGracefully();
        }
    }
}
