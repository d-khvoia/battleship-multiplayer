package com.spribe.battleship.server.domain.services.impl;

import com.smartfoxserver.v2.entities.Room;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;
import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;
import com.smartfoxserver.v2.entities.variables.SFSRoomVariable;
import com.spribe.battleship.server.api.RPCountUpdService;

import java.util.Arrays;

public class RPCountUpdServiceImpl extends BaseClientRequestHandler implements RPCountUpdService {

    @Override
    public void handleClientRequest(User sender, ISFSObject params) {
        Room room = sender.getLastJoinedRoom();

        getApi().setRoomVariables(room.getOwner(), room, Arrays.asList(new SFSRoomVariable("readyPlayersCount",
                room.getVariable("readyPlayersCount").getIntValue().byteValue() + 1)));

        ISFSObject response = new SFSObject();

        response.putUtfString("roomName", room.getName());

        send("updateReadyPlayersCount", response, room.getUserList());
    }
}
