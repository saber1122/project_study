package com.zsl.netty;

import com.zsl.netty.codec.ProtostuffDecoder;
import com.zsl.netty.codec.ProtostuffEncoder;
import com.zsl.netty.handler.server.ServerInboundHandler1;
import com.zsl.netty.handler.server.ServerInboundHandler2;
import com.zsl.netty.handler.server.ServerOutboundHandler1;
import com.zsl.netty.handler.server.TcpStickHalfHandler1;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;

/**
 * @author ZSLONG
 * @Description
 * @Date 2021/6/26 21:14
 */
@Slf4j
public class NettyServer {

    public static void main(String[] args) {
        NettyServer nettyServer = new NettyServer();
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
                            //向pipeline中添加handler
                            //往客户端写数据
                            //pipeline.addLast(new ServerOutboundHandler1());
                            //
                            //pipeline.addLast(new ServerInboundHandler1());
                            //pipeline.addLast(new ServerInboundHandler2());

                        //    分隔符测试
                        //    pipeline.addLast(new LineBasedFrameDecoder(66536));
                        //    byte[] bytes = "$".getBytes(StandardCharsets.UTF_8);
                        //    ByteBuf buf = Unpooled.wrappedBuffer(bytes);
                        //    pipeline.addLast(new DelimiterBasedFrameDecoder(65536,buf));

                            //往客户端写数据
                            pipeline.addLast(new LengthFieldPrepender(2));
                            //pipeline.addLast(new StringEncoder());
                            pipeline.addLast(new ProtostuffEncoder());


                            //基于消息头的一次解码
                            pipeline.addLast(new LengthFieldBasedFrameDecoder(65536,0,2,0,2));
                            //二次解码
                            //pipeline.addLast(new StringDecoder());
                            pipeline.addLast(new ProtostuffDecoder());
                            //    tcp 粘包、半包测试
                            pipeline.addLast(new TcpStickHalfHandler1());
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
