package com.webcheckers.ui;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.webcheckers.appl.PlayerLobby;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.webcheckers.ui.GetSignInRoute;
import spark.*;

/**
 * The unit test suite for the {@link GetSignInRoute} component.
 *
 * @author <a href='mailto:mea5692@rit.edu'>Matt Agger</a>
 */
@Tag("UI-tier")
public class GetSignInRouteTest {

    private GetSignInRoute CuT;

    private Request request;
    private Session session;
    private Response response;
    private TemplateEngine engine;

    /**
     * Setup new mock objects for each test.
     */
    @BeforeEach
    public void setup() {
        request = mock(Request.class);
        session = mock(Session.class);
        when(request.session()).thenReturn(session);
        response = mock(Response.class);
        engine = mock(TemplateEngine.class);

        CuT = new GetSignInRoute(engine);
    }

    /**
     * Tests the constructor when given a valid TemplateEngine.
     */
    @Test
    public void testConstructorValid() {
        boolean isThrown = false;

        try {
            CuT = new GetSignInRoute(engine);
        } catch (Exception e) {
            isThrown = true;
        }
        assertFalse(isThrown);
    }

    /**
     * Tests the constructor for its reaction to a null TemplateEngine.
     */
    @Test
    public void testConstructorNullTempEng() {
        boolean isThrown = false;

        try {
            CuT = new GetSignInRoute(null);
        } catch (Exception e) {
            isThrown = true;
        }
        assertTrue(isThrown);
    }

    /**
     * Tests the handle method for rendering the WebCheckers Sign In page.
     */
    @Test
    public void testHandle() {
        final TemplateEngineTester testHelper = new TemplateEngineTester();
        when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());
        new PlayerLobby();

        CuT.handle(request, response);

        testHelper.assertViewModelExists();
        testHelper.assertViewModelIsaMap();

        testHelper.assertViewModelAttribute("title", "Sign in");
        testHelper.assertViewModelAttribute("numOnline", 0);

        testHelper.assertViewName("signin.ftl");
    }
}
