package io.ui.gui.controllers;

import api.Response;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;
import models.Flat;

import java.util.Collection;

public class MainController {

    @FXML private Label userInfoLabel;
    @FXML private Pane mapPane;

    @FXML private TableView<Flat> table;
    @FXML private TableColumn<Flat, Number> idCol;
    @FXML private TableColumn<Flat, String> nameCol;
    @FXML private TableColumn<Flat, Number> areaCol;
    @FXML private TableColumn<Flat, Number> roomsCol;
    @FXML private TableColumn<Flat, Number> xCol;
    @FXML private TableColumn<Flat, Number> yCol;
    @FXML private TableColumn<Flat, Number> ownerCol;
    @FXML private TableColumn<Flat, String> houseNameCol;
    @FXML private TableColumn<Flat, Number> houseYearCol;
    @FXML private TableColumn<Flat, Number> houseFloorsCol;

    @FXML private TextField filterField;

    private ObservableList<Flat> masterData = FXCollections.observableArrayList();
    private Timeline refreshTimeline;

    @FXML
    public void initialize() {
        idCol.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getId()));
        nameCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        areaCol.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getArea()));
        roomsCol.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getNumberOfRooms()));
        xCol.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getCoordinates().getX()));
        yCol.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getCoordinates().getY()));
        ownerCol.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getOwnerId()));
        houseNameCol.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getHouse() != null ? cellData.getValue().getHouse().getName() : ""));
        houseYearCol.setCellValueFactory(cellData ->
                new SimpleObjectProperty<>(cellData.getValue().getHouse() != null ? cellData.getValue().getHouse().getYear() : 0));
        houseFloorsCol.setCellValueFactory(cellData ->
                new SimpleObjectProperty<>(cellData.getValue().getHouse() != null ? cellData.getValue().getHouse().getNumberOfFloors() : 0));

        javafx.collections.transformation.FilteredList<Flat> filteredData =
                new javafx.collections.transformation.FilteredList<>(masterData, p -> true);

        filterField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(flat -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                return flat.getName().toLowerCase().contains(lowerCaseFilter);
            });
        });

        javafx.collections.transformation.SortedList<Flat> sortedData =
                new javafx.collections.transformation.SortedList<>(filteredData);

        sortedData.comparatorProperty().bind(table.comparatorProperty());

        table.setItems(sortedData);

        refreshTimeline = new Timeline(new KeyFrame(Duration.seconds(3), event -> {
            loadCollectionFromServer();
        }));
        refreshTimeline.setCycleCount(Timeline.INDEFINITE); // Бесконечный цикл
        refreshTimeline.play();
    }

    public void initUser(String username) {
        userInfoLabel.setText("Пользователь: " + username);
        loadCollectionFromServer();
    }

    public void loadCollectionFromServer() {
        new Thread(() -> {
            try {
                Response response = AuthController.networkClient.sendCommand("show", null, null);
                if (response.isSuccess()) {
                    Collection<Flat> flats = response.getCollection();
                    if (flats != null) {
                        Platform.runLater(() -> {
                            masterData.clear();
                            masterData.addAll(flats);
                            table.refresh();
                        });
                    }
                } else {
                    Platform.runLater(() -> showAlert("Ошибка", response.getMessage()));
                }
            } catch (Exception e) {
                Platform.runLater(() -> showAlert("Ошибка сети", "Не удалось загрузить данные с сервера"));
            }
        }).start();
    }

    @FXML
    void handleAdd(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/FlatFormView.fxml"));
            java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("bundles.gui", java.util.Locale.getDefault());
            loader.setResources(bundle);
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle(bundle.containsKey("main.button.add") ? bundle.getString("main.button.add") : "Новая квартира");
            stage.setScene(new Scene(root));
            stage.setResizable(false);

            FlatFormController controller = loader.getController();
            controller.setMainController(this);

            stage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Ошибка", "Не удалось открыть окно добавления");
        }
    }

    @FXML
    void handleUpdate(ActionEvent event) {
        Flat selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Внимание", "Сначала выберите объект в таблице!");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/FlatFormView.fxml"));
            java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("bundles.gui", java.util.Locale.getDefault());
            loader.setResources(bundle);
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle(bundle.containsKey("form.title.update") ? bundle.getString("form.title.update") : "Редактирование");
            stage.setScene(new Scene(root));
            stage.setResizable(false);

            FlatFormController controller = loader.getController();
            controller.setMainController(this);
            controller.setEditableFlat(selected);

            stage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Ошибка", "Не удалось открыть окно редактирования");
        }
    }

    @FXML
    void handleRemove(ActionEvent event) {
        Flat selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Внимание", "Сначала выберите объект в таблице!");
            return;
        }

        new Thread(() -> {
            try {
                Response response = AuthController.networkClient.sendCommand("remove_by_id", String.valueOf(selected.getId()), null);
                Platform.runLater(() -> {
                    if (response.isSuccess()) {
                        masterData.remove(selected);
                    } else {
                        showAlert("Ошибка", response.getMessage());
                    }
                });
            } catch (Exception e) {
                Platform.runLater(() -> showAlert("Ошибка сети", "Не удалось удалить элемент"));
            }
        }).start();
    }

    @FXML
    void handleClear(ActionEvent event) {
        new Thread(() -> {
            try {
                Response response = AuthController.networkClient.sendCommand("clear", null, null);
                Platform.runLater(() -> {
                    if (response.isSuccess()) {
                        masterData.clear();
                    } else {
                        showAlert("Ошибка", response.getMessage());
                    }
                });
            } catch (Exception e) {
                Platform.runLater(() -> showAlert("Ошибка сети", "Не удалось очистить коллекцию"));
            }
        }).start();
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public void updateHeaders(java.util.ResourceBundle bundle) {
        idCol.setText("ID");
        nameCol.setText(bundle.getString("main.table.name"));
        areaCol.setText(bundle.getString("main.table.area"));
        roomsCol.setText(bundle.getString("main.table.rooms"));
        xCol.setText("X");
        yCol.setText("Y");
        ownerCol.setText(bundle.getString("main.table.owner"));
        houseNameCol.setText(bundle.getString("form.label.house.name"));
        houseYearCol.setText(bundle.getString("form.label.house.year"));
        houseFloorsCol.setText(bundle.getString("form.label.house.floors"));
    }
}