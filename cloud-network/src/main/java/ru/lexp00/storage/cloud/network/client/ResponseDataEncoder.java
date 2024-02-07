package ru.lexp00.storage.cloud.network.client;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.handler.codec.MessageToMessageDecoder;
import ru.lexp00.storage.cloud.network.common.*;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class ResponseDataEncoder
        extends MessageToByteEncoder<Message> {//outbaund
    private Charset charset = StandardCharsets.UTF_8;


    @Override
    protected void encode(ChannelHandlerContext ctx, Message msg, ByteBuf out) throws Exception {
        int state = msg.getState().getTitle();
        out.writeInt(state);
        if (msg instanceof ListRequest) {
            System.out.println("В ResponseDataEncoder клиента прилетело сообщение на кодирование: " + msg.getClass().getCanonicalName());
            System.out.println("Сообщение отправлено на сервер в Декодер сервера " + State.SEND_LIST_REQUEST);
        } else if(msg instanceof DirMessage) {
            System.out.println("В ResponseDataEncoder клиента прилетело сообщение на кодирование: " + msg.getClass().getCanonicalName());
            String dirTitle = ((DirMessage) msg).getDirTitle();
            out.writeInt(dirTitle.length());
            out.writeCharSequence(dirTitle, charset);
            System.out.println("сообщение о создании папки улетело на сервер");
        }
    }
}