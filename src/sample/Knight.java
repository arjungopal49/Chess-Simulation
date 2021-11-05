package sample;

import javafx.scene.image.Image;

import java.util.ArrayList;

public class Knight extends Piece{

    public Knight(String name, Image pieceImage, int x, int y, String team) {
        super(name, pieceImage, x, y, team);
    }

    public boolean checkMovement(int futureX, int futureY, int[][] board, String function ) {
        if ((futureX == getX() + 1 && futureY == getY() + 2) || (futureX == getX() - 1 && futureY == getY() + 2) || (futureX == getX() - 1 && futureY == getY() - 2) || (futureX == getX() + 1 && futureY == getY() - 2)) {
            return true;
        }
        if ((futureX == getX() + 2 && futureY == getY() + 1) || (futureX == getX() - 2 && futureY == getY() + 1) || (futureX == getX() - 2 && futureY == getY() - 1) || (futureX == getX() + 2 && futureY == getY() - 1)) {
            return true;
        }
        return false;
    }

    public void listPossibleMoves(int[][] board, ArrayList<Piece> whitePiecesArray, ArrayList<Piece> blackPiecesArray){
        clearPossibleMovements();
        checkKnightSpot(1, 2, board, whitePiecesArray, blackPiecesArray);
        checkKnightSpot(-1, 2, board, whitePiecesArray, blackPiecesArray);
        checkKnightSpot(-1, -2, board, whitePiecesArray, blackPiecesArray);
        checkKnightSpot(1, -2, board, whitePiecesArray, blackPiecesArray);
        checkKnightSpot(2, 1, board, whitePiecesArray, blackPiecesArray);
        checkKnightSpot(-2, 1, board, whitePiecesArray, blackPiecesArray);
        checkKnightSpot(-2, -1, board, whitePiecesArray, blackPiecesArray);
        checkKnightSpot(2, -1, board, whitePiecesArray, blackPiecesArray);
    }

    public void checkKnightSpot(int x, int y, int[][] board, ArrayList<Piece> whitePiecesArray, ArrayList<Piece> blackPiecesArray){
        int[] coordinates = new int[2];
        if ((getX() + x < 8 && getX() + x > -1) && (getY() + y < 8 && getY() + y > -1)){
            coordinates[0] = getX() + x;
            coordinates[1] = getY() + y;
            if(board[getX() + x][getY() + y] == 0 ){
                addPossibleMovements(coordinates);
            }
            if(board[getX() + x][getY() + y] == 1 && !getPieceAt(getX()+x, getY()+y, whitePiecesArray, blackPiecesArray).getTeam().equals(getTeam())){
                addPossibleMovements(coordinates);
            }
        }

    }


}
