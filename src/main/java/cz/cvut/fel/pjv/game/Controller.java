package cz.cvut.fel.pjv.game;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.cvut.fel.pjv.game.model.Block;
import cz.cvut.fel.pjv.game.model.Model;
import cz.cvut.fel.pjv.game.view.View;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Controller extends Application {
    private View view;
    private Model model;
    private static final Logger LOGGER = Logger.getLogger(Controller.class.getName());
    private final int FPS = 120;
    private final double timePerFrame = 1000000000.0 / FPS;
    private AnimationTimer gameLoop;

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws Exception {
        LOGGER.setLevel(Level.FINEST);
        LOGGER.info("starting game");
        model = new Model(loadGameData(), this);
        view = new View(stage, model, this);
        view.showStartMenu();
        stage.addEventHandler(KeyEvent.KEY_PRESSED, (key) -> {
            if ("wasd".contains(key.getCode().toString().toLowerCase())) {
                model.movePlayer(key.getText().toLowerCase().charAt(0));
            }
        });
        stage.addEventHandler(KeyEvent.KEY_RELEASED, (key) -> {
            String letter = key.getCode().toString().toLowerCase();
            if (("w".equals(letter) && model.getPlayer().getFacing().equals("up")) || ("a".equals(letter) && model.getPlayer().getFacing().equals("left")) || ("s".equals(letter) && model.getPlayer().getFacing().equals("down")) || ("d".equals(letter) && model.getPlayer().getFacing().equals("right"))) {
                model.stopPlayer();
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
        gameLoop.start();
    }

    private GameData loadGameData() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        GameData readData = objectMapper.readValue(new File("src/main/resources/init.json"), GameData.class);
        LOGGER.log(Level.FINE, "config file succesfully loaded");
        return readData;
    }

    public void updateView() {
        view.draw();
    }

    public void updateModel() {
        model.update();
    }

}