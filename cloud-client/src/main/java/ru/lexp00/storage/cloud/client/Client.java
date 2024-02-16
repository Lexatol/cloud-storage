package ru.lexp00.storage.cloud.client;

import ru.lexp00.storage.cloud.client.gui.StorageClientGUI;

import javax.swing.*;

public class Client {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(StorageClientGUI::new);
    }
}
