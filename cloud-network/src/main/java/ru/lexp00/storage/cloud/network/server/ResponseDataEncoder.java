package ru.lexp00.storage.cloud.network.server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import ru.lexp00.storage.cloud.network.common.ListMessage;
import ru.lexp00.storage.cloud.network.common.Message;

import java.nio.charset.Charset;

public class ResponseDataEncoder
        extends MessageToByteEncoder<Message> {

    @Override
    protected void encode(ChannelHandlerContext ctx,
                          Message msg, ByteBuf out) throws Exception {
        System.out.println("В ResponseDataEncoder Сервера прилетело сообщение");

       if (msg instanceof ListMessage) {
           ListMessage message = (ListMessage) msg;
           int sizeListMessage = message.getListFiles().size();
           out.writeInt(sizeListMessage);
           for (int i = 0; i < sizeListMessage; i++) {
               String str = message.getListFiles().get(i);
               out.writeInt(str.length());
               out.writeCharSequence(str, Charset.defaultCharset());
           }
           System.out.println("Сервер Отправил сообщение в сеть для получения клиентом с сообщением " + message.getListFiles().toString());
       }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}