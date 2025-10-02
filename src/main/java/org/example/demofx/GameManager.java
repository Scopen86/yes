package org.example.demofx;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * Main game controller that manages game state, logic, and flow.
 * Demonstrates OOP principle: Encapsulation (manages all game objects internally)
 */
public class GameManager {
    // Game constants
    private static final int GAME_WIDTH = 800;
    private static final int GAME_HEIGHT = 600;
    private static final int PADDLE_WIDTH = 100;
    private static final int PADDLE_HEIGHT = 20;
    private static final int BALL_SIZE = 20;

    // Game objects
    private Paddle paddle;
    private Ball ball;
    private List<Brick> bricks;
    
    // Game state
    private int score;
    private Random random;

    public GameManager() {
        random = new Random();
        score = 0;

        // Initialize paddle
        paddle = new Paddle((GAME_WIDTH - PADDLE_WIDTH)/2, GAME_HEIGHT - PADDLE_HEIGHT - 30,
                            PADDLE_WIDTH, PADDLE_HEIGHT, GAME_WIDTH);

        // Initialize ball
        ball = new Ball(GAME_WIDTH/2 - BALL_SIZE/2, GAME_HEIGHT/2 - BALL_SIZE/2,
                        BALL_SIZE, BALL_SIZE, GAME_WIDTH, GAME_HEIGHT);

        // Initialize bricks
        bricks = new ArrayList<>();
        int rows = 5;
        int cols = 10;
        int brickWidth = GAME_WIDTH / cols;
        int brickHeight = 20;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                bricks.add(new NormalBrick(j * brickWidth, 50 + i * brickHeight,
                                           brickWidth - 2, brickHeight - 2));
            }
        }
    }

    // Getters
    public Paddle getPaddle() { return paddle; }
    public Ball getBall() { return ball; }
    public List<Brick> getBricks() { return bricks; }
    public int getScore() { return score; }

    public void update() {
        // Update game objects
        paddle.update();
        ball.update();

        // Check ball-paddle collision
        if (ball.collidesWith(paddle)) {
            ball.bounceOffPaddle(paddle);
        }

        // Check ball-brick collisions
        Iterator<Brick> it = bricks.iterator();
        while (it.hasNext()) {
            Brick brick = it.next();
            if (ball.collidesWith(brick)) {
                brick.takeHit();
                if (brick.isDestroyed()) {
                    score += brick.getScoreValue();
                    it.remove();
                }
                ball.bounceOffBrick();
                break; // Only hit one brick per frame
            }
        }

        // Reset ball if it falls off bottom
        if (ball.isOutOfBottom()) {
            ball.reset(GAME_WIDTH/2 - BALL_SIZE/2, GAME_HEIGHT/2 - BALL_SIZE/2);
        }
    }
}
