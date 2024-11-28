//package com.Desktop.angryBird.Sprites;
//
//import com.badlogic.gdx.math.Vector2;
//import com.badlogic.gdx.physics.box2d.*;
//
//public class Collision implements ContactListener {
//    private World world;
//
//    public Collision(World world) {
//        this.world = world;
//
//    }
//
//    @Override
//    public void beginContact(Contact contact) {
//        Fixture fixtureA = contact.getFixtureA();
//        Fixture fixtureB = contact.getFixtureB();
//
//        // Bird and Pig Collision
//        if (isBirdAndPigCollision(fixtureA, fixtureB)) {
//            handleBirdPigCollision(fixtureA, fixtureB);
//        }
//
//        // Bird and Obstacle Collision
//        if (isBirdAndObstacleCollision(fixtureA, fixtureB)) {
//            handleBirdObstacleCollision(fixtureA, fixtureB);
//        }
//    }
//
//    @Override
//    public void endContact(Contact contact) {
//        // Optional: Implement any end contact logic if needed
//    }
//
//    @Override
//    public void preSolve(Contact contact, Manifold oldManifold) {
//        // Optional: Pre-solve contact handling
//    }
//
//    @Override
//    public void postSolve(Contact contact, ContactImpulse impulse) {
//        // Optional: Post-solve contact handling, could be used for damage calculation
//    }
//
//    private boolean isBirdAndPigCollision(Fixture fixtureA, Fixture fixtureB) {
//        return (fixtureA.getUserData() instanceof Bird && fixtureB.getUserData() instanceof Pigs) ||
//            (fixtureB.getUserData() instanceof Bird && fixtureA.getUserData() instanceof Pigs);
//    }
//
//    private void handleBirdPigCollision(Fixture fixtureA, Fixture fixtureB) {
//        Pigs pig = fixtureA.getUserData() instanceof Pigs
//            ? (Pigs) fixtureA.getUserData()
//            : (Pigs) fixtureB.getUserData();
//        pig.damage();
//    }
//
//    private boolean isBirdAndObstacleCollision(Fixture fixtureA, Fixture fixtureB) {
//        return (fixtureA.getUserData() instanceof Bird && fixtureB.getUserData() instanceof Obstacles) ||
//            (fixtureB.getUserData() instanceof Bird && fixtureA.getUserData() instanceof Obstacles);
//    }
//
//    private void handleBirdObstacleCollision(Fixture fixtureA, Fixture fixtureB) {
//        Obstacles obstacle = fixtureA.getUserData() instanceof Obstacles
//            ? (Obstacles) fixtureA.getUserData()
//            : (Obstacles) fixtureB.getUserData();
//
//        // Change obstacle body type to dynamic if it's static
//        if (obstacle.body.getType() == BodyDef.BodyType.StaticBody) {
//            obstacle.body.setType(BodyDef.BodyType.DynamicBody);
//
//            // Apply an initial impulse to the obstacle
//            float impulseX = 5f;
//            float impulseY = 5f;
//            obstacle.body.applyLinearImpulse(new Vector2(impulseX, impulseY), obstacle.body.getWorldCenter(), true);
//        }
//    }
//}
