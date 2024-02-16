package ru.lexp00.storage.cloud.network.server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import ru.lexp00.storage.cloud.network.common.FileMessage;
import ru.lexp00.storage.cloud.network.common.FileRequest;
import ru.lexp00.storage.cloud.network.common.ListMessage;
import ru.lexp00.storage.cloud.network.common.Message;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class ResponseDataEncoder
        extends MessageToByteEncoder<Message> {

    private Charset charset = StandardCharsets.UTF_8;


    @Override
    protected void encode(ChannelHandlerContext ctx,
                          Message msg, ByteBuf out) throws Exception {
        System.out.println("Server Encoder: Пришло сообщение для кодирования и последующей отправки в сеть");
        int state = msg.getState().getTitle();
        out.writeInt(state);
       if (msg instanceof ListMessage) {
           ListMessage message = (ListMessage) msg;
           int sizeListMessage = message.getListFiles().size();
           out.writeInt(sizeListMessage);
           for (int i = 0; i < sizeListMessage; i++) {
               String str = message.getListFiles().get(i);
               out.writeInt(str.length());
               out.writeCharSequence(str, Charset.defaultCharset());
           }
           System.out.println("Server Encoder: Отправил сообщение в сеть сообщение типа " + message.getListFiles().toString());
       } else if (msg instanceof FileMessage) {
           FileMessage fileMessage = (FileMessage) msg;
           String fileTitle = fileMessage.getFileTitle();
           int fileTitleLength = fileTitle.length();
           out.writeInt(fileTitleLength);
           System.out.println("Server Encoder: Отправил длину названия файла");
           out.writeCharSequence(fileTitle, charset);
           System.out.println("Server Encoder: Отправил название файла");
           byte[] data = fileMessage.getData();
           int listStringLength = data.length;
           out.writeInt(listStringLength);
           out.writeBytes(data);
           System.out.println("Server Encoder: Все пакеты отправлены");

           //todo проверить код
       }
       else {
           System.out.println("Server Encoder: Не известный тип сообщения");
       }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
