package com.webcheckers.ui;

import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.util.Message;
import spark.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;


/**
 * The UI Controller to POST the Sign Out Action.
 *
 * @author <a href='mailto:sxs4599@rit.edu'>Sarah Strickman</a>
 */
public class PostSignOutRoute implements Route {

    //ATTRIBUTES

    // the players that are currently signed in.
    private final PlayerLobby lobby;

    // The LOG or something
    private static final Logger LOG = Logger.getLogger(GetHomeRoute.class.getName());

    // The default message that instructs the user to input a new username
    private static final Message SIGNIN_MSG = Message.info("Input a new User Account.");

    private final TemplateEngine templateEngine;


    /**
     * Create the Spark Route (UI controller) to handle all
     * {@code POST /Signout} HTTP requests.
     *
     * @param lobby the player lobby that is associated with the game
     * @param templateEngine the HTML template rendering engine
     */
    public PostSignOutRoute(final PlayerLobby lobby, final TemplateEngine templateEngine) {
        assert lobby != null : "lobby is required";
        assert templateEngine != null : "templateEngine is required";

        this.templateEngine = templateEngine;
        this.lobby = lobby;
        //
        LOG.config("PostSignInRoute is initialized.");
    }

    @Override
    public Object handle(Request request, Response response) {
        LOG.fine("PostSignOutRoute is invoked.");
        //
        Map<String, Object> vm = new HashMap<>();

        // Make the appropriate ModelAndView for rendering.
        ModelAndView mv;
        mv = new ModelAndView(vm, "home.ftl");

        lobby.removePlayer(request);

        // display the title
        vm.put("title", "Sign in");

        vm.put("numOnline", lobby.getPlayerNumber());
        vm.put("onlinePlayers", lobby.playerString());

        // Redirect to the original home page
        response.redirect(WebServer.HOME_URL);

        // render the View
        return templateEngine.render(mv);
    }
}

