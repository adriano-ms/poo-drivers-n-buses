package com.edu.fateczl.view;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainView extends Application {

    private VBox root;
    private MenuBar menuBar;
    private Menu mainMenu;
    private MenuItem driversMenu;
    private MenuItem busesMenu;
    private TabPane tabPane;

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Gerenciamento de Motoristas");
        primaryStage.setMinHeight(480);
        primaryStage.setMinWidth(720);

        root = new VBox();
        driversMenu = new MenuItem("Motoristas");
        busesMenu = new MenuItem("Ônibus");
        mainMenu = new Menu("Cadastros");
        mainMenu.getItems().addAll(driversMenu, busesMenu);
        menuBar = new MenuBar();
        menuBar.getMenus().add(mainMenu);
        driversMenu.setOnAction(e -> tabPane.getTabs().add(new Tab("Motoristas",  new DriversView().render())));
        busesMenu.setOnAction(e -> tabPane.getTabs().add(new Tab("Ônibus", new BusesView().render())));
        tabPane = new TabPane();

        root.getChildren().addAll(menuBar, tabPane);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

}