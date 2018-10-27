package com.spribe.battleship.server.dto;

import com.spribe.battleship.client.models.Ship;
import com.spribe.battleship.client.models.Square;

import java.util.ArrayList;
import java.util.List;

public class GridDTO {

    private boolean isRevealed;
    private Square[][] squares;
    private Boolean[][] squareStates;
    private List ships;

    public GridDTO(Square[][] squares, int numberOfShips) {
        this.squares = squares;

        squareStates = new Boolean[squares.length][squares.length];

        ships = new ArrayList(numberOfShips);
    }

    public void fire(int x, int y) {
        squareStates[x][y] = Boolean.FALSE;

        updateGrid(notifyAllShips(squares[x][y]));

        if (ships.isEmpty()) {
            isRevealed = true; //End of the game
        }
    }

    public int notifyAllShips(Square hitSquare) {
        Ship ship;
        for (int i = 0; i < ships.size(); i++) {
            ship = (Ship) ships.get(i);
            if (ship.update(hitSquare)) {
                if (ship.isSunken) {
                    return i;
                }
                break;
            }
        }
        return -1;
    }

    public boolean addShip(Ship candidateShip) {
        Ship ship;
        for (int i = 0; i < ships.size(); i++) {
            ship = (Ship) ships.get(i);
            if (hasDecksAsAdjacentSquares(ship, candidateShip)) {
                return false;
            }
        }
        ships.add(candidateShip);
        return true;
    }

    public void removeShip(Ship ship) {
        ships.remove(ship);
    }

    public boolean getStatus() {
        return isRevealed;
    }

    public List getShips() {
        return ships;
    }

    public Square[][] getSquares() {
        return squares;
    }

    public Boolean[][] getSquareStates() {
        return squareStates;
    }

    public void setStatus(boolean isRevealed) {
        this.isRevealed = isRevealed;
    }

    public void setSquares(Square[][] squares) {
        this.squares = squares;
    }

    public void setSquareStates(Boolean[][] squareStates) {
        this.squareStates = squareStates;
    }

    public void setShips(Ship[] ships) {
        this.ships = new ArrayList(ships.length);

        for (int i = 0; i < ships.length; i++) {
            this.ships.add(ships[i]);
        }
    }

    private void updateGrid(int shipIndex) {
        if (shipIndex != -1) {
            Ship ship = (Ship) ships.get(shipIndex);
            Square[] adjacentSquares = ship.getAdjacentSquares();
            for (int i = 0; i < adjacentSquares.length; i++) {
                squareStates[adjacentSquares[i].getX()][adjacentSquares[i].getY()] = Boolean.FALSE;
            }
            ships.remove(shipIndex);
        }
    }

    private boolean hasDecksAsAdjacentSquares(Ship ship, Ship candidateShip) {
        Square square;
        for (int i = 0; i < ship.getDecks().size(); i++) {
            square = (Square) ship.getDecks().get(i);
            for (int j = 0; j < candidateShip.getAdjacentSquares().length; j++) {
                if (square.equals(candidateShip.getAdjacentSquares()[j])) {
                    return true;
                }
            }
        }
        return false;
    }
}
