package com.spribe.battleship.client.ui;

import sfs2x.client.SmartFox;
import sfs2x.client.util.ConfigData;

import javax.swing.*;

public class Connection {

    private static final String host = "localhost";
    private static final String port = "9933";

    private JLabel headerText;
    private JTextField portTextField;
    private JPanel mainPanel;
    private JLabel portLabel;
    private JButton connectButton;
    private JLabel hostLabel;
    private JTextField hostTextField;

    public Connection(SmartFox sfs) {
        connectButton.addActionListener(e -> {
            String host = hostTextField.getText();
            String port = portTextField.getText();

            if (host.equals(this.host) && port.equals(this.port)) {
                ConfigData cfg = new ConfigData();

                cfg.setZone("Battleship");
                cfg.setHost(host);
                cfg.setPort(Integer.parseInt(port));

                sfs.connect(cfg);
            } else {
                JOptionPane.showMessageDialog(mainPanel, "Connection error. Try again!");
            }
        });
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public JTextField getHostTextField() {
        return hostTextField;
    }

    public JTextField getPortTextField() {
        return portTextField;
    }
}
