package com.spribe.battleship.server.domain.services.impl;

import com.smartfoxserver.v2.entities.Room;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;
import com.smartfoxserver.v2.entities.variables.SFSRoomVariable;
import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;
import com.spribe.battleship.server.api.UserExitLobbyService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UserExitLobbyServiceImpl extends BaseClientRequestHandler implements UserExitLobbyService {

    @Override
    public void handleClientRequest(User sender, ISFSObject params) {
        sender.getZone().getRoomByName("The Lobby").removeUser(sender);

        updateLobbyUserList(sender);

        send("removeUserFromLobby", null, sender.getZone().getRoomByName("The Lobby").getUserList());
    }

    @SuppressWarnings("Duplicates")
    private void updateLobbyUserList(User sender) {
        Room lobby = sender.getZone().getRoomByName("The Lobby");

        List<User> lobbyUserList = lobby.getUserList();
        lobbyUserList.remove(sender);

        List<String> lobbyUserNames = new ArrayList<>();

        for (User user : lobbyUserList) {
            lobbyUserNames.add(user.getName());
        }

        ISFSObject userNames = new SFSObject();

        userNames.putUtfStringArray("userList", lobbyUserNames);

        getApi().setRoomVariables(null, lobby, Arrays.asList(new SFSRoomVariable("userList", userNames)));
    }
}
