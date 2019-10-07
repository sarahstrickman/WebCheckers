package com.webcheckers.ui;

import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.model.Game;
import com.webcheckers.model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import spark.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GetHomeRouteTest {

    /**
     * The component-under-test (CuT).
     *
     * <p>
     * This is a stateless component so we only need one.
     * The {@link com.webcheckers.appl.PlayerLobby} component is thoroughly tested so
     * we can use it safely as a "friendly" dependency.
     */
    private GetHomeRoute CuT;

    // friendly objects
    private PlayerLobby lobby;

    // mock objects
    private Request request;
    private Session session;
    private Player asker;
    private TemplateEngine engine;
    private Response response;

    /**
     * Setup new mock objects for each test.
     */
    @BeforeEach
    public void setup() {
        request = mock(Request.class);
        session = mock(Session.class);
        when(request.session()).thenReturn(session);
        when(session.id()).thenReturn("Star-Platinum");
        response = mock(Response.class);
        engine = mock(TemplateEngine.class);

        lobby = new PlayerLobby();

        asker = new Player("Jotaro");
        lobby.addPlayer(asker.getName(), request);
//        when(lobby.getPlayerFromSessionId(request.session())).thenReturn(asker);

        // create a unique CuT for each test
        // the GameCenter is friendly but the engine mock will need configuration
        CuT = new GetHomeRoute(lobby, engine);
    }

    /**
     * Test that CuT shows the Home view when the session is brand new.
     */
    @Test
    public void new_session() {
        // To analyze what the Route created in the View-Model map you need
        // to be able to extract the argument to the TemplateEngine.render method.
        // Mock up the 'render' method by supplying a Mockito 'Answer' object
        // that captures the ModelAndView data passed to the template engine
        final TemplateEngineTester testHelper = new TemplateEngineTester();
        when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());

        // Invoke the test
        try {
            CuT.handle(request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Analyze the results:
        //   * model is a non-null Map
        testHelper.assertViewModelExists();
        testHelper.assertViewModelIsaMap();
        //   * model contains all necessary View-Model data
        Player asker = lobby.getPlayerFromSessionId(request.session());
        testHelper.assertViewModelAttribute("title", "Welcome!");
        testHelper.assertViewModelAttribute("currentUser", asker);
        testHelper.assertViewModelAttribute("numOnline", 1);

        //   * test view name
        testHelper.assertViewName("home.ftl");
    }

    @Test
    public void game_set(){
        final TemplateEngineTester testHelper = new TemplateEngineTester();
        when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());

        Player asker = lobby.getPlayerFromSessionId(request.session());
        Player opp = new Player("DIO");
        Game game = new Game(asker, opp);
        asker.setGame(game);
        opp.setGame(game);
        // Invoke the test
        try {
            CuT.handle(request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Analyze the results:
        //   * model is a non-null Map
        testHelper.assertViewModelExists();
        testHelper.assertViewModelIsaMap();
        //   * model contains all necessary View-Model data

        testHelper.assertViewModelAttribute("title", "theGame");
        //   * test view name
        testHelper.assertViewName("home.ftl");
    }

    public static class PostSignOutRouteTester {

        private PostSignOutRoute CuT;
    }
}
