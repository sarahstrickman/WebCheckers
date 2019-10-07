package com.webcheckers.model;

import com.webcheckers.util.Message;

public class Player {

    // Stores the unique name of the player
    private String name;

    // The color of the player
    private PieceColor color;

    // The message associated with this player
    private Message message;

    //The game the the player is playing. TODO change to the appropriate type later
    private Game game;
    private int gamesWon;       // the number of games that this player has won
    private int gamesPlayed;    // the number of games that this player has played
    // include other attributes?? stats? etc.

    /**
     * Initialize a single player.
     *
     * @param name
     *      the name the player has
     */
    public Player(String name) {
        assert name != null : "Player name missing.";

        // The player has no messages associated with it yet
        this.message = null;

        this.name = name;
        this.gamesPlayed = 0;
        this.gamesWon = 0;
        this.color = PieceColor.NOCOLOR;
    }

    /**
     * Gets the message associated with the current player for display.
     */
    public Message getMessage() {
        return this.message;
    }

    /**
     * Sets the current player's message to what you want.
     */
    public void setMessage(Message message) {
        this.message = message;
    }

    public PieceColor getColor(){
        return this.color;
    }

    public void assignColor(PieceColor color){
        this.color = color;
    }

    /**
     * Get the player's name.
     * @return
     *      the name that the player is using for the game
     */
    public String getName() {
        return name;
    }

    /**
     * What percentage of games has this player won? Returns a float from
     *      0 to 1.
     * @return
     *      a float of what percentage of games this player has won.
     */
    public float percentageWon() {
        if (gamesPlayed == 0) {
            return 0;
        }
        else {
            return ((float) gamesWon) / ((float) gamesPlayed);
        }
    }

    public void setGame(Game game){
        this.game = game;
    }

    public boolean hasGame(){
        return this.game != null;
    }

    public Game getGame(){
        return this.game;
    }
}
