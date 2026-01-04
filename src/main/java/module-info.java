module com.sampahin {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.web;
    requires java.sql;
    requires mysql.connector.j;
    requires jbcrypt;
    requires java.prefs;
    requires jdk.jsobject;
    opens controller to javafx.fxml, javafx.web, javafx.base;

    opens models to javafx.base;

    exports com.sampahin;
}