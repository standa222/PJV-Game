package cz.cvut.fel.pjv.game;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.cvut.fel.pjv.game.model.Block;
import cz.cvut.fel.pjv.game.model.Model;
import cz.cvut.fel.pjv.game.view.View;
import javafx.application.Application;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class Controller extends Application {
    private View view;
    private Model model;
    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws Exception {
        model = new Model(loadGameData());
        view = new View(stage, model);
        view.showStartMenu();
        for (Block b : model.getBoard().getBlocks()) {
            System.out.println(b.getX());
        }
        stage.addEventHandler(KeyEvent.KEY_PRESSED, (key) -> {
            System.out.println(key.getCode());
            System.out.println("X: " + model.getPlayer().getPosX());
            System.out.println("Y: " + model.getPlayer().getPosY());
            System.out.println();
            if ("wasd".contains(key.getCode().toString().toLowerCase())) {
                model.movePlayer(key.getText().charAt(0));
                view.draw();
            }
        });
    }

    private GameData loadGameData() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        GameData readData = objectMapper.readValue(new File("src/main/resources/init.json"), GameData.class);
        return readData;
    }




}