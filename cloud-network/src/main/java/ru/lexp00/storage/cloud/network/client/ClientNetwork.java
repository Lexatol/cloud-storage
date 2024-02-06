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
    private ClientNetworkListHandler clientNetworkListHandler;

    public ClientNetwork(String host, int port, ClientNetworkListHandler clientNetworkListHandler) {
        this.clientNetworkListHandler = clientNetworkListHandler;
        this.host = host;
        this.port = port;
        System.out.println("Constructor clientNetwork started");
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
                public void initChannel(SocketChannel ch)
                        throws Exception {
                    ch.pipeline().addLast(
                            new ResponseDataEncoder(),
                            new RequestDecoder(),
                            new ClientNetworkHandler(clientNetworkListHandler)
                    );
                }
            });

            channelFuture = b.connect(host, port).sync();
            System.out.println("Client started");

            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            workerGroup.shutdownGracefully();
        }

    }

    public void close() {
        channelFuture.channel().close();
        channelFuture.addListener(ChannelFutureListener.CLOSE);
    }

    public void sendMessage(Message msg) {
        System.out.println("зашли в метод отправки сообщения");
        channelFuture.channel().writeAndFlush(msg);
    }
}