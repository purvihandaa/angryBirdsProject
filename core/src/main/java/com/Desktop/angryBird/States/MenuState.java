package com.Desktop.angryBird.States;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

public class MenuState extends state {

    private Texture menu;
    private Texture level1;
    private Texture level2;
    private Texture level3;
    private Texture level4;
    private Texture level5;
    private Texture loadgameButton;
    public Rectangle lev1Bounds;
    public Rectangle lev2Bounds;
    private Rectangle lev3Bounds;
    private Rectangle lev4Bounds;
    private Rectangle lev5Bounds;
    private Rectangle loadgameButtonBounds;

    public MenuState(GameStateManager gsm) {
        super(gsm);
        menu = new Texture("menu.png");
        level1 = new Texture("one.png");
        level2 = new Texture("two.png");
        level3 = new Texture("three.png");
        level4 = new Texture("four.png");
        level5 = new Texture("five.png");
        loadgameButton = new Texture("loadgame.png");

        lev1Bounds = new Rectangle(490, 320, 38, 38);
        lev2Bounds = new Rectangle(645, 325, 38, 38);
        lev3Bounds = new Rectangle(790, 385, 36, 36);
        lev4Bounds = new Rectangle(925, 470, 38, 38);
        lev5Bounds = new Rectangle(1040, 305, 38, 38);
        loadgameButtonBounds = new Rectangle(1000, 660, 140, 60);
    }


    @Override
    protected void handleInput() {
        if (Gdx.input.justTouched()) {
            float touchX = Gdx.input.getX();
            float touchY = Gdx.graphics.getHeight() - Gdx.input.getY();

            if (loadgameButtonBounds.contains(touchX, touchY)) {
                World world = new World(new Vector2(0, -9.8f), true);
                Level1 loadedLevel1 = new Level1(gsm);
                SaveGameManager.loadGameState(loadedLevel1, world);
                gsm.set(loadedLevel1);
                return;

            }

            if (lev1Bounds.contains(touchX, touchY)) {
                gsm.set(new Level1(gsm));
            }
            else if (lev2Bounds.contains(touchX, touchY)) {
                gsm.set(new Level2(gsm));
            }
            else if (lev3Bounds.contains(touchX, touchY)) {
                gsm.set(new Level3(gsm));
            }

        }

    }

    @Override
    public void update(float dt) {
        handleInput();

    }

    @Override
    public void render(SpriteBatch sb) {
        sb.begin();
        sb.draw(menu, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        sb.draw(loadgameButton, loadgameButtonBounds.x, loadgameButtonBounds.y, loadgameButtonBounds.width, loadgameButtonBounds.height);
        sb.draw(level1, lev1Bounds.x, lev1Bounds.y, lev1Bounds.width, lev1Bounds.height);
        sb.draw(level2, lev2Bounds.x, lev2Bounds.y, lev2Bounds.width, lev2Bounds.height);
        sb.draw(level3, lev3Bounds.x, lev3Bounds.y, lev3Bounds.width, lev3Bounds.height);
        sb.draw(level4, lev4Bounds.x, lev4Bounds.y, lev4Bounds.width, lev4Bounds.height);
        sb.draw(level5, lev5Bounds.x, lev5Bounds.y, lev5Bounds.width, lev5Bounds.height);
        sb.end();

    }

    public Rectangle getLev1Bounds() {
        return lev1Bounds;
    }

    public Rectangle getLev2Bounds() {
        return lev2Bounds;
    }

    // Add a method to get the input (for testing)
    public Object getInput() {
        try {
            java.lang.reflect.Field inputField = getClass().getSuperclass().getDeclaredField("input");
            inputField.setAccessible(true);
            return inputField.get(this);
        } catch (Exception e) {
            throw new RuntimeException("Could not get input", e);
        }
    }

    public MenuState(GameStateManager gsm, boolean testing) {
        super(gsm);
        if (!testing) {
            // Only load textures if not testing
            menu = new Texture("menu.png");
            level1 = new Texture("one.png");
            level2 = new Texture("two.png");
            level3 = new Texture("three.png");
            level4 = new Texture("four.png");
            level5 = new Texture("five.png");
        }

        // Create bounds regardless of testing mode
        lev1Bounds = new Rectangle(490, 320, 38, 38);
        lev2Bounds = new Rectangle(645, 325, 38, 38);
        lev3Bounds = new Rectangle(790, 385, 36, 36);
        lev4Bounds = new Rectangle(925, 470, 38, 38);
        lev5Bounds = new Rectangle(1040, 305, 38, 38);
    }



}



