package org.example.arkanoidFX.gameobject.powerup;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import org.example.arkanoidFX.gameobject.movable.Ball;
import org.example.arkanoidFX.gameobject.movable.MovableObject;

/**
 * Power-up that increases ball speed.
 * Demonstrates OOP principle: Inheritance and Polymorphism
 */
public class FastBallPowerUp extends PowerUp {
    private Ball ball;

    public FastBallPowerUp(int x, int y, int width, int height) {
        super(x, y, width, height);
        this.type = "FastBall";
        this.duration = 100; // Duration in game ticks

        // Create the Rectangle shape for JavaFX rendering
        Rectangle rectangle = new Rectangle(width, height);
        rectangle.setFill(Color.YELLOW);
        rectangle.setStroke(Color.ORANGE);
        rectangle.setStrokeWidth(2);
        rectangle.setLayoutX(x);
        rectangle.setLayoutY(y);
        this.shape = rectangle;
    }

    @Override
    public void applyEffect(MovableObject movable) {
        if (ball != null) {
            ((Ball) ball).increaseSpeed();
        }
    }

    @Override
    public void removeEffect(MovableObject movable) {
        if (ball != null) {
            ((Ball) ball).normalSpeed();
        }
    }

    @Override
    public void update() {
        fall();
        updateShapePosition();
    }

    @Override
    public void render() {
        // Render method for terminal
    }
}
