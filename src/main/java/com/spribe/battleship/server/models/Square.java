package com.spribe.battleship.server.models;

public class Square {

    private int x, y;

    public Square(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public boolean equals(Object obj) throws ClassCastException {
        Square square = (Square) obj;
        return x == square.getX() && y == square.getY();
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }
}
