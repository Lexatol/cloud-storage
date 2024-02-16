package ru.lexp00.storage.cloud.network.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import ru.lexp00.storage.cloud.network.common.*;

import java.io.IOException;
import java.nio.file.*;

public class ProcessingHandler extends ChannelInboundHandlerAdapter {


    private final String prefixDir = "[DIR]";
    private final String splitDelimiter = " ";

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
            String dirTitle = ((DirMessage) msg).getTitleDir();
            Path path = Paths.get(serverDir, serverFiles, dirTitle);
            if (!Files.exists(path)) {
                Files.createDirectory(path);
                System.out.println("Создали папку");
            } else {
                System.out.println("Listener: папка с таким именем уже существует");
            }
            sendMessageWithListFilesOnServer(ctx);

        } else if (msg instanceof RenameMessage) {
            RenameMessage renameMessage = (RenameMessage) msg;
            String lastTitleFile = renameMessage.getLastTitleFile();
            String newTitleFile = renameMessage.getNewTitleFile();
            Path destinationPath = Paths.get(serverDir, serverFiles, newTitleFile);
            Path sourcePath = getPath(lastTitleFile);
            if (!Files.exists(destinationPath)) {
                try {
                    Files.move(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            sendMessageWithListFilesOnServer(ctx);
        } else if (msg instanceof DeleteMessage) {
            DeleteMessage deleteMessage = (DeleteMessage) msg;
            String titleFile = deleteMessage.getTitleFile();
            Path path;
            path = getPath(titleFile);
            if (!Files.exists(path)) {
                Files.delete(path);
            } else {
                throw new RuntimeException("Такой файл не найден");
            }
            sendMessageWithListFilesOnServer(ctx);
        } else if (msg instanceof FileMessage) {
            System.out.println("Пришло сообщение с файлом из декодера");
            FileMessage fileMessage = (FileMessage) msg;
            String strTitle = fileMessage.getTitleFile();
            Path path;
            if (!strTitle.contains(prefixDir)) {
                path = Paths.get(serverDir, serverFiles, strTitle);
                Files.write(path, fileMessage.getDataFile());
            } else {
                throw new RuntimeException("На данный момент не умеем копировать папки");
            }
            sendMessageWithListFilesOnServer(ctx);
        } else if (msg instanceof FileRequest) {
            FileRequest fileRequest = (FileRequest) msg;
            String strFile = fileRequest.getTitleFile();
            Path path = Paths.get(serverDir, serverFiles, strFile);
            FileMessage fileMessage = new FileMessage(path, State.SEND_FILE);
            ctx.writeAndFlush(fileMessage);
        } else {
            throw new RuntimeException("Не известный формат сообщения " + msg.getClass().getCanonicalName());
        }
    }

    private Path getPath(String titleFile) {
        Path path;
        if (!titleFile.contains(prefixDir)) {
            path = Paths.get(serverDir, serverFiles, titleFile);
        } else {
            String[] str = titleFile.split(splitDelimiter);
            path = Paths.get(serverDir, serverFiles, str[1]);
        }
        return path;
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
    }
}


