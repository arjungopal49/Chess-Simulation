package sample;

import com.sun.javafx.scene.paint.MaterialHelper;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class Controller {
    private Image whitePawnImage = new Image("resources/chess_white_pawn.png");
    private Image blackPawnImage = new Image("resources/chess_black_pawn.png");
    private Image whiteBishopImage = new Image("resources/chess_white_bishop.png");
    private Image blackBishopImage = new Image("resources/chess_black_bishop.png");
    private Image whiteKnightImage = new Image("resources/chess_white_knight.png");
    private Image blackKnightImage = new Image("resources/chess_black_knight.png");
    private Image whiteRookImage = new Image("resources/chess_white_rook.png");
    private Image blackRookImage = new Image("resources/chess_black_rook.png");
    private Image whiteQueenImage = new Image("resources/chess_white_queen.png");
    private Image blackQueenImage = new Image("resources/chess_black_queen.png");
    private Image whiteKingImage = new Image("resources/chess_white_king.png");
    private Image blackKingImage = new Image("resources/chess_black_king.png");
    private Image blankImage = new Image("resources/blank.png");
    private ArrayList<Piece> whitePiecesArray = new ArrayList<>();
    private ArrayList<Piece> blackPiecesArray = new ArrayList<>();
    private ArrayList<Piece> whiteCapturedPieces = new ArrayList<>();
    private ArrayList<Piece> blackCapturedPieces = new ArrayList<>();

    @FXML
    Label lblTurn, lblTaken, endingLabel;
    @FXML
    Button btnStart, queenPromoteButton, rookPromoteButton, knightPromoteButton, bishopPromoteButton;
    @FXML
    Rectangle winningRectangle;
    @FXML
    AnchorPane endScreen, chooseGameModePane;
    @FXML
    GridPane gdpPlayGrid;
    //Creates a 2D array of the board
    private int [][] board = new int[8][8];
    //Creates a 2D array of ImageViews
    private ImageView [][] boardSpotsIMG = new ImageView [8][8];

    int clicks, rowIndex, colIndex;
    int gameMode = 0;
    int orientation = -1;
    int turn = 0;


    Piece pieceClickedOn;
    String colorClickedOn;

    String botColor;
    int botTurn;




    @FXML
    private void start(){
        clicks = 0;
        btnStart.setDisable(true);
        for (int i = 0; i < boardSpotsIMG.length; i++) {
            for (int j = 0; j < boardSpotsIMG.length; j++) {
                boardSpotsIMG[i][j] = new ImageView();
                boardSpotsIMG[i][j].setPickOnBounds(true);
                boardSpotsIMG[i][j].setImage(blankImage);
                boardSpotsIMG[i][j].setFitHeight(68);
                boardSpotsIMG[i][j].setFitWidth(68);
            }
        }
        initializePieces();
        setBoard();
        computerTurn();
        for (int i = 0; i < boardSpotsIMG.length; i++) {
            for (int j = 0; j < boardSpotsIMG.length; j++) {
                gdpPlayGrid.add(boardSpotsIMG[i][j], j, i);
            }
        }
        //this is the mouse event: same as if you were adding it in scenebuilder but this lets you do it dynamically
        EventHandler z = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                rowIndex = GridPane.getRowIndex(((ImageView) t.getSource()));
                colIndex = GridPane.getColumnIndex(((ImageView) t.getSource()));
                if (gameMode == 0 || (gameMode == 1 && turn != botTurn)) {
                    //on the first click, if a piece is clicked, it stores that piece
                    if (clicks == 0) {
                        if (board[rowIndex][colIndex] > 0) {
                            pieceClickedOn = getPieceAt(rowIndex, colIndex);
                            colorClickedOn = pieceClickedOn.getTeam();
                            if ((colorClickedOn.equals("White") && turn == 0) || (colorClickedOn.equals("Black") && turn == 1)) {
                                clicks = 1;
                            } else {
                                clicks = 0;
                            }

                        }
                    } else if (clicks == 1) {
                        // on the second click, it checks if it can move, take or reset the current piece based on the value of that square
                        if (board[rowIndex][colIndex] == 0) {
                            move(pieceClickedOn, rowIndex, colIndex);
                        } else if (!getPieceAt(rowIndex, colIndex).getTeam().equals(pieceClickedOn.getTeam())) {
                            take(pieceClickedOn, rowIndex, colIndex);
                        } else {
                            pieceClickedOn = getPieceAt(rowIndex, colIndex);
                            colorClickedOn = pieceClickedOn.getTeam();
                            if ((colorClickedOn.equals("White") && turn == 0) || (colorClickedOn.equals("Black") && turn == 1)) {
                                clicks = 1;
                            } else {
                                clicks = 0;
                            }
                        }

                    }
                }
            }
        };

        for (int i = 0; i < boardSpotsIMG.length; i++) {
            for (int j = 0; j < boardSpotsIMG.length; j++) {
                //setting the onMouseClicked property for each of the ImageViews to call z (the event handler)
                boardSpotsIMG[i][j].setOnMouseClicked(z);
            }
        }

    }


    public void initializePieces(){
        //create white pieces
        whitePiecesArray.clear();
        whitePiecesArray.add(new Rook("Rook", whiteRookImage, Math.floorMod(orientation, 7) - orientation, 0,"White"));
        whitePiecesArray.add(new Knight("Knight", whiteKnightImage, Math.floorMod(orientation, 7) - orientation, 1, "White"));
        whitePiecesArray.add(new Bishop("Bishop", whiteBishopImage, Math.floorMod(orientation, 7) - orientation, 2, "White"));
        if (orientation == 1){
            whitePiecesArray.add(new King("King", whiteKingImage, Math.floorMod(orientation, 7) - orientation, 3, "White"));
            whitePiecesArray.add(new Queen("Queen", whiteQueenImage, Math.floorMod(orientation, 7) - orientation, 4, "White"));
        } else {
            whitePiecesArray.add(new King("King", whiteKingImage, Math.floorMod(orientation, 7) - orientation, 4, "White"));
            whitePiecesArray.add(new Queen("Queen", whiteQueenImage, Math.floorMod(orientation, 7) - orientation, 3, "White"));
        }
        whitePiecesArray.add(new Bishop("Bishop", whiteBishopImage, Math.floorMod(orientation, 7) - orientation, 5, "White"));
        whitePiecesArray.add(new Knight("Knight", whiteKnightImage, Math.floorMod(orientation, 7) - orientation, 6, "White"));
        whitePiecesArray.add(new Rook("Rook", whiteRookImage, Math.floorMod(orientation, 7) - orientation, 7, "White"));
        for (int i = 0; i < 8; i++) {
            whitePiecesArray.add(new Pawn("Pawn", whitePawnImage, Math.floorMod(orientation, 7), i, "White"));
        }


        blackPiecesArray.clear();
        int blackOrientation = orientation*-1;
        //create black pieces
        blackPiecesArray.add(new Rook("Rook", blackRookImage, Math.floorMod(blackOrientation, 7) - blackOrientation, 0, "Black"));
        blackPiecesArray.add(new Knight("Knight", blackKnightImage, Math.floorMod(blackOrientation, 7) - blackOrientation, 1, "Black"));
        blackPiecesArray.add(new Bishop("Bishop", blackBishopImage, Math.floorMod(blackOrientation, 7) - blackOrientation, 2, "Black"));
        if (blackOrientation == 1){
            blackPiecesArray.add(new King("King", blackKingImage, Math.floorMod(blackOrientation, 7) - blackOrientation, 4, "Black"));
            blackPiecesArray.add(new Queen("Queen", blackQueenImage, Math.floorMod(blackOrientation, 7) - blackOrientation, 3, "Black"));
        } else {
            blackPiecesArray.add(new King("King", blackKingImage, Math.floorMod(blackOrientation, 7) - blackOrientation, 3, "Black"));
            blackPiecesArray.add(new Queen("Queen", blackQueenImage, Math.floorMod(blackOrientation, 7) - blackOrientation, 4, "Black"));
        }
        blackPiecesArray.add(new Bishop("Bishop", blackBishopImage, Math.floorMod(blackOrientation, 7) - blackOrientation, 5, "Black"));
        blackPiecesArray.add(new Knight("Knight", blackKnightImage, Math.floorMod(blackOrientation, 7) - blackOrientation, 6, "Black"));
        blackPiecesArray.add(new Rook("Rook", blackRookImage, Math.floorMod(blackOrientation, 7) - blackOrientation, 7, "Black"));
        for (int i = 0; i < 8; i++) {
            blackPiecesArray.add(new Pawn("Pawn", blackPawnImage, Math.floorMod(blackOrientation, 7), i, "Black"));
        }


        for (Piece value : whitePiecesArray) {
            value.setDirection(orientation);
        }
        for (Piece piece : blackPiecesArray) {
            piece.setDirection(blackOrientation);
        }

    }


    public void setBoard() {
        //update board array and images
        for (int i = 0; i < 8; i++){
            for ( int j = 0; j < 8; j++){
                board[i][j] = 0;
                boardSpotsIMG[i][j].setImage(blankImage);
            }
        }
        for (Piece piece : whitePiecesArray) {
            board[piece.getX()][piece.getY()] = 1;
            boardSpotsIMG[piece.getX()][piece.getY()].setImage(piece.getPieceImage());
        }
        for (Piece piece : blackPiecesArray) {
            board[piece.getX()][piece.getY()] = 1;
            boardSpotsIMG[piece.getX()][piece.getY()].setImage(piece.getPieceImage());
        }

    }

    public Piece getPieceAt(int x, int y){
        //returns the piece at a specific coordinate
        for (Piece piece: whitePiecesArray){
            if (piece.getX() == x && piece.getY() == y) {
                return piece;
            }
        }
        for (Piece piece: blackPiecesArray){
            if (piece.getX() == x && piece.getY() == y) {
                return piece;
            }

        }
        return null;
    }

    public void move(Piece piece, int x, int y){
        //moves piece if it is a legal move
        if (checkLegalMove(piece, x, y, "Move", -1)){
            piece.setHasMoved(true);
            updateBoardSpot(piece, "Make Blank");
            piece.setX(x);
            piece.setY(y);
            updateBoardSpot(piece, "Put Piece");

            if ((piece.getX() == 7 && piece.getDirection() == 1 || piece.getX() == 0 && piece.getDirection() == -1 ) && piece.getName().equals("Pawn")){
                setPromotionButtonVisibility(true);
            } else {
                switchTurn();
            }
        }
        else if (piece.getName().equals("King") && !piece.getHasMoved()){
            castle(piece, x, y);
        }
        clicks = 0;

    }

    public void castle(Piece piece, int x, int y){
        if (x == piece.getX()){

            //if Kings are on the 4th Column:

                //short (king-side castle)
                checkShortCastle(3, 1, 2, 0, piece, y);

                //long (queen-side castle)
                checkLongCastle(3, 5, 6, 4, 7, piece, y);

            //if Kings are on the 5th Column:

                checkShortCastle(4, 6, 5, 7, piece, y);
                checkLongCastle(4, 2, 1, 3, 0, piece, y);
        }

    }

    public void checkShortCastle(int a, int b, int c, int y, Piece piece, int col){
        if (piece.getY() == a && col == b ){
            if (board[piece.getX()][c] == 0 && board[piece.getX()][b] == 0){
                if (piece.getTeam().equals("White")) {
                    int rookIndex = getRookIndex(whitePiecesArray, y);
                    if (!whitePiecesArray.get(rookIndex).getHasMoved() && rookIndex != -1) {
                        performCastle(piece, whitePiecesArray, b, c, rookIndex);
                    }
                } else {
                    int rookIndex = getRookIndex(blackPiecesArray, y);
                    if (!blackPiecesArray.get(rookIndex).getHasMoved() && rookIndex != -1) {
                        performCastle(piece, blackPiecesArray, b, c, rookIndex);
                    }
                }
            }
        }
    }

    public void checkLongCastle(int a, int b, int c, int d, int y, Piece piece, int col){
        if (piece.getY() == a && (col == b || col == c)){
            if (board[piece.getX()][d] == 0 && board[piece.getX()][b] == 0 && board[piece.getX()][c] == 0){
                if (piece.getTeam().equals("White")) {
                    int rookIndex = getRookIndex(whitePiecesArray, y);
                    if (!whitePiecesArray.get(rookIndex).getHasMoved() && rookIndex != -1) {
                        performCastle(piece, whitePiecesArray, b, d, rookIndex);
                    }
                } else {
                    int rookIndex = getRookIndex(blackPiecesArray, y);
                    if (!blackPiecesArray.get(rookIndex).getHasMoved() && rookIndex != -1) {
                        performCastle(piece, blackPiecesArray, b, d, rookIndex);
                    }
                }
            }
        }
    }

    public int getRookIndex(ArrayList<Piece> pieceArray, int yCoord){
        //returns index of rook in a specific array
        for (int i = 0; i < pieceArray.size(); i++){
            if (pieceArray.get(i).getName().equals("Rook") && pieceArray.get(i).getY() == yCoord){
                return i;
            }
        }
        return -1;
    }

    public void performCastle(Piece piece, ArrayList<Piece> piecesArray, int kingY, int rookY, int rookIndex){
        //moves king
        updateBoardSpot(piece, "Make Blank");
        piece.setY(kingY);
        updateBoardSpot(piece, "Put Piece");
        piece.setHasMoved(true);

        //moves rook
        updateBoardSpot(piecesArray.get(rookIndex), "Make Blank");
        piecesArray.get(rookIndex).setY(rookY);
        updateBoardSpot(piecesArray.get(rookIndex), "Put Piece");
        piecesArray.get(rookIndex).setHasMoved(true);

        if (turn == botTurn){
            botCastled = true;
        }

        switchTurn();

    }

    public boolean checkLegalMove (Piece piece, int x, int y, String f, int q){
        //move is legal if the piece movement matches up and if the move does not allow a check
       if (piece.checkMovement(x, y, board, f)){
           board[piece.getX()][piece.getY()] = 0;
           int pieceOldX = piece.getX();
           int pieceOldY = piece.getY();

           Piece oldPiece = null;
           if (f.equals("Take")){
               oldPiece = getPieceAt(x, y);
           }

           piece.setX(x);
           piece.setY(y);
           board[piece.getX()][piece.getY()] = 1;

           //calculates if the move allows a check
           boolean underCheck;
           if (f.equals("Move")){
               if (piece.getTeam().equals("White")){
                   underCheck = calculateChecks(getKing("White"), -1, q);
               } else {
                   underCheck = calculateChecks(getKing("Black"), -1, q);
               }
               board[piece.getX()][piece.getY()] = 0;
           } else {
               if (piece.getTeam().equals("White")){
                   underCheck = calculateChecks(getKing("White"), getPieceIndex(oldPiece), q);
               } else {
                   underCheck = calculateChecks(getKing("Black"), getPieceIndex(oldPiece), q);
               }
               board[piece.getX()][piece.getY()] = 1;
           }


           piece.setX(pieceOldX);
           piece.setY(pieceOldY);
           board[piece.getX()][piece.getY()] = 1;
           return !underCheck;
       }
       return false;
    }

    public void updateBoardSpot(Piece piece, String function){
        if (function.equals("Make Blank")) {
            board[piece.getX()][piece.getY()] = 0;
            boardSpotsIMG[piece.getX()][piece.getY()].setImage(blankImage);
        }
        if (function.equals("Put Piece")) {
            board[piece.getX()][piece.getY()] = 1;
            boardSpotsIMG[piece.getX()][piece.getY()].setImage(piece.getPieceImage());
        }
    }

    public void take(Piece piece, int x, int y){
        //removes the captured piece from its array and moves the new piece to that spot
        if(checkLegalMove(piece, x, y, "Take", -1)){
            updateBoardSpot(piece, "Make Blank");

            int index = 0;
            if (piece.getTeam().equals("White")) {
                for (int i=0;i<blackPiecesArray.size(); i++){
                    if (blackPiecesArray.get(i).equals(getPieceAt(x, y))){
                        index = i;
                    }
                }
                whiteCapturedPieces.add(blackPiecesArray.remove(index));
            } else {
                for (int i=0;i<whitePiecesArray.size(); i++){
                    if (whitePiecesArray.get(i).equals(getPieceAt(x, y))){
                        index = i;
                    }
                }
                blackCapturedPieces.add(whitePiecesArray.remove(index));
            }

            piece.setHasMoved(true);
            piece.setX(x);
            piece.setY(y);
            updateBoardSpot(piece, "Put Piece");
            if ((piece.getX() == 7 && piece.getDirection() == 1 || piece.getX() == 0 && piece.getDirection() == -1 ) && piece.getName().equals("Pawn")){
                setPromotionButtonVisibility(true);
            } else {
                switchTurn();
                clicks = 0;
            }

        }
    }

    @FXML
    private void flipBoard(){
        //changes the piece orientations and reflects the position and updates the images.
        orientation = orientation*-1;
        int blackOrientation = orientation*-1;
        for (Piece piece : whitePiecesArray) {
            piece.setDirection(orientation);
        }
        for (Piece piece:blackPiecesArray){
            piece.setDirection(blackOrientation);
        }

        int[][] tempBoard = new int[8][8];

        tempBoard = updateFlippedBoard(whitePiecesArray, tempBoard);
        tempBoard = updateFlippedBoard(blackPiecesArray, tempBoard);

        board = tempBoard;

        for (int i = 0; i < 8; i++){
            for (int j = 0; j < 8; j++){
                if (board[i][j]==0){
                    boardSpotsIMG[i][j].setImage(blankImage);
                } else {
                    boardSpotsIMG[i][j].setImage(getPieceAt(i, j).getPieceImage());
                }
            }
        }

    }

    public int[][] updateFlippedBoard(ArrayList<Piece> pieceArray, int[][] tempBoard) {
        for (Piece currentPiece : pieceArray) {
            currentPiece.setX(7 - currentPiece.getX());
            currentPiece.setY(7 - currentPiece.getY());
            tempBoard[currentPiece.getX()][currentPiece.getY()] = 1;
        }

        return tempBoard;
    }

    public void switchTurn(){
        //move to the next turn and checks if there is a forced end
        turn++;
        turn=turn%2;

        if (turn == 0){
            lblTurn.setText("Turn: White");
        } else {
            lblTurn.setText("Turn: Black");
        }

        boolean underCheck;
        if (turn%2 == 0){
            underCheck = calculateChecks(getKing("White"), -1, 1);
            if (underCheck){
                checkForcedEnd(getKing("White"), "Checkmate");
            } else {
                checkForcedEnd(getKing("White"), "Draw");
            }
        } else {
            underCheck = calculateChecks(getKing("Black"), -1, -1);
            if (underCheck){
                checkForcedEnd(getKing("Black"), "Checkmate");
            } else {
                checkForcedEnd(getKing("Black"), "Draw");
            }
        }
        checkDraw();

        computerTurn();

    }


    public Piece getKing(String team){
        //returns the king piece based on the team
        if (team.equals("White")){
            for (Piece piece : whitePiecesArray) {
                if (piece.getName().equals("King")) {
                    return piece;
                }
            }
        } else {
            for (Piece piece : blackPiecesArray) {
                if (piece.getName().equals("King")) {
                    return piece;
                }
            }
        }
        return null;
    }

    public int getPieceIndex(Piece piece){
        //returns a piece's index in their specific array
        if (piece.getTeam().equals("White")){
            for (int i = 0; i < whitePiecesArray.size(); i++){
                if (piece.equals(whitePiecesArray.get(i))){
                    return i;
                }
            }
        } else {
            for (int i = 0; i < blackPiecesArray.size(); i++){
                if (piece.equals(blackPiecesArray.get(i))){
                    return i;
                }
            }
        }
        return -1;
    }

    //returns true if there is a check
    public boolean calculateChecks(Piece king, int index1, int index2){
            if (king.getTeam().equals("White")){
                for (int i = 0; i < blackPiecesArray.size(); i++) {
                    if (i != index1 && i != index2 && blackPiecesArray.get(i).calculateCheckingKing(board, king)) {
                        return true;
                    }
                }
            } else {
                for (int i = 0; i < whitePiecesArray.size(); i++) {
                    if (i != index1 && i != index2 && whitePiecesArray.get(i).calculateCheckingKing(board, king)) {
                        return true;
                    }
                }
            }


        return false;
    }

    public void promotePawn(Piece piece, String type){
        //replaces the pawn with a brand new promoted piece and updates that image
        int xCoord = piece.getX();
        int yCoord = piece.getY();
        String team = piece.getTeam();
        if (team.equals("White")){
            whitePiecesArray.remove(getPieceIndex(piece));
        } else {
            blackPiecesArray.remove(getPieceIndex(piece));
        }

        switch (type) {
            case "Queen":
                if (team.equals("White")) {
                    whitePiecesArray.add(new Queen("Queen", whiteQueenImage, xCoord, yCoord, "White"));
                } else {
                    blackPiecesArray.add(new Queen("Queen", blackQueenImage, xCoord, yCoord, "Black"));
                }
                break;
            case "Rook":
                if (team.equals("White")) {
                    whitePiecesArray.add(new Rook("Rook", whiteRookImage, xCoord, yCoord, "White"));
                } else {
                    blackPiecesArray.add(new Rook("Rook", blackRookImage, xCoord, yCoord, "Black"));
                }
                break;
            case "Knight":
                if (team.equals("White")) {
                    whitePiecesArray.add(new Knight("Knight", whiteKnightImage, xCoord, yCoord, "White"));
                } else {
                    blackPiecesArray.add(new Knight("Knight", blackKnightImage, xCoord, yCoord, "Black"));
                }
                break;
            case "Bishop":
                if (team.equals("White")) {
                    whitePiecesArray.add(new Bishop("Bishop", whiteBishopImage, xCoord, yCoord, "White"));
                } else {
                    blackPiecesArray.add(new Bishop("Bishop", blackBishopImage, xCoord, yCoord, "Black"));
                }
                break;
        }

        if (team.equals("White")){
            boardSpotsIMG[xCoord][yCoord].setImage(whitePiecesArray.get(whitePiecesArray.size()-1).getPieceImage());
        } else {
            boardSpotsIMG[xCoord][yCoord].setImage(blackPiecesArray.get(blackPiecesArray.size()-1).getPieceImage());
        }

        switchTurn();
        clicks = 0;
    }

    @FXML
    private void setPromoteQueen(){
        setPromotionButtonVisibility(false);
        promotePawn(pieceClickedOn, "Queen");
    }

    @FXML
    private void setPromoteRook(){
        setPromotionButtonVisibility(false);
        promotePawn(pieceClickedOn, "Rook");
    }

    @FXML
    private void setPromoteKnight(){
        setPromotionButtonVisibility(false);
        promotePawn(pieceClickedOn, "Knight");
    }

    @FXML
    private void setPromoteBishop(){
        setPromotionButtonVisibility(false);
        promotePawn(pieceClickedOn, "Bishop");
    }

    public void setPromotionButtonVisibility(boolean v){
        queenPromoteButton.setVisible(v);
        rookPromoteButton.setVisible(v);
        knightPromoteButton.setVisible(v);
        bishopPromoteButton.setVisible(v);
    }

    public void checkForcedEnd(Piece king, String end){
        //if there are no legal moves for a team and there is a check, then checkmate, if there is no check, then stalemate
        int xCoord = king.getX();
        int yCoord = king.getY();
        boolean gameOver = false;
        if (!kingCanMove(king, xCoord-1,yCoord ) && !kingCanMove(king, xCoord+1,yCoord) && !kingCanMove(king, xCoord-1,yCoord+1) && !kingCanMove(king, xCoord+1,yCoord+1) && !kingCanMove(king, xCoord-1,yCoord-1) && !kingCanMove(king, xCoord+1,yCoord-1) && !kingCanMove(king, xCoord,yCoord-1) && !kingCanMove(king, xCoord,yCoord+1)){
            if (king.getTeam().equals("White")){
                gameOver = noLegalMoves(whitePiecesArray);
            } else {
                gameOver = noLegalMoves(blackPiecesArray);
            }
        }

        if (gameOver){
            if (end.equals("Draw")){
                endGame("Draw by Stalemate", "Draw");
            } else {
                endGame(end, king.getTeam());
            }
        }

    }

    public boolean kingCanMove(Piece king, int x, int y){
        //returns if the king can move
        if ((x<8 && x>-1) && (y>-1&&y<8)){
            if (board[x][y]==0) {
                return checkLegalMove(king, x, y, "Move", -1);
            }
            if (board[x][y] == 1 && !getPieceAt(x, y).getTeam().equals(king.getTeam())){
                return checkLegalMove(king, x, y, "Take", -1);
            }
        }
        return false;
    }

    public boolean noLegalMoves(ArrayList<Piece> piecesArray) {
        //goes through all possible calculated moves by each piece and if no piece has any moves then return true
        for (Piece piece : piecesArray) {
            ArrayList<int[]> possibleMoves = piece.calculatePossibleMovements(board, whitePiecesArray, blackPiecesArray);
            if (possibleMoves.size() > 0) {
                for (int[] possibleMove : possibleMoves) {
                    if (board[possibleMove[0]][possibleMove[1]] == 1 && checkLegalMove(piece, possibleMove[0], possibleMove[1], "Take", -1)){
                        return false;
                    } else if (checkLegalMove(piece, possibleMove[0], possibleMove[1], "Move", -1)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }


    public void checkDraw(){
        //Different cases for when game ends due to a draw by insufficient material:
        if (whitePiecesArray.size() == 1 && blackPiecesArray.size() == 1){
            endGame("Draw by insufficient material", "Draw");
        }
        if (whitePiecesArray.size() == 1){
            if (blackPiecesArray.size() == 2 && ((blackPiecesArray.get(0).getName().equals("Knight") || blackPiecesArray.get(1).getName().equals("Knight")) || (blackPiecesArray.get(0).getName().equals("Bishop") || blackPiecesArray.get(1).getName().equals("Bishop")))){
                endGame("Draw by insufficient material", "Draw");
            }
        }
        if (blackPiecesArray.size() == 1){
            if (whitePiecesArray.size() == 2 && ((whitePiecesArray.get(0).getName().equals("Knight") || whitePiecesArray.get(1).getName().equals("Knight")) || (whitePiecesArray.get(0).getName().equals("Bishop") || whitePiecesArray.get(1).getName().equals("Bishop")))){
                endGame("Draw by insufficient material", "Draw");
            }
        }
        if (blackPiecesArray.size() == 2 && whitePiecesArray.size() == 2){
            if ((whitePiecesArray.get(0).getName().equals("Bishop") || whitePiecesArray.get(1).getName().equals("Bishop")) && (blackPiecesArray.get(0).getName().equals("Bishop") || blackPiecesArray.get(1).getName().equals("Bishop"))){
                int whiteIndex = 0;
                if(whitePiecesArray.get(1).getName().equals("Bishop")){
                    whiteIndex = 1;
                }
                int blackIndex = 0;
                if(blackPiecesArray.get(1).getName().equals("Bishop")){
                    blackIndex = 1;
                }
                if ((whitePiecesArray.get(whiteIndex).getX() + whitePiecesArray.get(whiteIndex).getY())%2 == (blackPiecesArray.get(blackIndex).getX() + blackPiecesArray.get(blackIndex).getY())%2){
                    endGame("Draw by insufficient material", "Draw");
                }
            }
        }
    }

    public void endGame(String message, String team){
        //displays end game screen/winner
        endScreen.setVisible(true);
        if (team.equals("Draw")){
            winningRectangle.setVisible(false);
            endingLabel.setText(message);
        } else {
            winningRectangle.setVisible(true);
            if (team.equals("White")){
                winningRectangle.setLayoutX(247);
                endingLabel.setText("Black wins by " + message);

            } else {
                winningRectangle.setLayoutX(45);
                endingLabel.setText("White wins by " + message);
            }
        }


    }

    @FXML
    private void resign(){
        if (turn == 0){
            endGame("resignation", "White");
        } else {
            endGame("resignation", "Black");
        }
    }






    public void onClick(){

    }

    @FXML
    private void playAgain(){
        endScreen.setVisible(false);
        initializePieces();
        setBoard();
        turn = 0;
        clicks = 0;

    }

    @FXML
    private void againstBot(){
        gameMode = 1;
        if (Math.random()>0.5){
            botColor = "White";
            botTurn = 0;
        } else {
            botColor = "Black";
            botTurn = 1;
        }

        botColor = "Black";
        botTurn = 1;
        chooseGameModePane.setVisible(false);
    }

    @FXML
    private void twoPlayer(){
        gameMode = 0;
        chooseGameModePane.setVisible(false);
    }

    public void computerTurn(){
        //goes through the main checklist of priority of moves
        if (gameMode == 1){
            if (turn == botTurn){
                if (!defendCheckMateThreat()){
                    if(!defendTakeThreat()){
                        if(!doValuableTake(-1)){
                            developPiece();
                        }
                    }
                }
            }
        }
    }

    public boolean defendCheckMateThreat(){
        for (int i = 0; i < whitePiecesArray.size(); i++){
            whitePiecesArray.get(i).calculatePossibleMovements(board, whitePiecesArray, blackPiecesArray);
        }
        for (int i = 0; i < blackPiecesArray.size(); i++){
            blackPiecesArray.get(i).calculatePossibleMovements(board, whitePiecesArray, blackPiecesArray);
        }
        return false;
    }

    public boolean defendTakeThreat(){
        //checks if one of its pieces is under attack and responds accordingly
        if (botColor.equals("White")){
            ArrayList<Piece> piecesThatAreAttacked = new ArrayList<>();
            for (int i = 0; i < whitePiecesArray.size(); i++){
                if (attacked(whitePiecesArray.get(i), blackPiecesArray, whitePiecesArray, "Check", -1)){
                    piecesThatAreAttacked.add(whitePiecesArray.get(i));
                }
            }
            if (piecesThatAreAttacked.size() > 0){
                int maxVal = 0;
                int index = 0;
                for (int i = 0; i < piecesThatAreAttacked.size(); i++){
                    if (piecesThatAreAttacked.get(i).getValue() > maxVal){
                        maxVal = piecesThatAreAttacked.get(i).getValue();
                        index = i;
                    }
                }

                if (doValuableTake(maxVal)){
                    return true;
                } else {
                    return attacked(whitePiecesArray.get(index), blackPiecesArray, whitePiecesArray, "Move", -1);
                }
            }



        }
        return false;
    }

    public boolean doValuableTake(int z){
        //checks if there is a valuable piece it can take
        ArrayList<Piece> botPieceArray = whitePiecesArray;
        ArrayList<Piece> oppositePieceArray = blackPiecesArray;
        if (botColor.equals("Black")){
            botPieceArray = blackPiecesArray;
            oppositePieceArray = whitePiecesArray;
        }
            int x = -1;
            int y = -1;
            int valDifference = 0;
            int index = -1;
            for (int i = 0; i < botPieceArray.size(); i++){
                int maxValue = 0;
                Piece piece = null;
                for (int j = 0; j < oppositePieceArray.size(); j++){
                    if (checkPieceAttacking(botPieceArray.get(i), oppositePieceArray.get(j), -1)){
                        if (oppositePieceArray.get(j).getValue() > maxValue){
                            maxValue = oppositePieceArray.get(j).getValue();
                            piece = oppositePieceArray.get(j);
                        }
                    }
                }
                if (piece != null && maxValue - botPieceArray.get(i).getValue() > valDifference && maxValue > z){
                    if (checkGoodMove(botPieceArray.get(i), piece.getX(), piece.getY(), "Take")){
                        index = i;
                        x = piece.getX();
                        y = piece.getY();
                        valDifference = piece.getValue() - botPieceArray.get(i).getValue();
                    }
                }

            }

            if (index != -1){
                take(botPieceArray.get(index), x, y);
                return true;
            }
        return false;
    }


    public boolean attacked(Piece currentPiece, ArrayList<Piece> attackingArray, ArrayList<Piece> defendingArray, String function, int q){
        //returns true if the requested piece is being attacked by more attackers than defenders or by a piece with a lower value
        int numAttackers = 0;
        int smallestAttacker = -1;
        int numDefenders = 0;
        int smallestDefender = -1;
        for (int j = 0; j < attackingArray.size(); j++){
            if (j != q && checkPieceAttacking(attackingArray.get(j), currentPiece, q)){
                numAttackers++;
                if ((smallestAttacker > -1 && attackingArray.get(j).getValue() < attackingArray.get(smallestAttacker).getValue()) || smallestAttacker == -1){
                    smallestAttacker = j;
                }
                if (function.equals("Move") && checkPieceAttacking(currentPiece, attackingArray.get(j), q)){
                    if (checkGoodMove(currentPiece, attackingArray.get(j).getX(), attackingArray.get(j).getY(), "Take")){
                        take(currentPiece, attackingArray.get(j).getX(), attackingArray.get(j).getY());
                        return true;
                    }
                }
            }
        }
        for (int j = 0; j < defendingArray.size(); j++){
            if (j != getPieceIndex(currentPiece) && checkPieceAttacking(defendingArray.get(j), currentPiece, q)){
                numDefenders++;
                if ((smallestDefender > -1 && defendingArray.get(j).getValue() < defendingArray.get(smallestDefender).getValue()) || smallestDefender == -1){
                    smallestDefender = j;
                }
            }
        }
        if (function.equals("Move")){
            for (int i = 0; i < currentPiece.getPossibleMovements().size(); i++){
                int x = currentPiece.getPossibleMovements().get(i)[0];
                int y = currentPiece.getPossibleMovements().get(i)[1];
                if (board[x][y] == 0 && checkLegalMove(currentPiece, x, y, "Move", q)){
                    if (checkGoodMove(currentPiece, x, y, "Move")){
                        move(currentPiece, x, y);
                        return true;
                    }
                }
            }
        }
        if (function.equals("Check")){
            if (numAttackers > 0 && attackingArray.get(smallestAttacker).getValue() < currentPiece.getValue()){
                return true;
            }
            if (numDefenders > 0 && numAttackers > numDefenders){
                return true;
            }
        }

        return false;
    }


    public boolean checkPieceAttacking(Piece attackingPiece, Piece defendingPiece, int q){
        return checkLegalMove(attackingPiece, defendingPiece.getX(), defendingPiece.getY(), "Take", q);
    }

    public boolean checkGoodMove(Piece piece, int x, int y, String function){
        //returns true if the requested move does not move the piece to a heavily guarded spot
        boolean goodMove = true;
        board[piece.getX()][piece.getY()] = 0;
        int pieceOldX = piece.getX();
        int pieceOldY = piece.getY();
        int oldPieceIndex = -1;
        if (function.equals("Take")){
            oldPieceIndex = getPieceIndex(getPieceAt(x, y));
        }
        piece.setX(x);
        piece.setY(y);
        board[piece.getX()][piece.getY()] = 1;

        if (piece.getTeam().equals("White")){
            for (Piece whitePiece : whitePiecesArray) {
                if (attacked(whitePiece, blackPiecesArray, whitePiecesArray, "Check", oldPieceIndex)) {
                    if (getPieceAttackingValue(piece, blackPiecesArray, oldPieceIndex) < whitePiece.getValue()){
                        goodMove = false;
                    }
                }
            }
        } else {
            for (Piece blackPiece : blackPiecesArray) {
                if (attacked(blackPiece, whitePiecesArray, blackPiecesArray, "Check", oldPieceIndex)) {
                    if (getPieceAttackingValue(piece, whitePiecesArray, oldPieceIndex) < blackPiece.getValue()){
                        goodMove = false;
                    }
                }
            }
        }

        if (function.equals("Move")){
            board[piece.getX()][piece.getY()] = 0;
        }
        piece.setX(pieceOldX);
        piece.setY(pieceOldY);
        board[piece.getX()][piece.getY()] = 1;

        return goodMove;

    }

    public int getPieceAttackingValue(Piece piece, ArrayList<Piece> oppositeList, int q){
        int maxValue = 0;
        for (int i = 0; i < oppositeList.size(); i++){
            if (i != q && checkPieceAttacking(piece, oppositeList.get(i), q)){
                if (oppositeList.get(i).getValue() > maxValue){
                    maxValue = oppositeList.get(i).getValue();
                }
            }
        }
        return maxValue;
    }

    boolean botCastled = false;

    public void developPiece(){
        //goes through the most common development moves
        ArrayList<Piece> botPieceArray = whitePiecesArray;
        if (botColor.equals("Black")){
            botPieceArray = blackPiecesArray;
        }



        boolean done = false;
        int random = (int)(Math.random()*8 + 1);
        for (int i = 0; i < botPieceArray.size(); i++){
            if (botPieceArray.get(i).getName().equals("Pawn")){
                if (botPieceArray.get(i).getY() == 3 && (!botPieceArray.get(i).getHasMoved() || random == 1))   {
                    for (int y = 0; y < botPieceArray.get(i).getPossibleMovements().size(); y++){
                        if (board[botPieceArray.get(i).getPossibleMovements().get(y)[0]][botPieceArray.get(i).getPossibleMovements().get(y)[1]] == 0 && checkLegalMove(botPieceArray.get(i),botPieceArray.get(i).getPossibleMovements().get(y)[0], botPieceArray.get(i).getPossibleMovements().get(y)[1], "Move", -1)){
                            if (checkGoodMove(botPieceArray.get(i),botPieceArray.get(i).getPossibleMovements().get(y)[0], botPieceArray.get(i).getPossibleMovements().get(y)[1], "Move")){
                                move(botPieceArray.get(i),botPieceArray.get(i).getPossibleMovements().get(y)[0], botPieceArray.get(i).getPossibleMovements().get(y)[1]);
                                done = true;
                            }
                        }
                    }
                }
            }
        }
        if (!done){
            for (int i = 0; i < botPieceArray.size(); i++){
                if (botPieceArray.get(i).getName().equals("Pawn")){
                    if (botPieceArray.get(i).getY() == 4 && (!botPieceArray.get(i).getHasMoved() || random == 2))   {
                        for (int y = 0; y < botPieceArray.get(i).getPossibleMovements().size(); y++){
                            if (board[botPieceArray.get(i).getPossibleMovements().get(y)[0]][botPieceArray.get(i).getPossibleMovements().get(y)[1]] == 0 && checkLegalMove(botPieceArray.get(i),botPieceArray.get(i).getPossibleMovements().get(y)[0], botPieceArray.get(i).getPossibleMovements().get(y)[1], "Move", -1)){
                                if (checkGoodMove(botPieceArray.get(i),botPieceArray.get(i).getPossibleMovements().get(y)[0], botPieceArray.get(i).getPossibleMovements().get(y)[1], "Move")){
                                    move(botPieceArray.get(i),botPieceArray.get(i).getPossibleMovements().get(y)[0], botPieceArray.get(i).getPossibleMovements().get(y)[1]);
                                    done = true;
                                }
                            }
                        }
                    }
                }
            }
        }
            for (int i = 0; i < botPieceArray.size(); i++){
                if (!done && botPieceArray.get(i).getName().equals("Knight")){
                    if ((!botPieceArray.get(i).getHasMoved() || random == 3))   {
                        for (int y = 0; y < botPieceArray.get(i).getPossibleMovements().size(); y++){
                            if (board[botPieceArray.get(i).getPossibleMovements().get(y)[0]][botPieceArray.get(i).getPossibleMovements().get(y)[1]] == 0 && checkLegalMove(botPieceArray.get(i),botPieceArray.get(i).getPossibleMovements().get(y)[0], botPieceArray.get(i).getPossibleMovements().get(y)[1], "Move", -1)){
                                if (checkGoodMove(botPieceArray.get(i),botPieceArray.get(i).getPossibleMovements().get(y)[0], botPieceArray.get(i).getPossibleMovements().get(y)[1], "Move")){
                                    move(botPieceArray.get(i),botPieceArray.get(i).getPossibleMovements().get(y)[0], botPieceArray.get(i).getPossibleMovements().get(y)[1]);
                                    done = true;
                                }
                            }
                        }
                    }
                }
            }

        if (!done) {
            for (int i = 0; i < botPieceArray.size(); i++){
                if (botPieceArray.get(i).getName().equals("Bishop")){
                    if ((!botPieceArray.get(i).getHasMoved() || random == 4))   {
                        for (int y = 0; y < botPieceArray.get(i).getPossibleMovements().size(); y++){
                            if (board[botPieceArray.get(i).getPossibleMovements().get(y)[0]][botPieceArray.get(i).getPossibleMovements().get(y)[1]] == 0 && checkLegalMove(botPieceArray.get(i),botPieceArray.get(i).getPossibleMovements().get(y)[0], botPieceArray.get(i).getPossibleMovements().get(y)[1], "Move", -1)){
                                if (checkGoodMove(botPieceArray.get(i),botPieceArray.get(i).getPossibleMovements().get(y)[0], botPieceArray.get(i).getPossibleMovements().get(y)[1], "Move")){
                                    move(botPieceArray.get(i),botPieceArray.get(i).getPossibleMovements().get(y)[0], botPieceArray.get(i).getPossibleMovements().get(y)[1]);
                                    done = true;
                                }
                            }
                        }
                    }
                }
            }
        }
        boolean attemptingCastleThisRound = false;
        if (!done && !botCastled) {
            for (int i = 0; i < botPieceArray.size(); i++){
                if (botPieceArray.get(i).getName().equals("King")){
                    if ((!botPieceArray.get(i).getHasMoved()))  {
                        attemptingCastleThisRound = true;
                        castle(botPieceArray.get(i), botPieceArray.get(i).getX(), botPieceArray.get(i).getY()-2);
                        castle(botPieceArray.get(i), botPieceArray.get(i).getX(), botPieceArray.get(i).getY()+2);
                    }
                }
            }
        }
        if (attemptingCastleThisRound && botCastled){
            done = true;
        }

        System.out.println("done");;
    }




    //puzzles for presentation
//    @FXML
//    private void puzzle1(){
//        turn = 0;
//        whitePiecesArray.clear();
//
//        whitePiecesArray.add(new Rook("Rook", whiteRookImage, 7, 0,"White"));
//        whitePiecesArray.add(new Knight("Knight", whiteKnightImage, 7, 1, "White"));
//        whitePiecesArray.add(new Bishop("Bishop", whiteBishopImage, 7, 2, "White"));
//        whitePiecesArray.add(new Queen("Queen", whiteQueenImage, 7, 3, "White"));
//        whitePiecesArray.add(new King("King", whiteKingImage, 7, 4, "White"));
//        whitePiecesArray.add(new Bishop("Bishop", whiteBishopImage, 3, 1, "White"));
//        whitePiecesArray.add(new Knight("Knight", whiteKnightImage, 5, 5, "White"));
//        whitePiecesArray.add(new Rook("Rook", whiteRookImage, 7, 7, "White"));
//        whitePiecesArray.add(new Pawn("Pawn", whitePawnImage, 6, 0, "White"));
//        whitePiecesArray.add(new Pawn("Pawn", whitePawnImage, 6, 1, "White"));
//        whitePiecesArray.add(new Pawn("Pawn", whitePawnImage, 6, 2, "White"));
//        whitePiecesArray.add(new Pawn("Pawn", whitePawnImage, 4, 3, "White"));
//        whitePiecesArray.add(new Pawn("Pawn", whitePawnImage, 5, 4, "White"));
//        whitePiecesArray.add(new Pawn("Pawn", whitePawnImage, 6, 5, "White"));
//        whitePiecesArray.add(new Pawn("Pawn", whitePawnImage, 6, 6, "White"));
//        whitePiecesArray.add(new Pawn("Pawn", whitePawnImage, 6, 7, "White"));
//
//
//
//        blackPiecesArray.clear();
//        blackPiecesArray.add(new Rook("Rook", blackRookImage, 0, 0, "Black"));
//        blackPiecesArray.add(new Knight("Knight", blackKnightImage, 2, 2, "Black"));
//        blackPiecesArray.add(new Bishop("Bishop", blackBishopImage, 0, 2, "Black"));
//        blackPiecesArray.add(new Queen("Queen", blackQueenImage, 0, 3, "Black"));
//        blackPiecesArray.add(new King("King", blackKingImage, 0, 4, "Black"));
//        blackPiecesArray.add(new Bishop("Bishop", blackBishopImage, 0, 5, "Black"));
//        blackPiecesArray.add(new Knight("Knight", blackKnightImage, 2, 5, "Black"));
//        blackPiecesArray.add(new Rook("Rook", blackRookImage, 0, 7, "Black"));
//        blackPiecesArray.add(new Pawn("Pawn", blackPawnImage, 1, 0, "Black"));
//        blackPiecesArray.add(new Pawn("Pawn", blackPawnImage, 1, 1, "Black"));
//        blackPiecesArray.add(new Pawn("Pawn", blackPawnImage, 1, 2, "Black"));
//        blackPiecesArray.add(new Pawn("Pawn", blackPawnImage, 3, 3, "Black"));
//        blackPiecesArray.add(new Pawn("Pawn", blackPawnImage, 2, 4, "Black"));
//        blackPiecesArray.add(new Pawn("Pawn", blackPawnImage, 1, 5, "Black"));
//        blackPiecesArray.add(new Pawn("Pawn", blackPawnImage, 1, 6, "Black"));
//        blackPiecesArray.add(new Pawn("Pawn", blackPawnImage, 1, 7, "Black"));
//
//        for (Piece value : whitePiecesArray) {
//            value.setDirection(-1);
//        }
//        for (Piece piece : blackPiecesArray) {
//            piece.setDirection(1);
//        }
//
//
//        setBoard();
//
//    }
//
//    @FXML
//    private void puzzle2(){
//        turn = 1;
//        whitePiecesArray.clear();
//
//        whitePiecesArray.add(new Rook("Rook", whiteRookImage, 7, 0,"White"));
//        whitePiecesArray.add(new Knight("Knight", whiteKnightImage, 5, 2, "White"));
//        whitePiecesArray.add(new Bishop("Bishop", whiteBishopImage, 6, 3, "White"));
//        whitePiecesArray.add(new Queen("Queen", whiteQueenImage, 7, 3, "White"));
//        whitePiecesArray.add(new King("King", whiteKingImage, 7, 4, "White"));
//        whitePiecesArray.add(new Bishop("Bishop", whiteBishopImage, 3, 1, "White"));
//        whitePiecesArray.add(new Knight("Knight", whiteKnightImage, 5, 5, "White"));
//        whitePiecesArray.add(new Rook("Rook", whiteRookImage, 7, 7, "White"));
//        whitePiecesArray.add(new Pawn("Pawn", whitePawnImage, 6, 0, "White"));
//        whitePiecesArray.add(new Pawn("Pawn", whitePawnImage, 6, 1, "White"));
//        whitePiecesArray.add(new Pawn("Pawn", whitePawnImage, 6, 2, "White"));
//        whitePiecesArray.add(new Pawn("Pawn", whitePawnImage, 4, 3, "White"));
//        whitePiecesArray.add(new Pawn("Pawn", whitePawnImage, 5, 4, "White"));
//        whitePiecesArray.add(new Pawn("Pawn", whitePawnImage, 6, 5, "White"));
//        whitePiecesArray.add(new Pawn("Pawn", whitePawnImage, 6, 6, "White"));
//        whitePiecesArray.add(new Pawn("Pawn", whitePawnImage, 6, 7, "White"));
//
//
//
//        blackPiecesArray.clear();
//        blackPiecesArray.add(new Rook("Rook", blackRookImage, 0, 0, "Black"));
//        blackPiecesArray.add(new Knight("Knight", blackKnightImage, 2, 2, "Black"));
//        blackPiecesArray.add(new Bishop("Bishop", blackBishopImage, 0, 2, "Black"));
//        blackPiecesArray.add(new Queen("Queen", blackQueenImage, 1, 4, "Black"));
//        blackPiecesArray.add(new King("King", blackKingImage, 0, 4, "Black"));
//        blackPiecesArray.add(new Bishop("Bishop", blackBishopImage, 0, 5, "Black"));
//        blackPiecesArray.add(new Knight("Knight", blackKnightImage, 2, 5, "Black"));
//        blackPiecesArray.add(new Rook("Rook", blackRookImage, 0, 7, "Black"));
//        blackPiecesArray.add(new Pawn("Pawn", blackPawnImage, 1, 0, "Black"));
//        blackPiecesArray.add(new Pawn("Pawn", blackPawnImage, 1, 1, "Black"));
//        blackPiecesArray.add(new Pawn("Pawn", blackPawnImage, 1, 2, "Black"));
//        blackPiecesArray.add(new Pawn("Pawn", blackPawnImage, 3, 3, "Black"));
//        blackPiecesArray.add(new Pawn("Pawn", blackPawnImage, 2, 4, "Black"));
//        blackPiecesArray.add(new Pawn("Pawn", blackPawnImage, 1, 5, "Black"));
//        blackPiecesArray.add(new Pawn("Pawn", blackPawnImage, 1, 6, "Black"));
//        blackPiecesArray.add(new Pawn("Pawn", blackPawnImage, 1, 7, "Black"));
//
//        for (Piece value : whitePiecesArray) {
//            value.setDirection(-1);
//        }
//        for (Piece piece : blackPiecesArray) {
//            piece.setDirection(1);
//        }
//
//
//        setBoard();
//        flipBoard();
//    }
//
//    @FXML
//    private void puzzle3(){
//        turn = 0;
//        whitePiecesArray.clear();
//
//        whitePiecesArray.add(new Rook("Rook", whiteRookImage, 7, 0,"White"));
//        whitePiecesArray.add(new Knight("Knight", whiteKnightImage, 7, 1, "White"));
//        whitePiecesArray.add(new Bishop("Bishop", whiteBishopImage, 7, 2, "White"));
//        whitePiecesArray.add(new Queen("Queen", whiteQueenImage, 7, 3, "White"));
//        whitePiecesArray.add(new King("King", whiteKingImage, 7, 4, "White"));
//        whitePiecesArray.add(new Bishop("Bishop", whiteBishopImage, 3, 1, "White"));
//        whitePiecesArray.add(new Knight("Knight", whiteKnightImage, 5, 5, "White"));
//        whitePiecesArray.add(new Rook("Rook", whiteRookImage, 7, 7, "White"));
//        whitePiecesArray.add(new Pawn("Pawn", whitePawnImage, 6, 0, "White"));
//        whitePiecesArray.add(new Pawn("Pawn", whitePawnImage, 6, 1, "White"));
//        whitePiecesArray.add(new Pawn("Pawn", whitePawnImage, 6, 2, "White"));
//        whitePiecesArray.add(new Pawn("Pawn", whitePawnImage, 4, 3, "White"));
//        whitePiecesArray.add(new Pawn("Pawn", whitePawnImage, 5, 4, "White"));
//        whitePiecesArray.add(new Pawn("Pawn", whitePawnImage, 6, 5, "White"));
//        whitePiecesArray.add(new Pawn("Pawn", whitePawnImage, 6, 6, "White"));
//        whitePiecesArray.add(new Pawn("Pawn", whitePawnImage, 6, 7, "White"));
//
//
//
//        blackPiecesArray.clear();
//        blackPiecesArray.add(new Rook("Rook", blackRookImage, 0, 0, "Black"));
//        blackPiecesArray.add(new Knight("Knight", blackKnightImage, 2, 2, "Black"));
//        blackPiecesArray.add(new Bishop("Bishop", blackBishopImage, 0, 2, "Black"));
//        blackPiecesArray.add(new Queen("Queen", blackQueenImage, 0, 3, "Black"));
//        blackPiecesArray.add(new King("King", blackKingImage, 0, 4, "Black"));
//        blackPiecesArray.add(new Bishop("Bishop", blackBishopImage, 0, 5, "Black"));
//        blackPiecesArray.add(new Knight("Knight", blackKnightImage, 2, 5, "Black"));
//        blackPiecesArray.add(new Rook("Rook", blackRookImage, 0, 7, "Black"));
//        blackPiecesArray.add(new Pawn("Pawn", blackPawnImage, 1, 0, "Black"));
//        blackPiecesArray.add(new Pawn("Pawn", blackPawnImage, 1, 1, "Black"));
//        blackPiecesArray.add(new Pawn("Pawn", blackPawnImage, 1, 2, "Black"));
//        blackPiecesArray.add(new Pawn("Pawn", blackPawnImage, 3, 3, "Black"));
//        blackPiecesArray.add(new Pawn("Pawn", blackPawnImage, 2, 4, "Black"));
//        blackPiecesArray.add(new Pawn("Pawn", blackPawnImage, 1, 5, "Black"));
//        blackPiecesArray.add(new Pawn("Pawn", blackPawnImage, 1, 6, "Black"));
//        blackPiecesArray.add(new Pawn("Pawn", blackPawnImage, 1, 7, "Black"));
//
//        for (Piece value : whitePiecesArray) {
//            value.setDirection(-1);
//        }
//        for (Piece piece : blackPiecesArray) {
//            piece.setDirection(1);
//        }
//
//
//        setBoard();
//    }
//
//    @FXML
//    private void puzzle4(){
//        turn = 0;
//        whitePiecesArray.clear();
//
//        whitePiecesArray.add(new Rook("Rook", whiteRookImage, 7, 0,"White"));
//        whitePiecesArray.add(new Knight("Knight", whiteKnightImage, 7, 1, "White"));
//        whitePiecesArray.add(new Bishop("Bishop", whiteBishopImage, 7, 2, "White"));
//        whitePiecesArray.add(new Queen("Queen", whiteQueenImage, 7, 3, "White"));
//        whitePiecesArray.add(new King("King", whiteKingImage, 7, 4, "White"));
//        whitePiecesArray.add(new Bishop("Bishop", whiteBishopImage, 7, 5, "White"));
//        whitePiecesArray.add(new Knight("Knight", whiteKnightImage, 7, 6, "White"));
//        whitePiecesArray.add(new Rook("Rook", whiteRookImage, 7, 7, "White"));
//        whitePiecesArray.add(new Pawn("Pawn", whitePawnImage, 6, 0, "White"));
//        whitePiecesArray.add(new Pawn("Pawn", whitePawnImage, 2, 2, "White"));
//        whitePiecesArray.add(new Pawn("Pawn", whitePawnImage, 6, 2, "White"));
//        whitePiecesArray.add(new Pawn("Pawn", whitePawnImage, 6, 3, "White"));
//        whitePiecesArray.add(new Pawn("Pawn", whitePawnImage, 6, 4, "White"));
//        whitePiecesArray.add(new Pawn("Pawn", whitePawnImage, 6, 5, "White"));
//        whitePiecesArray.add(new Pawn("Pawn", whitePawnImage, 6, 6, "White"));
//        whitePiecesArray.add(new Pawn("Pawn", whitePawnImage, 6, 7, "White"));
//
//
//
//        blackPiecesArray.clear();
//        blackPiecesArray.add(new Rook("Rook", blackRookImage, 1, 1, "Black"));
//        blackPiecesArray.add(new Knight("Knight", blackKnightImage, 0, 3, "Black"));
//        blackPiecesArray.add(new Bishop("Bishop", blackBishopImage, 2, 4, "Black"));
//        blackPiecesArray.add(new Queen("Queen", blackQueenImage, 1, 4, "Black"));
//        blackPiecesArray.add(new King("King", blackKingImage, 0, 4, "Black"));
//        blackPiecesArray.add(new Bishop("Bishop", blackBishopImage, 0, 5, "Black"));
//        blackPiecesArray.add(new Knight("Knight", blackKnightImage, 0, 6, "Black"));
//        blackPiecesArray.add(new Rook("Rook", blackRookImage, 0, 7, "Black"));
//        blackPiecesArray.add(new Pawn("Pawn", blackPawnImage, 1, 0, "Black"));
//        blackPiecesArray.add(new Pawn("Pawn", blackPawnImage, 2, 1, "Black"));
//        blackPiecesArray.add(new Pawn("Pawn", blackPawnImage, 2, 3, "Black"));
//        blackPiecesArray.add(new Pawn("Pawn", blackPawnImage, 3, 4, "Black"));
//        blackPiecesArray.add(new Pawn("Pawn", blackPawnImage, 1, 5, "Black"));
//        blackPiecesArray.add(new Pawn("Pawn", blackPawnImage, 1, 6, "Black"));
//        blackPiecesArray.add(new Pawn("Pawn", blackPawnImage, 1, 7, "Black"));
//
//        for (Piece value : whitePiecesArray) {
//            value.setDirection(-1);
//        }
//        for (Piece piece : blackPiecesArray) {
//            piece.setDirection(1);
//        }
//
//
//        setBoard();
//    }
//
//    @FXML
//    private void puzzle5(){
//        turn = 0;
//        whitePiecesArray.clear();
//
//        whitePiecesArray.add(new Rook("Rook", whiteRookImage, 7, 0,"White"));
//        whitePiecesArray.add(new Knight("Knight", whiteKnightImage, 7, 1, "White"));
//        whitePiecesArray.add(new Bishop("Bishop", whiteBishopImage, 7, 2, "White"));
//        whitePiecesArray.add(new Queen("Queen", whiteQueenImage, 7, 3, "White"));
//        whitePiecesArray.add(new King("King", whiteKingImage, 7, 4, "White"));
//        whitePiecesArray.add(new Bishop("Bishop", whiteBishopImage, 7, 5, "White"));
//        whitePiecesArray.add(new Knight("Knight", whiteKnightImage, 7, 6, "White"));
//        whitePiecesArray.add(new Rook("Rook", whiteRookImage, 7, 7, "White"));
//        whitePiecesArray.add(new Pawn("Pawn", whitePawnImage, 6, 0, "White"));
//        whitePiecesArray.add(new Pawn("Pawn", whitePawnImage, 2, 2, "White"));
//        whitePiecesArray.add(new Pawn("Pawn", whitePawnImage, 6, 2, "White"));
//        whitePiecesArray.add(new Pawn("Pawn", whitePawnImage, 6, 3, "White"));
//        whitePiecesArray.add(new Pawn("Pawn", whitePawnImage, 6, 4, "White"));
//        whitePiecesArray.add(new Pawn("Pawn", whitePawnImage, 6, 5, "White"));
//        whitePiecesArray.add(new Pawn("Pawn", whitePawnImage, 6, 6, "White"));
//        whitePiecesArray.add(new Pawn("Pawn", whitePawnImage, 6, 7, "White"));
//
//
//
//        blackPiecesArray.clear();
//        blackPiecesArray.add(new Rook("Rook", blackRookImage, 1, 1, "Black"));
//        blackPiecesArray.add(new Knight("Knight", blackKnightImage, 0, 3, "Black"));
//        blackPiecesArray.add(new Bishop("Bishop", blackBishopImage, 2, 4, "Black"));
//        blackPiecesArray.add(new Queen("Queen", blackQueenImage, 1, 4, "Black"));
//        blackPiecesArray.add(new King("King", blackKingImage, 0, 4, "Black"));
//        blackPiecesArray.add(new Bishop("Bishop", blackBishopImage, 0, 5, "Black"));
//        blackPiecesArray.add(new Knight("Knight", blackKnightImage, 0, 6, "Black"));
//        blackPiecesArray.add(new Rook("Rook", blackRookImage, 0, 7, "Black"));
//        blackPiecesArray.add(new Pawn("Pawn", blackPawnImage, 1, 0, "Black"));
//        blackPiecesArray.add(new Pawn("Pawn", blackPawnImage, 2, 1, "Black"));
//        blackPiecesArray.add(new Pawn("Pawn", blackPawnImage, 2, 3, "Black"));
//        blackPiecesArray.add(new Pawn("Pawn", blackPawnImage, 3, 4, "Black"));
//        blackPiecesArray.add(new Pawn("Pawn", blackPawnImage, 1, 5, "Black"));
//        blackPiecesArray.add(new Pawn("Pawn", blackPawnImage, 1, 6, "Black"));
//        blackPiecesArray.add(new Pawn("Pawn", blackPawnImage, 1, 7, "Black"));
//
//        for (Piece value : whitePiecesArray) {
//            value.setDirection(-1);
//        }
//        for (Piece piece : blackPiecesArray) {
//            piece.setDirection(1);
//        }
//
//
//        setBoard();
//    }
//
//    @FXML
//    private void puzzle6(){
//        turn = 0;
//        whitePiecesArray.clear();
//
//        whitePiecesArray.add(new Rook("Rook", whiteRookImage, 7, 0,"White"));
//        whitePiecesArray.add(new Knight("Knight", whiteKnightImage, 7, 1, "White"));
//        whitePiecesArray.add(new Bishop("Bishop", whiteBishopImage, 7, 2, "White"));
//        whitePiecesArray.add(new Queen("Queen", whiteQueenImage, 7, 3, "White"));
//        whitePiecesArray.add(new King("King", whiteKingImage, 7, 4, "White"));
//        whitePiecesArray.add(new Bishop("Bishop", whiteBishopImage, 4, 2, "White"));
//        whitePiecesArray.add(new Knight("Knight", whiteKnightImage, 7, 6, "White"));
//        whitePiecesArray.add(new Rook("Rook", whiteRookImage, 7, 7, "White"));
//        whitePiecesArray.add(new Pawn("Pawn", whitePawnImage, 6, 0, "White"));
//        whitePiecesArray.add(new Pawn("Pawn", whitePawnImage, 6, 1, "White"));
//        whitePiecesArray.add(new Pawn("Pawn", whitePawnImage, 6, 2, "White"));
//        whitePiecesArray.add(new Pawn("Pawn", whitePawnImage, 6, 3, "White"));
//        whitePiecesArray.add(new Pawn("Pawn", whitePawnImage, 4, 4, "White"));
//        whitePiecesArray.add(new Pawn("Pawn", whitePawnImage, 6, 5, "White"));
//        whitePiecesArray.add(new Pawn("Pawn", whitePawnImage, 6, 6, "White"));
//        whitePiecesArray.add(new Pawn("Pawn", whitePawnImage, 6, 7, "White"));
//
//
//
//        blackPiecesArray.clear();
//        blackPiecesArray.add(new Rook("Rook", blackRookImage, 0, 0, "Black"));
//        blackPiecesArray.add(new Knight("Knight", blackKnightImage, 2, 2, "Black"));
//        blackPiecesArray.add(new Bishop("Bishop", blackBishopImage, 0, 2, "Black"));
//        blackPiecesArray.add(new Queen("Queen", blackQueenImage, 0, 3, "Black"));
//        blackPiecesArray.add(new King("King", blackKingImage, 0, 4, "Black"));
//        blackPiecesArray.add(new Bishop("Bishop", blackBishopImage, 0, 5, "Black"));
//        blackPiecesArray.add(new Knight("Knight", blackKnightImage, 0, 6, "Black"));
//        blackPiecesArray.add(new Rook("Rook", blackRookImage, 0, 7, "Black"));
//        blackPiecesArray.add(new Pawn("Pawn", blackPawnImage, 1, 0, "Black"));
//        blackPiecesArray.add(new Pawn("Pawn", blackPawnImage, 1, 1, "Black"));
//        blackPiecesArray.add(new Pawn("Pawn", blackPawnImage, 1, 2, "Black"));
//        blackPiecesArray.add(new Pawn("Pawn", blackPawnImage, 1, 3, "Black"));
//        blackPiecesArray.add(new Pawn("Pawn", blackPawnImage, 3, 4, "Black"));
//        blackPiecesArray.add(new Pawn("Pawn", blackPawnImage, 1, 5, "Black"));
//        blackPiecesArray.add(new Pawn("Pawn", blackPawnImage, 1, 6, "Black"));
//        blackPiecesArray.add(new Pawn("Pawn", blackPawnImage, 1, 7, "Black"));
//
//        for (Piece value : whitePiecesArray) {
//            value.setDirection(-1);
//        }
//        for (Piece piece : blackPiecesArray) {
//            piece.setDirection(1);
//        }
//
//
//        setBoard();
//    }
//
//    @FXML
//    private void puzzle7(){
//        turn = 0;
//        whitePiecesArray.clear();
//
//        whitePiecesArray.add(new Rook("Rook", whiteRookImage, 7, 0,"White"));
//        whitePiecesArray.add(new King("King", whiteKingImage, 7, 6, "White"));
//        whitePiecesArray.add(new Rook("Rook", whiteRookImage, 7, 4, "White"));
//        whitePiecesArray.add(new Pawn("Pawn", whitePawnImage, 6, 0, "White"));
//        whitePiecesArray.add(new Pawn("Pawn", whitePawnImage, 6, 1, "White"));
//        whitePiecesArray.add(new Pawn("Pawn", whitePawnImage, 6, 2, "White"));
//        whitePiecesArray.add(new Pawn("Pawn", whitePawnImage, 6, 5, "White"));
//        whitePiecesArray.add(new Pawn("Pawn", whitePawnImage, 6, 6, "White"));
//        whitePiecesArray.add(new Pawn("Pawn", whitePawnImage, 6, 7, "White"));
//
//
//
//        blackPiecesArray.clear();
//        blackPiecesArray.add(new King("King", blackKingImage, 0, 1, "Black"));
//        blackPiecesArray.add(new Rook("Rook", blackRookImage, 4, 7, "Black"));
//        blackPiecesArray.add(new Pawn("Pawn", blackPawnImage, 1, 0, "Black"));
//        blackPiecesArray.add(new Pawn("Pawn", blackPawnImage, 1, 1, "Black"));
//        blackPiecesArray.add(new Pawn("Pawn", blackPawnImage, 1, 2, "Black"));
//        blackPiecesArray.add(new Pawn("Pawn", blackPawnImage, 1, 5, "Black"));
//        blackPiecesArray.add(new Pawn("Pawn", blackPawnImage, 1, 6, "Black"));
//
//        for (Piece value : whitePiecesArray) {
//            value.setDirection(-1);
//        }
//        for (Piece piece : blackPiecesArray) {
//            piece.setDirection(1);
//        }
//
//
//
//
//        setBoard();
//
//    }
//
//    @FXML
//    private void puzzle8(){
//        turn = 1;
//        whitePiecesArray.clear();
//
//        whitePiecesArray.add(new King("King", whiteKingImage, 7, 6, "White"));
//        whitePiecesArray.add(new Rook("Rook", whiteRookImage, 5, 4, "White"));
//        whitePiecesArray.add(new Pawn("Pawn", whitePawnImage, 6, 0, "White"));
//        whitePiecesArray.add(new Pawn("Pawn", whitePawnImage, 6, 1, "White"));
//        whitePiecesArray.add(new Pawn("Pawn", whitePawnImage, 6, 2, "White"));
//        whitePiecesArray.add(new Pawn("Pawn", whitePawnImage, 6, 5, "White"));
//        whitePiecesArray.add(new Pawn("Pawn", whitePawnImage, 6, 6, "White"));
//        whitePiecesArray.add(new Pawn("Pawn", whitePawnImage, 6, 7, "White"));
//
//
//
//        blackPiecesArray.clear();
//        blackPiecesArray.add(new Rook("Rook", blackRookImage, 0, 3, "Black"));
//        blackPiecesArray.add(new King("King", blackKingImage, 0, 1, "Black"));
//        blackPiecesArray.add(new Rook("Rook", blackRookImage, 4, 7, "Black"));
//        blackPiecesArray.add(new Pawn("Pawn", blackPawnImage, 1, 0, "Black"));
//        blackPiecesArray.add(new Pawn("Pawn", blackPawnImage, 1, 1, "Black"));
//        blackPiecesArray.add(new Pawn("Pawn", blackPawnImage, 1, 2, "Black"));
//        blackPiecesArray.add(new Pawn("Pawn", blackPawnImage, 1, 5, "Black"));
//        blackPiecesArray.add(new Pawn("Pawn", blackPawnImage, 1, 6, "Black"));
//
//        for (Piece value : whitePiecesArray) {
//            value.setDirection(-1);
//        }
//        for (Piece piece : blackPiecesArray) {
//            piece.setDirection(1);
//        }
//
//
//
//
//        setBoard();
//        flipBoard();
//    }
//
//    @FXML
//    private void puzzle9(){
//        turn = 0;
//        whitePiecesArray.clear();
//
//        whitePiecesArray.add(new Rook("Rook", whiteRookImage, 7, 0,"White"));
//        whitePiecesArray.add(new Knight("Knight", whiteKnightImage, 7, 1, "White"));
//        whitePiecesArray.add(new Bishop("Bishop", whiteBishopImage, 7, 2, "White"));
//        whitePiecesArray.add(new Queen("Queen", whiteQueenImage, 7, 3, "White"));
//        whitePiecesArray.add(new King("King", whiteKingImage, 7, 4, "White"));
//        whitePiecesArray.add(new Bishop("Bishop", whiteBishopImage, 4, 2, "White"));
//        whitePiecesArray.add(new Knight("Knight", whiteKnightImage, 7, 6, "White"));
//        whitePiecesArray.add(new Rook("Rook", whiteRookImage, 7, 7, "White"));
//        whitePiecesArray.add(new Pawn("Pawn", whitePawnImage, 6, 0, "White"));
//        whitePiecesArray.add(new Pawn("Pawn", whitePawnImage, 6, 1, "White"));
//        whitePiecesArray.add(new Pawn("Pawn", whitePawnImage, 6, 2, "White"));
//        whitePiecesArray.add(new Pawn("Pawn", whitePawnImage, 6, 3, "White"));
//        whitePiecesArray.add(new Pawn("Pawn", whitePawnImage, 4, 4, "White"));
//        whitePiecesArray.add(new Pawn("Pawn", whitePawnImage, 6, 5, "White"));
//        whitePiecesArray.add(new Pawn("Pawn", whitePawnImage, 6, 6, "White"));
//        whitePiecesArray.add(new Pawn("Pawn", whitePawnImage, 6, 7, "White"));
//
//
//
//        blackPiecesArray.clear();
//        blackPiecesArray.add(new Rook("Rook", blackRookImage, 0, 0, "Black"));
//        blackPiecesArray.add(new Knight("Knight", blackKnightImage, 2, 2, "Black"));
//        blackPiecesArray.add(new Bishop("Bishop", blackBishopImage, 0, 2, "Black"));
//        blackPiecesArray.add(new Queen("Queen", blackQueenImage, 0, 3, "Black"));
//        blackPiecesArray.add(new King("King", blackKingImage, 0, 4, "Black"));
//        blackPiecesArray.add(new Bishop("Bishop", blackBishopImage, 0, 5, "Black"));
//        blackPiecesArray.add(new Knight("Knight", blackKnightImage, 0, 6, "Black"));
//        blackPiecesArray.add(new Rook("Rook", blackRookImage, 0, 7, "Black"));
//        blackPiecesArray.add(new Pawn("Pawn", blackPawnImage, 1, 0, "Black"));
//        blackPiecesArray.add(new Pawn("Pawn", blackPawnImage, 1, 1, "Black"));
//        blackPiecesArray.add(new Pawn("Pawn", blackPawnImage, 1, 2, "Black"));
//        blackPiecesArray.add(new Pawn("Pawn", blackPawnImage, 1, 3, "Black"));
//        blackPiecesArray.add(new Pawn("Pawn", blackPawnImage, 3, 4, "Black"));
//        blackPiecesArray.add(new Pawn("Pawn", blackPawnImage, 1, 5, "Black"));
//        blackPiecesArray.add(new Pawn("Pawn", blackPawnImage, 1, 6, "Black"));
//        blackPiecesArray.add(new Pawn("Pawn", blackPawnImage, 1, 7, "Black"));
//
//        for (Piece value : whitePiecesArray) {
//            value.setDirection(-1);
//        }
//        for (Piece piece : blackPiecesArray) {
//            piece.setDirection(1);
//        }
//
//
//        setBoard();
//    }
//
//    @FXML
//    private void puzzle10(){
//        turn = 0;
//        whitePiecesArray.clear();
//
//        whitePiecesArray.add(new Rook("Rook", whiteRookImage, 0, 7,"White"));
//        whitePiecesArray.add(new Queen("Queen", whiteQueenImage, 3, 1, "White"));
//        whitePiecesArray.add(new King("King", whiteKingImage, 7, 5, "White"));
//        whitePiecesArray.add(new Knight("Knight", whiteKnightImage, 5, 5, "White"));
//        whitePiecesArray.add(new Pawn("Pawn", whitePawnImage, 4, 7, "White"));
//
//
//
//        blackPiecesArray.clear();
//        blackPiecesArray.add(new King("King", blackKingImage, 1,  0, "Black"));
//        blackPiecesArray.add(new Pawn("Pawn", blackPawnImage, 3, 3, "Black"));
//        blackPiecesArray.add(new Pawn("Pawn", blackPawnImage, 3, 7, "Black"));
//        for (Piece value : whitePiecesArray) {
//            value.setDirection(-1);
//        }
//        for (Piece piece : blackPiecesArray) {
//            piece.setDirection(1);
//        }
//
//
//
//
//        setBoard();
//    }
//
//    @FXML
//    private void puzzle11(){
//        turn = 0;
//        whitePiecesArray.clear();
//        whitePiecesArray.add(new King("King", whiteKingImage, 5, 6, "White"));
//
//
//
//
//        blackPiecesArray.clear();
//        blackPiecesArray.add(new King("King", blackKingImage, 1,  1, "Black"));
//        blackPiecesArray.add(new Pawn("Pawn", blackPawnImage, 3, 5, "Black"));
//        for (Piece value : whitePiecesArray) {
//            value.setDirection(-1);
//        }
//        for (Piece piece : blackPiecesArray) {
//            piece.setDirection(1);
//        }
//
//
//
//
//        setBoard();
//    }
//
//    @FXML
//    private void puzzle12(){
//        turn = 0;
//        whitePiecesArray.clear();
//
//        whitePiecesArray.add(new Rook("Rook", whiteRookImage, 4, 1,"White"));
//        whitePiecesArray.add(new King("King", whiteKingImage, 6, 3, "White"));
//        whitePiecesArray.add(new Knight("Knight", whiteKnightImage, 5, 4, "White"));
//
//
//
//        blackPiecesArray.clear();
//        blackPiecesArray.add(new King("King", blackKingImage, 1,  0, "Black"));
//
//        for (Piece value : whitePiecesArray) {
//            value.setDirection(-1);
//        }
//        for (Piece piece : blackPiecesArray) {
//            piece.setDirection(1);
//        }
//
//
//
//
//        setBoard();
//    }







}
