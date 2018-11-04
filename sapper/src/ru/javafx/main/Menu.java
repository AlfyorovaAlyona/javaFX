package ru.javafx.main;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.awt.*;

public class Menu extends Application {

    private static final int WIDTH = 500;       //убрать константы, а добавить в меню выбор сложности
    private static final int HEIGHT = 500;
    public static final int PIECE_SIZE = 50;
    public static final int X_PIECES = WIDTH / PIECE_SIZE;
    public static final int Y_PIECES = HEIGHT / PIECE_SIZE;
    public static Piece[][] pieces = new Piece[X_PIECES][Y_PIECES];
    public static long bombs;
    public static long openedPieces;
    public static int isFlagged;
    private Scene scene;
    private Neighbors neighbors = new Neighbors();
    private Button restartButton = new Button("Restart");
    private Button exitButton = new Button("Exit");

    private Parent createTable() {
        Pane pane = new Pane();
        pane.setPrefSize(WIDTH, HEIGHT + 50);
        bombs = 0;
        openedPieces = 0;
        isFlagged = 0;

        restartButton.setLayoutX(10);
        restartButton.setLayoutY(HEIGHT + 15);
        exitButton.setLayoutX(WIDTH - 50);
        exitButton.setLayoutY(HEIGHT + 15);

        for (int y = 0; y < Y_PIECES; y++) {
            for (int x = 0; x < X_PIECES; x++) {
                Piece piece = new Piece(x, y, Math.random() < 0.1);
                if (piece.isBomb)
                    bombs++;
                pieces[x][y] = piece;
                pane.getChildren().add(piece);
            }
        }

        pane.getChildren().add(restartButton);
        pane.getChildren().add(exitButton);

        for (int y = 0; y < Y_PIECES; y++) {
            for (int x = 0; x < X_PIECES; x++) {
                Piece piece = pieces[x][y];
                countBombsNear(piece);
            }
        }
        return pane;
    }

    private void countBombsNear(Piece piece) {
        long numBomb = neighbors.getNeighbors(piece).stream().filter(t -> t.isBomb).count();
        if (numBomb > 0)
            piece.text.setText(String.valueOf(numBomb));

        //Rectangle numBombs = new Rectangle();
    }

    @Override
    public void start(Stage primaryStage) {
        BorderPane root = new BorderPane();
        Text labelTitle = new Text(" Welcome to PinkSapper! " +
                                   "Start a new game?");
        labelTitle.setFont(Font.font(15));
        Button startGame = new Button("Start");
        startGame.setPrefHeight(50);
        startGame.setPrefWidth(100);
        root.setTop(labelTitle);
        root.setCenter(startGame);
        setPinkBackground(root);

        Scene sceneMenu = new Scene(root, 350, 150);

        primaryStage.setTitle("PinkSapper");
        primaryStage.setScene(sceneMenu);
        primaryStage.show();

        startGame.setOnAction(click -> {
            startGame(primaryStage);
            restartButton.setOnAction(cl -> {
                startGame(primaryStage);
            });

            exitButton.setOnAction(ex -> {
                Platform.exit();
                System.exit(0);
            });
        });
    }

    private void setPinkBackground(BorderPane root) {
        String fon = getClass().getResource("fon2.png").toExternalForm();
        Image image = new Image(fon);
        BackgroundSize bSize = new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO,
                false, false, true, false);

        Background background = new Background(new BackgroundImage(image,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                bSize));
        root.setBackground(background);
    }

    private void startGame(Stage primaryStage) {
        scene = new Scene(createTable());
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
