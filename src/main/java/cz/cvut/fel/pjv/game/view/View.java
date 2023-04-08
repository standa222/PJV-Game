package cz.cvut.fel.pjv.game.view;

import cz.cvut.fel.pjv.game.GameData;
import cz.cvut.fel.pjv.game.model.Model;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.Stage;

import javax.swing.text.Position;
import java.awt.*;


public class View {
    private final Stage stage;
    private final Model model;

    public View(Stage stage, Model model) {
        this.stage = stage;
        this.model = model;
    }

    public void showStartMenu() {
        Button startGameButton = new Button("Start the game");
        startGameButton.setPadding(new Insets(16));
        startGameButton.setFont(new Font(30));
        startGameButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                showLevelChoice();
            }
        });
        ImageView menuImage = new ImageView();
        menuImage.setImage(new Image("menu.jpeg"));

        StackPane stackPane = new StackPane();
        stackPane.getChildren().addAll(menuImage,startGameButton);

        VBox root = new VBox();
        root.getChildren().add(stackPane);

        Scene startScene = new Scene(root, 1000, 667);
        stage.setTitle("Menu hry");
        stage.setScene(startScene);
        stage.setResizable(false);
        stage.show();
    }

    public void showLevelChoice() {
        Button buttonOne = new Button("Level 1");
        buttonOne.setFont(new Font(30));
        buttonOne.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
            }
        });

        Button buttonTwo = new Button("Level 2");
        buttonTwo.setFont(new Font(30));

        Button buttonThree = new Button("Level 3");
        buttonThree.setFont(new Font(30));

        VBox levels = new VBox();
        levels.setSpacing(25);
        levels.setAlignment(Pos.CENTER);

        ImageView menuImage = new ImageView();
        menuImage.setImage(new Image("menu.jpeg"));

        StackPane stackPane = new StackPane();
        stackPane.getChildren().addAll(menuImage,levels);

        levels.getChildren().addAll(buttonOne,buttonTwo,buttonThree);
        stage.setScene(new Scene(stackPane, 1000, 667));
    }
}
