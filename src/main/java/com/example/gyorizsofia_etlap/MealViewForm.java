package com.example.gyorizsofia_etlap;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.Optional;

public class MealViewForm {
    @FXML
    private TextField nameField;
    @FXML
    private Button addButton;
    @FXML
    private Spinner<Integer> priceSpinner;
    @FXML
    private TextArea descriptionField;
    @FXML
    private MenuButton menuButton;
    @FXML
    private MenuItem preMealMenuItem;
    @FXML
    private MenuItem mainMealMenuItem;
    @FXML
    private MenuItem dessertMenuItem;

    public void initialize(){
        priceSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(100,2500,1250, 10));
    }

    @FXML
    public void addClick(ActionEvent actionEvent) {
        String nev = nameField.getText().trim();
        String leiras = descriptionField.getText().trim();
        int ar = priceSpinner.getValue();
        String kategoria = menuButton.getText().trim();
        Meal etel = new Meal(nev, leiras, ar, kategoria);
        MealDB db = null;
        try {
            db = new MealDB();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            if (db.createMeal(etel)){
                alert(Alert.AlertType.WARNING, "Sikeres felvétel!", "");
            }else{
                alert(Alert.AlertType.WARNING, "Sikertelen felvétel!", "");
            }
        } catch (SQLException e) {
            Platform.runLater(() -> {
                alert(Alert.AlertType.WARNING, "Unexpected error!",
                        e.getMessage());
            });
        }
        Node source = (Node)  actionEvent.getSource();
        Stage stage  = (Stage) source.getScene().getWindow();
        stage.close();
    }

    private Optional<ButtonType> alert(Alert.AlertType alertType, String headerText, String contentText){
        Alert alert = new Alert(alertType);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        return alert.showAndWait();
    }

    @FXML
    public void menuItem1Select(ActionEvent actionEvent) {
        menuButton.setText(preMealMenuItem.getText());
    }

    @FXML
    public void menuItem2Select(ActionEvent actionEvent) {
        menuButton.setText(mainMealMenuItem.getText());
    }

    @FXML
    public void menuItem3Select(ActionEvent actionEvent) {
        menuButton.setText(dessertMenuItem.getText());
    }
}