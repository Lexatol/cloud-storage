package ru.lexp00.storage.cloud.network.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import ru.lexp00.storage.cloud.network.common.FileMessage;
import ru.lexp00.storage.cloud.network.common.ListMessage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


public class ClientNetworkHandler extends ChannelInboundHandlerAdapter {

    private final String CLIENTFILEDIR = "ClientFiles";
    private final String DIR = "./cloud-client";
    private ClientNetworkListHandler clientNetworkListHandler;

    public ClientNetworkHandler(ClientNetworkListHandler clientNetworkListHandler) {
        this.clientNetworkListHandler = clientNetworkListHandler;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Активирован ClientNetworkHandler");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof ListMessage) {
            System.out.println("ClientNetworkHandler: Сообщение со списком файлом отправлено на вьюшку");
            ListMessage listMessage = (ListMessage) msg;
            System.out.println("ClientNetworkHandler: Сообщение со списком файлом отправлено на вьюшку");
            System.out.println(listMessage.getListFiles().toString());
            clientNetworkListHandler.onServerListFiles(listMessage);
        } else if (msg instanceof FileMessage) {
            System.out.println("ClientNetworkHandler: Сообщение с файлом пришло с сервера");
            FileMessage fileMessage = (FileMessage) msg;
            clientNetworkListHandler.onDownloadFileOnLocal(fileMessage);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

}
