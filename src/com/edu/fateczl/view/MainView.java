package com.edu.fateczl.view;

import com.edu.fateczl.model.dao.DbException;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.Alert.AlertType;
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
        primaryStage.setWidth(primaryStage.getMinWidth());
        primaryStage.setHeight(primaryStage.getMinHeight());

        root = new VBox();
        driversMenu = new MenuItem("Motoristas");
        busesMenu = new MenuItem("Ônibus");
        mainMenu = new Menu("Cadastros");
        mainMenu.getItems().addAll(driversMenu, busesMenu);
        menuBar = new MenuBar();
        menuBar.getMenus().add(mainMenu);
        driversMenu.setOnAction(e -> selectMenu("Motoristas", new DriversView()));
        busesMenu.setOnAction(e -> selectMenu("Ônibus", new BusesView()));
        tabPane = new TabPane();

        root.getChildren().addAll(menuBar, tabPane);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    private void selectMenu(String tabName, View view){
        try {
            tabPane.getTabs().add(new Tab(tabName, view.render()));
        } catch (DbException e) {
            Alert.showAlert(AlertType.ERROR, "Erro!", null, e.getMessage(), () -> System.exit(10));
        }
    }

}