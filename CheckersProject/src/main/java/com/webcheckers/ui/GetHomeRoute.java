package com.webcheckers.ui;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.model.Game;
import com.webcheckers.model.PieceColor;
import com.webcheckers.model.Player;
import com.webcheckers.model.ViewType;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.TemplateEngine;

import com.webcheckers.util.Message;

import static org.eclipse.jetty.http.HttpMethod.GET;

/**
 * The UI Controller to GET the Home page.
 *
 * @author <a href='mailto:bdbvse@rit.edu'>Bryan Basham</a>
 */
public class GetHomeRoute implements Route {

  private static final Logger LOG = Logger.getLogger(GetHomeRoute.class.getName());

  private static final Message WELCOME_MSG = Message.info("Welcome to the world of online Checkers.");

  private final TemplateEngine templateEngine;

  private final PlayerLobby lobby;
  /**
   * Create the Spark Route (UI controller) to handle all {@code GET /} HTTP requests.
   *
   * @param templateEngine
   *   the HTML template rendering engine
   */
  public GetHomeRoute(final PlayerLobby lobby, final TemplateEngine templateEngine) {
    this.templateEngine = Objects.requireNonNull(templateEngine, "templateEngine is required");
    Objects.requireNonNull(lobby);
    this.lobby = lobby;
    //
    LOG.config("GetHomeRoute is initialized.");
  }

  /**
   * Render the WebCheckers Home page.
   *
   * @param request
   *   the HTTP request
   * @param response
   *   the HTTP response
   *
   * @return
   *   the rendered HTML for the Home page
   */
  @Override
  public Object handle(Request request, Response response) {
    LOG.fine("GetHomeRoute is invoked.");
    //
    Map<String, Object> vm = new HashMap<>();
    vm.put("numOnline", PlayerLobby.getPlayerNumber());
    vm.put("onlinePlayers", lobby.playerString());
    vm.put("title", "Welcome!");
    vm.put("webpage", WebServer.HOME_URL);

    // display a user message in the Home page
    vm.put("message", WELCOME_MSG);

    // If the current player is in a game, bring them to the game page.
    if (lobby.hasPlayer(request.session())) {
      Player player = lobby.getPlayerFromSessionId(request.session());
      vm.put("currentUser", player);

      if (player.getMessage() != null) {
        vm.put("message", player.getMessage());
      }
      // You are in a game!
      if(player.hasGame()){
        Game game = player.getGame();
        vm.put("currentUser", player);
        vm.put("whitePlayer", game.getWhitePlayer()); //someone assigned us this game
        vm.put("redPlayer", game.getRedPlayer());
        vm.put("title", "theGame");
        vm.put("viewMode", ViewType.PLAY);
        vm.put("activeColor", game.getActiveColor());
          if(player.getColor() == PieceColor.WHITE)
              vm.put("board", game.getMasterBoard().getAllSpacesReverse());
          else
              vm.put("board", game.getMasterBoard().getAllSpaces());

          response.redirect(WebServer.GAME_URL);
      }
      player.setMessage(null);
    }


    // render the View
    return templateEngine.render(new ModelAndView(vm , "home.ftl"));
  }
}
