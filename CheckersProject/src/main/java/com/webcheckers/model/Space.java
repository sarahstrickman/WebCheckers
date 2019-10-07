package com.webcheckers.model;

public class Space {

    private final int x;
    private final int y;
    private final SpaceColor color;

    private Piece piece;

    public Space(int x, int y, SpaceColor color) {
        this.x = x;
        this.y = y;
        this.color = color;
    }

    public int getX() { return x;}

    public int getY() { return y; }

    public SpaceColor getColor()
    { return color; }

    public boolean isValid()
    {
        return color == SpaceColor.BLACK && piece == null;
    }

    public void setPiece(Piece piece)
    {
        this.piece = piece;
    }

    public Piece getPiece()
    {
        return piece;
    }

    /**
     * I was annoyed that I had to write .getPiece().getColor() all the time
     * So I made this
     * @return -> Returns the piece color
     */
    public PieceColor getPieceColor()
    {
        return piece != null ? piece.getColor() : PieceColor.NOCOLOR;
    }
}
