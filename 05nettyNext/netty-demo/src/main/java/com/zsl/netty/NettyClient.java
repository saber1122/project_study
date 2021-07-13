package com.zsl.netty;

import com.zsl.netty.codec.ProtostuffDecoder;
import com.zsl.netty.codec.ProtostuffEncoder;
import com.zsl.netty.handler.client.ClientInboundHandler1;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @author ZSLONG
 * @Description
 * @Date 2021/6/27 20:43
 */
@Slf4j
public class NettyClient {
    public static void main(String[] args) {
        NettyClient nettyClient = new NettyClient();
        nettyClient.start("127.0.0.1", 8990);
    }

    public void start(String host, int port) {
        //创建线程池
        NioEventLoopGroup worker = new NioEventLoopGroup();

        try {
            //创建客户端引导类
            Bootstrap bootstrap = new Bootstrap();

            bootstrap.group(worker)
                    .channel(NioSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            //添加handler
                            //一次编码器
                            pipeline.addLast(new LengthFieldPrepender(2));
                            //二次编码器
                            //pipeline.addLast(new StringEncoder());
                            pipeline.addLast(new ProtostuffDecoder());

                            //解码服务端发来的消息
                            pipeline.addLast(new LengthFieldBasedFrameDecoder(65536, 0, 2, 0, 2));
                            //pipeline.addLast(new StringDecoder());
                            pipeline.addLast(new ProtostuffEncoder());
                            pipeline.addLast(new ClientInboundHandler1());

                        }
                    });
            //连接真正的客户端
            ChannelFuture future = bootstrap.connect(host, port).sync();
            //阻塞等待关闭
            future.channel().closeFuture().sync();
        } catch (Exception e) {
            log.error("netty client error,msg={}", e.getMessage());
        } finally {
            worker.shutdownGracefully();
        }

    }
}
