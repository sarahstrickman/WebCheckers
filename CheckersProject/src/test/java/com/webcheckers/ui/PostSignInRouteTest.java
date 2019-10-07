package com.webcheckers.ui;

import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import spark.*;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PostSignInRouteTest
{
    private PostSignInRoute CuT;

    // friendly objects
    private PlayerLobby lobby;

    // mock objects
    private Request request;
    private Session session;
    private Player asker;
    private TemplateEngine engine;
    private Response response;

    @BeforeEach
    public void setup()
    {
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
        CuT = new PostSignInRoute(lobby, engine);
    }


    @Test
    public void testConstructor_Null() {
        boolean isThrown = false;

        try {
            new PostSignInRoute(null, null);
        }
        catch (Error e) {
            isThrown = true;
        }

        assertTrue(isThrown);
    }

    @Test
    public void testConstructor() {

        boolean isThrown = false;

        try {
            CuT = new PostSignInRoute(lobby, engine);
        }
        catch (Error e) {
            isThrown = true;
        }

        assertFalse(isThrown);
    }

    @Test
    void testHandle() {
        final TemplateEngineTester testHelper = new TemplateEngineTester();
        when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());
        new PlayerLobby();

        try
        {
            CuT.handle(request, response);
        }
        catch(Exception e)
        {}
        testHelper.assertViewModelExists();
        testHelper.assertViewModelIsaMap();

        testHelper.assertViewModelAttribute("title", "Sign in");
        testHelper.assertViewModelAttribute("numOnline", 0);

        testHelper.assertViewName("home.ftl");
    }


}
