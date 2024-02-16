package ru.lexp00.storage.cloud.network.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.w3c.dom.ls.LSException;
import ru.lexp00.storage.cloud.network.common.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.stream.Stream;

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
            sendMessageWithListFilesOnServer(ctx);

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
            sendMessageWithListFilesOnServer(ctx);

        } else if (msg instanceof RenameMessage) {
            System.out.println("Прилетело сообщение из декодера с задаче переименовать папку на сервере");
            RenameMessage renameMessage = (RenameMessage) msg;
            String lastTitleFile = renameMessage.getLastTitleFile();
            System.out.println("lastTitleFile: " + lastTitleFile);

            String newTitleFile = renameMessage.getNewTitleFile();
            System.out.println("newTitleFile: " + newTitleFile);


            Path destinationPath = Paths.get(serverDir, serverFiles, newTitleFile);
            Path sourcePath;
            if (!lastTitleFile.contains("[DIR]")) {
                sourcePath = Paths.get(serverDir, serverFiles, lastTitleFile);
                System.out.println("SourcePath: " + sourcePath);
            } else {
                String[] strLasFile = lastTitleFile.split(" ");
                sourcePath = Paths.get(serverDir, serverFiles, strLasFile[1]);
                System.out.println("SourcePath: " + sourcePath);
            }
            System.out.println("DestinationPath" + destinationPath);
            System.out.println(Files.exists(sourcePath));
            if (!Files.exists(destinationPath)) {
                try {
                    Files.move(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            sendMessageWithListFilesOnServer(ctx);
        } else if (msg instanceof DeleteMessage) {
            System.out.println("Пришло сообщение из декодера с задачей об удалении файла на сервере");
            DeleteMessage deleteMessage = (DeleteMessage) msg;
            String strFile = deleteMessage.getStrTitle();
            Path path;
            if (!strFile.contains("[DIR]")) {
                path = Paths.get(serverDir, serverFiles, strFile);
            } else {
                String[] str = strFile.split(" ");
                path = Paths.get(serverDir, serverFiles, str[1]);
            }
            Files.delete(path);
            sendMessageWithListFilesOnServer(ctx);
        } else if (msg instanceof FileMessage) {
            System.out.println("Пришло сообщение с файлом из декодера");
            FileMessage fileMessage = (FileMessage) msg;
            String strTitle = fileMessage.getFileTitle();

            Path path;
            if (!strTitle.contains("[DIR]")) {
                path = Paths.get(serverDir, serverFiles, strTitle);
                Files.write(path, fileMessage.getData());
            } else {
                //todo добавить копирование папок

//                String[] str = strTitle.split(" ");
//                Path rootPath = Paths.get(serverDir, serverFiles, str[1]);
//                try {
//                    Files.walkFileTree(rootPath, new SimpleFileVisitor<Path>() {
//                        @Override
//                        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
//                            System.out.println("delete file: " + file.toString());
//                            Files.createDirectory(file);
//                            return FileVisitResult.CONTINUE;
//                        }
//
//                        @Override
//                        public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
//                            Files.createDirectory(dir);
//                            System.out.println("delete dir: " + dir.toString());
//                            return FileVisitResult.CONTINUE;
//                        }
//
//                    });
//                } catch(IOException e){
//                    e.printStackTrace();
//            }
            }
            sendMessageWithListFilesOnServer(ctx);
        } else if (msg instanceof FileRequest) {
            FileRequest fileRequest = (FileRequest) msg;
            String strFile = fileRequest.getStrFile();
            Path path = Paths.get(serverDir, serverFiles, strFile);
            FileMessage fileMessage = new FileMessage(path, State.SEND_FILE);
            ctx.writeAndFlush(fileMessage);
            System.out.println("Server Handler: отправил запрос в Server Encoder");
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    private void sendMessageWithListFilesOnServer(ChannelHandlerContext ctx) {
        ListMessage listMessage = null;
        try {
            listMessage = new ListMessage(serverPath, State.SEND_LIST_FILES);
            ctx.writeAndFlush(listMessage);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Отправили сообщение со всем списком файлов на сервере");
    }
}


