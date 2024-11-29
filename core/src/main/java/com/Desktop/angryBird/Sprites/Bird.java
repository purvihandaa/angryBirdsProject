package com.Desktop.angryBird.Sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

import java.util.ArrayList;
import java.util.List;

import static com.Desktop.angryBird.States.BaseLevel.PPM;


public abstract class Bird {
    protected Texture texture;
    public float x, y;
    public float width = 70 / PPM;
    public float height = 70 / PPM;
    public int power = 1000;
    protected float angle;
    public boolean isDisposed = false;
    private List<Body> birdsToDestroy = new ArrayList<>();

    protected World world;
    public Body body;
    protected BodyDef bodyDef;
    protected FixtureDef fixtureDef;
    protected PolygonShape shape;

    public boolean isLaunched = false;

    public Bird(World world, float x, float y, String texturePath) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.texture = new Texture(texturePath);

        // Create Box2D body
        createBody();
    }

    private void createBody() {
        // Body Definition
        bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody; // Set to StaticBody initially
        bodyDef.position.set(x / PPM, y / PPM);
        bodyDef.bullet = true; // Continuous collision detection

        // Create body in the world
        body = world.createBody(bodyDef);

        // Shape
        shape = new PolygonShape();
        shape.setAsBox(width / 2, height / 2);

        // Fixture Definition
        fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1.0f;
        fixtureDef.friction = 0.5f;
        fixtureDef.restitution = 0.01f; // Bounciness

        // Create fixture
        body.createFixture(fixtureDef);

        // Clean up
        shape.dispose();
    }

    public abstract void update(float dt);

    public void render(SpriteBatch sb) {
        // Get the body's current position
        Vector2 bodyPosition = body.getPosition();

        // Convert Box2D coordinates to screen coordinates
        float renderX = bodyPosition.x * PPM - width * PPM / 2;
        float renderY = bodyPosition.y * PPM - height * PPM / 2;

        sb.draw(texture, renderX, renderY, width * PPM, height * PPM);
    }

    public Rectangle getBounds() {
        Vector2 pos = body.getPosition();
        return new Rectangle(
            pos.x * PPM - width * PPM / 2,
            pos.y * PPM - height * PPM / 2,
            width * PPM,
            height * PPM
        );
    }


    public void dispose() {
        if (texture != null) {
            texture.dispose();
        }
        // Remove the body from the world
        if (body != null) {
            world.destroyBody(body);
        }
    }

    public void launch(Vector2 initialVelocity) {
        body.setType(BodyDef.BodyType.DynamicBody); // Change to DynamicBody when launching
        body.setLinearVelocity(initialVelocity);
        isLaunched = true;
    }

    public boolean isLaunched() {
        return isLaunched;
    }

    // Method to check if the bird is out of bounds
    public boolean isOutOfBounds() {
        Vector2 pos = body.getPosition();
        return pos.y < 0 || pos.x < 0 || pos.x > com.badlogic.gdx.Gdx.graphics.getWidth() / PPM;
    }

    // Additional method to reset bird state
    public List<Body> reset() {
        isLaunched = false;
        body.setLinearVelocity(0, 0);
        body.setType(BodyDef.BodyType.StaticBody);
        birdsToDestroy.add(body);
        return birdsToDestroy;
    }
}
