package ru.javafx.start;

import java.util.ArrayList;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import javax.swing.*;

public class Main extends Application {

    private static final int TILE_SIZE = 60;
    private static final int WIDTH = 900;
    private static final int HEIGHT = 600;
    private static final int X_TILES = WIDTH / TILE_SIZE;
    private static final int Y_TILES = HEIGHT / TILE_SIZE;

    private Piece[][] pieces = new Piece[X_TILES][Y_TILES];
    private Scene scene;
    String bomba = getClass().getResource("mine.png").toExternalForm();

    private Parent createTable() {
        Pane pane = new Pane();
        pane.setPrefSize(WIDTH, HEIGHT);

        for (int y = 0; y < Y_TILES; y++) {
            for (int x = 0; x < X_TILES; x++) {
                Piece piece = new Piece(x, y, Math.random() < 0.1 );
                pieces[x][y] = piece;
                pane.getChildren().add(piece);
            }
        }

        for (int y = 0; y < Y_TILES; y++) {
            for (int x = 0; x < X_TILES; x++) {
                Piece piece = pieces[x][y];

                /*if (tile.isItBomb)
                    continue;*/

                long numBomb = getNeighbors(piece).stream().filter(t -> t.isItBomb).count();

                if (numBomb > 0)
                    piece.text.setText(String.valueOf(numBomb));
            }
        }

        return pane;
    }


    private ArrayList<Piece> getNeighbors(Piece piece) {
        ArrayList<Piece> neighbors = new ArrayList<>();

        int[] points = new int[] {
                -1, -1,
                -1,  0,
                -1,  1,
                 0, -1,
                 0,  1,
                 1, -1,
                 1,  0,
                 1,  1
        };

        for (int i = 0; i < points.length; i++) {
            int dx = points[i];
            int dy = points[++i];

            int x1 = piece.x + dx;
            int y1 = piece.y + dy;

            if (x1 >= 0 && x1 < X_TILES && y1 >= 0 && y1 < Y_TILES) {
                neighbors.add(pieces[x1][y1]);
            }
        }

        return neighbors;
    }


    private class Piece extends StackPane {
        private int x;
        private int y;
        private boolean isItBomb;
        private boolean isOpen = false;

        private Button button = new Button();

        private Text text = new Text();

        private Piece(int x, int y, boolean isItBomb) {
            this.x = x;
            this.y = y;
            this.isItBomb = isItBomb;

            text.setFont(Font.font(20));
            text.setStroke(Color.MAGENTA);
            text.setVisible(false);
            setTranslateX(x * TILE_SIZE);
            setTranslateY(y * TILE_SIZE);

            button.setPrefWidth(TILE_SIZE - 1);
            button.setPrefHeight(TILE_SIZE - 1);

            //setMargin(text, new Insets(10, 10, 10, 10));

            button.setOnAction(click -> open());
            getChildren().addAll(button, text);
        }

        private void open() {
            if (isOpen) {
                return;
            }

            if (isItBomb) {
                Image bomb = new Image(bomba);
                button.setGraphic(new ImageView(bomb));
                button.setStyle("-fx-background-color: red;");
                //getChildren().add(button);
                JOptionPane.showMessageDialog(null,
                        "You are lost!",
                        "Game over!", JOptionPane.INFORMATION_MESSAGE);
                scene.setRoot(createTable());

                Platform.exit(); //close window
                System.exit(0); //return;
                return;
            }

            isOpen = true;
            text.setVisible(true);
            button.setStyle("-fx-background-color: pink;");

            if (text.getText().isEmpty()) {
                getNeighbors(this).forEach(Piece:: open);
            }
        }
    }

    @Override
    public void start(Stage stage) throws Exception {
        scene = new Scene(createTable());

        stage.setTitle("PinkSapper");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
