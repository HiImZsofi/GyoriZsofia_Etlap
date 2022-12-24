package com.example.gyorizsofia_etlap;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AppController {

    @FXML
    private Button newMealButton;
    @FXML
    private Button deleteButton;
    @FXML
    private Spinner<Integer> percentageSpinner;
    @FXML
    private Button percentageRaiseSpinner;
    @FXML
    private Spinner<Integer> priceSpinner;
    @FXML
    private Button priceRaiseSpinner;
    @FXML
    private TableView<Meal> Menu;
    @FXML
    private TableColumn<Meal, String> nameCol;
    @FXML
    private TableColumn<Meal, String> catCol;
    @FXML
    private TableColumn<Meal, Integer> priceCol;
    @FXML
    private ListView<String> description;

    private MealDB db;

    private List<Meal> list = new ArrayList<>();

    private int updateId;
    @FXML
    public void initialize(){
        nameCol.setCellValueFactory(new PropertyValueFactory<>("nev"));
        catCol.setCellValueFactory(new PropertyValueFactory<>("kategoria"));
        priceCol.setCellValueFactory(new PropertyValueFactory<>("ar"));
        percentageSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(5,50,10, 5));
        priceSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(50,3000,1250, 50));
        try {
            db = new MealDB();
            readEtelek();
        } catch (SQLException e) {
            Platform.runLater(() -> {
                alert(Alert.AlertType.WARNING, "Hiba történt az adatbázis kapcsolat kialakításakor!",
                        e.getMessage());
            });
        }
    }

    private void readEtelek() throws SQLException {
        List<Meal> etelek = db.readMeals();
        Menu.getItems().clear();
        Menu.getItems().addAll(etelek);
        list.addAll(etelek);
    }

    private void sqlAlert(SQLException e) {
        Platform.runLater(() -> {
            alert(Alert.AlertType.WARNING, "Hiba történt az adatbázis kapcsolat kialakításakor!",
                    e.getMessage());
        });
    }

    private Optional<ButtonType> alert(Alert.AlertType alertType, String headerText, String contentText){
        Alert alert = new Alert(alertType);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        return alert.showAndWait();
    }

    @FXML
    public void newFoodClick(ActionEvent actionEvent) {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("etel-form-view.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load(), 400, 320);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Stage stage = new Stage();
        stage.setTitle("Étel létrehozása");
        stage.setScene(scene);
        MealViewForm controller = fxmlLoader.getController();
        stage.show();
        try {
            readEtelek();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void deleteClick(ActionEvent actionEvent) {
        Meal selected = getSelectedMeal();
        if (selected == null) return;

        Optional<ButtonType> optionalButtonType = alert(Alert.AlertType.CONFIRMATION,"Biztos, hogy törölni szeretné a kiválasztott ételt?","");
        if (optionalButtonType.isEmpty() || !optionalButtonType.get().equals(ButtonType.OK) && !optionalButtonType.get().equals(ButtonType.YES)){
            return;
        }
        try {
            if (db.deleteMeal(selected.getId())) {
                alert(Alert.AlertType.WARNING, "Sikeres Törlés!", "");
            }else{
                alert(Alert.AlertType.WARNING, "Sikertelen törlés!", "");
            }
            readEtelek();
        } catch (SQLException e) {
            sqlAlert(e);
        }
        description.getItems().clear();
    }
    private Meal getSelectedMeal() {
        int selectedIndex = Menu.getSelectionModel().getSelectedIndex();
        if (selectedIndex == -1){
            alert(Alert.AlertType.WARNING, "Előbb válasszon ki egy ételt a táblázatból!","");
            return null;
        }
        Meal selected = Menu.getSelectionModel().getSelectedItem();
        return selected;
    }

    @FXML
    public void szazalekEmelesClick(ActionEvent actionEvent) {
        int selectedIndex = Menu.getSelectionModel().getSelectedIndex();

        if (selectedIndex != -1){
            Optional<ButtonType> optionalButtonType = alert(Alert.AlertType.CONFIRMATION,"Biztos, hogy emelni szeretné a kiválasztott ételt?","");
            if (optionalButtonType.isEmpty() || !optionalButtonType.get().equals(ButtonType.OK) && !optionalButtonType.get().equals(ButtonType.YES)){
                return;
            }
            Meal selected = Menu.getSelectionModel().getSelectedItem();
            updateId = selected.getId();
            double szazalek = percentageSpinner.getValue();
            szazalek = 1 + (szazalek/100);
            Meal etel = new Meal(updateId, selected.getName(), selected.getDescription(), selected.getPrice(), selected.getCategory());
            try{
                if (db.updateMealPricePercentage(etel, szazalek)){
                    alert(Alert.AlertType.WARNING, "Sikeres modosítás!", "");
                    readEtelek();
                }else{
                    alert(Alert.AlertType.WARNING, "Sikertelen modosítás!", "");
                }
            }catch (SQLException e){
                sqlAlert(e);
            }

        }else{
            Optional<ButtonType> optionalButtonType = alert(Alert.AlertType.CONFIRMATION,"Biztos, hogy emelni szeretné az ételeket?","");
            if (optionalButtonType.isEmpty() || !optionalButtonType.get().equals(ButtonType.OK) && !optionalButtonType.get().equals(ButtonType.YES)){
                return;
            }
            double szazalek = percentageSpinner.getValue();
            szazalek = 1 + (szazalek/100);
            boolean siker = false;
            int i = 0;
            while(list.size() > i){
                Meal etel = new Meal (list.get(i).getId(), list.get(i).getName(),  list.get(i).getDescription(), list.get(i).getPrice(), list.get(i).getCategory());
                try {
                    if(db.updateMealPricePercentage(etel, szazalek)) {
                        siker = true;
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                i++;
            }
            if (siker){
                alert(Alert.AlertType.WARNING, "Sikeres modosítás!", "");
                try {
                    readEtelek();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }else{
                alert(Alert.AlertType.WARNING, "Sikertelen modosítás!", "");
            }
        }
    }

    @FXML
    public void ftEmelesClick(ActionEvent actionEvent) {
        int selectedIndex = Menu.getSelectionModel().getSelectedIndex();

        if (selectedIndex != -1){
            Optional<ButtonType> optionalButtonType = alert(Alert.AlertType.CONFIRMATION,"Biztos, hogy emelni szeretné a kiválasztott ételt?","");
            if (optionalButtonType.isEmpty() || !optionalButtonType.get().equals(ButtonType.OK) && !optionalButtonType.get().equals(ButtonType.YES)){
                return;
            }
            Meal selected = Menu.getSelectionModel().getSelectedItem();
            updateId = selected.getId();
            int ar = priceSpinner.getValue();
            Meal etel = new Meal(updateId, selected.getName(), selected.getDescription(), selected.getPrice(), selected.getCategory());
            try{
                if (db.updateMealPrice(etel, ar)){
                    alert(Alert.AlertType.WARNING, "Sikeres modosítás!", "");
                    readEtelek();
                }else{
                    alert(Alert.AlertType.WARNING, "Sikertelen modosítás!", "");
                }
            }catch (SQLException e){
                sqlAlert(e);
            }

        }else{
            Optional<ButtonType> optionalButtonType = alert(Alert.AlertType.CONFIRMATION,"Biztos, hogy emelni szeretné az ételeket?","");
            if (optionalButtonType.isEmpty() || !optionalButtonType.get().equals(ButtonType.OK) && !optionalButtonType.get().equals(ButtonType.YES)){
                return;
            }
            int price = priceSpinner.getValue();
            boolean success = false;
            int i = 0;
            while(list.size() > i){
                Meal etel = new Meal (list.get(i).getId(), list.get(i).getName(),  list.get(i).getDescription(), list.get(i).getPrice(), list.get(i).getCategory());
                try {
                    if(db.updateMealPrice(etel, price)) {
                        success = true;
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                i++;
            }
            if (success){
                alert(Alert.AlertType.WARNING, "Sikeres modosítás!", "");
                try {
                    readEtelek();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }else{
                alert(Alert.AlertType.WARNING, "Sikertelen modosítás!", "");
            }
        }
    }

    @FXML
    public void tableViewClick(Event event) {
        description.getItems().clear();
        description.getItems().add(getSelectedMeal().getDescription());
    }

    @FXML
    public void sortList(Event event) {
        Menu.getOnSort();
    }

    public void addClick(ActionEvent actionEvent) {
    }

    public void menuItem1Select(ActionEvent actionEvent) {
    }

    public void menuItem2Select(ActionEvent actionEvent) {
    }

    public void menuItem3Select(ActionEvent actionEvent) {
    }
}