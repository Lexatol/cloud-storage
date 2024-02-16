package ru.lexp00.storage.cloud.network.server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import ru.lexp00.storage.cloud.network.common.FileMessage;
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
       } else if (msg instanceof FileMessage) {
           FileMessage fileMessage = (FileMessage) msg;
           String fileTitle = fileMessage.getTitleFile();
           int fileTitleLength = fileTitle.length();
           out.writeInt(fileTitleLength);
           out.writeCharSequence(fileTitle, charset);
           byte[] data = fileMessage.getDataFile();
           int listStringLength = data.length;
           out.writeInt(listStringLength);
           out.writeBytes(data);
       }
       else {
           throw new RuntimeException("Server Encoder: Не известный тип сообщения");
       }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
