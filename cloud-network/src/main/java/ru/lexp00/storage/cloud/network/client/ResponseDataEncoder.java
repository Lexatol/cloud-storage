package ru.lexp00.storage.cloud.network.client;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.handler.codec.MessageToMessageDecoder;
import ru.lexp00.storage.cloud.network.common.ListMessage;
import ru.lexp00.storage.cloud.network.common.ListRequest;
import ru.lexp00.storage.cloud.network.common.Message;

import java.nio.charset.Charset;
import java.util.List;

public class ResponseDataEncoder
        extends MessageToByteEncoder<Message> {//outbaund


    @Override
    protected void encode(ChannelHandlerContext ctx, Message msg, ByteBuf out) throws Exception {
        if (msg instanceof ListRequest) {
            System.out.println("В ResponseDataEncoder клиента прилетело сообщение на кодирование: " + msg.getClass().getCanonicalName());
            ListRequest message = (ListRequest) msg;
            out.writeCharSequence(message.getClass().getCanonicalName(), Charset.defaultCharset());
            System.out.println("Сообщение отправлено на сервер в Декодер сервера");
        }
    }
}