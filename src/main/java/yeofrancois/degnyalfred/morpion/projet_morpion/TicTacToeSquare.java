package yeofrancois.degnyalfred.morpion.projet_morpion;

import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Label;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class TicTacToeSquare extends Label {
    private static TicTacToeModel model = TicTacToeModel.getInstance();

    private ObjectProperty<Owner> ownerProperty = new SimpleObjectProperty<>(Owner.NONE);
    private BooleanProperty winnerProperty = new SimpleBooleanProperty(false);

    public ObjectProperty<Owner> ownerProperty() {
        return ownerProperty;
    }

    public BooleanProperty colorProperty() {
        return winnerProperty;
    }


    public TicTacToeSquare(final int row, final int column) {

        this.setOnMouseClicked(event -> {
            System.out.println(model.turnProperty().get());
            model.play(row, column);
        });

        this.setOnMouseEntered(event -> {

            if (Boolean.TRUE.equals(model.legalMove(row, column).getValue())){
                this.setStyle("-fx-background-color: #02cc02");
            }
            else this.setStyle("-fx-background-color: red");

        });

        this.setOnMouseExited(event -> {
            this.setStyle("-fx-background-color: transparent");
        });

        this.textProperty().bind(Bindings.when(ownerProperty.isEqualTo(Owner.FIRST))
                .then("X")
                .otherwise(Bindings.when(ownerProperty.isEqualTo(Owner.SECOND))
                        .then("O")
                        .otherwise("")
                ));

        this.fontProperty().bind(Bindings.when(model.getWinningSquare(row, column))
                .then(Font.font("Arial", FontWeight.BOLD, 32))
                .otherwise(Font.font("Arial", FontWeight.BOLD, 15)));

        this.setMaxWidth(200);
        this.setMaxHeight(200);

    }

}



