package ru.javafx.main;

import ru.javafx.events.PieceHandler;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class Piece extends StackPane {

    public int x;
    public int y;
    public boolean isBomb;
    public boolean isOpened = false;
    public boolean isFlag = false;

    public Text text = new Text();
    public Button button = new Button();

    public Piece(int x, int y, boolean isBomb) {
        this.x = x;
        this.y = y;
        this.isBomb = isBomb;

        text.setFont(Font.font(20));
        text.setStroke(Color.MAGENTA);
        text.setVisible(false);
        setTranslateX(x * Menu.PIECE_SIZE);
        setTranslateY(y * Menu.PIECE_SIZE);

        button.setPrefWidth(Menu.PIECE_SIZE - 1);
        button.setPrefHeight(Menu.PIECE_SIZE - 1);

        PieceHandler handler = new PieceHandler();
        handler.pieceHandling(this);

        getChildren().addAll(button, text);
    }
}
