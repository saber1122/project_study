package com.zsl.tcpdemo.tcpCode.server;

import com.zsl.tcpdemo.tcpCode.utils.MyDecoder;
import com.zsl.tcpdemo.tcpCode.utils.MyEncoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @author ZSLONG
 * @Description
 * @Date 2021/6/20 20:00
 */
public class NettyServer {

    public static void main(String[] args) {
        //    主线程不做任务业务逻辑，只是接收客户端的连接请求
        NioEventLoopGroup boss = new NioEventLoopGroup();
        //工作线程，默认线程数：CPU*2
        NioEventLoopGroup worker = new NioEventLoopGroup();
        try {
            //    服务启动
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(boss, worker);

            //    配置server通道
            serverBootstrap.channel(NioServerSocketChannel.class);
            serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(new MyDecoder()).
                            addLast(new MyEncoder())
                            .addLast(new ServerHandler());
                }
            });
            //    woker 线程处理器
            ChannelFuture future = serverBootstrap.bind(7989).sync();
            System.out.println("服务器启动完成....");
            //    等待服务端监听端口关闭
            future.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }


    }
}
