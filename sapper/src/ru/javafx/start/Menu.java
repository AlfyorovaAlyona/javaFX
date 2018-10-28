package ru.javafx.start;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;


public class Menu extends Application {

    public static final int PIECE_SIZE = 60;
    private static final int WIDTH = 600;
    private static final int HEIGHT = 600;
    public static final int X_TILES = WIDTH / PIECE_SIZE;
    public static final int Y_TILES = HEIGHT / PIECE_SIZE;
    public static Piece[][] pieces = new Piece[X_TILES][Y_TILES];
    public static boolean lost = false;
    public static int flags = 0;
    public Scene scene;
    private Neighbors neighbors = new Neighbors();

    public Parent createTable() {
        Pane pane = new Pane();
        pane.setPrefSize(WIDTH, HEIGHT);

        for (int y = 0; y < Y_TILES; y++) {
            for (int x = 0; x < X_TILES; x++) {
                Piece piece = new Piece(x, y, Math.random() < 0.1);
                pieces[x][y] = piece;

                pane.getChildren().add(piece);
            }
        }

        for (int y = 0; y < Y_TILES; y++) {
            for (int x = 0; x < X_TILES; x++) {
                Piece piece = pieces[x][y];

                long numBomb = neighbors.getNeighbors(piece).stream().filter(t -> t.isBomb).count();

                if (numBomb > 0)
                    piece.text.setText(String.valueOf(numBomb));
            }
        }
        return pane;
    }


    @Override
    public void start(Stage primaryStage) {
        BorderPane root = new BorderPane();
        Text labelTitle = new Text("Welcome to PinkSapper! " +
                                   " Start a new game?");
        Button startGame = new Button("Start");
        startGame.setPrefHeight(50);
        startGame.setPrefWidth(100);
        root.setTop(labelTitle);
        root.setCenter(startGame);

        Scene scene1 = new Scene(root, 350, 150);

        primaryStage.setTitle("PinkSapper");
        primaryStage.setScene(scene1);
        primaryStage.show();

        startGame.setOnAction(click -> {
            scene = new Scene(createTable());
            primaryStage.setScene(scene);
            primaryStage.show();
        });

    }

    public static void main(String[] args) {
        launch(args);
    }
}
