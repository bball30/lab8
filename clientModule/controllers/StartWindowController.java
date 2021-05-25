package clientModule.controllers;

import clientModule.Client;
import clientModule.localization.LanguageBundles;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.util.ResourceBundle;

public class StartWindowController {
    public Label groupLabel;
    ObservableList<String> languages = FXCollections.observableArrayList("Русский", "Eestlane", "Français", "Español");
    private static Stage startStage;

    public static void setStage(Stage stage) {
        StartWindowController.startStage = stage;
    }

    private static ResourceBundle bundle;

    public static void setBundle(ResourceBundle bundle) {
        LanguageBundles.setCurrentLanguage(bundle.getString("language"));
        StartWindowController.bundle = bundle;
    }

    @FXML
    private Button authorizationButton;

    @FXML
    private Button exitButton;

    @FXML
    private Button registerButton;

    @FXML
    private ChoiceBox<String> language;

    @FXML
    void authorization(ActionEvent event) {
        LogInWindowController.setPrevWindow("/clientModule/scenes/start.fxml");
        LogInWindowController.setBundle(bundle);
        LogInWindowController.setStartStage(Client.changeWindow("/clientModule/scenes/logging.fxml", startStage, 400, 250));
    }


    @FXML
    void register(ActionEvent event) {
        RegisterWindowController.setPrevWindow("/clientModule/scenes/start.fxml");
        RegisterWindowController.setBundle(bundle);
        RegisterWindowController.setStartStage(Client.changeWindow("/clientModule/scenes/registration.fxml", startStage, 400, 250));
    }

    @FXML
    void exit(ActionEvent event) {
        System.exit(2);
    }

    @FXML
    void initialize() {
        setLanguage();
        language.setItems(languages);
        language.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                //System.out.println(newValue);
                switch ((int) newValue) {
                    case 0:
                        bundle = LanguageBundles.bundleRu;
                        break;
                    case 1:
                        bundle = LanguageBundles.bundleEt;
                        break;
                    case 2:
                        bundle = LanguageBundles.bundleFr;
                        break;
                    case 3:
                        bundle = LanguageBundles.bundleEs;
                        break;
                }
                LanguageBundles.setCurrentLanguage(bundle.getString("language"));
                setLanguage();
            }
        });
    }

    private void setLanguage() {
        groupLabel.setText(bundle.getString("groups"));
        authorizationButton.setText(bundle.getString("logInButton"));
        registerButton.setText(bundle.getString("registration"));
        exitButton.setText(bundle.getString("exit"));
        language.setValue(bundle.getString("language"));
    }

}

