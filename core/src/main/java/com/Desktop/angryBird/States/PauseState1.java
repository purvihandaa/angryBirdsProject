package com.Desktop.angryBird.States;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class PauseState1 extends state {
    private Texture overlay;
    private Texture play;
    private Texture restart;
    private Texture backmenu;

    private Rectangle playBounds;
    private Rectangle restartBounds;
    private Rectangle backmenuBounds;

    private state previousState; // Reference to the state being paused

    public PauseState1(GameStateManager gsm, state previousState) {
        super(gsm);
        this.previousState = previousState; // Save the state to render behind the pause screen

        overlay = new Texture("overlay.png");
        play = new Texture("playb.png");
        restart = new Texture("replayb.png");
        backmenu = new Texture("menub.png");

        playBounds = new Rectangle(440, 383, 95, 95);
        restartBounds = new Rectangle(560, 380, 95, 95);
        backmenuBounds = new Rectangle(680, 373, 100, 100);
    }

    @Override
    protected void handleInput() {
        float touchX = Gdx.input.getX();
        float touchY = Gdx.graphics.getHeight() - Gdx.input.getY();

        if (Gdx.input.justTouched()) {
            if (playBounds.contains(touchX, touchY)) {
                gsm.pop(); // Resume game by removing the PauseState
            } else if (restartBounds.contains(touchX, touchY)) {
                gsm.set(new Level1(gsm)); // Restart the level
            } else if (backmenuBounds.contains(touchX, touchY)) {
                gsm.set(new MenuState(gsm)); // Go back to the main menu
            }
        }
    }

    @Override
    public void update(float dt) {
        handleInput();
    }

    @Override
    public void render(SpriteBatch sb) {
        // Render the previous state as the background
        previousState.render(sb);

        sb.begin();
        // Render the pause screen overlay and buttons
        sb.draw(overlay, 380, 330, 470, 200);
        sb.draw(play, playBounds.x, playBounds.y, playBounds.width, playBounds.height);
        sb.draw(restart, restartBounds.x, restartBounds.y, restartBounds.width, restartBounds.height);
        sb.draw(backmenu, backmenuBounds.x, backmenuBounds.y, backmenuBounds.width, backmenuBounds.height);
        sb.end();
    }

}
