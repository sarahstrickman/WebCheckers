package com.webcheckers.ui;

import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.model.Game;
import com.webcheckers.model.PieceColor;
import com.webcheckers.model.Player;
import com.webcheckers.model.ViewType;
import com.webcheckers.util.Message;
import freemarker.template.TemplateMethodModel;
import freemarker.template.TemplateModelException;
import spark.*;

import java.util.*;
import java.util.logging.Logger;

public class GetGameRoute implements Route {

    private static final Logger LOG = Logger.getLogger(GetHomeRoute.class.getName());

    private final TemplateEngine templateEngine;

    private final HashMap<String, Game> games;

    private final PlayerLobby lobby;

    /**
     * Create the Spark Route (UI controller) to handle all {@code GET /} HTTP requests.
     *
     * @param templateEngine
     *   the HTML template rendering engine
     */
    public GetGameRoute(final PlayerLobby lobby, final TemplateEngine templateEngine) {
        this.templateEngine = Objects.requireNonNull(templateEngine, "templateEngine is required");
        Objects.requireNonNull(lobby);
        this.lobby = lobby;
        this.games = lobby.getGames();
        //
        LOG.config("GetGameRoute is initialized.");
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
        LOG.finer("GetGameRoute is invoked.");

        Map<String, Object> vm = new HashMap<>();
        Player asker = lobby.getPlayerFromSessionId(request.session());
        String temp = request.queryParams("opp");
        Player opponent = lobby.getPlayerFromName(temp);
        vm.put("title", "Game");
        vm.put("webpage", WebServer.HOME_URL);

        // This block handles starting a game with another player
        //      (when their name is clicked)
        if(!asker.hasGame()){
            if(opponent.hasGame()){
                response.redirect(WebServer.LOGGED_IN_URL);
                vm.put("numOnline", PlayerLobby.getPlayerNumber());
                vm.put("onlinePlayers", lobby.playerString());
                //vm.put("message", Message.error("player is in a game already!"));
                asker.setMessage( Message.error(opponent.getName() +
                        " is in a game already!"));
                return templateEngine.render(new ModelAndView(vm, "home.ftl"));
            }
            Game game = new Game(asker, opponent);
            //The hashmap will have both the asker and opponent's names as the key, and will map to the game
            this.games.put(asker.getName(), game);
            this.games.put(opponent.getName(), game);
            asker.setGame(game);
            opponent.setGame(game);
            response.redirect(WebServer.GAME_URL);
        }
        Game game = games.get(asker.getName());

        vm.put("currentUser", asker);
        vm.put("redPlayer", game.getRedPlayer());
        vm.put("whitePlayer", game.getWhitePlayer());
        vm.put("viewMode", ViewType.PLAY);
        vm.put("activeColor", game.getActiveColor());
        if(asker.getColor() == PieceColor.WHITE)
            vm.put("board", game.getMasterBoard().getAllSpacesReverse());
        else
            vm.put("board", game.getMasterBoard().getAllSpaces());
        return templateEngine.render(new ModelAndView(vm, "game.ftl"));
    }
}
