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

import java.util.ResourceBundle;

public class LogInWindowController {

    public Label enterLabel;
    public Label loginLabel;
    public Label passwordLabel;

    private static ResourceBundle bundle;

    public static void setBundle(ResourceBundle bundle) {
        LanguageBundles.setCurrentLanguage(bundle.getString("language"));
        LogInWindowController.bundle = bundle;
    }

    public static void setStartStage(Stage startStage) {
        LogInWindowController.startStage = startStage;
    }

    private static Stage startStage;

    private static String prevWindow;

    public static void setPrevWindow(String prevWindow) {
        LogInWindowController.prevWindow = prevWindow;
    }

    @FXML
    private PasswordField password;

    @FXML
    private Button enterButton;

    @FXML
    private Button back;

    @FXML
    private TextField login;

    @FXML
    void enter(ActionEvent event) {
        User user = new User(login.getText(), password.getText());
        Request request = new Request("sign_in","", user);
        Response response;
        response = Client.execute(request);
        String ans = response.getResponseBody();
        if (ans.equals("signInOK")) {
            CommandsWindowController.setBundle(bundle);
            Client.setUser(user);
            CommandsWindowController.setStartStage(Client.changeWindow("/clientModule/scenes/commands.fxml", startStage, 400, 530));
        } else {
            login.clear();
            password.clear();
            Client.showWindow(200, 400, bundle.getString(ans), Color.RED);
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
        enterLabel.setText(bundle.getString("vhod"));
        loginLabel.setText(bundle.getString("login"));
        passwordLabel.setText(bundle.getString("password"));
        enterButton.setText(bundle.getString("logInButton"));
        back.setText(bundle.getString("back"));
    }
}

