package com.zsl.netty.handler.client;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

/**
 * @author ZSLONG
 * @Description
 * @Date 2021/7/3 20:47
 */
@Slf4j
@ChannelHandler.Sharable
public class KeepaliveHandler extends ChannelDuplexHandler {
    /**
     *
     * @param ctx
     * @param evt
     * @throws Exception
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt == IdleStateEvent.FIRST_WRITER_IDLE_STATE_EVENT) {
            log.info("client write idle,so send keepalive msg to server");
            ctx.writeAndFlush("this is keepalive msg");
        }
        super.userEventTriggered(ctx, evt);
    }
}
