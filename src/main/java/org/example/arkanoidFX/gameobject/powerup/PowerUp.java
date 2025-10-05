package org.example.arkanoidFX.gameobject.powerup;


import org.example.arkanoidFX.gameobject.GameObject;
import org.example.arkanoidFX.gameobject.movable.MovableObject;

/**
 * Base class for power-ups.
 * Demonstrates OOP principle: Abstraction
 */
public abstract class PowerUp extends GameObject {
    protected String type;
    protected int duration;
    protected boolean falling;
    protected int fallSpeed;

    public PowerUp(int x, int y, int width, int height) {
        super(x, y, width, height);
        this.falling = true;
        this.fallSpeed = 1;
    }

    public String getType() {
        return type;
    }

    public boolean isFalling() {
        return falling;
    }

    public void setFalling(boolean falling) {
        this.falling = falling;
    }

    public int getDuration() {
        return this.duration;
    }

    public void fall() {
        if (falling) {
            y += fallSpeed;
        }
    }

    public abstract void applyEffect(MovableObject movable);
    public abstract void removeEffect(MovableObject movable);
}
