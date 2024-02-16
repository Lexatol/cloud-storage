package ru.lexp00.storage.cloud.network.client;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import ru.lexp00.storage.cloud.network.common.*;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class ResponseDataEncoder
        extends MessageToByteEncoder<Message> {//outbaund
    private Charset charset = StandardCharsets.UTF_8;
    private int sizePacket = 100000;


    @Override
    protected void encode(ChannelHandlerContext ctx, Message msg, ByteBuf out) throws Exception {
        int state = msg.getState().getTitle();
        out.writeInt(state);
        if (msg instanceof ListRequest) {
            System.out.println("СlientEncoder: Пришло сообщение на кодирование " + msg.getClass().getCanonicalName());
        } else if (msg instanceof DirMessage) {
            System.out.println("СlientEncoder: Пришло сообщение на кодирование: " + msg.getClass().getCanonicalName());
            String dirTitle = ((DirMessage) msg).getDirTitle();
            out.writeInt(dirTitle.length());
            out.writeCharSequence(dirTitle, charset);
            System.out.println("СlientEncoder: Отпавлено сообщение о создании папки на сервер");
        } else if (msg instanceof RenameMessage) {
            System.out.println("СlientEncoder: Пришло сообщение на кодирование: " + msg.getClass().getCanonicalName());
            RenameMessage renameMessage = (RenameMessage) msg;
            String lasTitleFile = renameMessage.getLastTitleFile();
            String newTitleFile = renameMessage.getNewTitleFile();
            int lasTitleFileLength = lasTitleFile.length();
            int newTitleFileLength = newTitleFile.length();
            out.writeInt(lasTitleFileLength);
            out.writeCharSequence(lasTitleFile, charset);
            out.writeInt(newTitleFileLength);
            out.writeCharSequence(newTitleFile, charset);
            System.out.println("СlientEncoder: Отправлено сообщение с переименование папки на сервере");
        } else if (msg instanceof DeleteMessage) {
            System.out.println("СlientEncoder: Пришло сообщение на кодирование: " + msg.getClass().getCanonicalName());
            DeleteMessage deleteMessage = (DeleteMessage) msg;
            String strFile = deleteMessage.getStrTitle();
            int strFileLength = strFile.length();
            out.writeInt(strFileLength);
            out.writeCharSequence(strFile, charset);
            System.out.println("СlientEncoder: Отправлено сообщение с удалением файла на сервере");
        } else if (msg instanceof FileMessage) {
            System.out.println("СlientEncoder: Пришло сообщение на кодирование: " + msg.getClass().getCanonicalName());
            FileMessage fileMessage = (FileMessage) msg;
            String fileTitle = fileMessage.getFileTitle();
            int fileTitleLength = fileTitle.length();
            out.writeInt(fileTitleLength);
            System.out.println("СlientEncoder: Отправил длину названия файла");
            out.writeCharSequence(fileTitle, charset);
            System.out.println("СlientEncoder: Отправил название файла");
            byte[] data = fileMessage.getData();
            int listStringLength = data.length;
            out.writeInt(listStringLength);
            out.writeBytes(data);
            System.out.println("СlientEncoder: Все пакеты отправлены");
        } else if(msg instanceof FileRequest) {
            System.out.println("СlientEncoder: Пришло сообщение на кодирование " + msg.getClass().getCanonicalName());
            FileRequest fileRequest = (FileRequest) msg;
            String fileTitle = fileRequest.getStrFile();
            int fileTitleLength = fileTitle.length();
            out.writeInt(fileTitleLength);
            System.out.println("СlientEncoder: Отправил длину названия файла");
            out.writeCharSequence(fileTitle, charset);
            System.out.println("СlientEncoder: Отправил название файле");
        }
    }
}
