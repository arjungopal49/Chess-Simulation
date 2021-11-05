package sample;

import javafx.scene.image.Image;

import java.util.ArrayList;

public class Rook extends Piece{

    public Rook(String name, Image pieceImage, int x, int y, String team) {
        super(name, pieceImage, x, y, team);
    }


    public boolean checkMovement(int futureX, int futureY, int[][] board, String function ) {
        if ((futureX == getX()) || (futureY == getY())){
            if (checkPath(board, futureX, futureY, "Straight")){
                return true;
            }
        }
        return false;
    }

    public void listPossibleMoves(int[][] board, ArrayList<Piece> whitePiecesArray, ArrayList<Piece> blackPiecesArray){
        clearPossibleMovements();
        addStraightPossibleMovements(-1, "x", board, whitePiecesArray, blackPiecesArray);
        addStraightPossibleMovements(1, "x", board, whitePiecesArray, blackPiecesArray);
        addStraightPossibleMovements(-1, "y", board, whitePiecesArray, blackPiecesArray);
        addStraightPossibleMovements(1, "y", board, whitePiecesArray, blackPiecesArray);
    }




}
