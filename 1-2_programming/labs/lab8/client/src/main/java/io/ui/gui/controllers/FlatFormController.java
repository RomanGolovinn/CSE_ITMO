package io.ui.gui.controllers;

import api.Response;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.Coordinates;
import models.Flat;
import models.House;
import models.enums.Furnish;
import models.enums.Transport;
import models.enums.View;

import java.util.ResourceBundle;

public class FlatFormController {

    @FXML private TextField nameField;
    @FXML private TextField areaField;
    @FXML private TextField roomsField;
    @FXML private TextField xField;
    @FXML private TextField yField;

    @FXML private ComboBox<Furnish> furnishComboBox;
    @FXML private ComboBox<View> viewComboBox;
    @FXML private ComboBox<Transport> transportComboBox;

    @FXML private TextField houseNameField;
    @FXML private TextField houseYearField;
    @FXML private TextField houseFloorsField;

    @FXML private ResourceBundle resources;

    private MainController mainController;
    private Flat editableFlat;

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    @FXML
    public void initialize() {
        furnishComboBox.setItems(FXCollections.observableArrayList(Furnish.values()));
        viewComboBox.setItems(FXCollections.observableArrayList(View.values()));
        transportComboBox.setItems(FXCollections.observableArrayList(Transport.values()));
    }

    public void setEditableFlat(Flat flat) {
        this.editableFlat = flat;
        if (flat != null) {
            nameField.setText(flat.getName());
            areaField.setText(String.valueOf(flat.getArea()));
            roomsField.setText(String.valueOf(flat.getNumberOfRooms()));
            xField.setText(String.valueOf(flat.getCoordinates().getX()));
            yField.setText(String.valueOf(flat.getCoordinates().getY()));
            furnishComboBox.setValue(flat.getFurnish());
            viewComboBox.setValue(flat.getView());
            transportComboBox.setValue(flat.getTransport());

            if (flat.getHouse() != null) {
                houseNameField.setText(flat.getHouse().getName());
                houseYearField.setText(String.valueOf(flat.getHouse().getYear()));
                houseFloorsField.setText(String.valueOf(flat.getHouse().getNumberOfFloors()));
            }
        }
    }

    @FXML
    void handleSave(ActionEvent event) {
        try {
            String name = nameField.getText().trim();
            long area = Long.parseLong(areaField.getText().trim());
            long rooms = Long.parseLong(roomsField.getText().trim());
            long x = Long.parseLong(xField.getText().trim());
            Float y = Float.parseFloat(yField.getText().trim());

            if (x < 0 || x > 800 || y < 0 || y > 600 || area < 1 || area > 250) {
                showAlert("Лимит превышен: X (0-800), Y (0-600), Площадь (1-250)");
                return;
            }

            Furnish furnish = furnishComboBox.getValue();
            View view = viewComboBox.getValue();
            Transport transport = transportComboBox.getValue();

            if (name.isEmpty() || furnish == null || view == null || transport == null) {
                showAlert(resources.getString("form.error.validation"));
                return;
            }

            Coordinates coords = new Coordinates(x, y);

            House house = null;
            String houseName = houseNameField.getText().trim();
            if (!houseName.isEmpty()) {
                int year = Integer.parseInt(houseYearField.getText().trim());
                long floors = Long.parseLong(houseFloorsField.getText().trim());
                house = new House(houseName, year, floors);
            }

            Flat flatObj = new Flat(name, coords, area, rooms, furnish, view, transport, house);

            final String commandName;
            final String argument;
            final Flat finalFlat;

            if (editableFlat == null) {
                commandName = "add";
                argument = null;
                finalFlat = flatObj;
            } else {
                commandName = "update";
                argument = String.valueOf(editableFlat.getId());
                flatObj.setId(editableFlat.getId());
                finalFlat = flatObj;
            }

            new Thread(() -> {
                try {
                    Response response = AuthController.networkClient.sendCommand(commandName, argument, finalFlat);
                    Platform.runLater(() -> {
                        if (response.isSuccess()) {
                            mainController.loadCollectionFromServer();
                            closeWindow();
                        } else {
                            showErrorAlert(response.getMessage());
                        }
                    });
                } catch (Exception e) {
                    Platform.runLater(() -> showErrorAlert("Network Error"));
                }
            }).start();

        } catch (NumberFormatException e) {
            showAlert(resources.getString("form.error.number"));
        }
    }

    @FXML
    void handleCancel(ActionEvent event) {
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) nameField.getScene().getWindow();
        stage.close();
    }

    private void showAlert(String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("");
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void showErrorAlert(String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("");
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}