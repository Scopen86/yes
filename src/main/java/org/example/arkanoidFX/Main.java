import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import gamemanager.*;
import gameobject.GameObject;
import gameobject.brick.*;
import gameobject.movable.*;
import gameobject.powerup.*;


/**
 * Main entry point for the Arkanoid game.
 * Handles the game loop and user input.
 */
public class Main {
    // Mutable target FPS so it can be adjusted at runtime via + / - or command-line
    private static volatile int TARGET_FPS = 5; // default, can be overridden by args

    public static void main(String[] args) {



        System.out.println("Welcome to Arkanoid!");
        System.out.println("Loading game...\n");

        // Parse optional command-line FPS setting: --fps=N or a single numeric arg
        if (args != null && args.length > 0) {
            for (String a : args) {
                if (a.startsWith("--fps=")) {
                    String v = a.substring("--fps=".length());
                    try {
                        int parsed = Integer.parseInt(v);
                        if (parsed > 0) TARGET_FPS = parsed;
                    } catch (NumberFormatException ignored) {
                    }
                } else {
                    // allow plain numeric first arg
                    try {
                        int parsed = Integer.parseInt(a);
                        if (parsed > 0) TARGET_FPS = parsed;
                    } catch (NumberFormatException ignored) {
                    }
                }
            }
        }

        System.out.println("Starting with target FPS: " + TARGET_FPS + " (use + / - to adjust)");

        GameManager gameManager = new GameManager();

        // Create input thread for non-blocking input (single reader)
        InputHandler inputHandler = new InputHandler(gameManager);
        Thread inputThread = new Thread(inputHandler);
        inputThread.setDaemon(true);
        inputThread.start();

        // Main game loop
        boolean running = true;
        while (running) {
            long frameStart = System.currentTimeMillis();

            // Render current state
            gameManager.render();

            // Update game state
            if (gameManager.getGameState().equals("PLAYING")) {
                gameManager.updateGame();
            }

            // Frame rate control (compute per-loop from mutable TARGET_FPS)
            int fps = TARGET_FPS;
            if (fps <= 0) fps = 1;
            long frameTimeMs = 1000L / fps;

            long frameTime = System.currentTimeMillis() - frameStart;
            long sleepTime = frameTimeMs - frameTime;

            if (sleepTime > 0) {
                try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        System.out.println("\nThanks for playing Arkanoid!");
    }

    /**
     * Inner class to handle input in a separate thread.
     * Uses a blocking readLine() so we don't poll System.in.available().
     * Also handles runtime FPS adjustments using '+' and '-' lines.
     */
    static class InputHandler implements Runnable {
        private GameManager gameManager;
        private volatile boolean running = true;

        public InputHandler(GameManager gameManager) {
            this.gameManager = gameManager;
        }

        @Override
        public void run() {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
                String line;
                while (running && (line = reader.readLine()) != null) {
                    String input = line.trim();
                    if (input.length() == 0) continue;

                    // allow runtime FPS control with "+" and "-"
                    if (input.equals("+") || input.equalsIgnoreCase("f+")) {
                        TARGET_FPS = Math.min(60, TARGET_FPS + 1);
                        System.out.println("Target FPS -> " + TARGET_FPS);
                        continue;
                    } else if (input.equals("-") || input.equalsIgnoreCase("f-")) {
                        TARGET_FPS = Math.max(1, TARGET_FPS - 1);
                        System.out.println("Target FPS -> " + TARGET_FPS);
                        continue;
                    }

                    // If we're in MENU or GAME_OVER allow start/quit directly from here
                    String state = gameManager.getGameState();
                    if ((input.equalsIgnoreCase("s") || input.equalsIgnoreCase("start"))
                            && (state.equals("MENU") || state.equals("GAME_OVER"))) {
                        gameManager.startGame();
                        continue;
                    }

                    if ((input.equalsIgnoreCase("q") || input.equalsIgnoreCase("quit"))
                            && state.equals("MENU")) {
                        // Exit the JVM gracefully
                        System.out.println("Quitting...");
                        System.exit(0);
                    }

                    // Fallback: let game manager handle the input (movement, pause, etc.)
                    gameManager.handleInput(input);
                }
            } catch (IOException e) {
                // If console reading fails, nothing else we can do here
            }
        }

        public void stop() {
            running = false;
        }
    }
}
