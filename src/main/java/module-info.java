module com.example.bp2realisatie {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.bp2realisatie to javafx.fxml;
    exports com.example.bp2realisatie;
    exports com.example.bp2realisatie.classes;
}