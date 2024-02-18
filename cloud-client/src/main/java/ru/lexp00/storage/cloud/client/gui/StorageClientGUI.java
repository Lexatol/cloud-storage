package ru.lexp00.storage.cloud.client.gui;

import ru.lexp00.storage.cloud.client.core.ClientController;
import ru.lexp00.storage.cloud.client.core.ClientGUIListener;
import ru.lexp00.storage.cloud.client.core.StatePlace;
import ru.lexp00.storage.cloud.client.gui.frames.FrameAddFolder;
import ru.lexp00.storage.cloud.client.gui.frames.FrameAddServer;
import ru.lexp00.storage.cloud.client.gui.frames.FrameRenameFile;
import ru.lexp00.storage.cloud.network.client.ClientListener;
import ru.lexp00.storage.cloud.network.common.FileMessage;
import ru.lexp00.storage.cloud.network.common.ListMessage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.nio.file.Path;

public class StorageClientGUI extends JFrame implements Thread.UncaughtExceptionHandler,
        ActionListener, ClientListener, ClientGUIListener {
    private final int POS_X = 30;
    private final int POS_Y = 30;
    private final int WITH = 800;
    private final int HEIGHT = 600;
    private final int WITHPANELFILE = WITH / 2 - 10;


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


    private final ClientController clientController;

    private final Path clientPath;

    public StorageClientGUI() {
        initFrame();
        clientController = new ClientController();
        clientPath = clientController.getClientPath();
        updateClientFiles();
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
        } else if (event == menuFileItemAddCloud) {
            new FrameAddServer(clientController, this);
        } else if (event == btnCreateServerFolder) {
            new FrameAddFolder(this, StatePlace.SERVER_FOLDER);
        } else if (event == btnCreateLocalFolder) {
            new FrameAddFolder(this, StatePlace.LOCAL_FOLDER);
        } else if (event == btnRenameLocalFile) {
            String lastTitleFile = fileListClient.getSelectedValue();
            new FrameRenameFile(lastTitleFile, this, StatePlace.LOCAL_FOLDER);
        } else if (event == btnRenameServerFile) {
            String lastTitleFile = fileListServer.getSelectedValue();
            new FrameRenameFile(lastTitleFile, this, StatePlace.SERVER_FOLDER);
        } else if (event == btnLocalDelete) {
            String strTitle = fileListClient.getSelectedValue();
            onDeleteFile(strTitle, StatePlace.LOCAL_FOLDER);
            updateClientFiles();
        } else if (event == btnServerDelete) {
            String strTile = fileListServer.getSelectedValue();
            onDeleteFile(strTile, StatePlace.SERVER_FOLDER);
            updateClientFiles();
        } else if (event == btnUploadToServer) {
            String strFile = fileListClient.getSelectedValue();
            System.out.println(strFile);
            fileUploadOnServer(strFile);
        } else if (event == btnDownloadToLocal) {
            String strFile = fileListServer.getSelectedValue();
            fileDownloadOnLocal(strFile);
            System.out.println(strFile);
        } else {
            throw new RuntimeException("Обработай событие, ты про него забыл");
        }
    }

    private void updateClientFiles() {
        String[] listFiles = clientController.updateClientFiles();
        updateListFiles(fileListClient, listFiles);
    }

    private void updateListFiles(JList<String> fileList, String[] list) {
        clientController.updateListFiles(fileList, list);
    }

    @Override
    public void onServerListFiles(ListMessage listMessage) {
        String[] listFiles = listMessage.getListFiles().toArray(String[]::new);
        updateListFiles(fileListServer, listFiles);
    }

    @Override
    public void onDownloadFileOnLocal(FileMessage fileMessage) {
        clientController.addFile(fileMessage);
        updateClientFiles();
        JOptionPane.showMessageDialog(null,"File download " + fileMessage.getTitleFile());

    }

    @Override
    public void onClientException(String msg) {
        JOptionPane.showMessageDialog(null, " Exception " + msg);
    }

    @Override
    public void onAddFolder(String newTitleDir, StatePlace stateFolder) {
        clientController.addFolder(newTitleDir, stateFolder);
        updateClientFiles();
        JOptionPane.showMessageDialog(null, "Add folder " + newTitleDir);

    }

    @Override
    public void onRenameFile(String lastTitleFile, String newTitleFile, StatePlace statePlace) {
        clientController.renameFile(lastTitleFile, newTitleFile, statePlace);
        updateClientFiles();
        JOptionPane.showMessageDialog(null, "File rename " + lastTitleFile);
    }

    @Override
    public void onDeleteFile(String strTitle, StatePlace statePlace) {
        try {
            clientController.deleteFile(strTitle, statePlace);
            JOptionPane.showMessageDialog(null, "File delete " + strTitle);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void fileUploadOnServer(String titleFile) {
        clientController.addFileOnServer(titleFile);

    }

    @Override
    public void fileDownloadOnLocal(String titleFile) {
        clientController.addFileOnLocal(titleFile);
        updateClientFiles();
    }
}




