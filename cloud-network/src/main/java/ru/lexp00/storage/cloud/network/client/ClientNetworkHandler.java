package ru.lexp00.storage.cloud.network.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import ru.lexp00.storage.cloud.network.common.FileMessage;
import ru.lexp00.storage.cloud.network.common.ListMessage;


public class ClientNetworkHandler extends ChannelInboundHandlerAdapter {

    private final ClientListener clientListener;

    public ClientNetworkHandler(ClientListener clientListener) {
        this.clientListener = clientListener;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        if (msg instanceof ListMessage) {
            ListMessage listMessage = (ListMessage) msg;
            clientListener.onServerListFiles(listMessage);
        } else if (msg instanceof FileMessage) {
            FileMessage fileMessage = (FileMessage) msg;
            clientListener.onDownloadFileOnLocal(fileMessage);
        } else {
            clientListener.onClientException("ClientNetworkHandler: Не известный тип сообщения " + msg.getClass().getCanonicalName());
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        clientListener.onClientException(cause.getMessage());
        ctx.close();

    }

}
