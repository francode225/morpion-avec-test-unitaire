package yeofrancois.degnyalfred.morpion.projet_morpion;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class HelloController implements Initializable {

    TicTacToeModel ticTacToeModel = TicTacToeModel.getInstance();


    @FXML
    private Label caseLibre;

    @FXML
    private Text caseO;

    @FXML
    private Text caseX;

    @FXML
    private Label gagnantText;

    @FXML
    private Label gameOverText;

    @FXML
    private GridPane gridTicTacToe;


    @FXML
    private Button restartBtn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        for (int i = 0; i < TicTacToeModel.BOARD_HEIGHT; i++) {
            for (int j = 0; j < TicTacToeModel.BOARD_WIDTH; j++) {

                TicTacToeSquare ticTacToeSquare = new TicTacToeSquare(i, j);
                ticTacToeSquare.ownerProperty().bind(ticTacToeModel.getSquare(i, j));

                gridTicTacToe.add(ticTacToeSquare, j, i);

            }
        }


        restartBtn.setOnAction(actionEvent -> ticTacToeModel.restart());

        caseO.textProperty().bind(Bindings.concat(ticTacToeModel.getScore(Owner.SECOND)));

        caseX.textProperty().bind(Bindings.concat(ticTacToeModel.getScore(Owner.FIRST)));

        caseLibre.textProperty().bind(Bindings.concat(ticTacToeModel.getCaseLibre()).concat(" case(s) libre(s)"));


        gagnantText.textProperty().bind(
                Bindings.createObjectBinding(
                        () -> {
                            Owner winner = ticTacToeModel.getWinner();
                            if (winner != Owner.NONE) {
                                return "Game over, le gagnant est le joueur " + (winner == Owner.FIRST ? "X" : "O");
                            } else {
                                return "";
                            }
                        },
                        ticTacToeModel.turnProperty()
                )
        );



    }
}