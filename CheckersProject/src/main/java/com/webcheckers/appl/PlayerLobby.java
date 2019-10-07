package com.webcheckers.appl;


import com.webcheckers.model.Game;
import com.webcheckers.model.Player;
import spark.Request;
import spark.Session;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Keeps track of the players that are logged into the server.
 */
public class PlayerLobby {

    //Stores the HTTP Session and the player's name that's associated with it.
    static Map<String, Player> players;

    //All of the games, key is the playername. 2 players of a game map to the same game.
    private HashMap<String, Game> games = new HashMap<>();

    // How invalid can this username be?????
    public enum NameValid { VALID, BAD_CHARS, TAKEN, LONG, SHORT}

    /**
     * Constructor for the PlayerLobby class.
     * Initialize the Player map.
     */
    public PlayerLobby() {
        this.players = new HashMap<>();
    }

    /**
     * How many players are logged into the game?
     * @return the number of Players in the players hashmap
     */
    public static int getPlayerNumber() {
        return players.size();
    }

    public HashMap<String, Game> getGames(){
        return games;
    }

    /**
     * Given a name, find the player object associated with this username.
     * @param name
     *      the name of the player that's being searched for
     * @return the Player object associated with a certain username.  Return
     * null if there is no match
     */
    public Player getPlayerFromName(String name) {
        for (Player player : players.values()) {
            if (player.getName().equals(name)) {
                return player;
            }
        }
        return null;
    }

    /**
     * Return an arrayList of all of the players in the game.
     * @return
     *      An arrayList of sll of the users that are logged into the thing
     */
    public ArrayList<Player> getPlayers() {
        ArrayList<Player> lst = new ArrayList<>();
        for (Player entry : players.values()) {
            lst.add(entry);
        }
        return lst;
    }

    /**
     * Return a comma separated list of player usernames.
     */
    public ArrayList<String> playerString() {
        ArrayList<Player> playerArray = new ArrayList<>(players.values());
        ArrayList<String> playerNames = new ArrayList<>();

        for(Player player : playerArray)
            playerNames.add(player.getName());

        return playerNames;
    }

    /**
     * Return a player object based on a certain HTTP Session ID.
     *
     * @param session
     *      The Session ID of the player that you're searching for
     * @return The player associated with the HTTP session, null if it doesn't
     *      exist.
     */
    public Player getPlayerFromSessionId(Session session) {
        if (players.containsKey(session.id())) {
            return players.get(session.id());
        }
        else {
            return null;
        }
    }

    /**
     * Does the players map contain a player associated with a certain session?
     */
    public boolean hasPlayer(Session session) {
        if (players.containsKey(session.id())) {
            return true;
        }
        return false;
    }

    /**
     * Add a player to the logged in players.
     *
     * @param username
     *      the username that the player is using while they are playing the
     *      game.
     * @param request
     *      the HTTP request
     */
    public void addPlayer(String username, Request request) {
        players.put(request.session().id(), new Player(username));
    }

    /**
     * Remove a player from the player lobby.
     *
     * @param request
     *      the HTTP request
     */
    public void removePlayer(Request request) {
        players.remove(request.session().id());
    }

    /**
     * Checks a username to see if it's a valid one to put into the player list.
     *
     * A username is considered invalid if it is longer than 30 characters, or
     * if it is already taken (another player is logged in with the same
     * username).
     *
     * @param username
     *      the username that someone wants to use to sign in to the game.
     *
     * @return JACK SHIt about why the username is/isn't valid
     */
    public NameValid validateName(String username) {
        if ((username.length() > 30)) {
            return NameValid.LONG;
        }
        else if ((username.length() <= 0)) {
            return NameValid.SHORT;
        }
        int alphaNumCount = 0;
        for (char c : username.toCharArray()) {
            if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')
                    || (c >= '0' && c <= '9')) {
                alphaNumCount++;
            }
            else if (c == ' ') {
                continue;
            }
            else {
                return NameValid.BAD_CHARS;
            }
        }
        if(alphaNumCount <= 0) {
            return NameValid.SHORT;
        }
        for (Player player : players.values()) {
            if (username.equals( player.getName() )) {
                return NameValid.TAKEN;
            }
        }
        return NameValid.VALID;
    }
}
