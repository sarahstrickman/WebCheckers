package com.webcheckers.ui;

import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.util.Message;
import spark.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;


/**
 * The UI Controller to GET the Sign In Page.
 *
 * @author <a href='mailto:sxs4599@rit.edu'>Sarah Strickman</a>
 */
public class GetSignInRoute implements Route {

    // The LOG or something
    private static final Logger LOG = Logger.getLogger(GetHomeRoute.class.getName());

    private static final Message SIGNIN_MSG = Message.info("Welcome to the world of online Checkers.");

    private final TemplateEngine templateEngine;

    /**
     * Create the Spark Route (UI controller) to handle all
     * {@code GET /Signin} HTTP requests.
     *
     * @param templateEngine the HTML template rendering engine
     */
    public GetSignInRoute(final TemplateEngine templateEngine) {
        this.templateEngine = Objects.requireNonNull(templateEngine, "templateEngine is required");
        //
        LOG.config("GetSignInRoute is initialized.");
    }

    /**
     * Render the WebCheckers Sign In page.
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
        vm.put("title", "Sign in");
        vm.put("numOnline", PlayerLobby.getPlayerNumber());
        vm.put("webpage", WebServer.SIGNIN_URL);

        // render the View
        return templateEngine.render(new ModelAndView(vm , "signin.ftl"));
    }
}
