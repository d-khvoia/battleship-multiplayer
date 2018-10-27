package com.spribe.battleship.client.ui;

import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;
import sfs2x.client.SmartFox;
import sfs2x.client.requests.ExtensionRequest;

import javax.swing.*;

public class Lobby {

    private JPanel mainPanel;
    private JLabel headerText;
    private JList userList;
    private JList roomList;
    private JButton createRoomButton;
    private JButton joinRoomButton;
    private JScrollBar roomScrollBar;
    private JScrollBar userScrollBar;


    public Lobby(SmartFox sfs) {
        createRoomButton.addActionListener(e -> {
            String roomName = (String) JOptionPane.showInputDialog(null, "Enter room name:",
                    "Customized Dialog", JOptionPane.PLAIN_MESSAGE, null, null,
                    sfs.getMySelf().getName() + "'s BattleshipGame Room");

            ISFSObject params = new SFSObject();

            params.putUtfString("name", roomName);

            sfs.send(new ExtensionRequest("removeUserFromLobby", null));

            sfs.send(new ExtensionRequest("createRoom", params));
        });

        joinRoomButton.addActionListener(e -> {
            ISFSObject params = new SFSObject();

            params.putUtfString("name", roomList.getSelectedValue().toString());

            sfs.send(new ExtensionRequest("removeUserFromLobby", null));

            sfs.send(new ExtensionRequest("joinRoom", params));
        });
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public JList getUserList() {
        return userList;
    }

    public JList getRoomList() {
        return roomList;
    }

    public JLabel getHeaderText() {
        return headerText;
    }

}
