package com.edu.fateczl.view;

import javafx.scene.control.Alert.AlertType;

public class Alert {

    public static void showAlert(AlertType type, String title, String header, String content){
        javafx.scene.control.Alert a = new javafx.scene.control.Alert(type);
        a.setTitle(title);
        a.setHeaderText(header);
        a.setContentText(content);
        a.show();
    }
}
