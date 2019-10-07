package com.webcheckers.model;

import java.util.ArrayList;



public class Board {

    private static final int MAX_PIECES = 12;
    public static final int BOARD_SIZE = 8;
    private ArrayList<ArrayList<Space>> spaces;
    private ArrayList<Piece> redPieces;
    private ArrayList<Piece> whitePieces;

    public Board()
    {
        spaces = new ArrayList<>();
        redPieces = new ArrayList<>();
        whitePieces = new ArrayList<>();

        generateBoard();
        populatePieces();
    }

    public ArrayList<Piece> getRedPieces()
    {
        return redPieces;
    }

    public ArrayList<Piece> getWhitePieces()
    {
        return whitePieces;
    }

    /**
     * IF ACCESSING WITH X,Y, REVERSE THE INPUT
     * ALL PROBLEMS WERE ASSOCIATED WITH THIS
     *
     * otherwise it's fine
     *
     * @param row -> row you're accessing
     * @param col -> column you're accessing
     * @return -> a neat space
     */
    public Space getSpace(int row, int col)
    {
        if(row <= BOARD_SIZE && row >= 0 && col <= BOARD_SIZE && col >= 0)
        {
            return spaces.get(row).get(col);
        }
        return null;
    }

    public ArrayList<ArrayList<Space>> getAllSpaces()
    {
        return spaces;
    }

    public ArrayList<ArrayList<Space>> getAllSpacesReverse()
    {
        ArrayList<ArrayList<Space>> temp = new ArrayList<>();
        for(int i = BOARD_SIZE - 1; i >= 0; i--)
        {
            ArrayList<Space> temp2 = new ArrayList<>();
            for(int j = BOARD_SIZE - 1; j >= 0; j--)
            {
                temp2.add(getSpace(i, j));
            }
            temp.add(temp2);
        }
        return temp;
    }
    /**
     * Generates all of the spaces on the board, does so in a neat, orderly fashion.
     */
    private void generateBoard()
    {
        for(int row = 0; row < BOARD_SIZE; row++)
        {
            spaces.add(new ArrayList<>());
            for(int col = 0; col < BOARD_SIZE; col++)
            {
                spaces.get(row).add(new Space(col, row, (col + row) % 2 == 1 ? SpaceColor.WHITE : SpaceColor.BLACK));
            }
        }
    }

    /**
     * Generates the pieces on the checkerboard.
     * It only places pieces on 'even' spaces (counting from bottom left to top right), ensuring
     * a well-designed checkerboard.
     */
    private void populatePieces()
    {
        for(int row = 0; row < BOARD_SIZE; row++) {
            for(int col = 0; col < BOARD_SIZE; col++) {
                if ((row + col) % 2 == 0) {
                    if (row <= 2) {
                        Piece temp = new Piece(PieceColor.RED);
                        redPieces.add(temp);
                        getSpace(row, col).setPiece(temp);
                    } else if (row >= 5) {
                        Piece temp = new Piece(PieceColor.WHITE);
                        whitePieces.add(temp);
                        getSpace(row, col).setPiece(temp);
                    }
                }
            }
        }
    }

    /**
     * Helper function to get the x-distance between two spaces.
     * Negative values mean space2 is left of space1
     * Positive means they're to the right
     * @param space1 -> The first space, usually the original space
     * @param space2 -> The second space, usually the destination
     * @return -> The difference between the spaces
     */
    public int getDistX(Space space1, Space space2)
    {
        return space2.getX() - space1.getX();
    }

    /**
     * Helper function to get the y-distance between two spaces.
     * Negative values mean space2 is behind space1
     * Positive means they're above.
     * @param space1 -> The first space, usually the original space
     * @param space2 -> The second space, usually the destination
     * @return -> The difference between the spaces
     */
    public int getDistY(Space space1, Space space2)
    {
        return space2.getY() - space1.getY();
    }


    /**
     * Checks to see if
     * A: The move is diagonal
     * B: The move is less than / equal to a jump of 2 spaces
     * C: If the move is forward, from either player's perspective
     * D: If the player is a king, ignore C.
     * @param prev -> Space the player is starting at.
     * @param dest -> Space the player wants to go.
     * @return -> boolean, whether or not the move is valid.
     */
    private boolean validateMove(Space prev, Space dest)
    {
        if (prev.getPiece() == null){return false;}
        int xdif = Math.abs(getDistX(prev, dest));
        int ydif = prev.getPiece().isKing() ? Math.abs(getDistY(prev, dest)) : getDistY(prev, dest);
        if(prev.getPieceColor() == PieceColor.WHITE && !prev.getPiece().isKing()) { ydif = -ydif; }
        return xdif == ydif && ydif <= 2;
    }

    public boolean checkMove(Space prev, Space dest, boolean isCheck)
    {
        if(!validateMove(prev, dest) || dest.getPiece() != null) //If the move isn't diagonal/too big, return false
            return false; //This also returns false if there's a piece in the destination

        if(Math.abs(getDistX(prev, dest)) == 2) //If your distance is 2 (already verified distances are the same in ValidateMove)
        {
            int col = prev.getX() + getDistX(prev, dest) / 2; //col, row
            int row = prev.getY() + getDistY(prev, dest) / 2; //They should be coords to the spot where the piece would be

            if(getSpace(row, col).getPiece() == null || prev.getPiece().getColor() == getSpace(row, col).getPiece().getColor())
                return false; //If there's no piece there or the piece is your piece, return false

            if(isCheck)
                return true;
            jumpPiece(getSpace(row, col)); //Otherwise, jump the pieces
        }
        return true;
    }

    /**
     * Fully checks if a move is valid and then performs the task.
     * Checks using validateMove, and checks to see if there's a piece to remove.
     *
     * I recommend reading it from top to bottom if you're curious how it works.
     * @param prev -> previous space
     * @param dest -> destination space
     * @return -> boolean, whether or not the move was successful
     */
    public boolean move(Space prev, Space dest)
    {

        if(!checkMove(prev, dest ,false))
            return false;

        if( dest.getY() == BOARD_SIZE - 1 && prev.getPiece().getColor() == PieceColor.WHITE
                || dest.getY() == 0 && prev.getPiece().getColor() == PieceColor.RED) {
            prev.getPiece().setKing();
            //If you're at the edge of the board, and you're the correct color, set you to king
        }

        dest.setPiece(prev.getPiece()); //Set the piece at the destination to be equal to the piece you moved
        prev.setPiece(null); //Destroy the old piece
        return true;
    }

    /**
     * Handles the logic for jumping a piece, by searching through the list of pieces and removing the one
     * you desired
     * @param jumped -> the piece you're jumping
     */
    private void jumpPiece(Space jumped)
    {
        Piece sacrifice = jumped.getPiece();
        switch(sacrifice.getColor())
        {
            case RED:
                redPieces.remove(sacrifice);
                break;
            case WHITE:
                whitePieces.remove(sacrifice);
                break;
        }
        jumped.setPiece(null);
    }

    private boolean testValidate(int sx, int sy, int dx, int dy)
    {
        return validateMove(getSpace(sx, sy), getSpace(dx, dy));
    }

    public static void main(String[] args)
    {
        Board testBoard = new Board();
        System.out.println(testBoard.testValidate(3, 2, 4, 3));
        System.out.println(testBoard.testValidate(2, 2, 3, 4));
        System.out.println(testBoard.testValidate(BOARD_SIZE - 1, BOARD_SIZE - 1, BOARD_SIZE - 2, BOARD_SIZE - 2));
        System.out.println(testBoard.testValidate(BOARD_SIZE - 1, BOARD_SIZE - 1, BOARD_SIZE - 3, BOARD_SIZE - 3));


    }
}
