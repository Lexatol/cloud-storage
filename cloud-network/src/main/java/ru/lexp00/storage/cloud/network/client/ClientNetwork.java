package ru.lexp00.storage.cloud.network.client;


import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import ru.lexp00.storage.cloud.network.common.Message;


public class ClientNetwork extends Thread {

    private final String host;
    private final int port;

    private ChannelFuture channelFuture;
    private final ClientListener clientListener;

    public ClientNetwork(String host, int port, ClientListener clientListener) {
        this.clientListener = clientListener;
        this.host = host;
        this.port = port;
        start();
    }

    @Override
    public void run() {
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            Bootstrap b = new Bootstrap();
            b.group(workerGroup);
            b.channel(NioSocketChannel.class);
            b.option(ChannelOption.SO_KEEPALIVE, true);
            b.handler(new ChannelInitializer<SocketChannel>() {

                @Override
                public void initChannel(SocketChannel ch) {
                    ch.pipeline().addLast(
                            new ResponseDataEncoder(clientListener),
                            new RequestDecoder(clientListener),
                            new ClientNetworkHandler(clientListener)
                    );
                }
            });

            channelFuture = b.connect(host, port).sync();
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            workerGroup.shutdownGracefully();
        }

    }

    public void sendMessage(Message msg) {
        channelFuture.channel().writeAndFlush(msg);
    }
}