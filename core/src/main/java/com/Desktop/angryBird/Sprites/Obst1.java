package com.Desktop.angryBird.Sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.World;

public class Obst1 extends Obstacles {

    // Define a hit threshold for this obstacle
    private static final int HIT_THRESHOLD = 5; // Example threshold

    private static final float DENSITY = 0.5f; // Example density
    private static final float RESTITUTION = 0.1f; // Example restitution
    private static final float FRICTION = 0.9f; // Example friction

    public Obst1(World world, float x, float y, float width, float height) {
        // Pass the hit threshold and physical properties to the superclass
        super(world, x, y, width, height, HIT_THRESHOLD, DENSITY, RESTITUTION, FRICTION);
        setTexture(new Texture("obj1.png"));  // Set the texture for this obstacle
    }

    // You can add additional methods or overrides specific to Obst4 if needed
}
