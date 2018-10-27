package com.spribe.battleship.server.domain.services.impl;

import com.smartfoxserver.v2.entities.Room;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;
import com.smartfoxserver.v2.entities.variables.SFSRoomVariable;
import com.smartfoxserver.v2.exceptions.SFSJoinRoomException;
import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;
import com.spribe.battleship.server.api.UserJoinedLobbyService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UserJoinedLobbyServiceImpl extends BaseClientRequestHandler implements UserJoinedLobbyService {

    @Override
    public void handleClientRequest(User sender, ISFSObject params) {
        Room lobby = sender.getZone().getRoomByName("The Lobby");

        try {
            lobby.addUser(sender);

            sender.addJoinedRoom(lobby);

            updateLobbyUserList(sender);

            updateLobbyRoomList(sender);

            send("addUserToLobby", null, lobby.getUserList());
        } catch (SFSJoinRoomException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("Duplicates")
    private void updateLobbyUserList(User sender) {
        Room lobby = sender.getZone().getRoomByName("The Lobby");

        List<User> lobbyUserList = lobby.getUserList();

        List<String> lobbyUserNames = new ArrayList<>();

        for (User user : lobbyUserList) {
            lobbyUserNames.add(user.getName());
        }

        ISFSObject userNames = new SFSObject();

        userNames.putUtfStringArray("userList", lobbyUserNames);

        getApi().setRoomVariables(null, lobby, Arrays.asList(new SFSRoomVariable("userList", userNames)));
    }

    @SuppressWarnings("Duplicates")
    private void updateLobbyRoomList(User sender) {
        List<Room> lobbyRoomList = sender.getZone().getRoomList();

        List<String> lobbyRoomNames = new ArrayList<>();

        for (Room room : lobbyRoomList) {
            lobbyRoomNames.add(room.getName());
        }

        ISFSObject roomNames = new SFSObject();

        roomNames.putUtfStringArray("roomList", lobbyRoomNames);

        getApi().setRoomVariables(null, sender.getZone().getRoomByName("The Lobby"),
                Arrays.asList(new SFSRoomVariable("roomList", roomNames)));
    }
}
