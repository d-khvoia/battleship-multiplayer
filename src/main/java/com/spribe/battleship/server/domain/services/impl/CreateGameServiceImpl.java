package com.spribe.battleship.server.domain.services.impl;

import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.variables.SFSRoomVariable;
import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;
import com.spribe.battleship.server.api.CreateGameService;

import java.util.Arrays;

public class CreateGameServiceImpl extends BaseClientRequestHandler implements CreateGameService {

    @Override
    public void handleClientRequest(User sender, ISFSObject params) {
        getApi().setRoomVariables(sender, sender.getLastJoinedRoom(),
                Arrays.asList(new SFSRoomVariable("isGameStarted", true)));

        send("createNewGame", null, sender.getLastJoinedRoom().getUserList());
    }
}
