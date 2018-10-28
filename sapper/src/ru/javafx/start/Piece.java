package ru.javafx.start;

import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class Piece extends StackPane {
    private String bomba = getClass().getResource("mine.png").toExternalForm();
    private String flag = getClass().getResource("flag.png").toExternalForm();

    public int x;
    public int y;
    public boolean isBomb;
    public boolean isOpened = false;

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

        pieceHangling();

        getChildren().addAll(button, text);
    }

    public void pieceHangling() {
        button.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                MouseButton mouseButton = event.getButton();

                if (mouseButton == MouseButton.PRIMARY) {
                    open();
                    if (Menu.lost) {
                        System.out.println("You are lost!");
                    }
                } else if (mouseButton == MouseButton.SECONDARY) {
                    Image fl = new Image(flag);
                    button.setGraphic(new ImageView(fl));
                    button.setStyle("-fx-background-color: magenta;");
                    Menu.flags++;
                }
            }
        });
    }

    public void open() {
        if (isOpened) {
            return;
        }

        if (isBomb) {
            Image bomb = new Image(bomba);
            button.setGraphic(new ImageView(bomb));
            button.setStyle("-fx-background-color: magenta;");
            Menu.lost = true;
            return;
        }

        isOpened = true;
        text.setVisible(true);
        button.setStyle("-fx-background-color: pink;");

        if (text.getText().isEmpty()) {
            Neighbors neighbors = new Neighbors();
            neighbors.getNeighbors(this).forEach(Piece::open);
        }
    }
}
