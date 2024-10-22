package com.Desktop.angryBird.States;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class PauseState extends state{

    private Texture play;
    private Texture restart;
    private Rectangle playBounds;
    private Rectangle restartBounds;

    public PauseState(GameStateManager gsm) {
        super(gsm);
        play=new Texture("playb.png");
        restart=new Texture("replayb.png");
        playBounds = new Rectangle(10, 650, 80,80);
        restartBounds = new Rectangle(10, 250, 80,80);
    }

    @Override
    protected void handleInput() {
        if (Gdx.input.justTouched()) {
            float touchX = Gdx.input.getX();
            float touchY = Gdx.graphics.getHeight() - Gdx.input.getY();

            if (playBounds.contains(touchX, touchY)) {
                gsm.pop();
            }
            else if (restartBounds.contains(touchX, touchY)) {
                gsm.set(new Level1(gsm));
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
        sb.draw(play, playBounds.x, playBounds.y, playBounds.width, playBounds.height);
        sb.draw(restart, restartBounds.x, restartBounds.y, restartBounds.width, restartBounds.height);
        sb.end();

    }
}
