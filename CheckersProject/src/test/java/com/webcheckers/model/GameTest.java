package com.webcheckers.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;


/**
 * The unit test suite for the Game class.
 *
 * @author sxs4599 Sarah Strickman
 */
@Tag("Model-Tier")
class GameTest {

    /**
     * Tests that the constructor for the Game class works.
     *
     * Makes sure that the players are assigned correctly
     *      - Player 1 is red
     *      - Player 2 is white
     */
    @Test
    void testConstructor_valid() throws Exception {
        String p1 = "player1";
        String p2 = "player2";

        Game game = new Game(new Player(p1), new Player(p2));
        assertEquals(game.getRedPlayer().getName(),p1);
        assertEquals(game.getWhitePlayer().getName(), p2);
    }


    /**
     * Test that the Game constructor doesn't work when both the Red and White
     * players are null.
     */
    @Test
    void testConstructor_bothNull() {
        String p1 = "player1";
        String p2 = "player2";

        // Test 1 : Both are null
        boolean isThrown = false;
        try {
            new Game(null, null);
        }
        catch (Error e) {
            isThrown = true;
        }
        assertTrue(isThrown);
    }

    /**
     * Tests the constructor when the red player is null.
     */
    @Test
    void testConstructor_redNull() {
        String p1 = "player1";
        String p2 = "player2";

        boolean isThrown = false;
        try {
            new Game(null, new Player(p2));
        }
        catch (Error e) {
            isThrown = true;
        }
        assertTrue(isThrown);
    }

    /**
     * Tests the constructor when the white player is null.
     */
    @Test
    void testConstructor_whiteNull() {
        String p1 = "player1";
        String p2 = "player2";

        boolean isThrown = false;
        try {
            new Game(new Player(p1), null);
        }
        catch (Error e) {
            isThrown = true;
        }
        assertTrue(isThrown);
    }

    /**
     * Tests the various getter functions for Game.
     *  - getWhitePlayer()
     *  - getRedPlayer()
     *  - getBoard()
     *  - getSpace()
     */
    @Test
    void testGetters_validGame() {
        String p1 = "player1";
        String p2 = "player2";
        Player redPlayer = new Player(p1);
        Player whitePlayer = new Player(p2);
        Game game = new Game(redPlayer, whitePlayer);

        // Test the getPlayer function
        assertSame(game.getWhitePlayer(), whitePlayer);
        assertSame(game.getWhitePlayer().getName(), p2);
        assertSame(game.getRedPlayer(), redPlayer);
        assertSame(game.getRedPlayer().getName(), p1);

        // Test the Board function: the board has been created
        assertNotNull(game.getMasterBoard());

        // Test the getSpace functions
        assertNotNull(game.getSpace(0, 1));
        assertNotNull(game.getSpace(5, 1));
    }


    //TODO : Finish tests for makeMove. Ask about functionality in Game.java
    // @{Johnny}

    /**
     * Tests the makeMove function.
     */
    @Test
    void testMakeMove_notTurn() {
        String p1 = "player1";
        String p2 = "player2";
        Player redPlayer = new Player(p1);
        Player whitePlayer = new Player(p2);
        Game game = new Game(redPlayer, whitePlayer);

        // This would be a valid move for White to make.
        assertTrue(!game.makeMove(game.getWhitePlayer(),
                game.getSpace(3, 3),
                game.getSpace(4, 4)));
    }
}
