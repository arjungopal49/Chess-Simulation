package sample;

import javafx.scene.image.Image;

import java.util.ArrayList;

public class King extends Piece{
    public King (String name, Image pieceImage, int x, int y, String team){
        super(name, pieceImage, x, y, team);

    }

    public boolean checkMovement(int futureX, int futureY, int[][] board, String function ) {
        //one block movement
        if (futureX == getX() + 1 || futureX == getX() - 1 || futureX == getX()) {
            if (futureY == getY() + 1 || futureY == getY() - 1 || futureY == getY()) {
                return true;
            }
        }
        return false;
    }



}
