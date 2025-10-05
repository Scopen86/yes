package org.example.arkanoidFX.gameobject.powerup;


import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import org.example.arkanoidFX.gameobject.movable.MovableObject;
import org.example.arkanoidFX.gameobject.movable.Paddle;

/**
 * Power-up that expands the paddle.
 * Demonstrates OOP principle: Inheritance and Polymorphism
 */


public class ExpandPaddlePowerUp extends PowerUp {
    public ExpandPaddlePowerUp(int x, int y, int width, int height) {
        super(x, y, width, height);
        this.type = "ExpandPaddle";
        this.duration = 100; // Duration in game ticks

        // Create the Rectangle shape for JavaFX rendering
        Rectangle rectangle = new Rectangle(width, height);
        rectangle.setFill(Color.GREEN);
        rectangle.setStroke(Color.DARKGREEN);
        rectangle.setStrokeWidth(2);
        rectangle.setLayoutX(x);
        rectangle.setLayoutY(y);
        this.shape = rectangle;
    }

    @Override
    public void applyEffect(MovableObject paddle) {
        ((Paddle) paddle).expandPaddle();
    }

    @Override
    public void removeEffect(MovableObject paddle) {
        ((Paddle) paddle).restorePaddleSize();
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
