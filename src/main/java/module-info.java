module com.example.gyorizsofia_etlap {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.gyorizsofia_etlap to javafx.fxml;
    exports com.example.gyorizsofia_etlap;
}