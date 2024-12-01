package com.Desktop.angryBird.Sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;

import static com.Desktop.angryBird.States.BaseLevel.PPM;

public abstract class Obstacles {
    public Body body;
    public boolean isDisposed = false;
    private boolean toRemove = false;
    protected Texture texture;
    private World world;
    private float width, height; // Store dimensions for consistent physics and rendering

    // Hit counter and threshold
    private int hitCounter = 0;

    public void setBody(Body body) {
        this.body = body;
    }

    public boolean isDisposed() {
        return isDisposed;
    }

    public void setDisposed(boolean disposed) {
        isDisposed = disposed;
    }

    public boolean isToRemove() {
        return toRemove;
    }

    public Texture getTexture() {
        return texture;
    }

    public World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public int getHitCounter() {
        return hitCounter;
    }

    public void setHitCounter(int hitCounter) {
        this.hitCounter = hitCounter;
    }

    public float getDensity() {
        return density;
    }

    public float getRestitution() {
        return restitution;
    }

    public float getFriction() {
        return friction;
    }

    private final int hitThreshold;

    // New properties for physical characteristics
    private final float density;
    private final float restitution;
    private final float friction;

    public Obstacles(World world, float x, float y, float width, float height, int hitThreshold, float density, float restitution, float friction) {
        this.world = world;
        this.width = width * PPM;  // Store the original dimensions in pixels
        this.height = height * PPM;
        this.hitThreshold = hitThreshold; // Set the hit threshold
        this.density = density; // Set the density
        this.restitution = restitution; // Set the restitution
        this.friction = friction; // Set the friction
        createBody(x, y, width, height);
    }

    private void createBody(float x, float y, float width, float height) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;

        // Position the body at the center of where we want the obstacle
        bodyDef.position.set((x + width / 2) / PPM, (y + height / 2) / PPM);

        body = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width / 2, height / 2); // Use the actual half-dimensions

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = this.density; // Use the density passed to the constructor
        fixtureDef.restitution = this.restitution; // Use the restitution passed to the constructor
        fixtureDef.friction = this.friction; // Use the friction passed to the constructor

        body.createFixture(fixtureDef).setUserData(this);
        shape.dispose();
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    public Rectangle getBounds() {
        float bodyX = body.getPosition().x * PPM;
        float bodyY = body.getPosition().y * PPM;

        return new Rectangle(
            bodyX - width / 2,  // Left edge
            bodyY - height / 2, // Bottom edge
            width,              // Width
            height              // Height
        );
    }

    public void render(SpriteBatch sb) {
        if (texture == null) {
            throw new IllegalStateException("Texture not set for obstacle");
        }

        float bodyX = body.getPosition().x * PPM;
        float bodyY = body.getPosition().y * PPM;
        float rotation = (float) Math.toDegrees(body.getAngle());

        float renderX = bodyX - width / 2;
        float renderY = bodyY - height / 2;

        sb.draw(
            texture,
            renderX,
            renderY,
            width / 2,
            height / 2,
            width,
            height,
            1f,
            1f,
            rotation,
            0,
            0,
            texture.getWidth(),
            texture.getHeight(),
            false,
            false
        );
    }

    public void hit() {
        hitCounter++;
        if (hitCounter >= hitThreshold) {
            toRemove = true; // Mark for removal
            System.out.println("obstacle ready to be removed");
        }
    }

    public void setToRemove(boolean val) {
        toRemove=val;
    }

    public void dispose(World world) {
        world.destroyBody(body);
        if (texture != null) {
            texture.dispose();
        }
    }

    public void setDensity(float density) {
        // This method can't directly modify the final density field
        // You might need to recreate the body with the new density
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.density = density;
        body.createFixture(fixtureDef);
    }

    public void setFriction(float friction) {
        // Update the fixture's friction
        for (Fixture fixture : body.getFixtureList()) {
            fixture.setFriction(friction);
        }
    }

    public void setRestitution(float restitution) {
        // Update the fixture's restitution
        for (Fixture fixture : body.getFixtureList()) {
            fixture.setRestitution(restitution);
        }
    }

    public int getHitCount() { return hitCounter; }
    public int getHitThreshold() { return hitThreshold; }
    public Body getBody() { return body; }
}
