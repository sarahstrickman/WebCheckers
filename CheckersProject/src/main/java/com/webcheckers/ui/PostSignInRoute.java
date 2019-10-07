package com.webcheckers.ui;

import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.util.Message;
import spark.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

public class PostSignInRoute implements Route {

    // Some string things for posting the name
    static final String NO_NAME = "no name";
    static final String LONG_NAME = "too long";
    static final String TAKEN_NAME = "already taken";
    static final String INVALID_CHARS = "invalid characters";
    static final String AVAILABLE_NAME = "goodName";

    //ATTRIBUTES

    // the players that are currently signed in.
    private final PlayerLobby lobby;

    // The LOG or something
    private static final Logger LOG = Logger.getLogger(GetHomeRoute.class.getName());

    // The default message that instructs the user to input a new username
    private static final Message SIGNIN_MSG = Message.info("Input a new User Account.");

    private final TemplateEngine templateEngine;

    // STATIC METHODS

    /**
     * Make an error when the user tries to input an empty username.
     */
    private static String makeNoName(final String nameStr) {
        return "Please input a username with at least one alphanumeric " +
                "character.";
    }

    /**
     * Make an error when the user tries to make a username that's too long.
     */
    private static String makeLongName(final String nameStr) {
        return String.format("\" %s \" is too long! Input another " +
                "username with at most 30 characters.", nameStr);
    }

    /**
     * Make an error when the user tries to use a username that's already taken.
     */
    private static String makeTakenName(final String nameStr) {
        return String.format("\" %s \" is already taken! " +
                "Input another username.", nameStr);
    }

    /**
     * The user tried to input a name that contains no alphanumeric characters
     */
    private String makeBadName(String name) {
        return "Name includes invalid characters. Please input a name " +
                "that includes one or more letters (Aa-Zz) and/or numbers " +
                "(0-9). Spaces are optional.";
    }

    /**
     * Let the user know that they have successfully signed in using the name
     * that they chose.
     */
    private static String makeGoodName(final String nameStr) {
        return String.format("Signed in using \" %s \"", nameStr);
    }

    //CONSTRUCTOR

    /**
     * Create the Spark Route (UI controller) to handle all
     * {@code POST /Signin} HTTP requests.
     *
     * @param lobby the player lobby that's associated with the game
     * @param templateEngine the HTML template rendering engine
     */
    public PostSignInRoute(final PlayerLobby lobby, final TemplateEngine templateEngine) {
        Objects.requireNonNull(lobby, "lobby is required");
        this.templateEngine = Objects.requireNonNull(templateEngine, "templateEngine is required");
        this.lobby = lobby;
        //
        LOG.config("PostSignInRoute is initialized.");
    }

    /**
     * Attempt to sign the user in to the page.
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
        LOG.fine("GetSignInRoute is invoked.");
        //
        Map<String, Object> vm = new HashMap<>();

        // Make the username and the appropriate ModelAndView for rendering.
        ModelAndView mv;

        String name = request.queryParams("name");

        // Do logic for determining which sign in to use
        PlayerLobby.NameValid isValid = lobby.validateName(name);

        // display the title
        vm.put("title", "Sign in");

        // display a user message in the Home page
        vm.put("message", Message.error("Please help."));

        switch (isValid) {
            case LONG:
                vm.put("message", Message.error(makeLongName(name)));
                mv = new ModelAndView(vm, "signin.ftl");
                vm.put("webpage", WebServer.SIGNIN_URL);
                break;
            case SHORT:
                vm.put("message", Message.error(makeNoName(name)));
                mv = new ModelAndView(vm, "signin.ftl");
                vm.put("webpage", WebServer.SIGNIN_URL);
                break;
            case TAKEN:
                vm.put("message", Message.error(makeTakenName(name)));
                mv = new ModelAndView(vm, "signin.ftl");
                vm.put("webpage", WebServer.SIGNIN_URL);
                break;
            case BAD_CHARS:
                vm.put("message", Message.error(makeBadName(name)));
                mv = new ModelAndView(vm, "signin.ftl");
                vm.put("webpage", WebServer.SIGNIN_URL);
                break;
            default:
                // Add the player into the game.
                mv = new ModelAndView(vm, "home.ftl");
                lobby.addPlayer(name, request);
                vm.put("currentUser", lobby.getPlayerFromName(name));
                vm.put("message", Message.info(makeGoodName(name)));
                vm.put("webpage", WebServer.HOME_URL);
                // Redirect to the original home page
                response.redirect(WebServer.HOME_URL);
        }

        vm.put("numOnline", lobby.getPlayerNumber());
        vm.put("onlinePlayers", lobby.playerString());

        // render the View
        return templateEngine.render(mv);
    }
}
