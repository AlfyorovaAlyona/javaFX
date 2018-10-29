package ru.javafx.main;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Menu extends Application {

    private static final int WIDTH = 600;       //убрать константы, а добавить в меню выбор сложности
    private static final int HEIGHT = 600;
    public static final int PIECE_SIZE = 50;
    public static final int X_PIECES = WIDTH / PIECE_SIZE;
    public static final int Y_PIECES = HEIGHT / PIECE_SIZE;
    public static Piece[][] pieces = new Piece[X_PIECES][Y_PIECES];
    private Scene scene;
    private Neighbors neighbors = new Neighbors();

    private Parent createTable() {
        Pane pane = new Pane();
        pane.setPrefSize(WIDTH, HEIGHT);

        for (int y = 0; y < Y_PIECES; y++) {
            for (int x = 0; x < X_PIECES; x++) {
                Piece piece = new Piece(x, y, Math.random() < 0.1);
                pieces[x][y] = piece;
                pane.getChildren().add(piece);
            }
        }

        for (int y = 0; y < Y_PIECES; y++) {
            for (int x = 0; x < X_PIECES; x++) {
                Piece piece = pieces[x][y];
                countBombs(piece);
            }
        }
        return pane;
    }

    private void countBombs(Piece piece) {
        long numBomb = neighbors.getNeighbors(piece).stream().filter(t -> t.isBomb).count();
        if (numBomb > 0)
            piece.text.setText(String.valueOf(numBomb));
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
            scene = new Scene(createTable());
            primaryStage.setScene(scene);
            primaryStage.show();
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
