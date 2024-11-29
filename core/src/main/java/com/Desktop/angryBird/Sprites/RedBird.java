package com.Desktop.angryBird.Sprites;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

public class RedBird extends Bird {


    public RedBird(World world,float x, float y) {
        super(world,x, y, "birdRed.png");
    }

    @Override
    public void update(float dt) {
    }
}



