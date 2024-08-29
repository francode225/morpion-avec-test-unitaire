module yeofrancois.degnyalfred.morpion.projet_morpion {
    requires javafx.controls;
    requires javafx.fxml;


    opens yeofrancois.degnyalfred.morpion.projet_morpion to javafx.fxml;
    exports yeofrancois.degnyalfred.morpion.projet_morpion;
}