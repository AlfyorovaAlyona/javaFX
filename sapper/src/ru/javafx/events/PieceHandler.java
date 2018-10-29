package ru.javafx.events;

import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import ru.javafx.main.Neighbors;
import ru.javafx.main.Piece;

import javax.swing.*;

import static ru.javafx.main.Menu.X_PIECES;
import static ru.javafx.main.Menu.Y_PIECES;
import static ru.javafx.main.Menu.pieces;

public class PieceHandler {
    private boolean lost = false;
    private String bomba = getClass().getResource("mine.png").toExternalForm();
    private String flag = getClass().getResource("flag.png").toExternalForm();

    private Image imageFlag = new Image(flag);
    private ImageView viewFlag = new ImageView(imageFlag);

    public void pieceHandling(Piece piece) {
        piece.button.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                MouseButton mouseButton = event.getButton();

                if (mouseButton == MouseButton.PRIMARY) { //to open a button
                    open(piece);
                    if (lost)
                        showBombs();
                    if (event.getClickCount() == 2 && piece.isFlag) {// double click to throw the flag
                        viewFlag.setImage(null);
                        piece.isOpened = false;
                    }
                } else if (mouseButton == MouseButton.SECONDARY) { //to put a flag on button supposing it's bomb
                    piece.button.setGraphic(viewFlag);
                    piece.isOpened = true;
                    piece.isFlag = true;
                }
            }
        });
    }

    private void open(Piece piece) {
        if (piece.isOpened) { //not allowed to click again
            return;
        }

        if (piece.isBomb) {
            Image bomb = new Image(bomba);
            piece.button.setGraphic(new ImageView(bomb));
            piece.button.setStyle("-fx-background-color: pink;");
            lost = true;
            return;
        }

        piece.isOpened = true;
        piece.text.setVisible(true);
        piece.button.setStyle("-fx-background-color: pink;");

        if (piece.text.getText().isEmpty()) {
            Neighbors neighbors = new Neighbors();
            for (Piece piece1 : neighbors.getNeighbors(piece)) {
                PieceHandler pH = new PieceHandler();
                pH.open(piece1);
            }
        }
    }

    private void showBombs() {
        JOptionPane.showMessageDialog(null, "You are lost!");
        System.out.println("You are lost! Close the window and try again.");
        for (int y = 0; y < Y_PIECES; y++) {
            for (int x = 0; x < X_PIECES; x++) {
                Piece piece2 = pieces[x][y];
                if (piece2.isBomb)
                    open(piece2);
            }
        }
    }
}
