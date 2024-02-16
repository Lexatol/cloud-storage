package ru.lexp00.storage.cloud.network.client;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import ru.lexp00.storage.cloud.network.common.*;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class ResponseDataEncoder
        extends MessageToByteEncoder<Message> {
    private Charset charset = StandardCharsets.UTF_8;

    @Override
    protected void encode(ChannelHandlerContext ctx, Message msg, ByteBuf out) throws Exception {
        int state = msg.getState().getTitle();
        out.writeInt(state);
        if (msg instanceof ListRequest) {
        } else if (msg instanceof DirMessage) {
            String dirTitle = ((DirMessage) msg).getTitleDir();
            outTitleFile(out, dirTitle.length(), dirTitle);
        } else if (msg instanceof RenameMessage) {
            RenameMessage renameMessage = (RenameMessage) msg;
            String lasTitleFile = renameMessage.getLastTitleFile();
            String newTitleFile = renameMessage.getNewTitleFile();
            int lasTitleFileLength = lasTitleFile.length();
            int newTitleFileLength = newTitleFile.length();
            outTitleFile(out, lasTitleFileLength, lasTitleFile);
            outTitleFile(out, newTitleFileLength, newTitleFile);
        } else if (msg instanceof DeleteMessage) {
            DeleteMessage deleteMessage = (DeleteMessage) msg;
            String titleFile = deleteMessage.getTitleFile();
            int strFileLength = titleFile.length();
            outTitleFile(out, strFileLength, titleFile);
        } else if (msg instanceof FileMessage) {
            FileMessage fileMessage = (FileMessage) msg;
            String fileTitle = fileMessage.getTitleFile();
            int fileTitleLength = fileTitle.length();
            outTitleFile(out, fileTitleLength, fileTitle);
            byte[] data = fileMessage.getDataFile();
            int listStringLength = data.length;
            out.writeInt(listStringLength);
            out.writeBytes(data);
        } else if(msg instanceof FileRequest) {
            FileRequest fileRequest = (FileRequest) msg;
            String fileTitle = fileRequest.getTitleFile();
            int fileTitleLength = fileTitle.length();
            outTitleFile(out, fileTitleLength, fileTitle);
        } else {
            throw new RuntimeException("ClientEnconder: Не известный тип сообщения " + msg.getClass().getCanonicalName());
        }
    }

    private void outTitleFile(ByteBuf out, int titleLength, String title) {
        out.writeInt(titleLength);
        out.writeCharSequence(title, charset);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
    }
}
