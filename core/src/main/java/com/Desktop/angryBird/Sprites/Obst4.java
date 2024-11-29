package com.Desktop.angryBird.Sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.World;

public class Obst4 extends Obstacles {

    private static final int HIT_THRESHOLD = 2; // Example threshold

    private static final float DENSITY = 0.8f; // Example density
    private static final float RESTITUTION = 0.2f; // Example restitution
    private static final float FRICTION = 0.8f; // Example friction

    public Obst4(World world, float x, float y, float width, float height) {
        // Pass the hit threshold and physical properties to the superclass
        super(world, x, y, width, height, HIT_THRESHOLD, DENSITY, RESTITUTION, FRICTION);
        setTexture(new Texture("obj4.png"));  // Set the texture for this obstacle
    }

}
