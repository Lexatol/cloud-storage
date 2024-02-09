package ru.lexp00.storage.cloud.client.gui.frames;

import ru.lexp00.storage.cloud.client.core.ClientGUIListener;
import ru.lexp00.storage.cloud.client.core.StatePlace;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FrameRenameFile extends JFrame implements ActionListener, Thread.UncaughtExceptionHandler {
    private final int POS_X = 100;
    private final int POS_Y = 100;
    private final int WITH = 700;
    private final int HEIGHT = 200;

    private final ClientGUIListener clientGUIListener;


    private JPanel panelBottom = new JPanel(new GridLayout(3, 1));
    private JPanel panelLastFile = new JPanel(new GridLayout());
    private JPanel panelNewFile = new JPanel(new GridLayout());


    private JLabel labelLastTitleFile = new JLabel("Last title file: ");
    private JTextField txtLastTitleFile = new JTextField("");
    private JLabel labelNewTitleFile = new JLabel("New title file: ");
    private JTextField txtNewTitleFile = new JTextField();

    private JButton btnCreate = new JButton("OK");

    private final StatePlace state;


    public FrameRenameFile(String lastTitleFile, ClientGUIListener clientGUIListener, StatePlace state) {
        initFrame();
        this.clientGUIListener = clientGUIListener;
        this.state = state;
        txtLastTitleFile.setText(lastTitleFile);
        txtLastTitleFile.setEnabled(false);
    }


    private void initFrame() {
        setBounds(POS_X, POS_Y, WITH, HEIGHT);
        setResizable(false);

        panelLastFile.add(labelLastTitleFile);
        panelLastFile.add(txtLastTitleFile);
        panelNewFile.add(labelNewTitleFile);
        panelNewFile.add(txtNewTitleFile);
        panelBottom.add(panelLastFile);
        panelBottom.add(panelNewFile);
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
        clientGUIListener.onRenameFile(txtLastTitleFile.getText(), txtNewTitleFile.getText(), state);
        dispose();
    }
}

