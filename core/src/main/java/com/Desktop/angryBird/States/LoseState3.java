package com.Desktop.angryBird.States;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class LoseState3 extends state {

    private Texture loseOverlay;
    private Texture sadbird;
    private Texture menu;
    private Texture replay;
    private Texture soclose;

    private Rectangle menuBounds;
    private Rectangle replayBounds;

    private state previousState;


    public LoseState3(GameStateManager gsm, state previousState) {
        super(gsm);

        loseOverlay = new Texture("CommOverlay.png");
        menu = new Texture("menub.png");
        replay = new Texture("replayb.png");
        soclose = new Texture("soclose.png");
        sadbird = new Texture("sadbird.png");

        menuBounds = new Rectangle(740, 320, 80, 80);
        replayBounds = new Rectangle(450, 320, 80, 80);
        this.previousState = previousState; // Save the state to render behind the pause screen


    }

    @Override
    protected void handleInput() {

        float touchX = Gdx.input.getX();
        float touchY = Gdx.graphics.getHeight() - Gdx.input.getY();
        if (Gdx.input.justTouched()) {

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

        sb.draw(loseOverlay, 340, 290 , 550, 460);
        sb.draw(soclose, 550,470,150 ,35);
        sb.draw(sadbird, 565,325,170, 150);

        sb.draw(menu, menuBounds.x, menuBounds.y, menuBounds.width, menuBounds.height);
        sb.draw(replay, replayBounds.x, replayBounds.y, replayBounds.width, replayBounds.height);

        sb.end();

    }
}

