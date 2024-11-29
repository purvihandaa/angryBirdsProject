package com.Desktop.angryBird.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.Color;

import static com.Desktop.angryBird.States.BaseLevel.PPM;

public class BlackBird extends Bird {
    private boolean eggDropped = false;
    private static final float EGG_DENSITY = 0.8f;
    private static final float EGG_FRICTION = 0.2f;
    private static final float EGG_RESTITUTION = 0.4f;
    private static final float EGG_VELOCITY_MULTIPLIER = 1.5f;
    private ShapeRenderer shapeRenderer;
    private Body eggBody; // Store the egg body as an instance variable

    public BlackBird(World world, float x, float y) {
        super(world, x, y, "birdBlack.png");
        shapeRenderer = new ShapeRenderer();
    }

    @Override
    public void update(float dt) {
        if (!eggDropped && isLaunched && Gdx.input.justTouched()) {
            dropEgg();
            System.out.println("Dropping egg");
            eggDropped = true;
        }
    }

    private void dropEgg() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(body.getPosition());
        eggBody = body.getWorld().createBody(bodyDef); // Store the egg body

        CircleShape circle = new CircleShape();
        circle.setRadius(8f / PPM);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circle;
        fixtureDef.density = EGG_DENSITY;
        fixtureDef.friction = EGG_FRICTION;
        fixtureDef.restitution = EGG_RESTITUTION;
        eggBody.createFixture(fixtureDef);

        Vector2 birdVelocity = body.getLinearVelocity();
        eggBody.setLinearVelocity(
            birdVelocity.x * EGG_VELOCITY_MULTIPLIER,
            (birdVelocity.y - 8f) * EGG_VELOCITY_MULTIPLIER
        );

        circle.dispose(); // Dispose of the shape after use
    }

    @Override
    public void launch(Vector2 velocity) {
        super.launch(velocity);
        eggDropped = false; // Reset egg dropped state when launching
    }

    public void dispose() {
        super.dispose();
        shapeRenderer.dispose();
    }

    public void renderShapes() {
        if (eggDropped && eggBody != null) {
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(Color.WHITE);
            Vector2 pos = eggBody.getPosition();
            shapeRenderer.ellipse(
                pos.x * PPM - 8f,
                pos.y * PPM - 12f,
                16f,
                24f
            );
            shapeRenderer.end();
        }
    }
}
