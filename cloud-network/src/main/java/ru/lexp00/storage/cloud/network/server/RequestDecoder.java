package ru.lexp00.storage.cloud.network.server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;
import ru.lexp00.storage.cloud.network.common.*;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class RequestDecoder extends ReplayingDecoder<Message> {

    private Charset charset = StandardCharsets.UTF_8;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Установлено соединение в RequestDecoder");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        int state = in.readInt();
        if (state == 1) {
            out.add(new ListRequest(State.SEND_LIST_REQUEST));
        } else if (state == 3) {
            String titleDir = getString(in);
            out.add(new DirMessage(titleDir, State.SEND_ADD_FOLDER_SERVER));
        } else if (state == 4) {
            String lastTitleFile = getString(in);
            String newTitleFile = getString(in);
            out.add(new RenameMessage(lastTitleFile, newTitleFile, State.SEND_RENAME_FOLDER_SERVER));
        } else if (state == 5) {
            String titleFile = getString(in);
            out.add(new DeleteMessage(titleFile, State.SEND_DELETE_FILE));
        } else if (state == 6) {
            String titleFile = getString(in);
            int listStringLength = in.readInt();
            byte[] data = new byte[listStringLength];
            for (int i = 0; i < listStringLength; i++) {
                data[i] = in.readByte();
            }
            FileMessage fileMessage = new FileMessage(State.SEND_FILE);
            fileMessage.setTitleFile(titleFile);
            fileMessage.setDataFile(data);
            out.add(fileMessage);
        } else if (state == 7) {
            String titleFile = getString(in);
            out.add(new FileRequest(titleFile, State.SEND_FILE_REQUEST));
        }

    }

    private String getString(ByteBuf in) {
        int titleLength = in.readInt();
        return in.readCharSequence(titleLength, charset).toString();
    }
}




