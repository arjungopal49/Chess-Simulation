package sample;

import javafx.scene.image.Image;

import java.util.ArrayList;

public class Pawn extends Piece{

    public Pawn(String name, Image pieceImage, int x, int y, String team) {
        super(name, pieceImage, x, y, team);
    }


    public boolean checkMovement(int futureX, int futureY, int[][] board, String function ) {
        //if the requested coordinates match the correct movement pattern of the piece, true is returned
        if (function.equals("Move")) {
            if (getDirection() == 1) {
                if (futureX == getX() + 1 && futureY == getY()) {
                    return true;
                }
                if (getX() == 1) {
                    if (futureX == getX() + 2 && futureY == getY()) {
                        return true;
                    }
                }
            }
            if (getDirection()==-1) {
                if (futureX == getX() - 1 && futureY == getY()) {
                    return true;
                }
                if (getX() == 6) {
                    if (futureX == getX() - 2 && futureY == getY()) {
                        return true;
                    }
                }
            }

        } else {
            if (getDirection() == 1) {
                if (futureX == getX() + 1 && (futureY == getY() + 1 || futureY == getY() - 1)) {
                    return true;
                }
            }
            if (getDirection()==-1) {
                if (futureX == getX() - 1 && (futureY == getY() + 1 || futureY == getY() - 1)) {
                    return true;
                }
            }
        }
        return false;

    }

    public void listPossibleMoves(int[][] board, ArrayList<Piece> whitePiecesArray, ArrayList<Piece> blackPiecesArray){
        //calculates all possible ways the piece can be moved
        clearPossibleMovements();
        int[] coordinates = new int[2];
        if (getDirection() == 1) {
            coordinates[1] = getY();
            if (board[getX()+1][getY()] == 0){
                coordinates[0] = getX() + 1;
                addPossibleMovements(coordinates);
            }
            if (getX() == 1 && board[getX()+2][getY()] == 0){
                coordinates[0] = getX() + 2;
                addPossibleMovements(coordinates);
            }
        }
        if (getDirection() == -1) {
            coordinates[1] = getY();
            if (board[getX()-1][getY()] == 0){
                coordinates[0] = getX() - 1;
                addPossibleMovements(coordinates);
            }
            if (getX() == 6 && board[getX()-2][getY()] == 0){
                coordinates[0] = getX() - 2;
                addPossibleMovements(coordinates);
            }
        }
        if (getDirection() == 1) {
            coordinates[0] = getX() + 1;
            if (getY()-1 > -1 && getY()+1 <8){
                if (board[getX()+1][getY()+1] == 1 && !getPieceAt(getX()+1, getY()+1, whitePiecesArray, blackPiecesArray).getTeam().equals(getTeam())){
                    coordinates[1] = getY()+1;
                    addPossibleMovements(coordinates);
                }
                if (board[getX()+1][getY()-1] == 1 && !getPieceAt(getX()+1, getY()-1, whitePiecesArray, blackPiecesArray).getTeam().equals(getTeam())){
                    coordinates[1] = getY()-1;
                    addPossibleMovements(coordinates);
                }
            }

        }
        if (getDirection() == -1) {
            coordinates[0] = getX() - 1;
            if (getY()-1 > -1 && getY()+1 <8) {
                if (board[getX() - 1][getY() + 1] == 1 && !getPieceAt(getX() - 1, getY() + 1, whitePiecesArray, blackPiecesArray).getTeam().equals(getTeam())) {
                    coordinates[1] = getY() + 1;
                    addPossibleMovements(coordinates);
                }
                if (board[getX() - 1][getY() - 1] == 1 && !getPieceAt(getX() - 1, getY() - 1, whitePiecesArray, blackPiecesArray).getTeam().equals(getTeam())) {
                    coordinates[1] = getY() - 1;
                    addPossibleMovements(coordinates);
                }
            }
        }
    }




}
