package com.spribe.battleship.client.ui;

import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;
import sfs2x.client.SmartFox;
import sfs2x.client.requests.ExtensionRequest;

import javax.swing.*;

public class PreGame {

    private JPanel mainPanel;
    private JTextField gridSizeTextField;
    private JButton readyButton;
    private JLabel headerText;
    private JLabel gridSizeLabel;
    private JLabel playersReadyLabel;
    private JLabel readyPlayersCountLabel;
    private JLabel maxPlayersLabel;
    private JButton newGameButton;
    private JLabel playersTextLabel;

    public PreGame(SmartFox sfs, boolean isGameCreator) {

        gridSizeTextField.setEditable(isGameCreator);
        newGameButton.setVisible(isGameCreator);
        newGameButton.setEnabled(false);

        readyButton.addActionListener(e -> {

            readyButton.setVisible(false);

            gridSizeTextField.setEditable(false);

            sfs.send(new ExtensionRequest("updateReadyPlayersCount", null));
        });

        newGameButton.addActionListener(e -> {
            sfs.send(new ExtensionRequest("createNewGame", null));
        });

        gridSizeTextField.addActionListener(e -> {
            ISFSObject params = new SFSObject();

            params.putByte("gridSize", Byte.parseByte(gridSizeTextField.getText()));

            sfs.send(new ExtensionRequest("updateGridSize", params));
        });
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public JTextField getGridSizeTextField() {
        return gridSizeTextField;
    }

    public JLabel getReadyPlayersCountLabel() {
        return readyPlayersCountLabel;
    }

    public JLabel getPlayersTextLabel() {
        return playersTextLabel;
    }

    public JButton getNewGameButton() {
        return newGameButton;
    }
}
