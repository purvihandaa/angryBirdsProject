package com.Desktop.angryBird.States;

import dev.lyze.gdxUnBox2d.GameObjectState;

import java.io.*;
import java.util.List;

public class GameState implements Serializable {
    private static final long serialVersionUID = 1L;

    private int currentLevel;
    private List<Integer> solvedLevels;
    private int progressWithinLevel;
    private List<GameObjectState> gameObjects;

    // Getters and setters
    public int getCurrentLevel() {
        return currentLevel;
    }

    public void setCurrentLevel(int currentLevel) {
        this.currentLevel = currentLevel;
    }

    public List<Integer> getSolvedLevels() {
        return solvedLevels;
    }

    public void setSolvedLevels(List<Integer> solvedLevels) {
        this.solvedLevels = solvedLevels;
    }

    public int getProgressWithinLevel() {
        return progressWithinLevel;
    }

    public void setProgressWithinLevel(int progressWithinLevel) {
        this.progressWithinLevel = progressWithinLevel;
    }

    public List<GameObjectState> getGameObjects() {
        return gameObjects;
    }

    public void setGameObjects(List<GameObjectState> gameObjects) {
        this.gameObjects = gameObjects;
    }

    public void saveGameState(String filePath) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(this);
            System.out.println("Game state saved successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static GameState loadGameState(String filePath) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
            return (GameState) ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
