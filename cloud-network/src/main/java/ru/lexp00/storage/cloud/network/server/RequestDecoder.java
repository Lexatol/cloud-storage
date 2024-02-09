package ru.lexp00.storage.cloud.network.server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.ReplayingDecoder;
import org.w3c.dom.ls.LSOutput;
import ru.lexp00.storage.cloud.network.common.*;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class RequestDecoder extends ReplayingDecoder<Message> {//inbound

    private Charset charset = StandardCharsets.UTF_8;
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Установлено соединение в RequestDecoder");
    }
//        @Override
//    protected void decode(ChannelHandlerContext ctx, Message msg, List<Object> out) throws Exception {
//            System.out.println("Попали в decode");
//        if (msg.getState().equals(State.SEND_LIST_REQUEST)) {
//            System.out.println("Сверяем это у нас запрос на список файлов или нет");
//            ListRequest listRequest = (ListRequest) msg;
//            out.add(listRequest);
//        }
//        System.out.println("В декодер Сервера прилетело сообщение " + msg.getClass().getCanonicalName());
//        if (msg instanceof ListRequest) {
//            ListRequest listRequest = (ListRequest) msg;
//            System.out.println(listRequest.getClass().getCanonicalName());
//        }
//        out.add(msg);
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
//    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        int state = in.readInt();
        if (state == 1) {
            ListRequest listRequest = new ListRequest(State.SEND_LIST_REQUEST);
            out.add(listRequest);
        } else if (state == 3) {
            System.out.println("От КЛИЕНТА прилетело сообщение на декодирование - создание папки");
            int dirTitleLengh = in.readInt();
            String dirTitle = in.readCharSequence(dirTitleLengh, charset).toString();
            System.out.println("Название папки");
            DirMessage dir = new DirMessage(dirTitle, State.SEND_LIST_REQUEST);
            out.add(dir);
            System.out.println("Сообщение улетело в хендлер сервера");
        }
//        String str = in.readCharSequence(count, Charset.defaultCharset()).toString();
//        out.add(str);
    }
}




