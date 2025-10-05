package org.example.arkanoidFX.gameobject.brick;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;


/**
 * Normal brick that breaks after one hit.
 * Demonstrates OOP principle: Inheritance and Polymorphism
 */
public class NormalBrick extends Brick {
    public NormalBrick(int x, int y, int width, int height) {
        super(x, y, width, height);
        this.hitPoints = 1;
        this.type = "Normal";
        this.scoreValue = 10;

        // Create the Rectangle shape for JavaFX rendering
        Rectangle rectangle = new Rectangle(width, height);
        rectangle.setFill(Color.RED);
        rectangle.setStroke(Color.BLACK);
        rectangle.setStrokeWidth(1);
        rectangle.setLayoutX(x);
        rectangle.setLayoutY(y);
        this.shape = rectangle;
    }

    @Override
    public void takeHit() {
        hitPoints--;
    }

    @Override
    public boolean isDestroyed() {
        return hitPoints <= 0;
    }

    @Override
    public void update() {
        // Normal bricks don't need updating
    }

    @Override
    public void render() {
        // Render method for terminal
    }
}
