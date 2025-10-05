package org.example.arkanoidFX.gameobject.brick;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * Strong brick that requires multiple hits to destroy.
 * Demonstrates OOP principle: Inheritance and Polymorphism
 */
public class StrongBrick extends Brick {
    public StrongBrick(int x, int y, int width, int height) {
        super(x, y, width, height);
        this.hitPoints = 3;
        this.type = "Strong";
        this.scoreValue = 30;

        // Create the Rectangle shape for JavaFX rendering
        Rectangle rectangle = new Rectangle(width, height);
        rectangle.setFill(Color.ORANGE);
        rectangle.setStroke(Color.BLACK);
        rectangle.setStrokeWidth(1);
        rectangle.setLayoutX(x);
        rectangle.setLayoutY(y);
        this.shape = rectangle;
    }

    @Override
    public void takeHit() {
        hitPoints--;
        // Update color based on remaining hit points
        if (shape instanceof Rectangle) {
            Rectangle rect = (Rectangle) shape;
            if (hitPoints == 2) {
                rect.setFill(Color.YELLOW);
            } else if (hitPoints == 1) {
                rect.setFill(Color.LIGHTYELLOW);
            }
        }
    }

    @Override
    public boolean isDestroyed() {
        return hitPoints <= 0;
    }

    @Override
    public void update() {
        // Strong bricks don't need updating
    }

    @Override
    public void render() {
        // Render method for terminal
    }
}
