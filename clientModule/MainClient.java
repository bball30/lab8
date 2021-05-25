package clientModule;

import clientModule.controllers.StartWindowController;
import clientModule.localization.LanguageBundles;
import clientModule.util.AuthManager;
import clientModule.util.Console;
import common.exceptions.IncorrectInputInScriptException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

public class MainClient extends Application {
    private static String host = "localhost";
    private static int port = 13534;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Client client = new Client(host, port);
        launch();
    }

    @Override
    public void start(Stage stage) throws IOException {
        StartWindowController.setStage(stage);
        stage.setMinHeight(435);
        stage.setMinWidth(400);

        FXMLLoader root = new FXMLLoader();
        StartWindowController.setBundle(LanguageBundles.bundleRu);
        root.setLocation(getClass().getResource("/clientModule/scenes/start.fxml"));
        Parent xml = root.load();
        Scene scene = new Scene(xml);


        stage.setScene(scene);

        InputStream iconStream = getClass().getResourceAsStream("/clientModule/icon.png");
        Image image = new Image(iconStream);
        stage.getIcons().add(image);

        stage.setTitle("The best app ever");
        stage.setWidth(600);
        stage.setHeight(500);

        stage.show();
    }

}
