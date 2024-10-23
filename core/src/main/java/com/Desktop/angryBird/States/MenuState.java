package com.Desktop.angryBird.States;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class MenuState extends state {

    private Texture menu;
    private Texture level1;
    private Texture level2;
    private Texture level3;
    private Texture level4;
    private Texture level5;
    private Rectangle lev1Bounds;
    private Rectangle lev2Bounds;
    private Rectangle lev3Bounds;
    private Rectangle lev4Bounds;
    private Rectangle lev5Bounds;

    public MenuState(GameStateManager gsm) {
        super(gsm);
        menu = new Texture("menu.png");
        level1 = new Texture("one.png");
        level2 = new Texture("two.png");
        level3 = new Texture("three.png");
        level4 = new Texture("four.png");
        level5 = new Texture("five.png");

        lev1Bounds = new Rectangle(490, 320, 38, 38);
        lev2Bounds = new Rectangle(645, 325, 38, 38);
        lev3Bounds = new Rectangle(790, 385, 36, 36);
        lev4Bounds = new Rectangle(925, 470, 38, 38);
        lev5Bounds = new Rectangle(1040, 305, 38, 38);
    }

    @Override
    protected void handleInput() {
        if (Gdx.input.justTouched()) {
            float touchX = Gdx.input.getX();
            float touchY = Gdx.graphics.getHeight() - Gdx.input.getY();

            if (lev1Bounds.contains(touchX, touchY)) {
                gsm.set(new Level1(gsm));
            }
            else if (lev2Bounds.contains(touchX, touchY)) {
                gsm.set(new Level2(gsm));
            }
            else if (lev3Bounds.contains(touchX, touchY)) {
                gsm.set(new Level3(gsm));
            }
            else if (lev4Bounds.contains(touchX, touchY)) {
                gsm.set(new Level4(gsm));
            }
            else if (lev5Bounds.contains(touchX, touchY)) {
                gsm.set(new Level5(gsm));
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
        sb.draw(level1, lev1Bounds.x, lev1Bounds.y, lev1Bounds.width, lev1Bounds.height);
        sb.draw(level2, lev2Bounds.x, lev2Bounds.y, lev2Bounds.width, lev2Bounds.height);
        sb.draw(level3, lev3Bounds.x, lev3Bounds.y, lev3Bounds.width, lev3Bounds.height);
        sb.draw(level4, lev4Bounds.x, lev4Bounds.y, lev4Bounds.width, lev4Bounds.height);
        sb.draw(level5, lev5Bounds.x, lev5Bounds.y, lev5Bounds.width, lev5Bounds.height);
        sb.end();

    }



}



