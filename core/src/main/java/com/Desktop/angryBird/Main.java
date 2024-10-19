package com.Desktop.angryBird;

import com.Desktop.angryBird.States.GameStateManager;
import com.Desktop.angryBird.States.MenuState;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.awt.*;

public class Main extends ApplicationAdapter {
    public static final int WIDTH = 1200;
    public static final int HEIGHT= 700;


    public static final String TITLE = "Angry Birds";
    private GameStateManager gsm;
    private SpriteBatch batch;

    Texture img;



    public void create(){
        batch=new SpriteBatch();
        gsm= new GameStateManager();
        img =new Texture("seokjinniesun.png");
        gsm.push(new MenuState(gsm));
        Gdx.gl.glClearColor(1,1,3,2);


    }

    public void render(){
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        gsm.update(Gdx.graphics.getDeltaTime());
        gsm.render(batch);
        batch.begin();
        batch.draw(img,50,50);
        batch.end();

    }
}
