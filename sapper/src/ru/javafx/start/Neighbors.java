package ru.javafx.start;

import java.util.ArrayList;

public class Neighbors {

    public ArrayList<Piece> getNeighbors(Piece piece) {

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

            if (x1 >= 0 && x1 < Menu.X_TILES && y1 >= 0 && y1 < Menu.Y_TILES) {
                neighbors.add(Menu.pieces[x1][y1]);
            }
        }

        return neighbors;
    }
}
