package sample;

import javafx.scene.image.Image;

import java.util.ArrayList;

public class Queen extends Piece{

    public Queen(String name, Image pieceImage, int x, int y, String team) {
        super(name, pieceImage, x, y, team);
    }


    public boolean checkMovement(int futureX, int futureY, int[][] board, String function ) {
        if ((futureX == getX()) || (futureY == getY())){
            return checkPath(board, futureX, futureY, "Straight");
        } else {
            int Xdifference = futureX - getX();
            int Ydifference = futureY - getY();

            if (Math.abs(Xdifference) == Math.abs(Ydifference)){
                return checkPath(board, futureX, futureY, "Diagonal");
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

        addDiagonalPossibleMovements(-1, -1, board, whitePiecesArray, blackPiecesArray);
        addDiagonalPossibleMovements(1, -1, board, whitePiecesArray, blackPiecesArray);
        addDiagonalPossibleMovements(1, 1, board, whitePiecesArray, blackPiecesArray);
        addDiagonalPossibleMovements(-1, 1, board, whitePiecesArray, blackPiecesArray);
    }



}
