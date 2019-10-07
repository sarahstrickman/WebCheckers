package com.webcheckers.ui;

import com.google.gson.JsonObject;
import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.model.Board;
import com.webcheckers.model.Game;
import com.webcheckers.model.PieceColor;
import com.webcheckers.model.Player;
import com.webcheckers.util.Message;
import spark.*;

import javax.naming.InsufficientResourcesException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

public class PostValidateMoveRoute implements Route {

    private PlayerLobby lobby;

    private final TemplateEngine templateEngine;

    private static final Logger LOG = Logger.getLogger(GetHomeRoute.class.getName());


    public PostValidateMoveRoute(PlayerLobby lobby, final TemplateEngine templateEngine) {
        Objects.requireNonNull(lobby, "We need a lobby dingus");

        this.lobby = lobby;
        this.templateEngine = templateEngine;

        LOG.config("PostValidateMoveRoute initialized baybeeeeeeeeeeee");
    }

    /**
     * Takes a string, and takes the numbers at the beginning, then everything afterwards that isn't a string it ignores.
     * Only works with unsigned numbers
     * @param string
     * @return an int. returns -1 if there was no int found
     */
    private static int strtol(String string){
        char[] str = string.toCharArray();
        String temp = "";
        int count = 0;
        while (count < string.length() && Character.isDigit(str[count])){
            temp += str[count];
            count++;
        }
        if(temp.length() == 0)
            return -1;
        return Integer.parseInt(temp);
    }

    private class Move{
        int startRow;
        int startCell;
        int endRow;
        int endCell;
        boolean whiteMove;// In this case, the move is reversed, yada yada

        Move(String actionData, Player player){
            String[] temp = actionData.split(":");
            startRow = strtol(temp[2]);
            startCell = strtol(temp[3]);
            endRow = strtol(temp[5]);
            endCell = strtol(temp[6]);

            if(startRow < 0|| startCell < 0 || endRow < 0 || endCell < 0)
                System.err.println("A BIG BAD HAPPENED");
            if(player.getColor() == PieceColor.WHITE){
                startRow = Board.BOARD_SIZE - startRow - 1;
                //startCell = Board.BOARD_SIZE - startCell - 1;
                endRow = Board.BOARD_SIZE - endRow - 1;
                //endCell = Board.BOARD_SIZE - endCell - 1;
            }
        }
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
        LOG.fine("PostSignOutRoute is invoked.");
        System.out.println(request.queryParams("actionData"));
        String theThing = request.queryParams("actionData");
        Player player = lobby.getPlayerFromSessionId(request.session());
        Move move = new Move(theThing, player);
        Game game = player.getGame();
        boolean moveMade = game.makeMove(player, game.getSpace(move.startCell, move.startRow), game.getSpace(move.endCell, move.endRow));
        System.out.println(game);

        response.body(Message.info("info message").toString());
        return true;
    }
}
