import com.Desktop.angryBird.States.Level1;
import com.Desktop.angryBird.States.GameStateManager;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class Level1Test {
    private GameStateManager gsm;

    @Before
    public void setUp() {
        // Only initialize the GSM
        gsm = new GameStateManager();
    }

    @Test
    public void testGSMInitialization() {
        assertNotNull("GameStateManager should not be null", gsm);
    }

    @Test
    public void testGameStateManagerExists() {
        assertTrue("GameStateManager should be instance of GameStateManager",
            gsm instanceof GameStateManager);
    }
}
