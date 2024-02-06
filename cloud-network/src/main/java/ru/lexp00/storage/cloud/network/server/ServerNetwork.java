package ru.lexp00.storage.cloud.network.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class ServerNetwork {
    private int port;
    private ChannelFuture channelFuture;

    public ServerNetwork(int port) {
        this.port = port;
    }

    public void run() throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            System.out.println("Server started");
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch)
                                throws Exception {
                            ch.pipeline().addLast(
                                    new RequestDecoder(),
                                    new ResponseDataEncoder(),
                                    new ProcessingHandler());
                        }
                    }).option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            channelFuture = b.bind(port).sync();
            channelFuture.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

    public void stop() {
        if (channelFuture.channel().isActive()) {

            //todo сюда добавим пакет который отправим клиенту об отключении
            try {
                channelFuture.await(1000);//а это нам надо чтобы пакет успел улететь, иначе закроем раньше канал чем нужно

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            channelFuture.awaitUninterruptibly();
            channelFuture.addListener(ChannelFutureListener.CLOSE);
            System.out.println("Server stopped");
        } else {
            System.out.println("Server is stopped");
        }

    }
}
