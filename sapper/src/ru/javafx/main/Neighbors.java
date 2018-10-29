package ru.javafx.main;

import java.util.ArrayList;
import static ru.javafx.main.Menu.*;

public class Neighbors {

    public ArrayList<Piece> getNeighbors(Piece piece) {

        ArrayList<Piece> neighbors = new ArrayList<>();

        //neighbours coordinates
        int[] coordinates = new int[] {
                -1, -1,
                -1,  0,
                -1,  1,
                 0, -1,
                 0,  1,
                 1, -1,
                 1,  0,
                 1,  1
        };

        for (int i = 0; i < coordinates.length; i++) {
            int dx = coordinates[i];
            int dy = coordinates[++i];

            int x1 = piece.x + dx;
            int y1 = piece.y + dy;

            if (x1 >= 0 && x1 < X_PIECES && y1 >= 0 && y1 < Y_PIECES) {
                neighbors.add(pieces[x1][y1]);
            }
        }

        return neighbors;
    }
}
