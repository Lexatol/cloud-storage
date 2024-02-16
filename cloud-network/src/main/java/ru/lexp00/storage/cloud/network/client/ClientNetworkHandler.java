package ru.lexp00.storage.cloud.network.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import ru.lexp00.storage.cloud.network.common.FileMessage;
import ru.lexp00.storage.cloud.network.common.ListMessage;


public class ClientNetworkHandler extends ChannelInboundHandlerAdapter {

    private final ClientNetworkListHandler clientNetworkListHandler;

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
            ListMessage listMessage = (ListMessage) msg;
            System.out.println(listMessage.getListFiles().toString());
            clientNetworkListHandler.onServerListFiles(listMessage);
        } else if (msg instanceof FileMessage) {
            FileMessage fileMessage = (FileMessage) msg;
            clientNetworkListHandler.onDownloadFileOnLocal(fileMessage);
        } else {
            throw new RuntimeException("ClientNetworkHandler: Не известный тип сообщения " + msg.getClass().getCanonicalName());
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

}
