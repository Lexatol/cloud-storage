package ru.lexp00.storage.cloud.network.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class ServerNetwork {
    private final int port;
    private ChannelFuture channelFuture;
    private final ServerListener serverListener;

    public ServerNetwork(int port, ServerListener serverListener) {
        this.port = port;
        this.serverListener = serverListener;
    }

    public void run() throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) {
                            ch.pipeline().addLast(
                                    new RequestDecoder(serverListener),
                                    new ResponseDataEncoder(serverListener),
                                    new ProcessingHandler(serverListener));
                        }
                    }).option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            channelFuture = b.bind(port).sync();
            serverListener.onServerStarted("Server started");
            channelFuture.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

    public void stop() {
        if (channelFuture.channel().isActive()) {
            serverListener.onServerStopped("Сервер остановился");
            try {
                channelFuture.await(1000);

            } catch (InterruptedException e) {
                serverListener.onServerException(e.getMessage());
            }
            channelFuture.awaitUninterruptibly();
            channelFuture.addListener(ChannelFutureListener.CLOSE);
        } else {
            serverListener.onServerRequest("Сервер уже остановлен");
        }

    }
}
