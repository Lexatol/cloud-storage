//package ru.lexp00.storage.cloud.server.core;
//
//import io.netty.channel.ChannelHandlerContext;
//import io.netty.channel.ChannelInboundHandlerAdapter;
//import ru.lexp00.storage.cloud.network.common.ListMessage;
//import ru.lexp00.storage.cloud.network.common.ListRequest;
//import ru.lexp00.storage.cloud.network.common.State;
//
//import java.io.BufferedOutputStream;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//
//public class FileHandler extends ChannelInboundHandlerAdapter {
//
//    private int nextLength;
//    private long fileLength;
//    private long receivedFileLength;
//    private BufferedOutputStream out;
//
//    private final String serverDir = "server";
//    private Path serverPath;
//
//
//    @Override
//    public void channelActive(ChannelHandlerContext ctx) throws Exception {
//        serverPath = Paths.get("./", serverDir);
//    }
//
//    @Override
//    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//        System.out.println("Читаю входящие сообщения");
//        if (msg instanceof ListRequest) {
//            System.out.println("отправляю список файлов с сервера");
//            ctx.writeAndFlush(new ListMessage(serverPath, State.SEND_LIST_FILES));
//        }
//
////        ByteBuf buf = ((ByteBuf) msg);
////        while (buf.readableBytes() > 0) {
////            if (currentState == State.IDLE) {
////                byte readed = buf.readByte();
////                if (readed == (byte) 25) {
////                    currentState = State.NAME_LENGTH;
////                    receivedFileLength = 0L;
////                    System.out.println("STATE: Start file receiving");
////                } else {
////                    System.out.println("ERROR: Invalid first byte - " + readed);
////                }
////            }
////
////            if (currentState == State.NAME_LENGTH) {
////                if (buf.readableBytes() >= 4) {
////                    System.out.println("STATE: Get filename length");
////                    nextLength = buf.readInt();
////                    currentState = State.NAME;
////                }
////            }
////
////            if (currentState == State.NAME) {
////                if (buf.readableBytes() >= nextLength) {
////                    byte[] fileName = new byte[nextLength];
////                    buf.readBytes(fileName);
////                    System.out.println("STATE: Filename received - _" + new String(fileName, "UTF-8"));
////                    out = new BufferedOutputStream(new FileOutputStream("_" + new String(fileName)));
////                    currentState = State.FILE_LENGTH;
////                }
////            }
////
////            if (currentState == State.FILE_LENGTH) {
////                if (buf.readableBytes() >= 8) {
////                    fileLength = buf.readLong();
////                    System.out.println("STATE: File length received - " + fileLength);
////                    currentState = State.FILE;
////                }
////            }
////
////            if (currentState == State.FILE) {
////                while (buf.readableBytes() > 0) {
////                    out.write(buf.readByte());
////                    receivedFileLength++;
////                    if (fileLength == receivedFileLength) {
////                        currentState = State.IDLE;
////                        System.out.println("File received");
////                        out.close();
////                        break;
////                    }
////                }
////            }
////        }
////        if (buf.readableBytes() == 0) {
////            buf.release();
////        }
//    }
//
//    @Override
//    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
//        cause.printStackTrace();
//        ctx.close();
//    }
//}
