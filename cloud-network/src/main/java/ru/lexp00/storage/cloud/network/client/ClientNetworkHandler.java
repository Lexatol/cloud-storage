package ru.lexp00.storage.cloud.network.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import ru.lexp00.storage.cloud.network.common.ListMessage;


public class ClientNetworkHandler extends ChannelInboundHandlerAdapter {
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
        System.out.println("ClientNetworkHandler: Получил сообщение со списком файлов с сервера" + msg.getClass().getCanonicalName());
        if (msg instanceof ListMessage) {
            ListMessage listMessage = (ListMessage) msg;
            System.out.println("ClientNetworkHandler: Сообщение со списком файлом отправлено на вьюшку");
            System.out.println(listMessage.getListFiles().toString());
            clientNetworkListHandler.onServerListFiles(listMessage);
        }//todo как передать на фрейм
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

}
