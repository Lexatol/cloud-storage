package ru.lexp00.storage.cloud.network.client;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.ReplayingDecoder;
import org.w3c.dom.ls.LSException;
import ru.lexp00.storage.cloud.network.common.ListMessage;
import ru.lexp00.storage.cloud.network.common.ListRequest;
import ru.lexp00.storage.cloud.network.common.Message;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class RequestDecoder
        extends ReplayingDecoder<Message> {//inbound

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        System.out.println("В декодер клиента прилетела буфер байтов");
        ListMessage listMessage = new ListMessage();
        List<String> arr = new ArrayList<>();
        int size = in.readInt();
        System.out.println("размер массива = " + size);
        for (int i = 0; i < size; i++) {
            int length = in.readInt();
            arr.add(in.readCharSequence(length,Charset.defaultCharset()).toString());
        }
        System.out.println(arr);
        listMessage.setListFiles(arr);
        System.out.println("Сформировано сообщение для обработчика Клиента");
        out.add(listMessage);
    }
}