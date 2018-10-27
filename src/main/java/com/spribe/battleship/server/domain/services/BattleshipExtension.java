package com.spribe.battleship.server.domain.services;

import com.smartfoxserver.v2.core.SFSEventType;
import com.smartfoxserver.v2.extensions.SFSExtension;
import com.spribe.battleship.server.domain.services.impl.*;

public class BattleshipExtension  extends SFSExtension {

    @Override
    public void init() {
        addRequestHandler("addUserToLobby", UserJoinedLobbyServiceImpl.class);
        addRequestHandler("removeUserFromLobby", UserExitLobbyServiceImpl.class);
        addRequestHandler("createRoom", CreateRoomServiceImpl.class);
        addRequestHandler("joinRoom", UserJoinedRoomServiceImpl.class);
        addRequestHandler("updateReadyPlayersCount", RPCountUpdServiceImpl.class);
        addRequestHandler("updateGridSize", GridSizeUpdServiceImpl.class);
        addRequestHandler("createNewGame", CreateGameServiceImpl.class);

        addEventHandler(SFSEventType.USER_DISCONNECT, UserDisconnectServiceImpl.class);
    }

}
