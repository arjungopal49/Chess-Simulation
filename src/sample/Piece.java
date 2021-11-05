package sample;

import javafx.scene.image.Image;

import java.util.ArrayList;

public class Piece {
    private String name;
    private int value;
    private int x;
    private int y;
    private int direction;
    private Image pieceImage;
    private String team;
    private boolean hasMoved = false;
    private boolean isAttackingKing = false;
    private ArrayList<int[]> possibleMovements = new ArrayList<>();

    public Piece (String name, Image pieceImage, int x, int y, String team){
        this.name = name;
        this.pieceImage = pieceImage;
        this.x = x;
        this.y = y;
        this.team = team;

        if (name.equals("Pawn")){
            value = 1;
        }
        if (name.equals("Knight") || name.equals("Bishop")){
            value = 3;
        }
        if (name.equals("Rook")){
            value = 5;
        }
        if (name.equals("Queen")){
            value = 9;
        }
    }



    // class mutators
    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setDirection(int d){
        direction = d;
    }

    public void setHasMoved(boolean x){
        hasMoved = x;
    }

    public void setIsAttackingKing(boolean k) {
        isAttackingKing = k;
    }

    public void clearPossibleMovements(){
        possibleMovements.clear();
    }

    public void addPossibleMovements(int[] newCoord){
        possibleMovements.add(newCoord);
    }


    //class accessors

    public boolean checkMovement(int futureX, int futureY, int[][] board, String function ) {
        return false;
    }


    public boolean checkPath(int[][] board, int futureX, int futureY, String path){
        //checks to make sure that there are no pieces on the requested path
        if (path.equals("Diagonal")){
            int xSign = (futureX - x)/(Math.abs(futureX - x));
            int ySign = (futureY - y)/(Math.abs(futureY - y));

            if (xSign == 1){
                for (int i = x+1; i < futureX; i++){
                    if (checkDiagonalSpot(board, ySign, i, i-x)) return false;
                }
            } else {
                for (int i = x-1; i > futureX; i--){
                    if (checkDiagonalSpot(board, ySign, i, x-i)) return false;
                }
            }


        }
        if (path.equals("Straight")){
            if (futureX==x){
                if (futureY - y > 0){
                    for (int i = y+1; i < futureY; i++){
                        if (board[x][i]>0){
                            return false;
                        }
                    }
                } else {
                    for (int i = y-1; i > futureY; i--){
                        if (board[x][i]>0){
                            return false;
                        }
                    }
                }

            } else {
                if (futureX - x > 0){
                    for (int i = x+1; i < futureX; i++){
                        if (board[i][y]>0){
                            return false;
                        }
                    }
                } else {
                    for (int i = x-1; i > futureX; i--){
                        if (board[i][y]>0){
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    public boolean checkDiagonalSpot(int[][] board, int ySign, int i, int difference) {
        if (ySign==1) {
            if (board[i][y+(difference)] > 0){
                return true;
            }

        } else {
            if (board[i][y-(difference)] > 0){
                return true;
            }
        }
        return false;
    }

    public Piece getPieceAt(int xCoord, int yCoord, ArrayList<Piece> whitePiecesArray, ArrayList<Piece> blackPiecesArray){
        //same as the respective controller method
        for (Piece piece: whitePiecesArray){
            if (piece.getX() == xCoord && piece.getY() == yCoord) {
                return piece;
            }
        }
        for (Piece piece: blackPiecesArray){
            if (piece.getX() == xCoord && piece.getY() == yCoord) {
                return piece;
            }

        }
        return null;
    }

    public boolean calculateCheckingKing(int[][] board, Piece king) {
        setIsAttackingKing(checkMovement(king.getX(), king.getY(), board,"Take"));
        return isAttackingKing;
    }


    public void listPossibleMoves(int[][] board, ArrayList<Piece> whitePiecesArray, ArrayList<Piece> blackPiecesArray){

    }

    public void addDiagonalPossibleMovements(int xDirection, int yDirection, int[][] board, ArrayList<Piece> whitePiecesArray, ArrayList<Piece> blackPiecesArray){
        //adds all possible coordinates for pieces moving diagonally
        int x = getX() + xDirection;
        int y = getY() + yDirection;
        int[] coordinates = new int[2];
        boolean loopEnd = false;
        while (((x > -1 && x < 8) && (y > -1 && y < 8)) && !loopEnd){
            coordinates[0] = x;
            coordinates[1] = y;
            if (board[x][y] == 0){
                addPossibleMovements(coordinates);
            } else if (board[x][y] == 1 && !getPieceAt(x, y, whitePiecesArray, blackPiecesArray).getTeam().equals(getTeam())){
                addPossibleMovements(coordinates);
                loopEnd = true;
            } else {
                loopEnd = true;
            }
            x = x + xDirection;
            y = y + yDirection;

        }
    }

    public void addStraightPossibleMovements(int direction, String dimension, int[][] board, ArrayList<Piece> whitePiecesArray, ArrayList<Piece> blackPiecesArray){
        //adds all possible coordinates for pieces moving straight
        int x = getX();
        int y = getY();

        if (dimension.equals("x")){
            x = x + direction;
        } else {
            y = y + direction;
        }

        int[] coordinates = new int[2];
        boolean loopEnd = false;
        while (((x > -1 && x < 8) && (y > -1 && y < 8)) && !loopEnd){
            coordinates[0] = x;
            coordinates[1] = y;
            if (board[x][y] == 0){
                addPossibleMovements(coordinates);
            } else if (board[x][y] == 1 && !getPieceAt(x, y, whitePiecesArray, blackPiecesArray).getTeam().equals(getTeam())){
                addPossibleMovements(coordinates);
                loopEnd = true;
            } else {
                loopEnd = true;
            }
            if (dimension.equals("x")){
                x = x + direction;
            } else {
                y = y + direction;
            }

        }
    }



    public boolean getHasMoved(){
        return hasMoved;
    }

    public String getName() {
        return name;
    }

    public Image getPieceImage() {
        return pieceImage;
    }

    public int getValue() {
        return value;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getDirection(){
        return direction;
    }

    public String getTeam() {
        return team;
    }

    public ArrayList<int[]> calculatePossibleMovements(int[][] board, ArrayList<Piece> whitePiecesArray, ArrayList<Piece> blackPiecesArray) {
        listPossibleMoves(board, whitePiecesArray, blackPiecesArray);
        return possibleMovements;
    }

    public ArrayList<int[]> getPossibleMovements(){
        return possibleMovements;
    }



}
