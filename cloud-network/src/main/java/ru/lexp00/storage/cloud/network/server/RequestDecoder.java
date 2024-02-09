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
        System.out.println("State : " + state);
        if (state == 1) {
            ListRequest listRequest = new ListRequest(State.SEND_LIST_REQUEST);
            out.add(listRequest);
        } else if (state == 3) {
            System.out.println("От КЛИЕНТА прилетело сообщение на декодирование - создание папки");
            int dirTitleLength = in.readInt();
            String dirTitle = in.readCharSequence(dirTitleLength, charset).toString();
            System.out.println("Название папки");
            DirMessage dir = new DirMessage(dirTitle, State.SEND_ADD_FOLDER_SERVER);
            out.add(dir);
            System.out.println("Сообщение улетело в хендлер сервера");
        } else if (state == 4) {
            System.out.println("Пришло сообщение от КЛИЕНТА о переименовании папки на сервере");
            int lasTitleFileLength = in.readInt();
            String lastTitleFile = in.readCharSequence(lasTitleFileLength, charset).toString();
            int newTitleFileLength = in.readInt();
            String newTitleFile = in.readCharSequence(newTitleFileLength, charset).toString();
            RenameMessage renameMessage = new RenameMessage(lastTitleFile, newTitleFile, State.SEND_RENAME_FOLDER_SERVER);
            out.add(renameMessage);
            System.out.println("декодер отправил раскодированное сообщение");
        } else if (state == 5) {
            System.out.println("Пришло сообщение от КЛИЕНТА об удалении файла с сервера");
            int strTitleLength = in.readInt();
            String strTitle = in.readCharSequence(strTitleLength, charset).toString();
            DeleteMessage deleteMessage = new DeleteMessage(strTitle, State.SEND_DELETE_FILE);
            out.add(deleteMessage);
            System.out.println("Декодер отправил раскодированное сообщение");
        }
    }
}




