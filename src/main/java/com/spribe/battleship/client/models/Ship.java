package com.spribe.battleship.client.models;

import com.spribe.battleship.client.dto.GridDTO;

import java.util.ArrayList;
import java.util.List;

public abstract class Ship {

    protected int deckCount;

    protected Square[] adjacentSquares;
    protected List<Square> decks;
    protected GridDTO gridDTO;

    public boolean isSunken;

    public Ship(Square[] decks, int numberOfDecks) throws IllegalArgumentException {
        if (decks.length != numberOfDecks) throw new IllegalArgumentException();

        this.decks = new ArrayList<>(decks.length);
        this.decks.add(decks[0]);

        for (int i = 0; i < decks.length - 1; i++) {
            if (!((decks[i + 1].getX() - decks[0].getX() < decks.length)
                    && (decks[i + 1].getY() - decks[0].getY() < decks.length)
                    && (decks[i + 1].getX() == decks[i].getX() ^ decks[i + 1].getY() == decks[i].getY()))) {

                throw new IllegalArgumentException();
            }
            this.decks.add(decks[i + 1]);
        }

        deckCount = this.decks.size();
        adjacentSquares = getAdjacentSquares();
    }

    @SuppressWarnings("Duplicates")
    public boolean update(Square hitSquare) {
        for (Square deck : decks) {
            if (deck.getX() == hitSquare.getX() && deck.getY() == hitSquare.getY()) {
                gridDTO.getSquareStates()[hitSquare.getX()][hitSquare.getY()] = Boolean.TRUE;
                if (--deckCount == 0) {
                    isSunken = true;
                }
                return true;
            }
        }
        return false;
    }

    public boolean getStatus() {
        return isSunken;
    }

    public List<Square> getDecks() {
        return decks;
    }

    public void setStatus(boolean isDrown) {
        this.isSunken = isDrown;
    }

    public void setDecks(Square[] decks) {
        this.decks = new ArrayList(decks.length);

        for (int i = 0; i < decks.length; i++) {
            this.decks.add(decks[i]);
        }
    }

    public void setAdjacentSquares(Square[] adjacentSquares) {
        this.adjacentSquares = adjacentSquares;
    }

    @SuppressWarnings("Duplicates")
    public Square[] getAdjacentSquares() {
        List<Square> adjacentSquares = new ArrayList<>();
        for (Square deck : decks) {
            adjacentSquares.add(new Square(deck.getX() - 1, deck.getY()));
            adjacentSquares.add(new Square(deck.getX() + 1, deck.getY()));
            adjacentSquares.add(new Square(deck.getX(), deck.getY() - 1));
            adjacentSquares.add(new Square(deck.getX(), deck.getY() + 1));
            adjacentSquares.add(new Square(deck.getX() + 1, deck.getY() + 1));
            adjacentSquares.add(new Square(deck.getX() + 1, deck.getY() - 1));
            adjacentSquares.add(new Square(deck.getX() - 1, deck.getY() - 1));
            adjacentSquares.add(new Square(deck.getX() - 1, deck.getY() + 1));
        }
        return filterAdjacentSquares(adjacentSquares);
    }

    private Square[] filterAdjacentSquares(List<Square> adjacentSquares) {
        for (int i = 0; i < adjacentSquares.size();) {
            if (adjacentSquares.get(i).getX() < 0 || adjacentSquares.get(i).getY() < 0 || isADeck(adjacentSquares.get(i))) {
                adjacentSquares.remove(i);
            } else {
                i++;
            }
        }

        return adjacentSquares.toArray(new Square[adjacentSquares.size()]);
    }

    private boolean isADeck(Square candidateSquare) {
        for (Square square : decks) {
            if (square.equals(candidateSquare)) {
                return true;
            }
        }
        return false;
    }
}
