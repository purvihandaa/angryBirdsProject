package com.Desktop.angryBird.States;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class Level1 extends state {
    private Texture bg;
    private Texture birdRed;
    private Texture birdYellow;
    private Texture birdBlack;
    private Texture pig1;
    private Texture pig2;
    private Texture pig3;
    private Texture slingshot;
    private Texture obstacle1;
    private Texture obstacle2;
    private Texture obstacle3;
    private Texture obstacle4;
    private Texture obstacle5;

    public Level1(GameStateManager gsm) {
        super(gsm);
        bg = new Texture("bg.jpg");
        birdRed = new Texture("birdRed.png");
        birdYellow = new Texture("birdYellow.png");
        birdBlack = new Texture("birdBlack.png");
        pig1 = new Texture("pig1.png");
        pig2 = new Texture("pig2.png");
        pig3 = new Texture("pig3.png");
        slingshot = new Texture("slingshot.png");
        obstacle1 = new Texture("obj1.png");
        obstacle2 = new Texture("objSt.png");
        obstacle3 = new Texture("obj4.png");
        obstacle4 = new Texture("obj7.png");
        obstacle5 = new Texture("obj10.png");

    }

    @Override
    protected void handleInput() {


    }

    @Override
    public void update(float dt) {
        handleInput();

    }

    @Override
    public void render(SpriteBatch sb) {
        sb.begin();
        sb.draw(bg, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        sb.draw(slingshot, 130, 100, 120,120);
        sb.draw(birdRed, 150, 195, 70,70);
        sb.draw(birdYellow, 40, 95, 70,70);
        sb.draw(birdBlack, 100, 105, 70,70);
        sb.draw(pig3, 1000-5, 310, 85,100);
        sb.draw(pig2, 1000, 100, 65,65);
        sb.draw(obstacle1, 969, 230, 130,20);
        sb.draw(obstacle2, 1080, 98, 20,130);
        sb.draw(obstacle2, 970, 100, 20,130);
        sb.draw(obstacle3, 970, 250, 40,40);
        sb.draw(obstacle4, 1050, 250, 38,38);
        sb.draw(obstacle1, 969, 290, 130,20);
        sb.end();

    }




}
