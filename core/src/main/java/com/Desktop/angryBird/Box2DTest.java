package com.Desktop.angryBird;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;


public class Box2DTest {
    public static void main(String[] args) {
        // Step 1: Create a Box2D World
        Vector2 gravity = new Vector2(0, -9.8f); // Gravity pointing downwards
        boolean doSleep = true; // Allows bodies to sleep when inactive
        World world = new World(gravity, doSleep);

        // Step 2: Create a Ground Body (Static Body)
        BodyDef groundBodyDef = new BodyDef();
        groundBodyDef.position.set(0, 0); // Ground at (0, 0)
        Body groundBody = world.createBody(groundBodyDef);

        // Create a Box Shape for the ground
        PolygonShape groundBox = new PolygonShape();
        groundBox.setAsBox(50.0f, 1.0f); // Ground is 100 units wide and 2 units tall

        // Attach the shape to the ground body
        groundBody.createFixture(groundBox, 0.0f); // Density is 0 for static bodies
        groundBox.dispose(); // Dispose of shape when done

        // Step 3: Create a Dynamic Body (Falling Box)
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(0, 10); // Place it at (0, 10)
        Body fallingBody = world.createBody(bodyDef);

        // Create a box shape for the falling body
        PolygonShape dynamicBox = new PolygonShape();
        dynamicBox.setAsBox(1.0f, 1.0f); // 2x2 unit box

        // Define fixture properties
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = dynamicBox;
        fixtureDef.density = 1.0f; // Density affects mass
        fixtureDef.friction = 0.3f; // Friction for surface contact
        fallingBody.createFixture(fixtureDef);
        dynamicBox.dispose(); // Dispose of shape when done

        // Step 4: Simulate the World
        float timeStep = 1.0f / 60.0f; // 60 FPS
        int velocityIterations = 6; // Velocity constraint solver
        int positionIterations = 2; // Position constraint solver

        // Simulate for 3 seconds
        for (int i = 0; i < 180; i++) { // 60 steps per second
            world.step(timeStep, velocityIterations, positionIterations);

            // Get the position of the falling body
            Vector2 position = fallingBody.getPosition();
            float angle = fallingBody.getAngle();

            // Print the position and angle of the falling box
            System.out.printf("Step %d: Position=(%.2f, %.2f), Angle=%.2f%n",
                i, position.x, position.y, angle);
        }

        // Cleanup the world
        world.dispose();
    }
}
