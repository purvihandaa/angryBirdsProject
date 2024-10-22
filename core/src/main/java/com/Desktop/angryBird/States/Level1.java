package com.Desktop.angryBird.States;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class Level1 extends state {
    private Texture bg;
    private Texture bird1;
    private Texture bird2;
    private Texture bird3;
    private Texture pig;
    private Texture slingshot;
    private Texture obstacle1;
    private Texture obstacle2;
    private Texture obstacle3;

    public Level1(GameStateManager gsm) {
        super(gsm);
        bg = new Texture("bg.jpg");
        bird1 = new Texture("redbird.png");
        bird2 = new Texture("bird2.png");
        bird3 = new Texture("bird4.png");
        pig = new Texture("pig2.png");
        slingshot = new Texture("slingshot.png");
        obstacle1 = new Texture("obj1.png");
        obstacle2 = new Texture("objSt.png");
        obstacle3 = new Texture("obj4.png");

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
        sb.draw(bird1, 140, 150, 50,50);
        sb.draw(bird2, 30, 70, 50,50);
        sb.draw(bird3, 80, 70, 50,50);
        sb.draw(slingshot, 130, 70, 80,80);
        sb.draw(pig, 517, 70, 50,50);
        sb.draw(obstacle1, 493, 160, 100,10);
        sb.draw(obstacle2, 500, 63, 10,100);
        sb.draw(obstacle2, 580, 63, 10,100);
        sb.draw(obstacle3, 450, 70, 40,40);
        sb.end();

    }




}
