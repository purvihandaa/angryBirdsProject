package com.Desktop.angryBird.States;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.Stack; //FIFO

public class GameStateManager {

    private Stack<state> states;

    public GameStateManager(){
        states= new Stack<state>();
    }


    public void push(state state){
        states.push(state);
    }

    public void pop(){
        states.pop();
    }

    public void set(state state){
        states.pop();
        states.push(state);
    }


    public void update(float dt){ //delta
        states.peek().update(dt); //top item in stack
    }

    public void render(SpriteBatch sb){
        states.peek().render(sb);
    }


}
