package cz.cvut.fel.pjv.game.view;

import cz.cvut.fel.pjv.game.Controller;
import cz.cvut.fel.pjv.game.model.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.shape.Rectangle;
import java.util.HashMap;

public class View {
    private final float BLOCK_SIZE = 50;
    private final float sceneWidth;
    private final float sceneHeight;
    private final Stage stage;
    private final Model model;
    private final HashMap<Integer,Color> colorMap;
    private final Controller controller;
    private Pane pane;

    public View(Stage stage, Model model, Controller controller) {
        this.stage = stage;
        this.model = model;
        colorMap = new HashMap<>();
        sceneWidth = BLOCK_SIZE * model.getBoardWidth();
        sceneHeight = BLOCK_SIZE * model.getBoardHeight();
        colorMap.put(6, Color.rgb(44,229,80));
        colorMap.put(1, Color.rgb(150,150,150));
        colorMap.put(3, Color.rgb(63,111,233));
        this.controller = controller;
    }

    /**
     * Show start menu
     */
    public void showStartMenu() {
        Button startGameButton = new Button("Start the game");
        startGameButton.setPadding(new Insets(16));
        startGameButton.setFont(new Font(30));
        startGameButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                controller.startGameLoop();
            }
        });
        ImageView menuImage = new ImageView();
        menuImage.setImage(new Image("menu.png"));

        StackPane stackPane = new StackPane();
        stackPane.getChildren().addAll(menuImage,startGameButton);

        VBox root = new VBox();
        root.getChildren().add(stackPane);

        Scene startScene = new Scene(root, sceneWidth, sceneHeight);
        stage.setTitle("GAME");
        stage.setScene(startScene);
        stage.setResizable(false);
        stage.show();
    }

    /**
     * Key method of View, draw everything and bring it on stage.
     */
    public void draw() {
        pane = new Pane();
        drawBoard(pane);
        drawPlayer(pane);
        drawEnemies(pane);
        drawProjectiles(pane);
        drawItems(pane);
        drawInventoryHP(pane);
        stage.setScene(new Scene(pane, sceneWidth, sceneHeight));
    }

    public void drawBoard(Pane pane) {
        for (Block block : model.getBoard().getBlocks()) {
            Rectangle r = new Rectangle((block.getX()-1)*BLOCK_SIZE, (block.getY()-1)*BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE);
            r.setFill(colorMap.get(block.getValue()));
            pane.getChildren().add(r);
        }
    }

    public void drawPlayer(Pane pane) {
        Circle player = new Circle(model.getPlayer().getPosX()*BLOCK_SIZE-BLOCK_SIZE/2, model.getPlayer().getPosY()*BLOCK_SIZE-BLOCK_SIZE/2, 25);
        player.setFill(Color.YELLOW);
        pane.getChildren().add(player);
    }

    public void drawEnemies(Pane pane) {
        for (Enemy enemy : model.getEnemies()) {
            Circle c = new Circle(enemy.getPosX()*BLOCK_SIZE-BLOCK_SIZE/2, enemy.getPosY()*BLOCK_SIZE-BLOCK_SIZE/2, 25);
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
            for (Projectile p : e.getProjectiles()) {
                Circle c = new Circle(p.getPosX()*BLOCK_SIZE-BLOCK_SIZE/2, p.getPosY()*BLOCK_SIZE-BLOCK_SIZE/2, 5);
                pane.getChildren().add(c);
            }
        }
    }

    public void drawItems(Pane pane) {
        for (Item item : model.getItems()) {
            if (item instanceof Coin) {
                Circle c = new Circle(item.getPosX()*BLOCK_SIZE-BLOCK_SIZE/2, item.getPosY()*BLOCK_SIZE-BLOCK_SIZE/2, 5);
                c.setFill(Color.YELLOW);
                pane.getChildren().add(c);
            } else if (item instanceof Key) {
                Rectangle r = new Rectangle((int)item.getPosX()*BLOCK_SIZE-BLOCK_SIZE/2, (int)item.getPosY()*BLOCK_SIZE-BLOCK_SIZE/2, 25,10);
                r.setFill(Color.YELLOW);
                pane.getChildren().add(r);
            } else if (item instanceof HealingPotion) {
                Circle c = new Circle(item.getPosX()*BLOCK_SIZE-BLOCK_SIZE/2, item.getPosY()*BLOCK_SIZE-BLOCK_SIZE/2, 5);
                c.setFill(Color.RED);
                pane.getChildren().add(c);
            }
        }
    }

    public void drawInventoryHP(Pane pane) {
        int hp = model.getPlayer().getHp();
        int coinValue = model.getPlayer().getCoinValue();
        ImageView hpImage = new ImageView();
        hpImage.setImage(new Image("heart.png", 94, 0, true, false));
        hpImage.setLayoutX(27);
        hpImage.setLayoutY(sceneHeight-100);
        ImageView coinImage = new ImageView();
        coinImage.setImage(new Image("coin.png", 94, 0, true, false));
        coinImage.setLayoutX(27);
        coinImage.setLayoutY(sceneHeight-210);
        Text hpText = new Text(140, sceneHeight-45, String.valueOf(hp));
        hpText.setFont(new Font(40));
        Text coinText = new Text(140, sceneHeight-148, String.valueOf(coinValue));
        coinText.setFont(new Font(40));
        if (model.getPlayer().hasKey()) {
            ImageView keyImage = new ImageView();
            keyImage.setImage(new Image("key.png", 94, 0, true, false));
            keyImage.setLayoutX(27);
            keyImage.setLayoutY(sceneHeight-270);
            pane.getChildren().add(keyImage);
        }
        pane.getChildren().addAll(hpImage, coinImage, hpText, coinText);
    }

    public void upgrade() {
        int fontSize = 20;
        int padding = 16;
        float minWidth = 350;
        float minHeight = 70;
        Button upgradeADButton = new Button("Upgrade damage");
        upgradeADButton.setPadding(new Insets(padding));
        upgradeADButton.setFont(new Font(fontSize));
        upgradeADButton.setMinWidth(minWidth);
        upgradeADButton.setMinHeight(minHeight);
        upgradeADButton.setLayoutX((sceneWidth/4)-(minWidth/2));
        upgradeADButton.setLayoutY((sceneHeight/4)-(minHeight/2));
        upgradeADButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                model.getPlayer().upgradeAD();
                controller.resumeGame();
            }
        });

        Button upgradeASButton = new Button("Upgrade attack speed");
        upgradeASButton.setPadding(new Insets(padding));
        upgradeASButton.setFont(new Font(fontSize));
        upgradeASButton.setMinWidth(minWidth);
        upgradeADButton.setMinHeight(minHeight);
        upgradeASButton.setLayoutX((3*sceneWidth/4)-(minWidth/2));
        upgradeASButton.setLayoutY((sceneHeight/4)-(minHeight/2));
        upgradeASButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                model.getPlayer().upgradeAS();
                controller.resumeGame();
            }
        });

        Button upgradeHPButton = new Button("Upgrade max HP");
        upgradeHPButton.setPadding(new Insets(padding));
        upgradeHPButton.setFont(new Font(fontSize));
        upgradeHPButton.setMinWidth(minWidth);
        upgradeADButton.setMinHeight(minHeight);
        upgradeHPButton.setLayoutX((sceneWidth/4)-(minWidth/2));
        upgradeHPButton.setLayoutY((3*sceneHeight/4)-(minHeight/2));
        upgradeHPButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                model.getPlayer().upgradeHP();
                controller.resumeGame();
            }
        });

        Button upgradeMSButton = new Button("Upgrade movement speed");
        upgradeMSButton.setPadding(new Insets(padding));
        upgradeMSButton.setFont(new Font(fontSize));
        upgradeMSButton.setMinWidth(minWidth);
        upgradeADButton.setMinHeight(minHeight);
        upgradeMSButton.setLayoutX((3*sceneWidth/4)-(minWidth/2));
        upgradeMSButton.setLayoutY((3*sceneHeight/4)-(minHeight/2));
        upgradeMSButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                model.getPlayer().upgradeMS();
                model.getPlayer().getCoin().setValue(model.getPlayer().getCoinValue()-100);
                controller.resumeGame();
            }
        });

        pane.getChildren().addAll(upgradeADButton,upgradeASButton, upgradeHPButton, upgradeMSButton);
    }

    public void endButton() {
        Button endButton = new Button("End level");
        endButton.setPadding(new Insets(16));
        endButton.setFont(new Font(20));
        endButton.setMinWidth(350);
        endButton.setMinHeight(70);
        endButton.setLayoutX(sceneWidth/2 - 350/2);
        endButton.setLayoutY(100);
        endButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                controller.ggwp();
            }
        });

    }

    public void goodEnd() {

    }

    public void badEnd() {

    }
}
