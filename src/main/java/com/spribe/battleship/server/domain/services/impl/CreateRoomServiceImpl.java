package com.spribe.battleship.server.domain.services.impl;

import com.smartfoxserver.v2.api.CreateRoomSettings;
import com.smartfoxserver.v2.entities.*;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;
import com.smartfoxserver.v2.entities.variables.RoomVariable;
import com.smartfoxserver.v2.entities.variables.SFSRoomVariable;
import com.smartfoxserver.v2.exceptions.SFSCreateRoomException;
import com.smartfoxserver.v2.exceptions.SFSJoinRoomException;
import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;
import com.spribe.battleship.server.api.CreateRoomService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CreateRoomServiceImpl extends BaseClientRequestHandler implements CreateRoomService {

    @Override
    public void handleClientRequest(User sender, ISFSObject params) {
        CreateRoomSettings cfg = new CreateRoomSettings();

        cfg.setName(params.getUtfString("name"));
        cfg.setGame(true);
        cfg.setMaxUsers(2);
        cfg.setDynamic(true);

        RoomVariable gridSize = new SFSRoomVariable("gridSize", 10);
        RoomVariable readyPlayersCount = new SFSRoomVariable("readyPlayersCount", 0);
        RoomVariable isGameStarted = new SFSRoomVariable("isGameStarted", false);
        RoomVariable playersNames = new SFSRoomVariable("playersNames",
                "Players in the room: " + sender.getName());

        try {
            Room newRoom = getApi().createRoom(sender.getZone(), cfg, sender);

            getApi().setRoomVariables(sender, newRoom,
                    Arrays.asList(gridSize, readyPlayersCount, isGameStarted, playersNames));

            newRoom.addUser(sender);

            sender.addJoinedRoom(newRoom);

            updateLobbyUserList(sender);

            updateLobbyRoomList(sender);

            send("createRoom", params, sender);

            send("createRoom", null, sender.getZone().getRoomByName("The Lobby").getUserList());

        } catch (SFSCreateRoomException e) {

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
