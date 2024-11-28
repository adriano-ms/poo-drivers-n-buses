package com.edu.fateczl.view;

import com.edu.fateczl.controller.BusController;
import com.edu.fateczl.controller.InvalidInputException;
import com.edu.fateczl.model.dao.DbException;
import com.edu.fateczl.model.entities.Bus;

import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;
import javafx.util.converter.LongStringConverter;

public class BusesView implements View{

    private VBox root;
    private GridPane form;
    private Label idLabel;
    private TextField licensePlateField;
    private Button clearButton;
    private TextField brandField;
    private TextField seatsNumber;
    private CheckBox eletricCheckBox;
    private TextField lineField;
    private Button addButton;
    private HBox searchBox;
    private TextField searchField;
    private Button searchButton;
    private TableView<Bus> tableView;
    private TableColumn<Bus, String> plateColumn;
    private TableColumn<Bus, String> brandColumn;
    private TableColumn<Bus, String> seatsNumberColumn;
    private TableColumn<Bus, String> eletricColumn;
    private TableColumn<Bus, String> lineColumn;
    private TableColumn<Bus, Bus> actionsColumn;

    private BusController controller;

    public BusesView(){
        controller = new BusController();
        root = new VBox();
        form = new GridPane(5,10);
        idLabel = new Label();
        licensePlateField = new TextField();
        clearButton = new Button("Limpar");
        clearButton.setOnAction(e -> controller.clearFields());
        brandField = new TextField();
        seatsNumber = new TextField();        
        eletricCheckBox = new CheckBox("Elétrico");
        lineField = new TextField();
        addButton = new Button("Salvar");
        addButton.setOnAction(e -> saveAction());
        form.add(new Label("ID"), 0, 0);
        form.add(idLabel, 1, 0);
        form.add(clearButton, 2, 0);
        form.add(new Label("Placa"), 0, 1);
        form.add(licensePlateField, 1, 1);
        form.add(new Label("Marca"), 0, 2);
        form.add(brandField, 1, 2);
        form.add(new Label("Linha"), 0, 3);
        form.add(lineField, 1, 3);
        form.add(new Label("Assentos"), 0, 4);
        form.add(seatsNumber, 1, 4);
        form.add(eletricCheckBox, 0, 5);
        form.add(addButton, 0, 6);
        Constraints.setTextFieldMaxLength(licensePlateField, 7);
        Constraints.setTextFieldInteger(seatsNumber);
        Constraints.setTextFieldMaxLength(seatsNumber, 3);

        searchBox = new HBox();
        searchField = new TextField();
        searchField.setPromptText("Pesquisar...");
        searchButton = new Button("Pesquisar");
        searchButton.setOnAction(e -> controller.searchBuses());
        searchBox.getChildren().addAll(searchField, searchButton);
        HBox.setHgrow(searchField, Priority.ALWAYS);

        tableView = new TableView<>();
        plateColumn = new TableColumn<>("Placa");
        brandColumn = new TableColumn<>("Marca");
        seatsNumberColumn = new TableColumn<>("Assentos");
        eletricColumn = new TableColumn<>("Elétrico");
        lineColumn = new TableColumn<>("Linha");
        actionsColumn = new TableColumn<>("Ações");
        plateColumn.setCellValueFactory(item -> new ReadOnlyStringWrapper(item.getValue().getLicensePlate()));
        brandColumn.setCellValueFactory(item -> new ReadOnlyStringWrapper(item.getValue().getBrand()));
        seatsNumberColumn.setCellValueFactory(item -> new ReadOnlyStringWrapper(String.valueOf(item.getValue().getSeatsNumber())));
        eletricColumn.setCellValueFactory(item -> new ReadOnlyStringWrapper(item.getValue().isEletric() ? "Sim" : "Não"));
        lineColumn.setCellValueFactory(item -> new ReadOnlyStringWrapper(item.getValue().getLine()));
        actionsColumn.setCellValueFactory(item -> new ReadOnlyObjectWrapper<>(item.getValue()));
        actionsColumn.setCellFactory(p -> new TableCell<Bus, Bus>(){
            private final Button deleteButton = new Button("Deletar");
            private final Button editButton = new Button("Editar");
            private final HBox actions = new HBox(deleteButton, editButton);
            @Override
            protected void updateItem(Bus item, boolean empty) {
                super.updateItem(item, empty);
                if(item == null)
                    setGraphic(null);
                else {
                    HBox.setMargin(deleteButton, new Insets(0, 2, 0, 0));
                    setGraphic(actions);
                    deleteButton.setOnAction(e -> controller.deleteBus(item.getId()));
                    editButton.setOnAction(e -> controller.entityToBoundary(item));
                }
            };
        });
        tableView.getColumns().add(plateColumn);
        tableView.getColumns().add(brandColumn);
        tableView.getColumns().add(seatsNumberColumn);
        tableView.getColumns().add(eletricColumn);
        tableView.getColumns().add(lineColumn);
        tableView.getColumns().add(actionsColumn);

        bindProperties();

        root.getChildren().addAll(form, searchBox, tableView);
        VBox.setMargin(form, new Insets(10));
    }

    @Override
    public Pane render() {
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

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private void bindProperties() {
        Bindings.bindBidirectional(idLabel.textProperty(), controller.getIdProperty(),(StringConverter) new LongStringConverter());
        Bindings.bindBidirectional(licensePlateField.textProperty(), controller.getLicensePlateProperty());
        Bindings.bindBidirectional(brandField.textProperty(), controller.getBrandProperty());
        Bindings.bindBidirectional(seatsNumber.textProperty(), controller.getSeatsNumberProperty());
        Bindings.bindBidirectional(eletricCheckBox.selectedProperty(), controller.getElectricProperty());
        Bindings.bindBidirectional(lineField.textProperty(), controller.getLineProperty());
        Bindings.bindBidirectional(searchField.textProperty(), controller.getSearchProperty());
    }
}
