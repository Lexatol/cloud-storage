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

    private final Charset charset = StandardCharsets.UTF_8;
    private final ServerListener serverListener;

    public ResponseDataEncoder(ServerListener serverListener) {
        this.serverListener = serverListener;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx,
                          Message msg, ByteBuf out) {
        int state = msg.getState().getTitle();
        out.writeInt(state);
        if (msg instanceof ListMessage) {
            ListMessage message = (ListMessage) msg;
            int sizeListMessage = message.getListFiles().size();
            out.writeInt(sizeListMessage);
            for (int i = 0; i < sizeListMessage; i++) {
                String titleFile = message.getListFiles().get(i);
                outWriteTitle(out, titleFile.length(), titleFile);
            }
        } else if (msg instanceof FileMessage) {
            FileMessage fileMessage = (FileMessage) msg;
            String fileTitle = fileMessage.getTitleFile();
            outWriteTitle(out, fileTitle.length(), fileTitle);
            byte[] data = fileMessage.getDataFile();
            out.writeInt(data.length);
            out.writeBytes(data);
        } else {
            serverListener.onServerException("Server Encoder: Не известный тип сообщения");
        }
    }

    private void outWriteTitle(ByteBuf out, int fileTitleLength, String fileTitle) {
        out.writeInt(fileTitleLength);
        out.writeCharSequence(fileTitle, charset);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        serverListener.onServerException(cause.getMessage());
        ctx.close();
    }
}
