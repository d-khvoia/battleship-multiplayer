package com.spribe.battleship.client.domain.factories;

import com.spribe.battleship.client.models.*;

public class ShipFactory {

    public Ship getShip(Square[] decks) {
        if (decks == null) {
            return null;
        }
        if (decks.length == 1) {
            return new OneDeckShip(decks);
        }
        if (decks.length == 2) {
            return new TwoDeckShip(decks);
        }
        if (decks.length == 3) {
            return new ThreeDeckShip(decks);
        }
        if (decks.length == 4) {
            return new FourDeckShip(decks);
        }

        return null;
    }
}
