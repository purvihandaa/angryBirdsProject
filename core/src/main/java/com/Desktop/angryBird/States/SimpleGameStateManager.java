
package com.Desktop.angryBird.States;

import com.Desktop.angryBird.States.GameStateManager;
import com.Desktop.angryBird.States.state;


public class SimpleGameStateManager extends GameStateManager {
    private state currentState;

    @Override
    public void set(state state) {
        this.currentState = state;
    }

    public state getCurrentState() {
        return currentState;
    }
}
