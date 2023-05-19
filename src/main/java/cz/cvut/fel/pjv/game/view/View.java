package cz.cvut.fel.pjv.game.view;

import cz.cvut.fel.pjv.game.Controller;
import cz.cvut.fel.pjv.game.model.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
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
    private final HashMap<Integer,String> graphicsMap;
    private final HashMap<String, Integer> rotateMap;
    private final Controller controller;
    private Pane pane;
    private Pane board;

    public View(Stage stage, Model model, Controller controller) {
        this.stage = stage;
        this.model = model;
        sceneWidth = BLOCK_SIZE * model.getBoardWidth();
        sceneHeight = BLOCK_SIZE * model.getBoardHeight();
        this.controller = controller;
        graphicsMap = new HashMap<>();
        graphicsMap.put(6, "grass.png");
        graphicsMap.put(1, "stone.png");
        graphicsMap.put(3, "water.jpeg");
        graphicsMap.put(5, "chest.jpg");
        rotateMap = new HashMap<>();
        rotateMap.put("up", 180);
        rotateMap.put("down", 0);
        rotateMap.put("left", 90);
        rotateMap.put("right", 270);
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
                drawBoard();
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
        pane.getChildren().add(board);
        drawPlayer(pane);
        drawEnemies(pane);
        drawProjectiles(pane);
        drawItems(pane);
        drawInventoryHP(pane);
        if (model.allEnemiesDead()) {
            end(pane);
        }
        stage.setScene(new Scene(pane, sceneWidth, sceneHeight));
    }

    public void drawBoard() {
        board = new Pane();
        for (Block block : model.getBoard().getBlocks()) {
            ImageView iv = new ImageView();
            iv.setImage(new Image(graphicsMap.get(block.getValue())));
            iv.setFitWidth(BLOCK_SIZE);
            iv.setFitHeight(BLOCK_SIZE);
            iv.setLayoutX((block.getX()-1) * BLOCK_SIZE);
            iv.setLayoutY((block.getY()-1) * BLOCK_SIZE);
            board.getChildren().add(iv);
        }
    }

    public void drawPlayer(Pane pane) {
        ImageView player = new ImageView();
        player.setImage(new Image("player.png"));
        player.setFitWidth(BLOCK_SIZE);
        player.setFitHeight(BLOCK_SIZE);
        player.setRotate(rotateMap.get(model.getPlayer().getFacing()));
        player.setLayoutX((model.getPlayer().getPosX()-1) * BLOCK_SIZE);
        player.setLayoutY((model.getPlayer().getPosY()-1) * BLOCK_SIZE);
        pane.getChildren().add(player);
    }

    public void drawEnemies(Pane pane) {
        for (Enemy enemy : model.getEnemies()) {
            ImageView e = new ImageView();
            e.setImage(new Image("enemy.jpg"));
            e.setFitWidth(BLOCK_SIZE);
            e.setFitHeight(BLOCK_SIZE);
            e.setLayoutX((enemy.getPosX()-1) * BLOCK_SIZE);
            e.setLayoutY((enemy.getPosY()-1) * BLOCK_SIZE);
            pane.getChildren().add(e);
        }
    }

    public void drawProjectiles(Pane pane) {
        for (Projectile p : model.getPlayer().getProjectiles()) {
            drawProjectile(pane, p);
        }
        for (Enemy e : model.getEnemies()) {
            for (Projectile p : e.getProjectiles()) {
                drawProjectile(pane, p);
            }
        }
    }

    public void drawProjectile(Pane pane, Projectile p) {
        ImageView arrow = new ImageView();
        arrow.setImage(new Image("arrow.png"));
        arrow.setFitWidth(BLOCK_SIZE);
        arrow.setFitHeight(BLOCK_SIZE);
        arrow.setLayoutX((p.getPosX()-1) * BLOCK_SIZE);
        arrow.setLayoutY((p.getPosY()-1) * BLOCK_SIZE);
        arrow.setRotate(rotateMap.get(p.getDirection())+90);
        pane.getChildren().add(arrow);
    }

    public void drawItems(Pane pane) {
        for (Item item : model.getItems()) {
            if (item instanceof Coin) {
                ImageView coin = new ImageView();
                coin.setImage(new Image("coin.png", 30, 0, true, false));
                coin.setLayoutX(item.getPosX()*BLOCK_SIZE-BLOCK_SIZE/2-15);
                coin.setLayoutY(item.getPosY()*BLOCK_SIZE-BLOCK_SIZE/2-15);
                pane.getChildren().add(coin);
            } else if (item instanceof Key) {
                ImageView key = new ImageView();
                key.setImage(new Image("key.png", 30, 0, true, false));
                key.setLayoutX(item.getPosX()*BLOCK_SIZE-BLOCK_SIZE/2-15);
                key.setLayoutY(item.getPosY()*BLOCK_SIZE-BLOCK_SIZE/2-5);
                pane.getChildren().add(key);
            } else if (item instanceof HealingPotion) {
                ImageView heart = new ImageView();
                heart.setImage(new Image("heart.png", 30, 0, true, false));
                heart.setLayoutX(item.getPosX()*BLOCK_SIZE-BLOCK_SIZE/2-15);
                heart.setLayoutY(item.getPosY()*BLOCK_SIZE-BLOCK_SIZE/2-15);
                pane.getChildren().add(heart);
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
                controller.resumeGame();
            }
        });
        pane.getChildren().addAll(upgradeADButton,upgradeASButton, upgradeHPButton, upgradeMSButton);
    }

    public void end(Pane pane) {
        Text endText = new Text("Press ESC to end level");
        endText.setFont(new Font(35));
        endText.setLayoutX(sceneWidth/2 - 190);
        endText.setLayoutY(130);
        pane.getChildren().add(endText);
    }

    public void goodEnd() {
        Rectangle goodEnd = new Rectangle(sceneWidth, sceneHeight);
        goodEnd.setFill(Color.GREEN);
        goodEnd.setOpacity(0.5);
        Text goodEndText = new Text("LEVEL COMPLETED");
        goodEndText.setFont(new Font(80));
        Button goodEndButton = new Button("Exit the game");
        goodEndButton.setMinSize(220, 50);
        goodEndButton.setLayoutX(sceneWidth/2-110);
        goodEndButton.setLayoutY(sceneHeight/2+50);
        goodEndButton.setFont(new Font(25));
        VBox vBox = new VBox(20, goodEndText, goodEndButton);
        vBox.setAlignment(Pos.CENTER);
        vBox.setMinSize(sceneWidth, sceneHeight);
        goodEndButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                System.exit(0);
            }
        });
        pane.getChildren().addAll(goodEnd, vBox);
    }

    public void badEnd() {
        Rectangle badEnd = new Rectangle(sceneWidth, sceneHeight);
        badEnd.setFill(Color.RED);
        badEnd.setOpacity(0.4);
        Text badEndText = new Text("GAME OVER");
        badEndText.setFont(new Font(80));
        Button badEndButton = new Button("Exit the game");
        badEndButton.setMinSize(220, 50);
        badEndButton.setLayoutX(sceneWidth/2-110);
        badEndButton.setLayoutY(sceneHeight/2+50);
        badEndButton.setFont(new Font(25));
        VBox vBox = new VBox(20, badEndText, badEndButton);
        vBox.setAlignment(Pos.CENTER);
        vBox.setMinSize(sceneWidth, sceneHeight);
        badEndButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                System.exit(0);
            }
        });
        pane.getChildren().addAll(badEnd, vBox);
    }
}
