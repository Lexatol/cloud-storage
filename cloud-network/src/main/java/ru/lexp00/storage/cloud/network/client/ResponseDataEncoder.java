package ru.lexp00.storage.cloud.network.client;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import ru.lexp00.storage.cloud.network.common.*;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class ResponseDataEncoder
        extends MessageToByteEncoder<Message> {
    private final Charset charset = StandardCharsets.UTF_8;

    private final ClientListener clientListener;

    public ResponseDataEncoder(ClientListener clientListener) {
        this.clientListener = clientListener;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, Message msg, ByteBuf out) {
        int state = msg.getState().getTitle();
        out.writeInt(state);
        if (msg instanceof DirMessage) {
            String dirTitle = ((DirMessage) msg).getTitleDir();
            outTitleFile(out, dirTitle.length(), dirTitle);
        } else if (msg instanceof RenameMessage) {
            RenameMessage renameMessage = (RenameMessage) msg;
            String lasTitleFile = renameMessage.getLastTitleFile();
            String newTitleFile = renameMessage.getNewTitleFile();
            outTitleFile(out, lasTitleFile.length(), lasTitleFile);
            outTitleFile(out, newTitleFile.length(), newTitleFile);
        } else if (msg instanceof DeleteMessage) {
            DeleteMessage deleteMessage = (DeleteMessage) msg;
            String titleFile = deleteMessage.getTitleFile();
            outTitleFile(out, titleFile.length(), titleFile);
        } else if (msg instanceof FileMessage) {
            FileMessage fileMessage = (FileMessage) msg;
            String fileTitle = fileMessage.getTitleFile();
            outTitleFile(out, fileTitle.length(), fileTitle);
            byte[] data = fileMessage.getDataFile();
            out.writeInt(data.length);
            out.writeBytes(data);
        } else if (msg instanceof FileRequest) {
            FileRequest fileRequest = (FileRequest) msg;
            String fileTitle = fileRequest.getTitleFile();
            outTitleFile(out, fileTitle.length(), fileTitle);
        } else if (msg instanceof ListRequest) {
            return;
        }
        else {
            clientListener.onClientException("ClientEnconder: Не известный тип сообщения " + msg.getClass().getCanonicalName());
        }
    }

    private void outTitleFile(ByteBuf out, int titleLength, String title) {
        out.writeInt(titleLength);
        out.writeCharSequence(title, charset);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause){
        clientListener.onClientException(cause.getMessage());
    }
}
