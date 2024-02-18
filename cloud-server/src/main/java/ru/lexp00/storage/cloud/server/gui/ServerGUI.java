package ru.lexp00.storage.cloud.server.gui;

import ru.lexp00.storage.cloud.network.server.ServerListener;
import ru.lexp00.storage.cloud.server.core.Server;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ServerGUI extends JFrame implements Thread.UncaughtExceptionHandler, ActionListener, ServerListener {
    private final int POS_X = 100;
    private final int POS_Y = 50;
    private final int WITH = 800;
    private final int HEIGHT = 600;

    private final JPanel panelBottom = new JPanel(new BorderLayout());
    private final JPanel top = new JPanel(new GridLayout(2, 1));
    private final JPanel panelInfoServer = new JPanel(new GridLayout(1, 3));

    private final JPanel panelStartStop = new JPanel(new GridLayout(1, 2));
    private final JButton buttonStart = new JButton("Start");
    private final JButton buttonStop = new JButton("Stop");


    private final JPanel topServerName = new JPanel(new GridLayout(2, 1));
    private final JPanel topServerIP = new JPanel(new GridLayout(2, 1));
    private final JLabel serverLabelName = new JLabel("Server name: ");
    private final JLabel serverName = new JLabel("localhost");
    private final JLabel serverPortLabel = new JLabel("Server Port: ");
    private final JLabel serverPort = new JLabel("8189");

    private final JPanel panelStatusServer = new JPanel(new GridLayout(2, 1));
    private final JLabel serverStatusLabel = new JLabel("Status: ");
    private final JLabel serverStatus = new JLabel("Stopped");

    private final JMenuBar menuBar = new JMenuBar();
    private final JMenu menuFile = new JMenu("File");
    private final JMenuItem menuItemExit = new JMenuItem("Exit");

    private final JTextArea log = new JTextArea();

    private final JLabel production = new JLabel("Production is Alexey Prikhodko");

    private final Server server = new Server(serverName.getText(), Integer.parseInt(serverPort.getText()), this);

    public ServerGUI() {
        initFrame();
    }

    private void initFrame() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        setBounds(POS_X, POS_Y, WITH, HEIGHT);
        setTitle("Server: " + serverName.getText() + ", IP: " + serverPort.getText());

        panelStartStop.add(buttonStart);
        panelStartStop.add(buttonStop);

        buttonStop.addActionListener(this);
        buttonStart.addActionListener(this);

        panelStatusServer.add(serverStatusLabel);
        panelStatusServer.add(serverStatus);

        topServerName.add(serverLabelName);
        topServerName.add(serverName);
        topServerIP.add(serverPortLabel);
        topServerIP.add(serverPort);

        panelInfoServer.add(topServerName);
        panelInfoServer.add(topServerIP);
        panelInfoServer.add(panelStatusServer);
        panelInfoServer.add(panelStartStop);

        menuFile.add(menuItemExit);
        menuBar.add(menuFile);

        menuItemExit.addActionListener(this);

        log.setEditable(false);
        log.setLineWrap(true);
        JScrollPane scrollLog = new JScrollPane(log);


        top.add(menuBar);
        top.add(panelInfoServer);
        panelBottom.add(top, BorderLayout.NORTH);
        panelBottom.add(scrollLog, BorderLayout.CENTER);
        panelBottom.add(production, BorderLayout.SOUTH);

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
        if (event == menuItemExit || event == buttonStop) {
            server.stop();
            dispose();
        } else if (event == buttonStart) {
            new Thread(()-> {
                    try {
                        server.run();
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                }).start();
        } else {
            throw new RuntimeException("Обработай событие, ты про него забыл");
        }
    }

    public void putLog(String msg) {
        log.append(msg + "\n");
        log.setCaretPosition(log.getDocument().getLength());
    }

    @Override
    public void onServerRequest(String msg) {
        putLog(msg);
    }

    @Override
    public void onServerException(String cause) {
        JOptionPane.showMessageDialog(null, cause, "Exception", JOptionPane.ERROR_MESSAGE);
        putLog(cause);

    }

    @Override
    public void onServerStopped(String msg) {
        putLog(msg);
    }

    @Override
    public void onServerStarted(String msg) {
        putLog("Сервер " + serverName.getText() + " запустился");

    }
}
