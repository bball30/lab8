package clientModule.controllers;

import clientModule.Client;
import clientModule.localization.LanguageBundles;
import common.utility.Request;
import common.utility.Response;
import common.utility.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ResourceBundle;

public class RegisterWindowController {

    public Label passwordLabel;
    public Button backButoon;
    public Label loginLabel;
    public Label registrationLabel;

    private static ResourceBundle bundle;

    public static void setBundle(ResourceBundle bundle) {
        LanguageBundles.setCurrentLanguage(bundle.getString("language"));
        RegisterWindowController.bundle = bundle;
    }

    public static void setStartStage(Stage startStage) {
        RegisterWindowController.startStage = startStage;
    }

    public static void setPrevWindow(String prevWindow) {
        RegisterWindowController.prevWindow = prevWindow;
    }

    private static Stage startStage;

    private static String prevWindow;

    @FXML
    private PasswordField password;

    @FXML
    private Button registerButton;

    @FXML
    private Button backButton;

    @FXML
    private TextField login;

    @FXML
    void getLogin(ActionEvent event) {
        System.out.println(login.getText());
    }

    @FXML
    void getPassword(ActionEvent event) {
        System.out.println(password.getText());
    }

    @FXML
    void register(ActionEvent event) {
        User user = new User(login.getText(), password.getText());
        Request request = new Request("sign_up","", user);
        Response response;
        response = Client.execute(request);
        String ans = response.getResponseBody();
        if (ans.equals("signUpOK")) {
            Client.setUser(user);
            CommandsWindowController.setBundle(bundle);
            CommandsWindowController.setStartStage(Client.changeWindow("/clientModule/scenes/commands.fxml", startStage, 400, 530));
        } else {
            login.clear();
            password.clear();
            Client.showWindow(200, 400, ans, Color.RED);
        }
    }

    @FXML
    void back(ActionEvent event) {
        StartWindowController.setStage(Client.changeWindow(prevWindow, startStage, 600, 500));
    }

    @FXML
    void initialize() {
        setLanguage();
    }

    private void setLanguage() {
        registrationLabel.setText(bundle.getString("registration"));
        loginLabel.setText(bundle.getString("login"));
        passwordLabel.setText(bundle.getString("password"));
        registerButton.setText(bundle.getString("register"));
        backButton.setText(bundle.getString("back"));
    }
}

