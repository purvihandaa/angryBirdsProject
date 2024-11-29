package com.Desktop.angryBird.Sprites;

import com.badlogic.gdx.physics.box2d.World;

public class Pig1 extends Pigs {
    private static final int HIT_THRESHOLD = 3; // Example threshold for Pig1

    public Pig1(World world, float x, float y) {
        super(world, x, y, "pig2.png", 65, 65, HIT_THRESHOLD); // Pass the hit threshold to the superclass
    }

    @Override
    public void update(float dt) {
        super.update(dt); // Call the superclass update method

        // Additional update logic specific to Pig1 can be added here if needed
    }
}
