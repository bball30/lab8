package clientModule.controllers;

import clientModule.Client;
import clientModule.localization.LanguageBundles;
import common.data.Color;
import common.data.Country;
import common.data.FormOfEducation;
import common.data.StudyGroup;
import common.utility.Request;
import common.utility.Response;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class TableWindowController {

    private static Stage startStage;
    private static List<StudyGroup> groups;
    public Button updateButton;
    public Button showButton;
    public Label tableLabel;

    ObservableList<String> fields = FXCollections.observableArrayList("id", bundle.getString("name"), "X", "Y", bundle.getString("averageMark"), bundle.getString("admin"));

    public static void setGroups(List<StudyGroup> groups) {
        TableWindowController.groups = groups;
    }

    public static void setStage(Stage startStage) {
        TableWindowController.startStage = startStage;
    }

    private static ResourceBundle bundle;

    public static void setBundle(ResourceBundle bundle) {
        LanguageBundles.setCurrentLanguage(bundle.getString("language"));
        TableWindowController.bundle = bundle;
    }

    @FXML
    private TableColumn<StudyGroup, Long> countColumn;

    @FXML
    private TableColumn<StudyGroup, String> locNameColumn;

    @FXML
    private TableColumn<StudyGroup, String> passportColumn;

    @FXML
    private TableColumn<StudyGroup, Country> nationalityColumn;

    @FXML
    private TableColumn<StudyGroup, Color> hairColumn;

    @FXML
    private TableColumn<StudyGroup, String> personColumn;

    @FXML
    private TableColumn<StudyGroup, Integer> markColumn;

    @FXML
    private TableColumn<StudyGroup, Double> yColumn;

    @FXML
    private TableColumn<StudyGroup, FormOfEducation> formColumn;

    @FXML
    private TableColumn<StudyGroup, Double> zzColumn;

    @FXML
    private TableColumn<StudyGroup, Long> expelledColumn;

    @FXML
    private TableColumn<StudyGroup, String> groupNameColumn;

    @FXML
    private TableColumn<StudyGroup, Double> xColumn;

    @FXML
    private TableColumn<StudyGroup, Long> xxColumn;

    @FXML
    private TableColumn<StudyGroup, LocalDateTime> timeColumn;

    @FXML
    private TableColumn<StudyGroup, Float> yyColumn;

    @FXML
    private TableColumn<StudyGroup, Integer> idColumn;

    @FXML
    private TableColumn<StudyGroup, String> userColumn;

    @FXML
    private Button sortButton;

    @FXML
    private Button filterButton;

    @FXML
    private Button backButton;

    @FXML
    private ChoiceBox<String> field;

    @FXML
    private TableView<StudyGroup> table;

    @FXML
    private TextField word;

    @FXML
    void sort(ActionEvent event) {
        ObservableList<StudyGroup> tableGroups = FXCollections.observableArrayList();
        if (field.getValue() == null) {
            Client.showWindow(200, 300, bundle.getString("selectField"), javafx.scene.paint.Color.BLACK);
        } else {
            table.getItems().removeAll(groups);
            String name = bundle.getString("name");
            String admin = bundle.getString("admin");
            String fField = field.getValue();
            if (fField.equals("id")) {
                tableGroups.addAll(groups.stream().sorted(Comparator.comparing(StudyGroup::getId)).collect(Collectors.toList()));
            }

            if (fField.equals("X")) {
                tableGroups.addAll(groups.stream().sorted(Comparator.comparing(StudyGroup::getX)).collect(Collectors.toList()));
            }

            if (fField.equals("Y")) {
                tableGroups.addAll(groups.stream().sorted(Comparator.comparing(t -> ((Double) t.getY()))).collect(Collectors.toList()));
            }

            if (fField.equals(name)) {
                tableGroups.addAll(groups.stream().sorted(Comparator.comparing(t -> ((String) t.getName()))).collect(Collectors.toList()));
            }

            if (fField.equals(admin)) {
                tableGroups.addAll(groups.stream().sorted(Comparator.comparing(t -> ((String) t.getAdmin()))).collect(Collectors.toList()));
            }

            if (fField.equals(bundle.getString("averageMark"))) {
                tableGroups.addAll(groups.stream().sorted(Comparator.comparing(t -> ((Integer) t.getAverageMark()))).collect(Collectors.toList()));
            }
            table.setItems(tableGroups);
        }
    }



    @FXML
    void filter(ActionEvent event) {
        String find = word.getText();
        String fField = field.getValue();
        ObservableList<StudyGroup> tableGroups = FXCollections.observableArrayList();
        //removeAllRows();
        if (fField == null) {
            Client.showWindow(200, 300, bundle.getString("selectField"), javafx.scene.paint.Color.BLACK);
        } else {
            table.getItems().removeAll(groups);
            String name = bundle.getString("name");
            String admin = bundle.getString("admin");
            if (fField.equals("id")) {
                tableGroups.addAll(groups.stream().filter(ticket -> String.valueOf(ticket.getId()).contains(find)).collect(Collectors.toList()));
            }

            if (fField.equals("X")) {
                tableGroups.addAll(groups.stream().filter(ticket -> String.valueOf(ticket.getX()).contains(find)).collect(Collectors.toList()));
            }

            if (fField.equals("Y")) {
                tableGroups.addAll(groups.stream().filter(ticket -> String.valueOf(ticket.getY()).contains(find)).collect(Collectors.toList()));
            }

            if (fField.equals(name)) {
                tableGroups.addAll(groups.stream().filter(ticket -> String.valueOf(ticket.getName()).contains(find)).collect(Collectors.toList()));
            }

            if (fField.equals(admin)) {
                tableGroups.addAll(groups.stream().filter(ticket -> String.valueOf(ticket.getGroupAdmin()).contains(find)).collect(Collectors.toList()));
            }

            if (fField.equals(bundle.getString("averageMark"))) {
                tableGroups.addAll(groups.stream().filter(ticket -> String.valueOf(ticket.getAverageMark()).contains(find)).collect(Collectors.toList()));
            }

            table.setItems(tableGroups);
        }
    }

    @FXML
    void update(ActionEvent event) {
        ObservableList<StudyGroup> selectedItems = table.getSelectionModel().getSelectedItems();
        for (StudyGroup group: selectedItems) {
            String arg = String.valueOf(group.getId());
            Response response = Client.execute(new Request("findId", arg, Client.getUser()));
            String ans = response.getResponseBody();
            if (ans.equals("true")) {
                GroupWindowController.setCommandName("update");
                GroupWindowController.setBundle(bundle);
                GroupWindowController.setPrevWindow("/clientModule/scenes/table.fxml");
                GroupWindowController.setArg(arg);
                GroupWindowController.setStudyGroup(group);
                GroupWindowController.setStage(Client.changeWindow("/clientModule/scenes/group.fxml", startStage, 550, 600));
            } else {
                //System.out.println(LanguageBundles.getCurrentBundle().getString("language"));
                ans = response.localize();
                //ans = response.getResponseBody();
                //System.out.println(ans);
                Client.showWindow(200, 500, ans, javafx.scene.paint.Color.BLACK);
            }
        }
    }

    @FXML
    void loadGroups() {
        ObservableList<StudyGroup> tableGroups = FXCollections.observableArrayList();
        groups = Client.getGroups();
        tableGroups.addAll(groups);
        table.setItems(tableGroups);
    }

    @FXML
    void back(ActionEvent event) {
        CommandsWindowController.setStartStage(Client.changeWindow("/clientModule/scenes/commands.fxml", startStage, 450, 530));
    }

    @FXML
    void initialize() {
        field.setItems(fields);
        initializeTable();
        loadGroups();
        tableLabel.setText(bundle.getString("table"));
        sortButton.setText(bundle.getString("sort"));
        updateButton.setText(bundle.getString("updateTheCell"));
        showButton.setText(bundle.getString("showAllGroups"));
        filterButton.setText(bundle.getString("filter"));
        backButton.setText(bundle.getString("back"));
    }

    private void initializeTable() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        groupNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        xColumn.setCellValueFactory(new PropertyValueFactory<>("X"));
        yColumn.setCellValueFactory(new PropertyValueFactory<>("Y"));
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("CreationDate"));
        countColumn.setCellValueFactory(new PropertyValueFactory<>("StudentsCount"));
        expelledColumn.setCellValueFactory(new PropertyValueFactory<>("ShouldBeExpelled"));
        markColumn.setCellValueFactory(new PropertyValueFactory<>("AverageMark"));
        formColumn.setCellValueFactory(new PropertyValueFactory<>("FormOfEducation"));
        personColumn.setCellValueFactory(new PropertyValueFactory<>("Admin"));
        passportColumn.setCellValueFactory(new PropertyValueFactory<>("Passport"));
        hairColumn.setCellValueFactory(new PropertyValueFactory<>("Color"));
        nationalityColumn.setCellValueFactory(new PropertyValueFactory<>("Nationality"));
        xxColumn.setCellValueFactory(new PropertyValueFactory<>("Xx"));
        yyColumn.setCellValueFactory(new PropertyValueFactory<>("Yy"));
        zzColumn.setCellValueFactory(new PropertyValueFactory<>("Zz"));
        locNameColumn.setCellValueFactory(new PropertyValueFactory<>("LocName"));
        userColumn.setCellValueFactory(new PropertyValueFactory<>("User"));
    }
}

