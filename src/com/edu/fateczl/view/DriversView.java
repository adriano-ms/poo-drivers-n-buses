package com.edu.fateczl.view;
import com.edu.fateczl.model.entities.Driver;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class DriversView implements View {

    private VBox root;
    private HBox searchBox;
    private TextField searchField;
    private Button searchButton;
    private TableView<Driver> tableView;
    private TableColumn<Driver, String> licenseColoumn;
    private TableColumn<Driver, String> nameColumn;
    private TableColumn<Driver, String> busLineColumn;

    public DriversView(){
        root = new VBox();
        searchBox = new HBox();
        searchField = new TextField();
        searchField.setPromptText("Pesquisar...");
        searchButton = new Button("Pesquisar");
        searchBox.getChildren().addAll(searchField, searchButton);
        HBox.setHgrow(searchField, Priority.ALWAYS);

        tableView = new TableView<>();
        licenseColoumn = new TableColumn<>("CNH");
        nameColumn = new TableColumn<>("Nome");
        busLineColumn = new TableColumn<>("Linha");
        licenseColoumn.setCellValueFactory(item -> new ReadOnlyStringWrapper(item.getValue().getDriverLicense()));
        nameColumn.setCellValueFactory(item -> new ReadOnlyStringWrapper(item.getValue().getName()));
        busLineColumn.setCellValueFactory(item -> new ReadOnlyStringWrapper(item.getValue().getBus().getLine()));
        tableView.getColumns().add(licenseColoumn);
        tableView.getColumns().add(nameColumn);
        tableView.getColumns().add(busLineColumn);
        root.getChildren().addAll(searchBox, tableView);
    }

    @Override
    public Pane render() {
        return root;
    }
}
