package com.Desktop.angryBird.Sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import static com.Desktop.angryBird.States.BaseLevel.PPM;

public abstract class Obstacles {
    protected Body body;
    private boolean toRemove = false;
    private float removeThreshold = -10; // Y-position at which the obstacle is considered "fallen"
    protected Texture texture;

    public Obstacles(World world, float x, float y, float width, float height) {
        BodyDef bdef = new BodyDef();
        bdef.type = BodyDef.BodyType.DynamicBody;
        bdef.position.set(x / PPM, y / PPM);
        body = world.createBody(bdef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width / 2 / PPM, height / 2 / PPM);

        FixtureDef fdef = new FixtureDef();
        fdef.shape = shape;
        fdef.density = 1.0f;
        fdef.restitution = 0.3f; // Bounciness
        fdef.friction = 0.5f; // Friction for sliding realism

        body.createFixture(fdef).setUserData(this);
        shape.dispose();
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


    public void checkCollisionWithBird(Bird bird) {
        Rectangle birdBounds = bird.getBounds();
        Rectangle obstacleBounds = getBounds(); // Get the obstacle's bounds using the new method

        if (birdBounds.overlaps(obstacleBounds)) {
            // Apply an impulse to the obstacle for realistic fall
            body.applyLinearImpulse(
                new Vector2(0, -10f), // Downward impulse
                body.getWorldCenter(),
                true
            );
        }
    }

    public void update(float dt) {
        if (body.getPosition().y < removeThreshold) {
            toRemove = true;
        }
    }

    public boolean isToRemove() {
        return toRemove;
    }

    public void render(SpriteBatch sb) {
        sb.draw(
            texture,
            body.getPosition().x * PPM - texture.getWidth() / 2,
            body.getPosition().y * PPM - texture.getHeight() / 2
        );
    }

    public void dispose(World world) {
        world.destroyBody(body);
    }
}
