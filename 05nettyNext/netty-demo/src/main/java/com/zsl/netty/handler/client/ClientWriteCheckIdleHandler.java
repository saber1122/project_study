package com.zsl.netty.handler.client;

import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

/**
 * @author ZSLONG
 * @Description 像服务端发送keepalive
 * @Date 2021/7/3 20:44
 */

public class ClientWriteCheckIdleHandler extends IdleStateHandler {
    public ClientWriteCheckIdleHandler() {
        super(0, 5, 0, TimeUnit.SECONDS);
    }

    /**
     * 也可直接进行事件操作
     * @param ctx
     * @param evt
     * @throws Exception
     */
 /*   @Override
    protected void channelIdle(ChannelHandlerContext ctx, IdleStateEvent evt) throws Exception {
        super.channelIdle(ctx, evt);
    }*/
}
