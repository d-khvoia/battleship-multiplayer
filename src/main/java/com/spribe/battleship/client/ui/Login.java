package com.spribe.battleship.client.ui;

import sfs2x.client.SmartFox;
import sfs2x.client.requests.LoginRequest;

import javax.swing.*;

public class Login {

    private JPanel mainPanel;
    private JLabel headerText;
    private JLabel loginLabel;
    private JLabel passwordLabel;
    private JTextField loginTextField;
    private JButton loginButton;
    private JPasswordField passwordTextField;

    public Login(SmartFox sfs) {
        loginButton.addActionListener(e -> sfs.send(new LoginRequest(loginTextField.getText(),
                passwordTextField.getPassword().toString(), sfs.getCurrentZone())));
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

}
