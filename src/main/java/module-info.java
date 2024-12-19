module com.example.musikafspiller {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires org.json;


    exports com.example.musikafspiller.GUI;
    opens com.example.musikafspiller.GUI to javafx.fxml;
}