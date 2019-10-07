package com.webcheckers.ui;

import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.model.Game;
import com.webcheckers.model.PieceColor;
import com.webcheckers.model.Player;
import com.webcheckers.model.ViewType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import spark.*;

import java.util.Objects;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

public class GetGameRouteTest {

    /**
     * The component-under-test (CuT).
     *
     * <p>
     * This is a stateless component so we only need one.
     * The {@link com.webcheckers.appl.PlayerLobby} component is thoroughly tested so
     * we can use it safely as a "friendly" dependency.
     */
    private GetGameRoute CuT;

    // friendly objects
    private PlayerLobby lobby;

    // mock objects
    private Request request;
    private Request oppRequest;
    private Session session;
    private Session oppSession;
    private Player asker;
    private Player opp;
    private Game game;
    private TemplateEngine engine;
    private Response response;

    /**
     * Setup new mock objects for each test.
     */
    @BeforeEach
    public void setup() {
        request = mock(Request.class);
        oppRequest = mock(Request.class);
        session = mock(Session.class);
        oppSession = mock(Session.class);
        when(request.session()).thenReturn(session);
        when(oppRequest.session()).thenReturn(oppSession);
        when(session.id()).thenReturn("Star-Platinum");
        when(oppSession.id()).thenReturn("Za-Warudo");
        response = mock(Response.class);
        engine = mock(TemplateEngine.class);

        lobby = new PlayerLobby();

        asker = new Player("Jotaro");
        opp = new Player("DIO");
        game = new Game(asker, opp);
        lobby.addPlayer(asker.getName(), request);
        lobby.addPlayer(opp.getName(), oppRequest);
//        when(lobby.getPlayerFromSessionId(request.session())).thenReturn(asker);

        // create a unique CuT for each test
        // the GameCenter is friendly but the engine mock will need configuration
        CuT = new GetGameRoute(lobby, engine);
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
        testHelper.assertViewModelAttribute("title", "game");
        testHelper.assertViewModelAttribute("currentUser", asker.getName());
        testHelper.assertViewModelAttribute("viewMode", ViewType.PLAY);
        testHelper.assertViewModelAttribute("activeColor", PieceColor.RED);

        //   * test view name
        testHelper.assertViewName("game.ftl");
    }

}
