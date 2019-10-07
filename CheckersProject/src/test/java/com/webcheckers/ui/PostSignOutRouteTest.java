package com.webcheckers.ui;

import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.model.Player;
import spark.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


/**
 * This tests the PostSignOutRoute class.
 */
public class PostSignOutRouteTest {

    /**
     * The component-under-test (CuT).
     *
     * <p>
     * This is a stateless component so we only need one.
     * The {@link com.webcheckers.appl.PlayerLobby} component is thoroughly tested so
     * we can use it safely as a "friendly" dependency.
     */
    private PostSignOutRoute CuT;

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
        when(session.id()).thenReturn("session1");
        response = mock(Response.class);
        engine = mock(TemplateEngine.class);

        lobby = new PlayerLobby();

        asker = new Player("player1");
        lobby.addPlayer(asker.getName(), request);
//        when(lobby.getPlayerFromSessionId(request.session())).thenReturn(asker);

        // create a unique CuT for each test
        CuT = new PostSignOutRoute(lobby, engine);
    }

    /**
     * Test the constructor with null arguments
     */
    @Test
    public void testConstructor_Null() {
        boolean isThrown = false;

        try {
            new PostSignOutRoute(null, null);
        }
        catch (Error e) {
            isThrown = true;
        }

        assertTrue(isThrown);
    }

    /**
     * Test the constructor of the postSignOutRoute
     */
    @Test
    public void testConstructor() {

        boolean isThrown = false;

        try {
            CuT = new PostSignOutRoute(lobby, engine);
        }
        catch (Error e) {
            isThrown = true;
        }

        assertFalse(isThrown);
    }

    /**
     * Test the handle method
     */
    @Test
    void testHandle() {
        final TemplateEngineTester testHelper = new TemplateEngineTester();
        when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());
        new PlayerLobby();

        CuT.handle(request, response);

        testHelper.assertViewModelExists();
        testHelper.assertViewModelIsaMap();

        testHelper.assertViewModelAttribute("title", "Sign in");
        testHelper.assertViewModelAttribute("numOnline", 0);

        testHelper.assertViewName("home.ftl");
    }
}





















