package com.Desktop.angryBird.Sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

import static com.Desktop.angryBird.States.BaseLevel.PPM;

public abstract class Obstacles {
    public Body body;
    public boolean isDisposed = false;
    private boolean toRemove = false;
    private float removeThreshold = -10; // Y-position at which the obstacle is considered "fallen"
    protected Texture texture;
    private World world;

    public Obstacles(World world, float x, float y, float width, float height) {
        this.world = world;

        // Create Box2D body
        createBody(x, y, width, height);
    }

    private void createBody(float x, float y, float width, float height) {
        // Body Definition
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody; // Set to StaticBody initially
        bodyDef.position.set(x / PPM, y / PPM);

        // Create body in the world
        body = world.createBody(bodyDef);

        // Shape Definition (Box shape for the obstacle)
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width / 2 / PPM, height / 2 / PPM); // Half-width and height

        // Fixture Definition
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1.0f;
        fixtureDef.restitution = 0.3f; // Bounciness
        fixtureDef.friction = 0.5f; // Friction for sliding realism

        // Create fixture and associate it with the body
        body.createFixture(fixtureDef).setUserData(this);

        // Dispose of the shape to free memory
        shape.dispose();
    }

    public void convertToDynamic() {
        // Method to convert obstacle to a dynamic body
        body.setType(BodyDef.BodyType.DynamicBody);
    }

    public Rectangle getBounds() {
        float x = body.getPosition().x * PPM;
        float y = body.getPosition().y * PPM;

        // Check if texture is null
        if (texture == null) {
            return new Rectangle(x, y, 0, 0); // Return a default, empty rectangle if texture is not set
        }

        float width = texture.getWidth();
        float height = texture.getHeight();
        return new Rectangle(x - width / 2, y - height / 2, width, height);
    }

    public void update(float dt) {
        if (body.getPosition().y < removeThreshold) {
            toRemove = true;
        }

        // Add angular damping to slow down rotation over time
        body.setAngularDamping(0.5f);
    }

    public void render(SpriteBatch sb) {
        // Get the body's position and rotation
        float x = body.getPosition().x * PPM;
        float y = body.getPosition().y * PPM;

        // Get the rotation in degrees
        float rotation = (float) Math.toDegrees(body.getAngle());

        // Draw the texture with rotation
        sb.draw(
            texture,
            x - texture.getWidth() / 2,  // Center X
            y - texture.getHeight() / 2, // Center Y
            texture.getWidth() / 2,      // Rotation origin X
            texture.getHeight() / 2,     // Rotation origin Y
            texture.getWidth(),          // Width
            texture.getHeight(),         // Height
            1f,                          // ScaleX
            1f,                          // ScaleY
            rotation,                    // Rotation angle
            0,                           // Source X
            0,                           // Source Y
            texture.getWidth(),          // Source Width
            texture.getHeight(),         // Source Height
            false,                       // FlipX
            false                        // FlipY
        );
    }

    public void applyImpulse(Vector2 impulse) {
        // Apply linear and angular impulses
        body.applyLinearImpulse(impulse, body.getWorldCenter(), true);

        // Add some angular impulse to create rotation
        float angularImpulse = impulse.len() * 0.1f; // Adjust multiplier as needed
        body.applyAngularImpulse(angularImpulse, true);
    }

    public boolean isToRemove() {
        return toRemove;
    }

    public void dispose(World world) {
        // Remove the body from the world
        world.destroyBody(body);
    }
}
