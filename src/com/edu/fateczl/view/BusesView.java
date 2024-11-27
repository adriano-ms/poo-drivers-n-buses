package com.edu.fateczl.view;

import com.edu.fateczl.controller.BusController;
import com.edu.fateczl.controller.InvalidInputException;
import com.edu.fateczl.model.dao.DbException;
import com.edu.fateczl.model.entities.Bus;

import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class BusesView implements View{

    private VBox root;
    private GridPane form;
    private TextField licensePlateField;
    private TextField modelField;
    private TextField lineField;
    private Button addButton;
    private Button deleteButton;
    private HBox searchBox;
    private TextField searchField;
    private Button searchButton;
    private TableView<Bus> tableView;
    private TableColumn<Bus, String> plateColumn;
    private TableColumn<Bus, String> modelColumn;
    private TableColumn<Bus, String> lineColumn;

    private BusController controller;

    public BusesView(){
        controller = new BusController();
        root = new VBox();

        form = new GridPane(5,10);
        licensePlateField = new TextField();
        modelField = new TextField();
        lineField = new TextField();
        addButton = new Button("Salvar");
        addButton.setOnAction(e -> saveAction());
        deleteButton = new Button("Deletar");
        form.add(new Label("Placa"), 0, 0);
        form.add(licensePlateField, 1, 0);
        form.add(new Label("Modelo"), 0, 1);
        form.add(modelField, 1, 1);
        form.add(new Label("Linha"), 0, 2);
        form.add(lineField, 1, 2);
        form.add(addButton, 0, 3);
        form.add(deleteButton, 1, 3);

        searchBox = new HBox();
        searchField = new TextField();
        searchField.setPromptText("Pesquisar...");
        searchButton = new Button("Pesquisar");
        searchBox.getChildren().addAll(searchField, searchButton);
        HBox.setHgrow(searchField, Priority.ALWAYS);

        tableView = new TableView<>();
        plateColumn = new TableColumn<>("Placa");
        modelColumn = new TableColumn<>("Modelo");
        lineColumn = new TableColumn<>("Linha");
        plateColumn.setCellValueFactory(item -> new ReadOnlyStringWrapper(item.getValue().getLicensePlate()));
        modelColumn.setCellValueFactory(item -> new ReadOnlyStringWrapper(item.getValue().getModel()));
        lineColumn.setCellValueFactory(item -> new ReadOnlyStringWrapper(item.getValue().getLine()));
        tableView.getColumns().add(plateColumn);
        tableView.getColumns().add(modelColumn);
        tableView.getColumns().add(lineColumn);

        root.getChildren().addAll(form, searchBox, tableView);
    }

    @Override
    public Pane render() {
        bindProperties();
        tableView.setItems(controller.getBusesObservableList());
        controller.findAllBuses();
        return root;  
    }

    private void saveAction(){
        try {
            controller.insertBus();
        } catch (DbException | InvalidInputException e) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Erro!");
            alert.setContentText(e.getMessage());
            alert.show();
        }
    }

    private void bindProperties() {
        Bindings.bindBidirectional(licensePlateField.textProperty(), controller.getLicensePlateProperty());
        Bindings.bindBidirectional(modelField.textProperty(), controller.getModelProperty());
        Bindings.bindBidirectional(lineField.textProperty(), controller.getLineProperty());
    }
    
}
