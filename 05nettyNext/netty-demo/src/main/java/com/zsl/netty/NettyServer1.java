package com.zsl.netty;

import com.zsl.netty.codec.ProtostuffDecoder;
import com.zsl.netty.codec.ProtostuffEncoder;
import com.zsl.netty.handler.server.ServerReadIdleCheckHandler;
import com.zsl.netty.handler.server.TcpStickHalfHandler1;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @author ZSLONG
 * @Description idle监测
 * @Date 2021/6/26 21:14
 */
@Slf4j
public class NettyServer1 {

    public static void main(String[] args) {
        NettyServer1 nettyServer = new NettyServer1();
        nettyServer.start(8990);
    }

    public void start(int port) {
        //    创建线程 boss，worker

        EventLoopGroup boss = new NioEventLoopGroup();
        EventLoopGroup worker = new NioEventLoopGroup();

        try {
            //    创建服务端引导类，引导整个服务端程序的启动
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(boss, worker)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        /**
                         * @Description initChannel 每次客户端创建完成后，
                         *                  为其初始化handler的时候执行
                         * @Author ZhangShuangLong
                         * @Date 2021-6-26 10:32
                         * @param socketChannel
                         * @return void
                         **/
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline pipeline = socketChannel.pipeline();
                           pipeline.addLast(new LoggingHandler(LogLevel.INFO));
                           pipeline.addLast(new ServerReadIdleCheckHandler());

                           pipeline.addLast(new LengthFieldPrepender(2));
                           pipeline.addLast(new StringEncoder());
                           pipeline.addLast(new LengthFieldBasedFrameDecoder(1024,0,2,0,2));
                           pipeline.addLast(new StringDecoder());
                        }
                    });
            //    服务端绑定端口号启动
            ChannelFuture future = serverBootstrap.bind(port).sync();
            //阻塞监听
            future.channel().closeFuture().sync();

        } catch (Exception e) {
            log.error("netty server error,msg={}", e.getMessage());
        } finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }

    }
}
