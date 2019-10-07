package com.webcheckers.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.webcheckers.model.Player;

/**
 * The unit test suite for the {@link Player} component.
 *
 * @author <a href='mailto:mea5692@rit.edu'>Matt Agger</a>
 */
@Tag("Model-tier")
public class PlayerTest {

    /**
     * Tests the constructor for expected initial values.
     */
    @Test
    public void testConstructorValid() {

        String name = "player";
        PieceColor color = PieceColor.NOCOLOR;
        float percent = 0;

        Player player = new Player(name);
        assertEquals(player.getName(), name);
        assertEquals(player.getColor(), color);
        assertEquals(player.percentageWon(), percent);
    }

    /**
     * Tests the constructor for its reaction to a null name.
     */
    @Test
    public void testConstructorNullName() {

        String name = null;
        boolean isThrown = false;

        try {
            new Player(name);
        } catch (Error e) {
            isThrown = true;
        }
        assertTrue(isThrown);
    }

    /**
     * Tests the methods for getting and assigning the color.
     */
    @Test
    public void testColor() {

        String name = "player";
        PieceColor color1 = PieceColor.NOCOLOR;
        PieceColor color2 = PieceColor.RED;

        Player player = new Player(name);
        assertEquals(player.getColor(), color1);
        player.assignColor(color2);
        assertEquals(player.getColor(), color2);
    }

    /**
     * Tests the method for checking if the player has a game
     * and the methods for setting and getting the game.
     */
    @Test
    public void testGame() {

        String name = "player";
        Game game;

        Player player = new Player(name);
        assertFalse(player.hasGame());
        game = new Game(player, player);
        player.setGame(game);
        assertEquals(player.getGame(), game);
    }
}
