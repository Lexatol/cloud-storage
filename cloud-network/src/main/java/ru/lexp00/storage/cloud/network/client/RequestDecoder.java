package ru.lexp00.storage.cloud.network.client;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;
import ru.lexp00.storage.cloud.network.common.*;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class RequestDecoder
        extends ReplayingDecoder<Message> {

    private final Charset charset = StandardCharsets.UTF_8;
    private final ClientListener clientListener;

    public RequestDecoder(ClientListener clientListener) {
        this.clientListener = clientListener;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        int state = in.readInt();
        if (state == 2 || state == 3) {
            ListMessage listMessage = new ListMessage(State.SEND_LIST_FILES);
            List<String> arr = new ArrayList<>();
            int size = in.readInt();
            for (int i = 0; i < size; i++) {
                int length = in.readInt();
                arr.add(in.readCharSequence(length, Charset.defaultCharset()).toString());
            }
            listMessage.setListFiles(arr);
            out.add(listMessage);
        } else if (state == 6) {
            FileMessage fileMessage = new FileMessage(State.SEND_FILE);
            int fileTitleLength = in.readInt();
            String strTitleFile = in.readCharSequence(fileTitleLength, charset).toString();
            int listStringLength = in.readInt();
            byte[] data = new byte[listStringLength];
            for (int i = 0; i < listStringLength; i++) {
                data[i] = in.readByte();
            }
            fileMessage.setTitleFile(strTitleFile);
            fileMessage.setDataFile(data);
            out.add(fileMessage);
        } else {
            clientListener.onClientException("Client decoder: Пришел не известный код State: " + state);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        clientListener.onClientException(cause.getMessage());
    }


}
