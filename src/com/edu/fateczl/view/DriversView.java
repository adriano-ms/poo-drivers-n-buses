package com.edu.fateczl.view;

import com.edu.fateczl.controller.DriverController;
import com.edu.fateczl.controller.InvalidInputException;
import com.edu.fateczl.model.dao.DbException;
import com.edu.fateczl.model.entities.Bus;
import com.edu.fateczl.model.entities.Driver;

import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.geometry.Insets;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;
import javafx.util.converter.LongStringConverter;

public class DriversView implements View {

    private VBox root;
    private GridPane form;
    private Label idLabel;
    private Button clearButton;
    private TextField licenseField;
    private TextField nameField;
    private DatePicker admissionDateField;
    private TextField shiftField;
    private TextField phoneField;
    private ComboBox<Bus> busesList;
    private Button addButton;
    private HBox searchBox;
    private TextField searchField;
    private Button searchButton;
    private TableView<Driver> tableView;
    private TableColumn<Driver, String> licenseColoumn;
    private TableColumn<Driver, String> nameColumn;
    private TableColumn<Driver, String> admissionDateColumn;
    private TableColumn<Driver, String> shiftColumn;
    private TableColumn<Driver, String> phoneColumn;
    private TableColumn<Driver, String> busLineColumn;
    private TableColumn<Driver, Driver> actionsColumn;

    private DriverController controller;

    public DriversView(){
        controller = new DriverController();
        root = new VBox();
        initializeForm();
        initializeSearchBox();
        initializeTable();
        bindProperties();
        root.getChildren().addAll(form, searchBox, tableView);
    }

    @Override
    public Pane render() {
        tableView.setItems(controller.getDriversObservableList());
        busesList.setItems(controller.getBusesObservableList());
        controller.findAllBuses();
        controller.findAllDrivers();
        controller.clearFields();
        return root;
    }

    private void saveAction(){
        try {
            controller.insertDriver();
        } catch (DbException | InvalidInputException e) {
            Alert.showAlert(AlertType.ERROR, "Erro!", null, e.getMessage());
        }
    }

    private void searchAction(){
        try {
            controller.searchDrivers();
        } catch (DbException e) {
            Alert.showAlert(AlertType.ERROR, "Erro!", null, e.getMessage());
        }
    }

    private void initializeTable() {
        tableView = new TableView<>();
        licenseColoumn = new TableColumn<>("CNH");
        nameColumn = new TableColumn<>("Nome");
        admissionDateColumn = new TableColumn<>("Admissão");
        shiftColumn = new TableColumn<>("Turno");
        phoneColumn = new TableColumn<>("Telefone");
        busLineColumn = new TableColumn<>("Linha");
        actionsColumn = new TableColumn<>("Ações");
        licenseColoumn.setCellValueFactory(item -> new ReadOnlyStringWrapper(item.getValue().getDriverLicense()));
        nameColumn.setCellValueFactory(item -> new ReadOnlyStringWrapper(item.getValue().getName()));
        admissionDateColumn.setCellValueFactory(item -> new ReadOnlyStringWrapper(item.getValue().getAdmissionDate().toString()));
        shiftColumn.setCellValueFactory(item -> new ReadOnlyStringWrapper(item.getValue().getShift()));
        phoneColumn.setCellValueFactory(item -> new ReadOnlyStringWrapper(item.getValue().getPhone()));
        busLineColumn.setCellValueFactory(item -> new ReadOnlyStringWrapper(item.getValue().getBus().getLine()));
        actionsColumn.setCellValueFactory(item -> new ReadOnlyObjectWrapper<>(item.getValue()));
        actionsColumn.setCellFactory(p -> new TableCell<Driver, Driver>(){
            private final Button deleteButton = new Button("Deletar");
            private final Button editButton = new Button("Editar");
            private final HBox actions = new HBox(deleteButton, editButton);
            @Override
            protected void updateItem(Driver item, boolean empty) {
                super.updateItem(item, empty);
                if(item == null)
                    setGraphic(null);
                else {
                    HBox.setMargin(deleteButton, new Insets(0, 2, 0, 0));
                    setGraphic(actions);
                    deleteButton.setOnAction(e -> {
                        try {
                            controller.deleteDriver(item.getId());
                        } catch (DbException e1) {
                            Alert.showAlert(AlertType.ERROR, "Erro!", null, e1.getMessage());
                        }
                    });
                    editButton.setOnAction(e -> controller.entityToBoundary(item));
                }
            };
        });
        actionsColumn.setReorderable(false);
        tableView.getColumns().add(licenseColoumn);
        tableView.getColumns().add(nameColumn);
        tableView.getColumns().add(admissionDateColumn);
        tableView.getColumns().add(shiftColumn);
        tableView.getColumns().add(phoneColumn);
        tableView.getColumns().add(busLineColumn);
        tableView.getColumns().add(actionsColumn);
    }

    private void initializeSearchBox() {
        searchBox = new HBox();
        searchField = new TextField();
        searchField.setPromptText("Pesquisar...");
        searchButton = new Button("Pesquisar");
        searchButton.setOnAction(e -> searchAction());
        searchBox.getChildren().addAll(searchField, searchButton);
        HBox.setHgrow(searchField, Priority.ALWAYS);
    }

    private void initializeForm() {
        form = new GridPane(5, 10);
        idLabel = new Label();
        clearButton = new Button("Limpar");
        clearButton.setOnAction(e -> controller.clearFields());
        licenseField = new TextField();
        nameField = new TextField();
        admissionDateField = new DatePicker();
        shiftField = new TextField();
        phoneField = new TextField();
        busesList = new ComboBox<>();
        addButton = new Button("Salvar");
        addButton.setOnAction(e -> saveAction());
        form.add(new Label("ID"), 0, 0);
        form.add(idLabel, 1, 0);
        form.add(clearButton, 2, 0);
        form.add(new Label("CNH"), 0, 1);
        form.add(licenseField, 1, 1);
        form.add(new Label("Nome"), 0, 2);
        form.add(nameField, 1, 2);
        form.add(new Label("Admissão"), 0, 3);
        form.add(admissionDateField, 1, 3);
        form.add(new Label("Turno"), 0, 4);
        form.add(shiftField, 1, 4);
        form.add(new Label("Telefone"), 0, 5);
        form.add(phoneField, 1, 5);
        form.add(new Label("Ônibus"), 0, 6);
        form.add(busesList, 1, 6);
        form.add(addButton, 0, 7);
        Constraints.setTextFieldMaxLength(licenseField, 11);
        Constraints.setTextFieldInteger(licenseField);
        Constraints.setTextFieldMaxLength(nameField, 64);
        Constraints.setTextFieldMaxLength(shiftField, 16);
        Constraints.setTextFieldMaxLength(phoneField, 11);
        VBox.setMargin(form, new Insets(10));
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private void bindProperties(){
        Bindings.bindBidirectional(idLabel.textProperty(), controller.getIdProperty(),(StringConverter) new LongStringConverter());
        Bindings.bindBidirectional(licenseField.textProperty(), controller.getLicenseProperty());
        Bindings.bindBidirectional(nameField.textProperty(), controller.getNameProperty());
        Bindings.bindBidirectional(admissionDateField.valueProperty(), controller.getAdmissionDateProperty());
        Bindings.bindBidirectional(shiftField.textProperty(), controller.getShiftProperty());
        Bindings.bindBidirectional(phoneField.textProperty(), controller.getPhoneProperty());
        Bindings.bindBidirectional(busesList.valueProperty(), controller.getBusProperty());
        Bindings.bindBidirectional(searchField.textProperty(), controller.getSearchProperty());
    }
}
