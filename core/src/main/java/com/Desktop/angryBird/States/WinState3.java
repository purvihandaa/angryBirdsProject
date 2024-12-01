package com.Desktop.angryBird.States;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.Gdx;

public class WinState3 extends state {

    private Texture winOverlay;
    private Texture nextlevel;
    private Texture menu;
    private Texture replay;
    private Texture welldone;

    private Rectangle nextLevelBounds;
    private Rectangle menuBounds;
    private Rectangle replayBounds;
    private Vector3 touchPoint;

    private state previousState;

    public WinState3(GameStateManager gsm, state previousState) {
        super(gsm);
        winOverlay = new Texture("CommOverlay.png");
        nextlevel = new Texture("nextLevelb.png");
        menu = new Texture("menub.png");
        replay = new Texture("replayb.png");
        welldone = new Texture("welldone.png");

        nextLevelBounds = new Rectangle(580, 360, 90,90);
        menuBounds = new Rectangle(730, 360, 80, 80);
        replayBounds = new Rectangle(430, 360, 80, 80);

        touchPoint = new Vector3();

        this.previousState = previousState; // Save the state to render behind the pause screen

    }

    @Override
    protected void handleInput() {

        float touchX = Gdx.input.getX();
        float touchY = Gdx.graphics.getHeight() - Gdx.input.getY();
        if (Gdx.input.justTouched()) {

            if (nextLevelBounds.contains(touchX, touchY)) {
                gsm.set(new MenuState(gsm));
            }

            if (menuBounds.contains(touchX,touchY)) {
                gsm.set(new MenuState(gsm));
            }

            if (replayBounds.contains(touchX,touchY)) {
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
        previousState.render(sb);

        sb.begin();

        sb.draw(winOverlay, 340, 290 , 550, 460);
        sb.draw(welldone, 450,450,360 ,55);

        sb.draw(nextlevel, nextLevelBounds.x, nextLevelBounds.y, nextLevelBounds.width, nextLevelBounds.height);
        sb.draw(menu, menuBounds.x, menuBounds.y, menuBounds.width, menuBounds.height);
        sb.draw(replay, replayBounds.x, replayBounds.y, replayBounds.width, replayBounds.height);

        sb.end();
    }

}
