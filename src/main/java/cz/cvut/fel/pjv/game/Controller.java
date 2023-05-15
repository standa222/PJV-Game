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

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws Exception {
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
     * Load game data from .json configuration file.
     *
     * @return returns data read from the file that are essential for creating model
     * @throws IOException if file is not found
     */
    private GameData loadGameData() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        GameData readData = objectMapper.readValue(new File("src/main/resources/init.json"), GameData.class);   // for testing use easyinit.json
        LOGGER.info("Configuration file loaded");
        return readData;
    }

    private void savePlayerData() {
        ObjectMapper objectMapper = new ObjectMapper();
        String playerData = String.valueOf(model.getPlayer().getMax_hp()) + " " + String.valueOf(model.getPlayer().getAttackDamage()) + " " + String.valueOf(model.getPlayer().getAttackSpeed()) + " " + String.valueOf(model.getPlayer().getMaxMovementSpeed());
        try {
            objectMapper.writeValue(new File("src/main/resources/player.txt"), playerData);
        } catch (IOException e) {
            LOGGER.severe("Couldn't save player data!");
        }
    }

    public void updateView() {
        view.draw();
    }

    public void redrawBoard() {
        view.drawBoard();
    }

    public void updateModel() {
        model.update();
    }

    public void startGameLoop() {
        gameLoop.start();
        paused = false;
        running = true;
    }

    public void pauseGame() {
        if (model.allEnemiesDead()) {
            ggwp();
        }
        gameLoop.stop();
        paused = true;
    }

    public void resumeGame() {
        gameLoop.start();
        paused = false;
    }

    public void gameOver() {
        running = false;
        gameLoop.stop();
        view.badEnd();
    }

    public void ggwp() {
        running = false;
        gameLoop.stop();
        savePlayerData();
        view.goodEnd();
    }

    public void upgrade() {
        updateView();
        gameLoop.stop();
        paused = true;
        view.upgrade();
    }
}