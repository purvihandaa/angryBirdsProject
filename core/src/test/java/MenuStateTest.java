import com.Desktop.angryBird.States.MenuState;
import com.Desktop.angryBird.States.GameStateManager;
import com.badlogic.gdx.math.Rectangle;
import org.junit.Test;
import static org.junit.Assert.*;

public class MenuStateTest {

    @Test
    public void testBounds() {
        GameStateManager gsm = new GameStateManager();
        MenuState menuState = new MenuState(gsm, true); // true for testing mode

        Rectangle lev1 = menuState.getLev1Bounds();
        Rectangle lev2 = menuState.getLev2Bounds();

        // Test level 1 bounds
        assertEquals(490, (int)lev1.x);
        assertEquals(320, (int)lev1.y);
        assertEquals(38, (int)lev1.width);
        assertEquals(38, (int)lev1.height);

        // Test level 2 bounds
        assertEquals(645, (int)lev2.x);
        assertEquals(325, (int)lev2.y);
        assertEquals(38, (int)lev2.width);
        assertEquals(38, (int)lev2.height);
    }
}
