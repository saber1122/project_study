package com.zsl.netty;

import com.zsl.netty.handler.server.MyHttpServerHandler;
import com.zsl.netty.handler.server.MyHttpServerHandler1;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpRequestEncoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @author ZSLONG
 * @Description
 * @Date 2021/7/1 21:21
 */
@Slf4j
public class NettyHttpServer {
    public static void main(String[] args) {
        NettyServer nettyServer = new NettyServer();
        nettyServer.start(8889);
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
                            ////编写http案例
                            //pipeline.addLast(new HttpRequestEncoder());
                            //
                            //pipeline.addLast(new HttpRequestDecoder());
                            //pipeline.addLast(new HttpObjectAggregator(1024 * 1024 * 8));
                            //
                            ////    添加业务处理器
                            //pipeline.addLast(new MyHttpServerHandler());

                            //编写http案例
                            pipeline.addLast(new HttpResponseEncoder());

                            pipeline.addLast(new HttpRequestDecoder());
                            pipeline.addLast(new HttpObjectAggregator(1024*1024*8));
                            //添加业务处理器
                            //pipeline.addLast(new MyHttpServerHandler());
                            pipeline.addLast(new MyHttpServerHandler1());

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
