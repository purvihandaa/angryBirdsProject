package com.Desktop.angryBird.Sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.math.Vector2;

import static com.Desktop.angryBird.States.BaseLevel.PPM;

public abstract class Pigs {
    protected Texture texture;
    protected float x, y;
    protected float width;
    protected float height;
    public boolean damaged = false;
    private float damageTimer = 0;
    public boolean isDisposed = false;

    // Hit counter and threshold
    private int hitCounter = 0;
    private final int hitThreshold;

    // Box2D related fields
    public Body body;
    private World world;
    private FixtureDef fixtureDef;

    public Pigs(World world, float x, float y, String texturePath, float w, float h, int hitThreshold) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.width = w;
        this.height = h;
        this.texture = new Texture(texturePath);
        this.hitThreshold = hitThreshold; // Set the hit threshold

        // Create Box2D body
        createPhysicsBody();
    }

    protected void createPhysicsBody() {
        // Body definition
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(x / PPM, y / PPM);
        bodyDef.fixedRotation = false; // Allow rotation

        // Create body in the world
        body = world.createBody(bodyDef);

        // Shape for collision
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width / 2 / PPM, height / 2 / PPM);

        // Fixture definition
        fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 0.2f;
        fixtureDef.friction = 0.5f;
        fixtureDef.restitution = 0.2f; // Bounciness

        // Create fixture
        body.createFixture(fixtureDef).setUserData(this);

        // Cleanup
        shape.dispose();
    }

    public void update(float dt) {
        // Sync sprite position with physics body
        x = body.getPosition().x * PPM;
        y = body.getPosition().y * PPM;

        if (damaged) {
            damageTimer += dt;
        }
    }

    public void render(SpriteBatch sb) {
        Vector2 position = body.getPosition();
        sb.draw(texture, position.x * PPM - width / 2, position.y * PPM - height / 2, width, height);
    }

    public Rectangle getBounds() {
        return new Rectangle(x - width / 2, y - height / 2, width, height);
    }

    public void setTexture(Texture newTexture) {
        if (this.texture != null) {
            this.texture.dispose(); // Dispose of the old texture to avoid memory leaks
        }
        this.texture = newTexture;
    }

    public void setReadyToRemove(boolean val) {
        isDisposed=val;

    }

    public void damage() {
        hitCounter++; // Increment hit counter
        if (hitCounter >= hitThreshold) {
            isDisposed = true; // Mark for removal
            System.out.println("obstacle ready to be removed");
        }

        if (!damaged) {
            this.texture = new Texture("pig_damaged.png"); // Set to damaged texture
            damaged = true;

            // Optional: Apply an impulse or change physics properties when damaged
            body.applyLinearImpulse(new Vector2(1f, 1f), body.getWorldCenter(), true);
        }
    }


    public void dispose() {
        if (texture != null) {
            texture.dispose();
        }

        // Remove body from the world
        if (body != null) {
            world.destroyBody(body);
        }
    }


    public int getHitCount() { return hitCounter; }
    public int getHitThreshold() { return hitThreshold; }
    public Body getBody() { return body; }
}
