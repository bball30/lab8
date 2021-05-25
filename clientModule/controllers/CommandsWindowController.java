package clientModule.controllers;

import clientModule.Client;
import clientModule.localization.LanguageBundles;
import clientModule.util.Console;
import common.data.StudyGroup;
import common.exceptions.IncorrectInputInScriptException;
import common.utility.Request;
import common.utility.Response;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Path;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Scanner;

public class CommandsWindowController {

    private static ResourceBundle bundle;

    public static void setBundle(ResourceBundle bundle) {
        LanguageBundles.setCurrentLanguage(bundle.getString("language"));
        CommandsWindowController.bundle = bundle;
    }


    public Button scriptButton;
    ObservableList<String> languages = FXCollections.observableArrayList("Русский", "Eestlane", "Français", "Español");
    private static Stage startStage;

    private Stage getStage;
    private String arg;

    public static void setStartStage(Stage startStage) {
        CommandsWindowController.startStage = startStage;
    }

    @FXML
    private Button historyButton;

    @FXML
    private Button tableButton;

    @FXML
    private Button removeGreaterButton;

    @FXML
    private Button removeByFormOfEducationButton;

    @FXML
    private Button printShouldBeExpelledButton;

    @FXML
    private Button addButton;

    @FXML
    private ChoiceBox<String> language;

    @FXML
    private Button uniqueAdminButton;

    @FXML
    private Button exitButton;

    @FXML
    private Button clearButton;

    @FXML
    private Button infoButton;

    @FXML
    private Button userNameButton;

    @FXML
    private Button updateButton;

    @FXML
    private Button removeLowerButton;

    @FXML
    private Button removeByIdButton;

    @FXML
    private Button visualizationButton;

    public void getUpdateWindow(String field) {
        Label label = new Label(field);
        TextField textField = new TextField();
        //final String[] arg = new String[1];
        textField.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                int id = checkId(textField.getText());
                if (id == -1) {
                    Client.showWindow(150, 200, bundle.getString("incorrectID"), Color.RED);
                } else {
                    String arg = String.valueOf(id);
                    Response response = Client.execute(new Request("findId", arg, null, Client.getUser()));
                    String ans = response.getResponseBody();
                    if (ans.equals("true")) {
                        List<StudyGroup> groups = Client.getGroups();
                        StudyGroup groupToInsert = null;
                        for (StudyGroup gr : groups) {
                            if (gr.getId() == id) groupToInsert = gr;
                        }
                        GroupWindowController.setStudyGroup(groupToInsert);
                        GroupWindowController.setBundle(bundle);
                        GroupWindowController.setCommandName("update");
                        GroupWindowController.setPrevWindow("/clientModule/scenes/commands.fxml");
                        GroupWindowController.setArg(arg);
                        GroupWindowController.setStage(Client.changeWindow("/clientModule/scenes/group.fxml", startStage, 550, 600));
                    } else {
                        ans = response.localize();
                        Client.showWindow(200, 500, ans, Color.RED);
                    }
                }
                getStage.close();
            }
        });
        label.setFont(new Font(20));
        VBox vBox = new VBox(label, textField);
        vBox.setSpacing(25);
        BorderPane pane = new BorderPane(vBox);
        Scene scene = new Scene(pane);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setMinHeight(100);
        stage.setHeight(200);
        stage.setMinWidth(150);
        stage.setWidth(300);
        getStage = stage;
        stage.show();
    }

    int checkId(String data) {
        try {
            return Integer.parseInt(data);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public void getScriptWindow(String field) {
        Label label = new Label(field);
        FileChooser fileChooser = new FileChooser();
        Button button = new Button("Select File");
        StringBuilder stringBuilder = new StringBuilder();
        button.setOnAction(event -> {
            try {

                File file = fileChooser.showOpenDialog(getStage);
                Scanner scaner = new Scanner(file);
                Console console = new Console(scaner, file, stringBuilder);
                do {
                    Request request = console.interactiveMode(null, Client.getUser());
                    if (request != null) {
                        Response response = Client.execute(request);
                        stringBuilder.append(response.getResponseBody() + "\n");
                    }
                } while (scaner.hasNextLine());
                Client.showWindow(150, 300, bundle.getString("scriptOK"), Color.GREEN);
            } catch (IOException e) {
                Client.showWindow(150, 300, "Problems with the file which you enter", Color.RED);
            } catch (IncorrectInputInScriptException recursiveScript) {
                Client.showWindow(150, 300, "Error! Recursive in script", Color.RED);
            }
            getStage.close();
            Client.showWindow(300, 400, stringBuilder.toString(), Color.BLACK);
        });
        label.setFont(new Font(20));
        VBox vBox = new VBox(label, button);
        vBox.setSpacing(25);
        BorderPane pane = new BorderPane(vBox);
        Scene scene = new Scene(pane);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setMinHeight(100);
        stage.setHeight(200);
        stage.setMinWidth(150);
        stage.setWidth(300);
        getStage = stage;
        stage.show();
    }

    /*public void getScriptWindow(String field) {
        Label label = new Label(field);
        FileChooser fileChooser = new FileChooser();
        TextField textField = new TextField();
        StringBuilder stringBuilder = new StringBuilder();
        textField.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    String path = new File(textField.getText()).getAbsolutePath();
                    File file = new File(path);
                    Console console = new Console(new Scanner(file), file, stringBuilder);
                    Request request = console.interactiveMode(null, Client.getUser());
                    if (request != null) {
                        Response response = Client.execute(request);
                        stringBuilder.append(response.getResponseBody());
                    }
                    Client.showWindow(150, 300, "Script executed", Color.GREEN);
                } catch (IOException e) {
                    Client.showWindow(150, 300, "Problems with the file which you enter", Color.RED);
                } catch (IncorrectInputInScriptException recursiveScript) {
                    Client.showWindow(150, 300, "Error! Recursive in script", Color.RED);
                }
                getStage.close();
                Client.showWindow(300, 400, stringBuilder.toString(), Color.BLACK);
            }
        });
        label.setFont(new Font(20));
        VBox vBox = new VBox(label, textField);
        vBox.setSpacing(25);
        BorderPane pane = new BorderPane(vBox);
        Scene scene = new Scene(pane);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setMinHeight(100);
        stage.setHeight(200);
        stage.setMinWidth(150);
        stage.setWidth(300);
        getStage = stage;
        stage.show();
    }*/

    public void getWindow(String field, String commandName) {
        Label label = new Label(field);
        TextField textField = new TextField();
        //final String[] arg = new String[1];
        textField.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                int id = checkId(textField.getText());
                if (id == -1) {
                    Client.showWindow(150, 200, bundle.getString("incorrectID"), Color.RED);
                } else {
                    String arg = textField.getText();
                    Response response = Client.execute(new Request(commandName, arg, Client.getUser()));
                    Client.showWindow(200, 400, bundle.getString(response.getResponseBody()), Color.BLACK);
                }
                getStage.close();
            }
        });
        label.setFont(new Font(20));
        VBox vBox = new VBox(label, textField);
        vBox.setSpacing(25);
        BorderPane pane = new BorderPane(vBox);
        Scene scene = new Scene(pane);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setMinHeight(100);
        stage.setHeight(200);
        stage.setMinWidth(150);
        stage.setWidth(300);
        getStage = stage;
        stage.show();
    }

    public void getFormWindow(String field, String commandName) {
        ObservableList<String> formsOfEducation = FXCollections.observableArrayList("DISTANCE_EDUCATION", "FULL_TIME_EDUCATION", "EVENING_CLASSES");
        ChoiceBox<String> box = new ChoiceBox<>(formsOfEducation);
        Label label = new Label(field);
        Button button = new Button("Ok");
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String arg = box.getValue();
                Response response = Client.execute(new Request(commandName, arg, Client.getUser()));
                String ans = response.localize();
                Client.showWindow(200, 400, ans, Color.BLACK);
                getStage.close();
            }
        });
        label.setFont(new Font(20));
        VBox vBox = new VBox(label, box, button);
        vBox.setSpacing(15);
        BorderPane pane = new BorderPane(vBox);
        Scene scene = new Scene(pane);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setMinHeight(100);
        stage.setHeight(200);
        stage.setMinWidth(150);
        stage.setWidth(300);
        getStage = stage;
        stage.show();
    }

    @FXML
    void initialize() {
        setLanguage();
        userNameButton.setText(Client.getUser().getLogin());
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
        language.setValue(bundle.getString("language"));
        addButton.setText(bundle.getString("add"));
        historyButton.setText(bundle.getString("history"));
        removeByFormOfEducationButton.setText(bundle.getString("removeByFormOfEducation"));
        uniqueAdminButton.setText(bundle.getString("printUniqueAdmin"));
        printShouldBeExpelledButton.setText(bundle.getString("printShouldBeExpelled"));
        tableButton.setText(bundle.getString("table"));
        removeByIdButton.setText(bundle.getString("removeById"));
        clearButton.setText(bundle.getString("clear"));
        infoButton.setText(bundle.getString("info"));
        removeLowerButton.setText(bundle.getString("removeLower"));
        removeGreaterButton.setText(bundle.getString("removeGreater"));
        scriptButton.setText(bundle.getString("executeScript"));
        updateButton.setText(bundle.getString("update"));
        visualizationButton.setText(bundle.getString("visualize"));
        exitButton.setText(bundle.getString("exit"));
    }

    @FXML
    void exit(ActionEvent event) {
        Response response = Client.execute(new Request("log_out","", Client.getUser()));
        System.exit(2);
        //StartWindowController.setStage(Client.changeWindow("/clientModule/scenes/start.fxml", startStage, 605, 500));
        //Client.showWindow(200, 300, response.getResponseBody(), Color.GREEN);
    }

    @FXML
    void add(ActionEvent event) {
        GroupWindowController.setStudyGroup(null);
        GroupWindowController.setBundle(bundle);
        GroupWindowController.setCommandName("add");
        GroupWindowController.setPrevWindow("/clientModule/scenes/commands.fxml");
        GroupWindowController.setStage(Client.changeWindow("/clientModule/scenes/group.fxml", startStage, 550, 600));
    }

    @FXML
    void update(ActionEvent event) {
        getUpdateWindow(" id");
    }

    @FXML
    void removeById(ActionEvent event) {
        getWindow(" id", "remove_by_id");
    }

    @FXML
    void removeLower(ActionEvent event) {
        GroupWindowController.setStudyGroup(null);
        GroupWindowController.setBundle(bundle);
        GroupWindowController.setCommandName("remove_lower");
        GroupWindowController.setPrevWindow("/clientModule/scenes/commands.fxml");
        GroupWindowController.setStage(Client.changeWindow("/clientModule/scenes/group.fxml", startStage, 550, 600));
    }

    @FXML
    void removeGreater(ActionEvent event) {
        GroupWindowController.setStudyGroup(null);
        GroupWindowController.setBundle(bundle);
        GroupWindowController.setCommandName("remove_greater");
        GroupWindowController.setPrevWindow("/clientModule/scenes/commands.fxml");
        GroupWindowController.setStage(Client.changeWindow("/clientModule/scenes/group.fxml", startStage, 550, 600));
    }

    @FXML
    void clear(ActionEvent event) {
        String ans = Client.execute(new Request("clear", "", Client.getUser())).getResponseBody();
        Client.showWindow(200, 500, bundle.getString(ans), Color.BLACK);
    }

    @FXML
    void removeByFormOfEducation(ActionEvent event) {
        getFormWindow(" Form of Education", "remove_any_by_form_of_education");
    }

    @FXML
    void history(ActionEvent event) {
        String ans = Client.execute(new Request("history", "", Client.getUser())).localize();
        Client.showWindow(500, 400, ans, Color.BLACK);
    }

    @FXML
    void info(ActionEvent event) {
        String ans = Client.execute(new Request("info", "", Client.getUser())).localize();
        Client.showWindow(200, 900, ans, Color.BLACK);
    }

    @FXML
    void uniqueAdmin(ActionEvent event) {
        String ans = Client.execute(new Request("print_unique_group_admin", "", Client.getUser())).localize();
        Client.showWindow(200, 900, ans, Color.BLACK);
    }

    @FXML
    void printShouldBeExpelled(ActionEvent event) {
        String ans = Client.execute(new Request("print_field_descending_should_be_expelled", "", Client.getUser())).localize();
        Client.showWindow(200, 900, ans, Color.BLACK);
    }

    @FXML
    void executeScript(ActionEvent event) {
        getScriptWindow(" File name");
    }

    @FXML
    void openTable(ActionEvent event) {
        TableWindowController.setBundle(bundle);
        TableWindowController.setGroups(Client.getGroups());
        //System.out.println(Client.getGroups());
        TableWindowController.setStage(Client.changeWindow("/clientModule/scenes/table.fxml", startStage, 500, 1000));
    }

    @FXML
    void visualize(ActionEvent event) {
        VisualizeWindowController.setBundle(bundle);
        VisualizeWindowController.setGroups(Client.getGroups());
        VisualizeWindowController.setStartStage(Client.changeWindow("/clientModule/scenes/visualize.fxml", startStage, 500, 800));
    }

    @FXML
    void changeUser(ActionEvent event) {
        Response response = Client.execute(new Request("log_out","", Client.getUser()));
        StartWindowController.setBundle(bundle);
        StartWindowController.setStage(Client.changeWindow("/clientModule/scenes/start.fxml", startStage, 605, 500));
        Client.showWindow(200, 300, bundle.getString(response.getResponseBody()), Color.GREEN);
    }

}

