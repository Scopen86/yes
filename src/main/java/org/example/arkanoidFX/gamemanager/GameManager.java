package org.example.arkanoidFX.gamemanager;

import org.example.arkanoidFX.gameobject.brick.NormalBrick;
import org.example.arkanoidFX.gameobject.brick.StrongBrick;
import org.example.arkanoidFX.gameobject.brick.Brick;
import org.example.arkanoidFX.gameobject.movable.Ball;
import org.example.arkanoidFX.gameobject.movable.Paddle;
import org.example.arkanoidFX.gameobject.powerup.PowerUp;
import org.example.arkanoidFX.gameobject.powerup.ExpandPaddlePowerUp;
import org.example.arkanoidFX.gameobject.powerup.FastBallPowerUp;
import renderer.Renderer;

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
    private static final int GAME_WIDTH = 60;
    private static final int GAME_HEIGHT = 30;
    private static final int PADDLE_WIDTH = 10;
    private static final int PADDLE_HEIGHT = 1;
    private static final int BALL_SIZE = 1;
    private static final int BRICK_WIDTH = 6;
    private static final int BRICK_HEIGHT = 2;

    // Game objects
    private Paddle paddle;
    private Ball ball;
    private List<Brick> bricks;
    private List<PowerUp> powerUps;
    private List<ActivePowerUp> activePowerUps; // Track active power-ups with duration

    // Game state
    private int score;
    private int lives;
    private int level;
    private String gameState; // "MENU", "PLAYING", "PAUSED", "GAME_OVER", "WIN"
    private Renderer renderer;
    private Random random;

    public Ball getBall() {
        return ball;
    }

    public Paddle getPaddle() {
        return paddle;
    }

    public List<Brick> getBricks() {
        return bricks;
    }

    public void update() {
    }

    public String getScore() {
        return "Score: " + score + "  Lives: " + lives + "  Level: " + level;
    }

    // Inner class to track active power-ups with remaining duration
    private class ActivePowerUp {
        PowerUp powerUp;
        int remainingTicks;

        ActivePowerUp(PowerUp powerUp, int duration) {
            this.powerUp = powerUp;
            this.remainingTicks = duration;
        }
    }

    public GameManager() {
        this.renderer = new Renderer(GAME_WIDTH, GAME_HEIGHT);
        this.random = new Random();
        this.bricks = new ArrayList<>();
        this.powerUps = new ArrayList<>();
        this.activePowerUps = new ArrayList<>();
        this.gameState = "MENU";
    }

    public void startGame() {
        this.score = 0;
        this.lives = 3;
        this.level = 1;
        this.gameState = "PLAYING";
        initializeLevel();
    }

    private void initializeLevel() {
        // Create paddle
        int paddleX = (GAME_WIDTH - PADDLE_WIDTH) / 2;
        int paddleY = GAME_HEIGHT - 3;
        paddle = new Paddle(paddleX, paddleY, PADDLE_WIDTH, PADDLE_HEIGHT, GAME_WIDTH);

        // Create ball
        int ballX = GAME_WIDTH / 2;
        int ballY = GAME_HEIGHT - 5;
        ball = new Ball(ballX, ballY, BALL_SIZE, BALL_SIZE, GAME_WIDTH, GAME_HEIGHT);

        // Create bricks
        bricks.clear();
        powerUps.clear();
        activePowerUps.clear();

        createBricks();
    }

    private void createBricks() {
        int rows = 5 + level; // More rows as level increases
        int cols = 8;
        int startY = 2;
        int spacing = 1;

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                int x = col * (BRICK_WIDTH + spacing) + 2;
                int y = row * (BRICK_HEIGHT + spacing) + startY;

                // Create mix of normal and strong bricks
                if (random.nextInt(100) < 20) { // 20% strong bricks
                    bricks.add(new StrongBrick(x, y, BRICK_WIDTH, BRICK_HEIGHT));
                } else {
                    bricks.add(new NormalBrick(x, y, BRICK_WIDTH, BRICK_HEIGHT));
                }
            }
        }
    }

    public void updateGame() {
        if (!gameState.equals("PLAYING")) {
            return;
        }

        // Update game objects
        paddle.update();
        ball.update();

        // Update power-ups (falling)
        for (PowerUp powerUp : powerUps) {
            powerUp.update();
        }

        // Update active power-up durations
        updateActivePowerUps();

        // Check collisions
        checkCollisions();

        // Check if ball fell off screen
        if (ball.getY() >= GAME_HEIGHT) {
            loseLife();
        }

        // Check win condition
        if (bricks.isEmpty()) {
            levelComplete();
        }
    }

    private void updateActivePowerUps() {
        Iterator<ActivePowerUp> iterator = activePowerUps.iterator();
        while (iterator.hasNext()) {
            ActivePowerUp active = iterator.next();
            active.remainingTicks--;

            if (active.remainingTicks <= 0) {
                active.powerUp.removeEffect(paddle);
                iterator.remove();
            }
        }
    }

    public void checkCollisions() {
        // Ball-Paddle collision
        if (ball.collidesWith(paddle)) {
            ball.bounceOffPaddle(paddle);
        }

        // Ball-Brick collision
        Iterator<Brick> brickIterator = bricks.iterator();
        while (brickIterator.hasNext()) {
            Brick brick = brickIterator.next();
            if (ball.collidesWith(brick)) {
                brick.takeHit();
                ball.bounceOffBrick();

                if (brick.isDestroyed()) {
                    score += brick.getScoreValue();

                    // Chance to drop power-up
                    if (random.nextInt(100) < 15) { // 15% chance
                        spawnPowerUp(brick.getX(), brick.getY());
                    }

                    brickIterator.remove();
                }
                break; // Only collide with one brick per frame
            }
        }

        // Paddle-PowerUp collision
        Iterator<PowerUp> powerUpIterator = powerUps.iterator();
        while (powerUpIterator.hasNext()) {
            PowerUp powerUp = powerUpIterator.next();

            // Remove power-ups that fell off screen
            if (powerUp.getY() >= GAME_HEIGHT) {
                powerUpIterator.remove();
                continue;
            }

            if (paddle.collidesWith(powerUp)) {
                activatePowerUp(powerUp);
                powerUpIterator.remove();
            }
        }
    }

    private void spawnPowerUp(int x, int y) {
        PowerUp powerUp;

        if (random.nextBoolean()) {
            powerUp = new ExpandPaddlePowerUp(x, y, 2, 1);
        } else {
            FastBallPowerUp fastBall = new FastBallPowerUp(x, y, 2, 1);
            powerUp = fastBall;
        }

        powerUps.add(powerUp);
    }

    private void activatePowerUp(PowerUp powerUp) {
        powerUp.applyEffect(paddle);
        activePowerUps.add(new ActivePowerUp(powerUp, powerUp.getDuration()));
    }

    private void loseLife() {
        lives--;

        if (lives <= 0) {
            gameOver();
        } else {
            // Reset ball and paddle
            int paddleX = (GAME_WIDTH - paddle.getWidth()) / 2;
            int paddleY = GAME_HEIGHT - 3;
            paddle.setX(paddleX);
            paddle.stop();

            int ballX = GAME_WIDTH / 2;
            int ballY = GAME_HEIGHT - 5;
            ball.reset(ballX, ballY);
        }
    }

    private void levelComplete() {
        level++;
        initializeLevel();
    }

    public void gameOver() {
        gameState = "GAME_OVER";
    }

    public void handleInput(String input) {
        if (input == null) return;

        switch (input.toLowerCase()) {
            case "a":
            case "left":
                if (gameState.equals("PLAYING")) {
                    paddle.moveLeft();
                }
                break;
            case "d":
            case "right":
                if (gameState.equals("PLAYING")) {
                    paddle.moveRight();
                }
                break;
            case "s":
            case "stop":
                if (gameState.equals("PLAYING")) {
                    paddle.stop();
                }
                break;
            case "p":
            case "pause":
                if (gameState.equals("PLAYING")) {
                    gameState = "PAUSED";
                } else if (gameState.equals("PAUSED")) {
                    gameState = "PLAYING";
                }
                break;
            case "q":
            case "quit":
                gameState = "MENU";
                break;
        }
    }

    public void render() {
        renderer.clear();

        if (gameState.equals("MENU")) {
            renderMenu();
        } else if (gameState.equals("GAME_OVER")) {
            renderGameOver();
        } else {
            renderGame();
        }

        renderer.display();
    }

    private void renderMenu() {
        renderer.drawText(GAME_WIDTH / 2 - 10, 8, "=== ARKANOID ===");
        renderer.drawText(GAME_WIDTH / 2 - 15, 12, "Press 'S' to Start Game");
        renderer.drawText(GAME_WIDTH / 2 - 12, 14, "Press 'Q' to Quit");
        renderer.drawText(GAME_WIDTH / 2 - 10, 18, "Controls:");
        renderer.drawText(GAME_WIDTH / 2 - 15, 19, "A/Left - Move Left");
        renderer.drawText(GAME_WIDTH / 2 - 15, 20, "D/Right - Move Right");
        renderer.drawText(GAME_WIDTH / 2 - 15, 21, "S - Stop Paddle");
        renderer.drawText(GAME_WIDTH / 2 - 15, 22, "P - Pause");
    }

    private void renderGameOver() {
        renderer.drawText(GAME_WIDTH / 2 - 8, 12, "GAME OVER!");
        renderer.drawText(GAME_WIDTH / 2 - 10, 14, "Final Score: " + score);
        renderer.drawText(GAME_WIDTH / 2 - 10, 16, "Level: " + level);
        renderer.drawText(GAME_WIDTH / 2 - 15, 18, "Press 'S' to Play Again");
        renderer.drawText(GAME_WIDTH / 2 - 12, 19, "Press 'Q' to Quit");
    }

    private void renderGame() {
        // Draw UI info
        renderer.drawText(2, 0, "Score: " + score + "  Lives: " + lives + "  Level: " + level);

        // Draw bricks
        for (Brick brick : bricks) {
            char symbol = brick instanceof StrongBrick ? '#' : '=';
            renderer.drawRect(brick.getX(), brick.getY(), brick.getWidth(), brick.getHeight(), symbol);
        }

        // Draw power-ups
        for (PowerUp powerUp : powerUps) {
            char symbol = powerUp instanceof ExpandPaddlePowerUp ? 'E' : 'F';
            renderer.drawChar(powerUp.getX(), powerUp.getY(), symbol);
        }

        // Draw paddle
        renderer.drawRect(paddle.getX(), paddle.getY(), paddle.getWidth(), paddle.getHeight(), '_');

        // Draw ball
        renderer.drawChar(ball.getX(), ball.getY(), 'O');

        // Draw game state
        if (gameState.equals("PAUSED")) {
            renderer.drawText(GAME_WIDTH / 2 - 6, GAME_HEIGHT / 2, "*** PAUSED ***");
        }
    }

    public String getGameState() {
        return gameState;
    }

    public void setGameState(String state) {
        this.gameState = state;
    }
}
