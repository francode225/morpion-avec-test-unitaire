package yeofrancois.degnyalfred.morpion.projet_morpion;

import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.NumberExpression;
import javafx.beans.property.*;
import javafx.beans.binding.StringExpression;

public class TicTacToeModel{

    //private int nombre_tour = 0;

    final static int BOARD_WIDTH = 3;
    final static int BOARD_HEIGHT = 3;
    private final static int WINNING_COUNT = 3;


    private final ObjectProperty<Owner> turn = new SimpleObjectProperty<>(Owner.FIRST);
    private final ObjectProperty<Owner> winner = new SimpleObjectProperty<>(Owner.NONE);
    private final ObjectProperty<Owner>[][] board;
    private final BooleanProperty[][] winningBoard;
    private final IntegerProperty caseOccupeeJoueur1;
    private final IntegerProperty caseOccupeeJoueur2;
    private final IntegerProperty caseLibre;


    public TicTacToeModel() {
        this.caseOccupeeJoueur1 = new SimpleIntegerProperty(0);
        this.caseOccupeeJoueur2 = new SimpleIntegerProperty(0);
        this.caseLibre = new SimpleIntegerProperty(BOARD_WIDTH * BOARD_HEIGHT);

        this.board = new ObjectProperty[BOARD_WIDTH][BOARD_HEIGHT];
        this.winningBoard = new BooleanProperty[BOARD_WIDTH][BOARD_HEIGHT];

        for(int i = 0; i < BOARD_WIDTH; ++i) {
            for(int j = 0; j < BOARD_HEIGHT; ++j) {
                board[i][j] = new SimpleObjectProperty<>(Owner.NONE);
                winningBoard[i][j] = new SimpleBooleanProperty(false);
            }
        }
    }

    public static TicTacToeModel getInstance() {
        return TicTacToeModelHolder.INSTANCE;
    }

    private static class TicTacToeModelHolder {
        private static final TicTacToeModel INSTANCE = new TicTacToeModel();
    }

    public void restart() {
        this.turn.setValue(Owner.FIRST);
        this.winner.setValue(Owner.NONE);

        for(int i = 0; i < BOARD_WIDTH; ++i) {
            for(int j = 0; j < BOARD_HEIGHT; ++j) {
                board[i][j].setValue(Owner.NONE);
                winningBoard[i][j].setValue(false);
            }
        }

        this.caseOccupeeJoueur1.setValue(0);
        this.caseOccupeeJoueur2.setValue(0);
        this.caseLibre.setValue(BOARD_WIDTH * BOARD_HEIGHT);
    }


    public final ObjectProperty<Owner> turnProperty() {
        return turn;
    }

    public final ObjectProperty<Owner> getSquare(int row, int column) {
        if (validSquare(row,column)) {
            return board[row][column];
        } else {
            return null;
        }
    }


    public final BooleanProperty getWinningSquare(int row, int column) {
        return winningBoard[row][column];
    }



    public void setWinner(Owner winner) {
        this.winner.setValue(winner);
    }

    public boolean validSquare(int row, int column) {
        return 0 <= row && row <= BOARD_WIDTH && 0 <= column && column <= BOARD_HEIGHT;
    }

    public Owner getWinner() {
        // Vérifier la victoire sur les lignes
        for (int i = 0; i < BOARD_WIDTH; ++i) {
            if (board[i][0].get() != Owner.NONE && board[i][0].get().equals(board[i][1].get()) && board[i][1].get().equals(board[i][2].get())) {

                winningBoard[i][0].setValue(true);
                winningBoard[i][1].setValue(true);
                winningBoard[i][2].setValue(true);

                return board[i][0].get();
            }
        }

        // Vérifier la victoire sur les colonnes
        for (int i = 0; i < BOARD_HEIGHT; ++i) {


            if (board[0][i].get() != Owner.NONE && board[0][i].get().equals(board[1][i].get()) && board[1][i].get().equals(board[2][i].get())) {

                winningBoard[0][i].setValue(true);
                winningBoard[1][i].setValue(true);
                winningBoard[2][i].setValue(true);
                return board[0][i].get();
            }
        }

        // Vérifier la victoire sur la diagonale gauche
        if (board[0][0].get() != Owner.NONE && board[0][0].get().equals(board[1][1].get()) && board[1][1].get().equals(board[2][2].get())) {

            winningBoard[0][0].setValue(true);
            winningBoard[1][1].setValue(true);
            winningBoard[2][2].setValue(true);
            return board[0][0].get();
        }

        // Vérifier la victoire sur la diagonale droite
        if (board[0][2].get() != Owner.NONE && board[0][2].get().equals(board[1][1].get()) && board[1][1].get().equals(board[2][0].get())) {

            winningBoard[0][2].setValue(true);
            winningBoard[1][1].setValue(true);
            winningBoard[2][0].setValue(true);
            return board[0][2].get();
        }



        return Owner.NONE;
    }


    public void nextPlayer() {
        this.turn.setValue(turn.get().opposite());
    }

    public void play(int row, int column) {


        if (legalMove(row, column).get() && getWinner().equals(Owner.NONE) && !gameOver().get()) {
            board[row][column].setValue(turn.get());

            if(turnProperty().get().equals(Owner.FIRST)){
                caseOccupeeJoueur1.setValue(caseOccupeeJoueur1.get()+1);
            }else if(turnProperty().get().equals(Owner.SECOND)){
                caseOccupeeJoueur2.setValue(caseOccupeeJoueur2.get()+1);
            }

            nextPlayer();
            caseLibre.setValue(caseLibre.get()-1);

        }

        if(getWinner().equals(Owner.FIRST)) {
            setWinner(Owner.FIRST);


        } else if (getWinner().equals(Owner.SECOND)) {
            setWinner(Owner.SECOND);
        }
    }

    public BooleanBinding legalMove(int row, int column) {

        return new BooleanBinding() {
            @Override
            protected boolean computeValue() {
                return validSquare(row, column) && board[row][column].get() == Owner.NONE;
            }
        };

        //return (validSquare(row, column) && board[row][column].get() == Owner.NONE && !gameOver();
    }

    /*public int numberOfRounds() {
        return nombre_tour;
*/

    public BooleanBinding gameOver(){


        if (getWinner() != Owner.NONE) {
            return new BooleanBinding() {
                @Override
                protected boolean computeValue(){
                    return true;
                }
            };
        } else {
            for (int i = 0; i < BOARD_WIDTH; i++) {
                for (int j = 0; j < BOARD_HEIGHT; j++) {
                    if (board[i][j].get() == Owner.NONE) {
                        return new BooleanBinding() {
                            @Override
                            protected boolean computeValue(){
                                return false;
                            }
                        };
                    }
                }
            }
        }

        return new BooleanBinding() {
            @Override
            protected boolean computeValue(){
                return true;
            }
        };

    }

    public NumberExpression getScore(Owner owner){
        if(owner.equals(Owner.FIRST)){
            return caseOccupeeJoueur1;
        }else {
            return caseOccupeeJoueur2;
        }
    }

    public NumberExpression getCaseLibre(){
        return caseLibre;
    }

}
