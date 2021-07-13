package com.zsl.netty.codec;

import com.zsl.netty.pojo.UserInfo;
import com.zsl.netty.util.ProtostuffUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author ZSLONG
 * @Description 解码器 将一次解码的数据（ByteBuf），转为java对象，
 * @Date 2021/6/27 22:06
 */
@Slf4j
public class ProtostuffDecoder extends MessageToMessageDecoder<ByteBuf> {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
        try {
            int length = msg.readableBytes();
            byte[] bytes = new byte[length];
            msg.readBytes(bytes);
            UserInfo userInfo = ProtostuffUtil.deserialize(bytes, UserInfo.class);
            out.add(userInfo);
        } catch (Exception e) {
            log.error("protostuff decode err,msg={}", e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
