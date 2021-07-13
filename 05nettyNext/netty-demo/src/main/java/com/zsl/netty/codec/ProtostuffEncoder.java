package com.zsl.netty.codec;

import com.zsl.netty.pojo.UserInfo;
import com.zsl.netty.util.ProtostuffUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import lombok.extern.slf4j.Slf4j;
import sun.security.x509.CertificateX509Key;

import java.util.List;

/**
 * @author ZSLONG
 * @Description 将java对象编码为 ByteBuf
 * @Date 2021/6/27 22:12
 */
@Slf4j
public class ProtostuffEncoder extends MessageToMessageEncoder<UserInfo> {
    @Override
    protected void encode(ChannelHandlerContext ctx, UserInfo msg, List<Object> out) throws Exception {
        try {
            byte[] bytes = ProtostuffUtil.serialize(msg);
            ByteBuf buffer = ctx.alloc().buffer(bytes.length);
            buffer.writeBytes(bytes);
            out.add(buffer);
        } catch (Exception e) {
            log.error("protostuff encode error,msg={}", e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
