package ru.lexp00.storage.cloud.network.client;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.ReplayingDecoder;
import org.w3c.dom.ls.LSException;
import org.w3c.dom.ls.LSOutput;
import ru.lexp00.storage.cloud.network.common.*;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class RequestDecoder
        extends ReplayingDecoder<Message> {//inbound

    private Charset charset = StandardCharsets.UTF_8;

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        int state = in.readInt();
        if (state == 2 || state == 3) {
            System.out.println("СlientDecoder: В декодер клиента прилетела буфер байтов");
            ListMessage listMessage = new ListMessage(State.SEND_LIST_FILES);
            List<String> arr = new ArrayList<>();
            int size = in.readInt();
            for (int i = 0; i < size; i++) {
                int length = in.readInt();
                arr.add(in.readCharSequence(length, Charset.defaultCharset()).toString());
            }
            System.out.println(arr);
            listMessage.setListFiles(arr);
            System.out.println("СlientDecoder: Сформировано сообщение для обработчика Клиента");
            out.add(listMessage);
        } else if (state == 6) {
            System.out.println("СlientDecoder: В декодер прилетела буфер байтов");
            FileMessage fileMessage = new FileMessage(State.SEND_FILE);
            int fileTitleLength = in.readInt();
            String strTitleFile = in.readCharSequence(fileTitleLength, charset).toString();
            int listStringLength = in.readInt();
            byte[] data = new byte[listStringLength];
            for (int i = 0; i < listStringLength; i++) {
                data[i] = in.readByte();
            }
            fileMessage.setFileTitle(strTitleFile);
            fileMessage.setData(data);
            out.add(fileMessage);
        } else {
            System.out.println("Client decoder: Пришел не известный код State: " + state);
        }
    }
}
