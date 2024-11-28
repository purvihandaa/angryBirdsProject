package com.Desktop.angryBird.Sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.math.Vector2;

import static com.Desktop.angryBird.States.BaseLevel.PPM;

public class Obst7 extends Obstacles {
    private Texture texture;
    private float width;
    private float height;

    public Obst7(World world, float x, float y, float width, float height) {
        super(world, x, y, width, height);
        this.width = width;
        this.height = height;
        texture = new Texture("obj7.png");

        // Create the body for the obstacle (scale to Box2D world)
        createBody(world, x, y, width, height);
    }

    private void createBody(World world, float x, float y, float width, float height) {
        // Box2D body definition
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;  // Assuming the obstacles are static (non-moving)
        bodyDef.position.set(x / PPM, y / PPM); // Convert to world coordinates

        // Create the Box2D body
        Body body = world.createBody(bodyDef);

        // Box2D shape
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width / 2 / PPM, height / 2 / PPM);  // Convert to world units

        // Create a fixture for the obstacle
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 0;  // Static obstacles should not have density
        fixtureDef.friction = 0.5f;  // Adjust friction if needed
        fixtureDef.restitution = 0.2f;  // Bounce effect when collided

        // Attach the fixture to the body
        body.createFixture(fixtureDef);
        shape.dispose();  // Dispose of the shape after use

        // Store the body to reference it later for rendering
        this.body = body;
    }




    @Override
    public void render(SpriteBatch sb) {
        // Scale the texture drawing according to PPM
        sb.draw(
            texture,
            body.getPosition().x * PPM - (width * PPM) / 2,  // Scale position by PPM
            body.getPosition().y * PPM - (height * PPM) / 2,  // Scale position by PPM
            width * PPM,  // Scale the width by PPM
            height * PPM   // Scale the height by PPM
        );
    }

    public void dispose() {
        texture.dispose();
    }
}
