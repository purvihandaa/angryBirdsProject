package com.Desktop.angryBird.States;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.Stack; //FIFO

public class GameStateManager {

    private Stack<State> states;

    public GameStateManager(){
        states= new Stack<State>();
    }


    public void push(State state){
        states.push(state);
    }

    public void pop(){
        states.pop();
    }

    public void set(State state){
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
