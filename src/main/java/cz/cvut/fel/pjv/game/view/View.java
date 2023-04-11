package cz.cvut.fel.pjv.game.view;

import cz.cvut.fel.pjv.game.Controller;
import cz.cvut.fel.pjv.game.model.Block;
import cz.cvut.fel.pjv.game.model.Enemy;
import cz.cvut.fel.pjv.game.model.Model;
import cz.cvut.fel.pjv.game.model.Projectile;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.shape.Rectangle;
import java.util.HashMap;

public class View {
    private Canvas canvas;
    private int sceneWidth = 1000;
    private int sceneHeight = 800;
    private final Stage stage;
    private Model model;
    private HashMap<Integer,Color> colorMap;
    private int BLOCK_SIZE = 50;
    private final Controller controller;

    public View(Stage stage, Model model, Controller controller) {
        this.stage = stage;
        this.model = model;
        colorMap = new HashMap<>();
        colorMap.put(0, Color.GREEN);
        colorMap.put(1, Color.DARKGRAY);
        colorMap.put(2, Color.BLUE);
        this.controller = controller;
    }

    public void showStartMenu() {
        Button startGameButton = new Button("Start the game");
        startGameButton.setPadding(new Insets(16));
        startGameButton.setFont(new Font(30));
        startGameButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                draw();
            }
        });
        ImageView menuImage = new ImageView();
        menuImage.setImage(new Image("menu.jpeg"));

        StackPane stackPane = new StackPane();
        stackPane.getChildren().addAll(menuImage,startGameButton);

        VBox root = new VBox();
        root.getChildren().add(stackPane);

        Scene startScene = new Scene(root, sceneWidth, sceneHeight);
        stage.setTitle("Menu hry");
        stage.setScene(startScene);
        stage.setResizable(false);
        stage.show();
    }

    public void draw() {
        Pane pane = new Pane();
        drawBoard(pane);
        drawPlayer(pane);
        drawEnemies(pane);
        drawProjectiles(pane);
        stage.setScene(new Scene(pane, sceneWidth, sceneHeight));
    }

    public void drawBoard(Pane pane) {
        for (Block block : model.getBoard().getBlocks()) {
            Rectangle r = new Rectangle(block.getX()*BLOCK_SIZE, block.getY()*BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE);
            r.setFill(colorMap.get(block.getValue()));
            pane.getChildren().add(r);
        }
    }

    public void drawPlayer(Pane pane) {
        Circle player = new Circle(model.getPlayer().getPosX()*BLOCK_SIZE-BLOCK_SIZE/2, model.getPlayer().getPosY()*BLOCK_SIZE-BLOCK_SIZE/2, 20);
        player.setFill(Color.YELLOW);
        pane.getChildren().add(player);
    }

    public void drawEnemies(Pane pane) {
        for (Enemy enemy : model.getEnemies()) {
            Circle c = new Circle(enemy.getPosX()*BLOCK_SIZE-BLOCK_SIZE/2, enemy.getPosY()*BLOCK_SIZE-BLOCK_SIZE/2, 20);
            c.setFill(Color.BLACK);
            pane.getChildren().add(c);
        }
    }

    public void drawProjectiles(Pane pane) {
        for (Projectile p : model.getPlayer().getProjectiles()) {
            Circle c = new Circle(p.getPosX()*BLOCK_SIZE-BLOCK_SIZE/2, p.getPosY()*BLOCK_SIZE-BLOCK_SIZE/2, 5);
            pane.getChildren().add(c);
        }
        for (Enemy e : model.getEnemies()) {
            for (Projectile p : model.getPlayer().getProjectiles()) {
                Circle c = new Circle(p.getPosX()*BLOCK_SIZE-BLOCK_SIZE/2, p.getPosY()*BLOCK_SIZE-BLOCK_SIZE/2, 5);
                pane.getChildren().add(c);
            }
        }
    }
}
