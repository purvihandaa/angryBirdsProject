package com.Desktop.angryBird.Sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public abstract class Bird {
    protected Texture texture;
    public float x, y;
    public float width = 70;
    public float height = 70;
    public int power = 1000;
    private Vector2 velocity = new Vector2(0, 0);  // Default velocity initialized
    private float mass = 1.0f;
    private boolean isLaunched = false;
    private final float gravity = 9.8f;  // Gravity constant for realistic physics

    public Bird(float x, float y, String texturePath) {
        this.x = x;
        this.y = y;
        this.texture = new Texture(texturePath);
    }

    public abstract void update(float dt);

    public void render(SpriteBatch sb) {
        sb.draw(texture, x, y, width, height);
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    // Reduce power when colliding with an obstacle
    public void reducePower(int damage) {
        this.power -= damage;
        if (this.power < 0) {
            this.power = 0; // Prevent negative power
        }
    }

    public void dispose() {
        if (texture != null) {
            texture.dispose();
        }
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public void setVelocity(Vector2 velocity) {
        this.velocity = velocity;
    }

    public void launch(Vector2 initialVelocity) {
        this.velocity.set(initialVelocity);
        this.isLaunched = true;
    }

    public float getMass() {
        return mass;
    }

    public void setMass(float mass) {
        this.mass = mass;
    }

    public boolean isLaunched() {
        return isLaunched;
    }

    public void update(float dt, float gravity) {
        if (isLaunched) {
            velocity.y -= gravity * dt;
            x += velocity.x * dt;
            y += velocity.y * dt;

            // Reset velocity and position when out of screen bounds
            if (y < 0 || x < 0 || x > com.badlogic.gdx.Gdx.graphics.getWidth()) {
                velocity.set(0, 0);
                isLaunched = false;
            }
        }
    }
}
