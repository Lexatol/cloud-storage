package ru.lexp00.storage.cloud.network.server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.ReplayingDecoder;
import ru.lexp00.storage.cloud.network.common.*;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class RequestDecoder extends MessageToMessageDecoder<Message> {//inbound
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Установлено соединение в RequestDecoder");
    }
        @Override
    protected void decode(ChannelHandlerContext ctx, Message msg, List<Object> out) throws Exception {
//        System.out.println("В декодер Сервера прилетело сообщение " + msg.getClass().getCanonicalName());
//        if (msg instanceof ListRequest) {
//            ListRequest listRequest = (ListRequest) msg;
//            System.out.println(listRequest.getClass().getCanonicalName());
//        }
        out.add(msg);
//    }

//    @Override
//    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
//        System.out.println("В декодер Сервера прилетело сообщение " );
//        System.out.println(in.toString());
//        out.add(in.toString());

//        if (msg instanceof ListRequest) {
//            ListRequest listRequest = (ListRequest) msg;
//            System.out.println(listRequest.getClass().getCanonicalName());
//        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}



