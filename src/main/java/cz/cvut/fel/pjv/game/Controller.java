package cz.cvut.fel.pjv.game;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.cvut.fel.pjv.game.model.Model;
import cz.cvut.fel.pjv.game.view.View;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

public class Controller extends Application {
    private View view;
    private Model model;
    private static final Logger LOGGER = Logger.getLogger(Controller.class.getName());
    private final int FPS = 120;
    private final double timePerFrame = 1000000000.0 / FPS;
    private AnimationTimer gameLoop;
    private boolean paused = true;
    private boolean running = false;

    public Controller() {
    }
    public static void main(String[] args) {
        launch();
    }

    /**

     Starts the game by initializing the necessary components and setting up event handlers.

     @param stage The JavaFX stage for displaying the game.

     @throws IOException if an error occurs during initialization (loading data from config file).
     */
    @Override
    public void start(Stage stage) throws IOException {
        LOGGER.info("starting game");
        model = new Model(loadGameData(), this);

        if (new File("src/main/resources/player.txt").exists()) {
            model.loadPlayerData();
        }
        view = new View(stage, model, this);
        view.showStartMenu();
        stage.addEventHandler(KeyEvent.KEY_PRESSED, (key) -> {
            if (running && !paused) {
                if ("wasd".contains(key.getCode().toString().toLowerCase())) {
                    model.movePlayer(key.getText().toLowerCase().charAt(0));
                }
            }
        });
        stage.addEventHandler(KeyEvent.KEY_RELEASED, (key) -> {
            String letter = key.getCode().toString().toLowerCase();
            if (("w".equals(letter) && model.getPlayer().getFacing().equals("up")) || ("a".equals(letter) && model.getPlayer().getFacing().equals("left")) || ("s".equals(letter) && model.getPlayer().getFacing().equals("down")) || ("d".equals(letter) && model.getPlayer().getFacing().equals("right"))) {
                model.stopPlayer();
            }
        });
        stage.addEventHandler(KeyEvent.KEY_PRESSED, (key) -> {
            if (key.getCode()==KeyCode.ESCAPE) {
                if (!paused) {
                    pauseGame();
                } else if (running) {
                    resumeGame();
                }
            }
        });
        gameLoop = new AnimationTimer() {
            private long lastFrame = 0;
            @Override
            public void handle(long now) {
                if (now - lastFrame > timePerFrame) {
                    updateView();
                    updateModel();
                    lastFrame = now;
                }
            }
        };
    }

    /**
     * Loads the game data from a configuration file.
     *
     * @return The loaded GameData object containing essential dat for creating Model.
     * @throws IOException if an error occurs while reading the file.
     */
    private GameData loadGameData() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        GameData readData = objectMapper.readValue(new File("src/main/resources/init.json"), GameData.class);   // for testing use easyinit.json
        LOGGER.info("Configuration file loaded");
        return readData;
    }

    /**
     * Saves the player data to a file.
     */
    private void savePlayerData() {
        ObjectMapper objectMapper = new ObjectMapper();
        String playerData = String.valueOf(model.getPlayer().getMax_hp()) + " " + String.valueOf(model.getPlayer().getAttackDamage()) + " " + String.valueOf(model.getPlayer().getAttackSpeed()) + " " + String.valueOf(model.getPlayer().getMaxMovementSpeed());
        try {
            objectMapper.writeValue(new File("src/main/resources/player.txt"), playerData);
        } catch (IOException e) {
            LOGGER.severe("Couldn't save player data!");
        }
    }

    /**
     * Updates the game view by triggering a redraw.
     */
    public void updateView() {
        view.draw();
    }

    /**
     * Redraws the game board if view is defined.
     */
    public void redrawBoard() {
        if (view !=null) {
            view.drawBoard();
        }
    }

    /**
     * Updates the game model.
     */
    public void updateModel() {
        model.update();
    }

    /**
     * Starts the game loop, allowing the game to run continuously.
     * Sets the game loop state to running and not paused.
     */
    public void startGameLoop() {
        gameLoop.start();
        paused = false;
        running = true;
    }

    /**
     * Pauses the game.
     * Stops the game loop and sets game to paused state.
     * If all enemies are dead, triggers the ending method.
     */
    public void pauseGame() {
        if (model.allEnemiesDead()) {
            ggwp();
        }
        gameLoop.stop();
        paused = true;
    }

    /**
     * Resumes the game
     */
    public void resumeGame() {
        gameLoop.start();
        paused = false;
    }

    /**
     * Handles the game over condition. Ends the game.
     * Sets the running state to false, stops the game loop, and triggers the "badEnd" method in the view.
     */
    public void gameOver() {
        running = false;
        gameLoop.stop();
        view.badEnd();
    }

    /**
     * Handles the "ggwp" (good game, well played) condition. Ends the game.
     * Sets the running state to false, stops the game loop, saves the player data,
     * and triggers the "goodEnd" method in the view.
     */
    public void ggwp() {
        running = false;
        gameLoop.stop();
        savePlayerData();
        view.goodEnd();
    }


    /**
     * Handles the upgrade event.
     * Updates the game view, stops the game loop, sets the paused state to true,
     * and triggers the "upgrade" method in the view.
     */
    public void upgrade() {
        updateView();
        gameLoop.stop();
        paused = true;
        view.upgrade();
    }
}