package ru.lexp00.storage.cloud.network.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import ru.lexp00.storage.cloud.network.common.ListMessage;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ProcessingHandler extends ChannelInboundHandlerAdapter {

    private final String serverDir = ".cloud-server";

    private final String serverFiles = "ServerFiles";
    private final Path serverPath = Paths.get("./cloud-server", serverFiles);

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Сформировал путь к папке сервера");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws IOException {
//        if (msg instanceof ListRequest) {
        ListMessage listMessage = new ListMessage(serverPath);
        ctx.writeAndFlush(listMessage);
        System.out.println("Отправил список файлов в енкодер Сервера");
//        }
    }


    //
//            ListMessage message = new ListMessage(State.FILE_LIST);
//            int countFiles = in.readInt();//отправляю размер массива
//            for (int i = 0; i <countFiles; i++) {//хожу по массиву заданное кол-во раз
//                int strLenFiles = in.readInt();//забираю длину каждой строки
//                message.getListFiles().add(in.readCharSequence(strLenFiles, charset).toString());//добавляю в новый пакет message
//            }
//            ListMessage list = (ListMessage) msg;
//            ctx.writeAndFlush(list);
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}


