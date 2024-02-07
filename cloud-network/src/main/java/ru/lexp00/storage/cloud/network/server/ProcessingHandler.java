package ru.lexp00.storage.cloud.network.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.w3c.dom.ls.LSException;
import ru.lexp00.storage.cloud.network.common.DirMessage;
import ru.lexp00.storage.cloud.network.common.ListMessage;
import ru.lexp00.storage.cloud.network.common.ListRequest;
import ru.lexp00.storage.cloud.network.common.State;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ProcessingHandler extends ChannelInboundHandlerAdapter {

    private final String serverDir = "./cloud-server";

    private final String serverFiles = "ServerFiles";
    private final Path serverPath = Paths.get(serverDir, serverFiles);

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Сформировал путь к папке сервера");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws IOException {
        System.out.println("В ProcessingHandler сервера прилетело сообщение " + msg.toString());
        if (msg instanceof ListRequest) {
            ListMessage listMessage = new ListMessage(State.SEND_LIST_FILES, serverPath);
            ctx.writeAndFlush(listMessage);
            System.out.println("Отправил список файлов в енкодер Сервера");
        } else if (msg instanceof DirMessage) {
            System.out.println("Прилетело сообщение из декодера о создании папки");
            String dirTitle = ((DirMessage) msg).getDirTitle();
            Path path = Paths.get(serverDir, serverFiles, dirTitle);
            if (!Files.exists(path)) {
                Files.createDirectory(path);
                System.out.println("Создали папку");
            } else {
                System.out.println("Listener: папка с таким именем уже существует");
            }
            ListMessage listMessage = new ListMessage(State.SEND_LIST_FILES, serverPath);
            ctx.writeAndFlush(listMessage);
            System.out.println("Отправили сообщение со всем списком файлов на сервере");
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}


