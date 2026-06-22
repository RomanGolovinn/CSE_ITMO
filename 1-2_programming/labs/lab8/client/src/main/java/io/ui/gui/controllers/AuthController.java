package io.ui.gui.controllers;

import api.Response;
import io.net.Client;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.Locale;
import java.util.ResourceBundle;

public class AuthController {

    public static Client networkClient;

    @FXML private TextField loginField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;
    @FXML private ComboBox<String> languageComboBox;
    @FXML private Button loginButton;
    @FXML private Button registerButton;
    @FXML private Label titleLabel;

    @FXML private ResourceBundle resources;

    @FXML
    public void initialize() {
        languageComboBox.setItems(FXCollections.observableArrayList(
                "Русский", "Eesti", "Polski", "Español (Guatemala)"
        ));

        String lang = resources.getLocale().getLanguage();
        if (lang.equals("et")) languageComboBox.getSelectionModel().select(1);
        else if (lang.equals("pl")) languageComboBox.getSelectionModel().select(2);
        else if (lang.equals("es")) languageComboBox.getSelectionModel().select(3);
        else languageComboBox.getSelectionModel().select(0);

        languageComboBox.getSelectionModel().selectedIndexProperty().addListener((obs, oldVal, newVal) -> {
            Locale newLocale;
            switch (newVal.intValue()) {
                case 1: newLocale = new Locale("et", "EE"); break;
                case 2: newLocale = new Locale("pl", "PL"); break;
                case 3: newLocale = new Locale("es", "GT"); break;
                default: newLocale = new Locale("ru", "RU"); break;
            }
            updateResources(newLocale);
            applyLanguage();
        });
    }

    private void updateResources(Locale locale) {
        resources = ResourceBundle.getBundle("bundles.gui", locale);
    }

    private void applyLanguage() {
        loginField.setPromptText(resources.getString("auth.login.prompt"));
        passwordField.setPromptText(resources.getString("auth.password.prompt"));
        loginButton.setText(resources.getString("auth.button.login"));
        registerButton.setText(resources.getString("auth.button.register"));
        if(titleLabel != null) titleLabel.setText(resources.getString("auth.title"));

        Stage stage = (Stage) loginButton.getScene().getWindow();
        stage.setTitle(resources.getString("auth.title"));
    }

    @FXML
    void handleLogin(ActionEvent event) { processAuth(true); }

    @FXML
    void handleRegister(ActionEvent event) { processAuth(false); }

    private void processAuth(boolean isLogin) {
        String login = loginField.getText().trim();
        String password = passwordField.getText().trim();

        if (login.isEmpty() || password.isEmpty()) {
            errorLabel.setText(resources.getString("auth.error.empty"));
            return;
        }

        errorLabel.setText("");
        setControlsDisabled(true);

        new Thread(() -> {
            try {
                networkClient.setCredentials(login, password);
                Response response = networkClient.sendCommand(isLogin ? "login" : "register", null, null);

                Platform.runLater(() -> {
                    if (response.isSuccess()) {
                        try {
                            Stage stage = (Stage) loginButton.getScene().getWindow();

                            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/MainView.fxml"));
                            loader.setResources(resources); // Передаем текущий bundle
                            Parent root = loader.load();

                            MainController mainController = loader.getController();
                            mainController.initUser(login);

                            stage.setTitle("Квартиры");
                            stage.setScene(new Scene(root, 1200, 700));
                            stage.centerOnScreen();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        errorLabel.setText(response.getMessage());
                        setControlsDisabled(false);
                    }
                });
            } catch (Exception e) {
                Platform.runLater(() -> {
                    errorLabel.setText(resources.getString("auth.error.network"));
                    setControlsDisabled(false);
                });
            }
        }).start();
    }

    private void setControlsDisabled(boolean disabled) {
        loginButton.setDisable(disabled);
        registerButton.setDisable(disabled);
        loginField.setDisable(disabled);
        passwordField.setDisable(disabled);
        languageComboBox.setDisable(disabled);
    }
}