package sample;

import javafx.scene.image.Image;

import java.util.ArrayList;

public class Bishop extends Piece{

    public Bishop(String name, Image pieceImage, int x, int y, String team) {
        super(name, pieceImage, x, y, team);
    }


    public boolean checkMovement(int futureX, int futureY, int[][] board, String function ) {
        int Xdifference = futureX - getX();
        int Ydifference = futureY - getY();

        if (Math.abs(Xdifference) == Math.abs(Ydifference)){
            if (checkPath(board, futureX, futureY, "Diagonal")){
                return true;
            }
        }
        return false;
    }

    public void listPossibleMoves(int[][] board, ArrayList<Piece> whitePiecesArray, ArrayList<Piece> blackPiecesArray){
        clearPossibleMovements();
        addDiagonalPossibleMovements(-1, -1, board, whitePiecesArray, blackPiecesArray);
        addDiagonalPossibleMovements(1, -1, board, whitePiecesArray, blackPiecesArray);
        addDiagonalPossibleMovements(1, 1, board, whitePiecesArray, blackPiecesArray);
        addDiagonalPossibleMovements(-1, 1, board, whitePiecesArray, blackPiecesArray);
    }





}
