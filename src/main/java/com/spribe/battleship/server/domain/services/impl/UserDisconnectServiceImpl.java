package com.spribe.battleship.server.domain.services.impl;

import com.smartfoxserver.v2.core.ISFSEvent;
import com.smartfoxserver.v2.core.SFSEventParam;
import com.smartfoxserver.v2.entities.Room;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;
import com.smartfoxserver.v2.entities.variables.SFSRoomVariable;
import com.smartfoxserver.v2.exceptions.SFSException;
import com.smartfoxserver.v2.extensions.BaseServerEventHandler;
import com.spribe.battleship.server.api.UserDisconnectService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UserDisconnectServiceImpl extends BaseServerEventHandler implements UserDisconnectService {

    @Override
    public void handleServerEvent(ISFSEvent isfsEvent) throws SFSException {
        User disconnectedUser = (User) isfsEvent.getParameter(SFSEventParam.USER);

        updateLobbyUserList(disconnectedUser);

        updateLobbyRoomList(disconnectedUser);

        send("removeUserFromLobby", null,
                disconnectedUser.getZone().getRoomByName("The Lobby").getUserList());
    }

    @SuppressWarnings("Duplicates")
    private void updateLobbyUserList(User disconnectedUser) {
        Room lobby = disconnectedUser.getZone().getRoomByName("The Lobby");

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
    private void updateLobbyRoomList(User disconnectedUser) {
        List<Room> lobbyRoomList = disconnectedUser.getZone().getRoomList();

        List<String> lobbyRoomNames = new ArrayList<>();

        for (Room room : lobbyRoomList) {
            lobbyRoomNames.add(room.getName());
        }

        ISFSObject roomNames = new SFSObject();

        roomNames.putUtfStringArray("roomList", lobbyRoomNames);

        getApi().setRoomVariables(null, disconnectedUser.getZone().getRoomByName("The Lobby"),
                Arrays.asList(new SFSRoomVariable("roomList", roomNames)));
    }
}
