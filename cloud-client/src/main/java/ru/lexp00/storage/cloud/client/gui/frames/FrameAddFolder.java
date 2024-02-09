package ru.lexp00.storage.cloud.client.gui.frames;

import ru.lexp00.storage.cloud.client.core.ClientGUIListener;
import ru.lexp00.storage.cloud.client.core.StatePlace;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FrameAddFolder extends JFrame implements ActionListener, Thread.UncaughtExceptionHandler{
    private final int POS_X = 100;
    private final int POS_Y = 100;
    private final int WITH = 700;
    private final int HEIGHT = 150;

    private final ClientGUIListener clientGUIListener;

    private JPanel panelBottom = new JPanel(new GridLayout(3, 1));

    private JLabel labelCreateFolderServer = new JLabel("Input folder title: ");
    private JTextField txtFolderTitle = new JTextField("");

    private JButton btnCreate = new JButton("Create Folder");

    private final StatePlace state;


    public FrameAddFolder(ClientGUIListener clientGUIListener, StatePlace state) {
        initFrame();
        this.clientGUIListener = clientGUIListener;
        this.state = state;
    }


    private void initFrame() {
        setBounds(POS_X, POS_Y, WITH, HEIGHT);
        setResizable(false);

        panelBottom.add(labelCreateFolderServer);
        panelBottom.add(txtFolderTitle);
        panelBottom.add(btnCreate);

        btnCreate.addActionListener(this);
        add(panelBottom);
        setVisible(true);

    }

    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {
        throwable.printStackTrace();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        clientGUIListener.onAddFolder(txtFolderTitle.getText(), state);
        dispose();
    }
}


