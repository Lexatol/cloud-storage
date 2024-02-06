package ru.lexp00.storage.cloud.client.gui;

import ru.lexp00.storage.cloud.client.network.ClientListener;
import ru.lexp00.storage.cloud.client.network.StorageClientNetwork;
import ru.lexp00.storage.cloud.network.client.ClientNetwork;
import ru.lexp00.storage.cloud.network.client.ClientNetworkListHandler;
import ru.lexp00.storage.cloud.network.common.ListMessage;
import ru.lexp00.storage.cloud.network.common.ListRequest;
import ru.lexp00.storage.cloud.network.common.Message;
import ru.lexp00.storage.cloud.network.common.State;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class ClientGUI extends JFrame implements ClientNetworkListHandler, ClientListener, Thread.UncaughtExceptionHandler, ActionListener {
    private final int POS_X = 30;
    private final int POS_Y = 30;
    private final int WITH = 800;
    private final int HEIGHT = 600;
    private final int WITHPANELFILE = (int) (WITH / 2 - 10);


    private JPanel panelBottom = new JPanel(new BorderLayout());
    private JPanel panelLeft = new JPanel(new BorderLayout());
    private JPanel panelRight = new JPanel(new BorderLayout());
    private JPanel panelNorth = new JPanel(new GridLayout(1, 1));
    private JPanel panelSouth = new JPanel(new GridLayout(2, 2));
    private JPanel panelCenter = new JPanel(new GridLayout(10, 1));
    private JList<String> fileListClient = new JList<>();
    private JList<String> fileListServer = new JList<>();
    private JLabel pathFolderLeft = new JLabel("Local files");
    private JLabel pathFolderRight = new JLabel("Server files");

    private JMenuBar menuBar = new JMenuBar();
    private JMenu menuFile = new JMenu("Файл");
    private JMenu menuNetwork = new JMenu("Сеть");

    private JMenuItem menuFileItemAddCloud = new JMenuItem("Добавить...");
    private JMenuItem menuFileItemExit = new JMenuItem("Выход");
    private JButton btnRenameLocalFile = new JButton("Rename local File");
    private JButton btnCreateLocalFolder = new JButton("Create Local Folder");
    private JButton btnUploadToServer = new JButton("Upload to Server");
    private JButton btnLocalDelete = new JButton("Local Delete");
    private JButton btnRenameServerFile = new JButton("Rename Server File");
    private JButton btnCreateServerFolder = new JButton("Create Server Folder");
    private JButton btnDownloadToLocal = new JButton("Download to Local");
    private JButton btnServerDelete = new JButton("Server Delete");


    private final String CLIENTFILEDIR = "ClientFiles";
    private final Path clientPath = Paths.get("./cloud-client", CLIENTFILEDIR);

    private ClientNetwork clientNetwork;


    public ClientGUI() {
        initFrame();
        try {
            updateClientFiles();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private void initFrame() {
        setBounds(POS_X, POS_Y, WITH, HEIGHT);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        JScrollPane scrollFileListLeftPane = new JScrollPane(fileListClient);
        scrollFileListLeftPane.setPreferredSize(new Dimension(WITHPANELFILE, 0));
        JScrollPane scrollFileListRightPane = new JScrollPane(fileListServer);
        scrollFileListRightPane.setPreferredSize(new Dimension(WITHPANELFILE, 0));
        panelRight.add(scrollFileListRightPane, BorderLayout.CENTER);
        panelRight.add(pathFolderRight, BorderLayout.NORTH);

        panelLeft.add(pathFolderLeft, BorderLayout.NORTH);
        panelLeft.add(scrollFileListLeftPane, BorderLayout.CENTER);

        panelSouth.add(btnRenameLocalFile);
        panelSouth.add(btnCreateLocalFolder);
        panelSouth.add(btnRenameServerFile);
        panelSouth.add(btnCreateServerFolder);
        panelSouth.add(btnUploadToServer);
        panelSouth.add(btnLocalDelete);
        panelSouth.add(btnDownloadToLocal);
        panelSouth.add(btnServerDelete);
        btnRenameLocalFile.addActionListener(this);
        btnCreateLocalFolder.addActionListener(this);
        btnRenameServerFile.addActionListener(this);
        btnCreateServerFolder.addActionListener(this);
        btnUploadToServer.addActionListener(this);
        btnLocalDelete.addActionListener(this);
        btnDownloadToLocal.addActionListener(this);
        btnServerDelete.addActionListener(this);

        menuFileItemAddCloud.addActionListener(this);
        menuFileItemExit.addActionListener(this);
        menuFile.add(menuFileItemAddCloud);
        menuFile.add(menuFileItemExit);
        menuBar.add(menuFile);
        panelNorth.add(menuBar);

        panelBottom.add(panelNorth, BorderLayout.NORTH);
        panelBottom.add(panelSouth, BorderLayout.SOUTH);
        panelBottom.add(panelLeft, BorderLayout.WEST);
        panelBottom.add(panelCenter, BorderLayout.CENTER);
        panelBottom.add(panelRight, BorderLayout.EAST);

        add(panelBottom);
        setVisible(true);
    }

    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {
        String msg;
        StackTraceElement[] ste = throwable.getStackTrace();
        msg = "Exception in thread " + thread.getName() + " " +
                throwable.getClass().getCanonicalName() + ": " +
                throwable.getMessage() + "\n\t" + ste[0];
        JOptionPane.showMessageDialog(null, msg, "Exception", JOptionPane.ERROR_MESSAGE);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object event = e.getSource();
        if (event == menuFileItemExit) {
            dispose();
            System.out.println("Программа завершена");
        } else if (event == menuFileItemAddCloud) {
            new FrameAddServer(this, this);
        } else if (event == btnCreateServerFolder) {
            clientNetwork.sendMessage(new ListRequest());
            //todo отправлять сообщение с запросом на создание папки
//            Message msg;
//            send(msg);
        } else {
            throw new RuntimeException("Обработай событие, ты про него забыл");
        }

    }


//    private void updateServerFiles(String[] listFiles) {
//        updateListFiles(fileListServer, listFiles);
//    }

    private void updateClientFiles() throws IOException {
        String[] listFiles = Files.list(clientPath)
                .map(p -> {
                    if (Files.isDirectory(p)) {
                        return "[DIR] " + p.getFileName().toString();
                    } else {
                        return p.getFileName().toString();
                    }
                })
                .sorted()
                .toArray(String[]::new);
        updateListFiles(fileListClient, listFiles);
    }

    private void updateListFiles(JList<String> fileList, String[] listFiles) {
        fileList.removeAll();
        fileList.setListData(listFiles);
    }

    private void send(Message msg) {
        clientNetwork.sendMessage(msg);
    }


    @Override
    public void clientOnConnect(ClientNetwork clientNetwork) {
        this.clientNetwork = clientNetwork;
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        send(new ListRequest());
    }

    @Override
    public void onServerListFiles(ListMessage listMessage) {
        List<String> list = listMessage.getListFiles();

        String [] listFiles = list.toArray(String[]::new);
        updateListFiles(fileListServer, listFiles);
    }

//
//    private void processServerMessage(Message message) throws IOException {
//        if(message instanceof ListMessage) {
//            ListMessage listMessage = (ListMessage) message;
//            String[] list = listMessage.getListFiles();
//            updateServerFiles(list);
//        }
//        if (message instanceof FileMessage) {
//        }
//        }
//            FileMessage file = (FileMessage) message;
//            if (file.getNum() == 1) {
//                Files.write(
//                        clientPath.resolve(file.getFileName()),
//                        file.getBytes(),
//                        StandardOpenOption.CREATE_NEW
//                );
//            } else {
//                Files.write(
//                        clientPath.resolve(file.getFileName()),
//                        file.getBytes(),
//                        StandardOpenOption.APPEND
//                );
//            }
//            if (file.isFinish()) {
//                updateClientView();
//            }
//        } else if (message instanceof ListMessage) {
//            ListMessage list = (ListMessage) message;
//            updateServerView(list.getFiles());
//        }
}



