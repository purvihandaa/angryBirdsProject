package com.Desktop.angryBird; // Ensure this follows the proper naming convention

import com.badlogic.gdx.Game;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends Game {
    @Override
    public void create() {
        setScreen(new FirstScreen());
    }

    // Fixed inner class definition
    public void Apple() {
            System.out.println("HELLO");
        }

}
