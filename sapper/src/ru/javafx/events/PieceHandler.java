package ru.javafx.events;

import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import ru.javafx.main.Neighbors;
import ru.javafx.main.Piece;

import javax.swing.*;

import static ru.javafx.main.Menu.*;

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
                    if (event.getClickCount() == 1) {
                        open(piece);
                    }
                    if (event.getClickCount() == 2 && piece.isFlag) {// double click to throw the flag
                        piece.isOpened = false;
                        piece.isFlag = false;
                        piece.button.setGraphic(null);
                        isFlagged--;
                    }
                } else if (mouseButton == MouseButton.SECONDARY && !piece.isOpened) { //to put a flag on button supposing it's bomb
                    piece.button.setGraphic(viewFlag);
                    piece.isOpened = true;
                    piece.isFlag = true;
                    isFlagged++;
                }

                winOrLost(); //check game's results
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

        openedPieces++;
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

    private void winOrLost() {
        if (lost) {
            JOptionPane.showMessageDialog(null, "You are lost!");
            showBombs();
            endGame();
        } else if (openedPieces == (X_PIECES * Y_PIECES - bombs) && bombs == isFlagged) {
            JOptionPane.showMessageDialog(null, "You are winner!");
            showBombs();
            endGame();
        }
    }

    private void endGame() {
        for (int y = 0; y < Y_PIECES; y++) {
            for (int x = 0; x < X_PIECES; x++) {
                Piece piece = pieces[x][y];
                piece.button.setOnMouseClicked(null);
            }
        }
    }

    private void showBombs() {
        for (int y = 0; y < Y_PIECES; y++) {
            for (int x = 0; x < X_PIECES; x++) {
                Piece piece2 = pieces[x][y];
                if (piece2.isBomb)
                    open(piece2);
            }
        }
    }

}
