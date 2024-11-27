package com.edu.fateczl.view;

import java.util.HashMap;
import java.util.Map;


import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainView extends Application {

    private VBox root;
    private MenuBar menuBar;
    private Menu mainMenu;
    private MenuItem driversMenu;
    private MenuItem busesMenu;
    private BorderPane borderPane;

    private Map<String, View> views;

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Gerenciamento de Motoristas");
        primaryStage.setMinHeight(480);
        primaryStage.setMinWidth(720);
        views = new HashMap<>();
        views.put("drivers", new DriversView());
        views.put("buses", new BusesView());

        root = new VBox();
        driversMenu = new MenuItem("Motoristas");
        busesMenu = new MenuItem("Ônibus");
        mainMenu = new Menu("Cadastros");
        mainMenu.getItems().addAll(driversMenu, busesMenu);
        menuBar = new MenuBar();
        menuBar.getMenus().add(mainMenu);
        driversMenu.setOnAction(e -> borderPane.setCenter(views.get("drivers").render()));
        busesMenu.setOnAction(e -> borderPane.setCenter(views.get("buses").render()));
        borderPane = new BorderPane();

        root.getChildren().addAll(menuBar, borderPane);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

}