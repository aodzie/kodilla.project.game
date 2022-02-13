package com.kodilla.project.game;
import javafx.scene.Parent;

public class Ship extends Parent {
    public int type;


    private int health;

    public Ship(int type, boolean b) {
        this.type = type;
        health = type;
    }

    public void shoot() {
        health--;
    }

    public boolean isAlive() {
        return health > 0;
    }
}