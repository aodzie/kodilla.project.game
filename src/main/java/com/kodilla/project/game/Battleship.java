package com.kodilla.project.game;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import java.util.Random;
import com.kodilla.project.game.Board.Cell;

public class Battleship extends Application {
    private Label enemyLbl = new Label("Plansza przeciwnika");
    private Label playerLbl = new Label("Twoja plansza");
    private Image imageback = new Image("file:src/main/resources/water.png");

    private boolean running = false;
    private Board playersBoard;
    private Board enemysBoard;
    private int numberOfShips = 5;
    private boolean enemysTurn = false;
    private Random random = new Random();

    private Parent createContent() {

        BackgroundSize backgroundSize = new BackgroundSize(100, 100, true, true,
                true, false);
        BackgroundImage backgroundImage = new BackgroundImage(imageback, BackgroundRepeat.REPEAT,
                BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
        Background background = new Background(backgroundImage);


        enemyLbl.setTextFill(Color.web("#000"));
        enemyLbl.setFont(new Font("Arial", 24));

        playerLbl.setTextFill(Color.web("#000"));
        playerLbl.setFont(new Font("Arial", 24));

        BorderPane borderPane = new BorderPane();
        borderPane.setBackground(background);
        borderPane.setPrefSize(600, 800);
        borderPane.setTop(playerLbl);
        borderPane.setBottom(enemyLbl);

        enemysBoard = new Board(true, event -> {
            if (!running)
                return;

            Cell cell = (Cell) event.getSource();
            if (cell.wasShot)
                return;

            enemysTurn = !cell.shoot();

            if (enemysBoard.shipsNumber == 0) {
                System.out.println("WYGRALES");
            }

            if (enemysTurn)
                OpponentMove();
        });

        playersBoard = new Board(false, event -> {
            if (running)
                return;

            Cell cell = (Cell) event.getSource();
            if (playersBoard.placeShip(new Ship(numberOfShips, event.getButton() == MouseButton.PRIMARY),
                    cell.x, cell.y)) {
                if (--numberOfShips == 0) {
                    placeEnemyShips();
                }
            }
        });

        VBox vbox = new VBox(50, playersBoard, enemysBoard );
        vbox.setAlignment(Pos.CENTER);
        borderPane.setCenter(vbox);

        return borderPane;
    }

    private void OpponentMove() {
        while (enemysTurn) {
            int x = random.nextInt(10);
            int y = random.nextInt(10);

            Cell cell = playersBoard.getCell(x, y);
            if (cell.wasShot)
                continue;

            enemysTurn = cell.shoot();

            if (playersBoard.shipsNumber == 0) {
                System.out.println("PRZEGRALES");
            }
        }
    }

    private void placeEnemyShips() {
        int type = 5;
        while (type > 0) {
            int x = random.nextInt(10);
            int y = random.nextInt(10);

            if (enemysBoard.placeShip(new Ship(type, Math.random() < 0.5), x, y)) {
                type--;
            }
        }
        running = true;
    }


    @Override
    public void start(Stage primaryStage) throws Exception {
        Scene scene = new Scene(createContent());
        primaryStage.setTitle("Gra w statki");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}