module com.example.gyorizsofia_etlap {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.gyorizsofia_etlap to javafx.fxml;
    exports com.example.gyorizsofia_etlap;
}