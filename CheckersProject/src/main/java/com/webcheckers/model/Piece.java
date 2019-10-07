package com.webcheckers.model;

enum Type
{
    SINGLE, KING;
}


public class Piece {

    private final PieceColor color;
    private Type type = Type.SINGLE;

    public Piece(PieceColor color) {
        this.color = color;
    }

    public PieceColor getColor() {
        return color;
    }

    public Type getType() {
        return type;
    }

    public boolean isKing()
    {
        return this.type == Type.KING;
    }

    public void setKing()
    {
        type = Type.KING;
    }
}
