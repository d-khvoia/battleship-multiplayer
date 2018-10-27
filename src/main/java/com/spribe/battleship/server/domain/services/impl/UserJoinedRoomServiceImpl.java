package com.spribe.battleship.server.domain.services.impl;

import com.smartfoxserver.v2.entities.Room;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.variables.SFSRoomVariable;
import com.smartfoxserver.v2.exceptions.SFSJoinRoomException;
import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;
import com.spribe.battleship.server.api.UserJoinedRoomService;

import java.util.Arrays;

public class UserJoinedRoomServiceImpl extends BaseClientRequestHandler implements UserJoinedRoomService {

    @Override
    public void handleClientRequest(User sender, ISFSObject params) {
        Room room = sender.getZone().getRoomByName(params.getUtfString("name"));

        try {
            room.addUser(sender);

            sender.addJoinedRoom(room);

            String playersNames = room.getVariable("playersNames").getStringValue() + ", " + sender.getName();

            getApi().setRoomVariables(room.getOwner(), room,
                     Arrays.asList(room.getVariable("gridSize"), room.getVariable("readyPlayersCount"),
                             room.getVariable("isGameStarted"), new SFSRoomVariable("playersNames", playersNames)));

            params.putUtfString("playerName", sender.getName());

            send("joinRoom", params, room.getUserList());
        } catch (SFSJoinRoomException e) {
            e.printStackTrace();
        }
    }
}
