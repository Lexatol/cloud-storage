package ru.lexp00.storage.cloud.client.gui.frames;

import ru.lexp00.storage.cloud.client.network.ClientNetworkListener;
import ru.lexp00.storage.cloud.client.network.StorageClientNetwork;
import ru.lexp00.storage.cloud.network.client.ClientNetwork;
import ru.lexp00.storage.cloud.network.client.ClientNetworkListHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FrameAddServer extends JFrame implements ActionListener, Thread.UncaughtExceptionHandler {
    private final int POS_X = 100;
    private final int POS_Y = 100;
    private final int WITH = 200;
    private final int HEIGHT = 200;


    private JPanel panelBottom = new JPanel(new FlowLayout());
    private JPanel panelServerHost = new JPanel(new GridLayout(1, 2));
    private JPanel panelServerPort = new JPanel(new GridLayout(1, 2));
    private JLabel labelServerHost = new JLabel(" Server host: ");
    private JLabel labelServerPort = new JLabel("Server port: ");
    private JTextField serverHost = new JTextField("localhost");
    private JTextField serverPort = new JTextField("8189");

    private JButton btnConnect = new JButton("Connect");

    private ClientNetwork clientNetwork;

    private ClientNetworkListener clientListener;
    private ClientNetworkListHandler clientNetworkListHandler;

    public FrameAddServer(ClientNetworkListener clientListener, ClientNetworkListHandler clientNetworkListHandler) {
        this.clientListener = clientListener;
        this.clientNetworkListHandler = clientNetworkListHandler;
        initFrame();
    }

    private void initFrame() {
        setBounds(POS_X, POS_Y, WITH, HEIGHT);
        setResizable(false);

        panelServerHost.add(labelServerHost);
        panelServerHost.add(serverHost);
        panelServerPort.add(labelServerPort);
        panelServerPort.add(serverPort);
        panelBottom.add(panelServerHost);
        panelBottom.add(panelServerPort);
        panelBottom.add(btnConnect);

        btnConnect.addActionListener(this);
        add(panelBottom);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        clientNetwork = new StorageClientNetwork(serverHost.getText(), Integer.parseInt(serverPort.getText()), clientNetworkListHandler);
        clientListener.clientOnConnect(clientNetwork);
        dispose();
    }

    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {
        throwable.printStackTrace();
    }

}


