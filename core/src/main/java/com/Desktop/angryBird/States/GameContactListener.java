package com.Desktop.angryBird.States;

import com.Desktop.angryBird.Sprites.Bird;
import com.Desktop.angryBird.Sprites.Obstacles;
import com.Desktop.angryBird.Sprites.Pigs;
import com.badlogic.gdx.physics.box2d.*;

import java.util.ArrayList;
import java.util.List;
import java.util.HashSet;
import java.util.Set;

public class GameContactListener implements ContactListener {
    private World world;
    private List<Body> bodiesToRemove;
    private Set<Contact> activeContacts;

    public GameContactListener(World world) {
        this.world = world;
        this.bodiesToRemove = new ArrayList<>();
        this.activeContacts = new HashSet<>();
    }

    @Override
    public void beginContact(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

        Object userDataA = fixtureA.getBody().getUserData();
        Object userDataB = fixtureB.getBody().getUserData();

        // Prevent duplicate contact processing
        if (activeContacts.contains(contact)) {
            return;
        }
        activeContacts.add(contact);

        // Bird-Pig Collision
        if ((userDataA instanceof Bird && userDataB instanceof Pigs) ||
            (userDataA instanceof Pigs && userDataB instanceof Bird)) {
            Pigs pig = (userDataA instanceof Pigs) ? (Pigs) userDataA : (Pigs) userDataB;
            handlePigDamage(pig);
        }

        // Bird-Obstacle Collision
        if ((userDataA instanceof Bird && userDataB instanceof Obstacles) ||
            (userDataA instanceof Obstacles && userDataB instanceof Bird)) {
            Obstacles obstacle = (userDataA instanceof Obstacles) ?
                (Obstacles) userDataA : (Obstacles) userDataB;
            handleObstacleHit(obstacle);
        }

        // Pig-Obstacle Collision
        if ((userDataA instanceof Pigs && userDataB instanceof Obstacles) ||
            (userDataA instanceof Obstacles && userDataB instanceof Pigs)) {
            Pigs pig = (userDataA instanceof Pigs) ? (Pigs) userDataA : (Pigs) userDataB;
            Obstacles obstacle = (userDataA instanceof Obstacles) ?
                (Obstacles) userDataA : (Obstacles) userDataB;

            // Additional damage to both objects
            handlePigDamage(pig);
            handleObstacleHit(obstacle);
        }
    }

    private void handlePigDamage(Pigs pig) {
        if (pig == null) return;
        System.out.println("Pig is damaging");

        pig.damage();

        if (pig.getHitCount() >= pig.getHitThreshold()) {
            Body pigBody = pig.getBody();
            if (pigBody != null && !bodiesToRemove.contains(pigBody)) {
                bodiesToRemove.add(pigBody);
                pig.setReadyToRemove(true);
            }
        }
    }

    private void handleObstacleHit(Obstacles obstacle) {
        if (obstacle == null) return;

        System.out.println("Obstacle is damaging");

        obstacle.hit();

        if (obstacle.getHitCount() >= obstacle.getHitThreshold()) {
            Body obstacleBody = obstacle.getBody();
            if (obstacleBody != null && !bodiesToRemove.contains(obstacleBody)) {
                System.out.println("Obstacle added to be removed");
                bodiesToRemove.add(obstacleBody);
                obstacle.setToRemove(true);
            }
        }
    }

    @Override
    public void endContact(Contact contact) {
        // Remove the contact from active contacts
        activeContacts.remove(contact);
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
        // Not needed for this implementation
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
        // Optional: Could use impulse magnitude to determine damage intensity
        float maxImpulse = 0;
        for (float impulseValue : impulse.getNormalImpulses()) {
            maxImpulse = Math.max(maxImpulse, Math.abs(impulseValue));
        }

        // You could use maxImpulse to adjust damage or hit count if desired
    }

    // Method to get bodies that need to be removed
    public List<Body> getBodiesToRemove() {
        return bodiesToRemove;
    }

    // Method to clear the removal list after processing
    public void clearBodiesToRemove() {
        bodiesToRemove.clear();
    }
}
