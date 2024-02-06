package ru.lexp00.storage.cloud.server.gui;

import ru.lexp00.storage.cloud.server.core.Server;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ServerGUI extends JFrame implements Thread.UncaughtExceptionHandler, ActionListener {
    private final int POS_X = 100;
    private final int POS_Y = 50;
    private final int WITH = 800;
    private final int HEIGHT = 600;

    private final JPanel panelBottom = new JPanel(new BorderLayout());//общая панель (разметка экрана)
    private final JPanel top = new JPanel(new GridLayout(2, 1));//верхняя панель
    private final JPanel panelInfoServer = new JPanel(new GridLayout(1, 3)); //панель с информацией по серверу - наименование, порт, статус

    private final JPanel panelStartStop = new JPanel(new GridLayout(1, 2));//панель включения/отключения сервера
    private JButton buttonStart = new JButton("Start");
    private JButton buttonStop = new JButton("Stop");


    private final JPanel topServerName = new JPanel(new GridLayout(2, 1));//панель имени сервера
    private final JPanel topServerIP = new JPanel(new GridLayout(2, 1));//панель ip сервера
    private JLabel serverLabelName = new JLabel("Server name: ");
    private JLabel serverName = new JLabel("localhost");
    private JLabel serverPortLabel = new JLabel("Server Port: ");
    private JLabel serverPort = new JLabel("8189");

    private JPanel panelStatusServer = new JPanel(new GridLayout(2, 1));//панель статуса сервера
    private JLabel serverStatusLabel = new JLabel("Status: ");
    private JLabel serverStatus = new JLabel("Stopped");

    private JMenuBar menuBar = new JMenuBar(); //общее меню
    private JMenu menuFile = new JMenu("File"); //меню File
    private JMenuItem menuItemNew = new JMenuItem("New server"); //кнопка меню файл
    private JMenuItem menuItemExit = new JMenuItem("Exit");//кнопка меню файль

    private JTextArea log = new JTextArea();

    private JLabel production = new JLabel("Production is Alexey Prikhodko");

    private Server server = new Server(serverName.getText(), Integer.parseInt(serverPort.getText()));

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

        menuFile.add(menuItemNew);
        menuFile.add(menuItemExit);
        menuBar.add(menuFile);

        menuItemNew.addActionListener(this);
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
        if (event == menuItemExit) {
            dispose();
            System.out.println("Программа завершена");
        } else if (event == menuItemNew) {

            String str = "Вы создали новый сервер";
            System.out.println(str);
            putLog(str);
        } else if (event == buttonStart) {
            new Thread(()-> {
                    try {
                        server.run();
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                }).start();
            putLog("Сервер " + serverName.getText() + " запустился");
        } else if (event == buttonStop) {
            server.stop();
            putLog("Сервер остановился");
        } else {
            putLog("Обработай событие, ты про него забыл");
            throw new RuntimeException("Обработай событие, ты про него забыл");
        }
    }

    public void putLog(String msg) {
        log.append(msg + "\n");
        log.setCaretPosition(log.getDocument().getLength());
    }
}
