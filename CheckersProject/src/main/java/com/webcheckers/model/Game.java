package com.webcheckers.model;

import java.util.ArrayList;
import java.util.Scanner;

public class Game
{
    private Player redPlayer;
    private Player whitePlayer;
    private PieceColor turnColor;
    private Board masterBoard;
    private Board backupBoard;
    //Cloner cloner = new Cloner();

    public Game(Player red, Player white)
    {
        assert red != null : "Red player missing.";
        assert white != null : "White player missing.";

        // The game will grash if you are missing either a white
        // player or a red player!
        this.redPlayer = red;
        redPlayer.assignColor(PieceColor.RED);
        this.whitePlayer = white;
        whitePlayer.assignColor(PieceColor.WHITE);
        this.turnColor = PieceColor.RED;
        this.masterBoard = new Board();

        assert this.masterBoard != null: "Board not created.";
    }

    public Player getWhitePlayer()
    {
        return whitePlayer;
    }

    public Player getRedPlayer()
    {
        return redPlayer;
    }

    public Board getMasterBoard()
    {
        return masterBoard;
    }

    /**
     * Returns the space at (x, y)
     * @param x -> The x position of the space
     * @param y -> The y position of the space
     * @return -> The space on the board at that selected location.
     */
    public Space getSpace(int x, int y)
    {
        return masterBoard.getSpace(y, x);
    }


    public boolean goBack()
    {
        if (backupBoard != null)
        {
            masterBoard = backupBoard;
            return true;
        }
        return false;
    }

    /**
     * Function to make a move on the board. Does all of the poking for you, all you need to do is find out
     * where it's going.
     * @param player -> The player who is making the move
     * @param prev -> The space you're starting at (see getSpace)
     * @param dest -> The space you want to go to (see getSpace)
     * @return -> True if successful, False if not successful.
     */
    public boolean makeMove(Player player, Space prev, Space dest)
    {
        //backupBoard = cloner.deepClone(masterBoard);
        if(player.getColor() != turnColor)
            return false; //If it's not your turn, you can't move.

        if(prev.getPieceColor() != player.getColor())
            return false;  //If it's not your piece, you can't move

        boolean moveOK = masterBoard.move(prev, dest);
        if(moveOK)
        {
            //If your move is ok, lets see if you can make another.
            Space[] temp_arr = { getSpace(dest.getX() + 2, dest.getY() + 2),
                            getSpace(dest.getX() - 2, dest.getY() + 2),
                            getSpace(dest.getX() + 2, dest.getY() - 2),
                            getSpace(dest.getX() - 2, dest.getY() - 2) };
            boolean hasMove = false;
            for(Space space : temp_arr)
            {
                if(space != null && space.getPiece() == null)
                {
                    int cx = prev.getX() + masterBoard.getDistX(dest, space) / 2; //cx, cy = check_x, check_y
                    int cy = prev.getY() + masterBoard.getDistY(dest, space) / 2; //They should be coords to the spot where the piece would be

                    if(getSpace(cx, cy).getPiece() != null && turnColor == getSpace(cx, cy).getPiece().getColor())
                    {
                        hasMove = true;
                        break;
                    }
                }
            }
            if(!hasMove)
                turnColor = turnColor == PieceColor.WHITE ? PieceColor.RED : PieceColor.WHITE;
        }
        return moveOK; //Return the result of the move function in Board.
    }

    public PieceColor getActiveColor()
    {
        return turnColor;
    }

    public static void testBoard()
    {
        Player p1 = new Player("Geoff");
        Player p2 = new Player("Jeff");
        Game testGame = new Game(p1, p2);
        p1.setGame(testGame);
        p2.setGame(testGame);
        System.out.println(testGame);
        Scanner scan = new Scanner(System.in);
        int row = 0, col = 0;
        int nrow = 0, ncol = 0;
        Player current = p1;
        while(true)
        {
            System.out.println("Order of ints: current row/col, new rol/col");
            row = scan.nextInt();
            col = scan.nextInt();
            nrow = scan.nextInt();
            ncol = scan.nextInt();

            if(row < 0 || col < 0 || nrow < 0 || ncol < 0)
                break;

            if(testGame.makeMove(current, testGame.getSpace(col, row), testGame.getSpace(ncol, nrow)))
            {
                current = current.getColor() == PieceColor.RED ? p2 : p1;
            }
            System.out.println(testGame);
        }

    }

    @Override
    public String toString()
    {
        String mat = "";
        ArrayList<ArrayList<Space>> spaces = masterBoard.getAllSpaces();
        int row = 0;
        mat += " ";
        for(int i = 0; i < 8; i++)
        {
            mat += " " + i;
        }
        mat += "\n";
        for(ArrayList<Space> spacerow : spaces) {
            mat += row + " ";
            for (Space space : spacerow)
            {

                switch(space.getPieceColor())
                {
                    case RED:
                        mat += space.getPiece().getType() == Type.KING ? "X" : "x";
                        break;
                    case WHITE:
                        mat += space.getPiece().getType() == Type.KING ? "O" : "o";
                        break;
                    case NOCOLOR:
                        mat += "_";
                        break;
                }
                mat += " ";
            }
            mat += "\n";
            row++;
        }

        return mat;

    }

    public static void main(String[] args) {
        testBoard();
    }

}
