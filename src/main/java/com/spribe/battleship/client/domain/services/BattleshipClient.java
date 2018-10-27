package com.spribe.battleship.client.domain.services;

import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;
import com.smartfoxserver.v2.exceptions.SFSException;
import com.spribe.battleship.client.ui.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sfs2x.client.SmartFox;
import sfs2x.client.core.BaseEvent;
import sfs2x.client.core.IEventListener;
import sfs2x.client.core.SFSEvent;
import sfs2x.client.entities.Room;
import sfs2x.client.requests.ExtensionRequest;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class BattleshipClient implements IEventListener {

    private JFrame battleshipGUI = new JFrame("Battleship");

    private Connection connection;
    private Login login;
    private Lobby lobby;
    private PreGame preGame;
    private GridSetup gridSetup;
    private BattleshipGame game;

    private final SmartFox sfs;

    private final Logger log = LoggerFactory.getLogger(getClass());

    public BattleshipClient() {
        sfs = new SmartFox();

        sfs.addEventListener(SFSEvent.CONNECTION, this);
        sfs.addEventListener(SFSEvent.CONNECTION_LOST, this);
        sfs.addEventListener(SFSEvent.LOGIN, this);
        sfs.addEventListener(SFSEvent.LOGIN_ERROR, this);
        sfs.addEventListener(SFSEvent.EXTENSION_RESPONSE, this);

        connection = new Connection(sfs);

        battleshipGUI.setContentPane(connection.getMainPanel());
        battleshipGUI.pack();

        battleshipGUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        battleshipGUI.setVisible(true);
    }

    public static void main(String[] args) {
        new BattleshipClient();
    }

    public void dispatch(BaseEvent evt) throws SFSException {
        if (evt.getType().equals(SFSEvent.CONNECTION)) {
            connect();
        } else if (evt.getType().equals(SFSEvent.CONNECTION_LOST)) {
            onConnectionLost();
        } else if (evt.getType().equals(SFSEvent.LOGIN)) {
            onLogin();
        } else if (evt.getType().equals(SFSEvent.LOGIN_ERROR)) {
            onLoginError(evt);
        } else if (evt.getArguments().get("cmd").equals("addUserToLobby")) {
            updateLobbyUserList();
            updateLobbyRoomList();
        } else if (evt.getArguments().get("cmd").equals("removeUserFromLobby")) {
            updateLobbyUserList();
            updateLobbyRoomList();
        } else if (evt.getArguments().get("cmd").equals("createRoom")) {
            createRoom(evt);
        } else if (evt.getArguments().get("cmd").equals("joinRoom")) {
            joinRoom(evt);
        } else if (evt.getArguments().get("cmd").equals("updateReadyPlayersCount")) {
            updateReadyPlayersCount(evt);
        } else if (evt.getArguments().get("cmd").equals("updateGridSize")) {
            updateGridSize(evt);
        } else if (evt.getArguments().get("cmd").equals("createNewGame")) {
            createNewGame();
        }
    }

    public SmartFox getSfs() {
        return sfs;
    }

    public Logger getLog() {
        return log;
    }

    public void connect() {
        log.info("Connection success: " + sfs.getConnectionMode());

        login = new Login(sfs);

        battleshipGUI.setContentPane(login.getMainPanel());
        battleshipGUI.pack();
    }

    public void onConnectionLost() {
        log.info("Connection was closed");

        JOptionPane.showMessageDialog(battleshipGUI, "Connection was closed");

        battleshipGUI.setContentPane(connection.getMainPanel());
        battleshipGUI.pack();
    }

    public void onLogin() {
        log.info("Logged in as: " + sfs.getMySelf().getName());

        JOptionPane.showMessageDialog(battleshipGUI, "Logged in as: " + sfs.getMySelf().getName());

        sfs.send(new ExtensionRequest("addUserToLobby", null));

        lobby = new Lobby(sfs);

        lobby.getHeaderText().setText("Welcome to the Lobby, " + sfs.getMySelf().getName());

        battleshipGUI.setContentPane(lobby.getMainPanel());
        battleshipGUI.pack();
    }

    public void onLoginError(BaseEvent evt) {
        log.warn("Login error:  " + evt.getArguments().get("errorMessage"));

        JOptionPane.showMessageDialog(battleshipGUI, "Login error:  " + evt.getArguments().get("errorMessage"));
    }

    public void createRoom(BaseEvent evt) {
        ISFSObject response = (SFSObject) evt.getArguments().get("params");

        if (response.size() != 0) {

            preGame = new PreGame(sfs, true);

            preGame.getPlayersTextLabel().setText("Players in the room: " + sfs.getMySelf().getName());

            preGame.getReadyPlayersCountLabel().setText("0");

            preGame.getGridSizeTextField().setText("10");

            battleshipGUI.setContentPane(preGame.getMainPanel());
            battleshipGUI.pack();
        } else {
            updateLobbyRoomList();
        }
    }

    public void joinRoom(BaseEvent evt) {
        ISFSObject response = (SFSObject) evt.getArguments().get("params");

        Room room = sfs.getRoomByName(response.getUtfString("name"));

        if (response.getUtfString("playerName").equals(sfs.getMySelf().getName())) {
            preGame = new PreGame(sfs, false);

            preGame.getPlayersTextLabel().setText(room.getVariable("playersNames").getStringValue());

            preGame.getReadyPlayersCountLabel().setText(room.getVariable("readyPlayersCount").getIntValue().toString());

            preGame.getGridSizeTextField().setText(room.getVariable("gridSize").getIntValue().toString());

            battleshipGUI.setContentPane(preGame.getMainPanel());
            battleshipGUI.pack();
        } else {
            preGame.getPlayersTextLabel().setText(room.getVariable("playersNames").getStringValue());
        }
    }

    public void updateReadyPlayersCount(BaseEvent evt) {
        ISFSObject response = (SFSObject) evt.getArguments().get("params");

        Room room = sfs.getRoomByName(response.getUtfString("roomName"));

        Integer readyPlayersCount = room.getVariable("readyPlayersCount").getIntValue();

        preGame.getReadyPlayersCountLabel().setText(readyPlayersCount.toString());

        if (readyPlayersCount == 2) {
            preGame.getNewGameButton().setEnabled(true);
        }
    }

    public void updateGridSize(BaseEvent evt) {

        if (!preGame.getGridSizeTextField().isEditable()) {
            ISFSObject response = (SFSObject) evt.getArguments().get("params");

            Room room = sfs.getRoomByName(response.getUtfString("roomName"));

            preGame.getGridSizeTextField().setText(room.getVariable("gridSize").getIntValue().toString());
        }
    }

    public void createNewGame() {
        gridSetup = new GridSetup(sfs);

        battleshipGUI.setContentPane(gridSetup.getMainPanel());
        battleshipGUI.pack();
    }

    public void updateLobbyUserList() {
        ISFSObject lobbyUserList = sfs.getRoomByName("The Lobby").getVariable("userList").getSFSObjectValue();

        List<String> lobbyUserNames = new ArrayList<>(lobbyUserList.getUtfStringArray("userList"));

        lobby.getUserList().setModel(new DefaultListModel<String>());

        DefaultListModel<String> userListModel = (DefaultListModel<String>) lobby.getUserList().getModel();

        for (String userName : lobbyUserNames) {
            userListModel.addElement(userName);
        }
    }

    public void updateLobbyRoomList() {
        ISFSObject lobbyRoomList = sfs.getRoomByName("The Lobby").getVariable("roomList").getSFSObjectValue();

        List<String> lobbyRoomNames = new ArrayList<>(lobbyRoomList.getUtfStringArray("roomList"));

        lobby.getRoomList().setModel(new DefaultListModel<String>());

        DefaultListModel<String> roomListModel = (DefaultListModel<String>) lobby.getRoomList().getModel();

        for (String roomName : lobbyRoomNames) {
            roomListModel.addElement(roomName);
        }
    }
}
