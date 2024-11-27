module com.example.musikafspiller {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.musikafspiller to javafx.fxml;
    exports com.example.musikafspiller;
}