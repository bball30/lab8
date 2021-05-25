package clientModule.controllers;

import clientModule.Client;
import clientModule.localization.LanguageBundles;
import common.data.*;
import common.exceptions.GroupExeption;
import common.exceptions.StudyGroupNotFoundException;
import common.utility.Request;
import common.utility.Response;
import common.utility.StudyGroupLite;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class GroupWindowController {
    private final int MIN_PASSPORT_ID_LENGTH = 7;
    private final int MAX_PASSPORT_ID_LENGTH = 39;
    private final long MIN_STUDENTS_COUNT = 0;
    private final int MIN_SHOULD_BE_EXPELLED = 0;
    private final int MIN_AVERAGE_MARK = 0;
    public Button enter;
    public Button back;
    public Label group;
    public Label coordLabell;
    public Label locName;
    public Label loc;
    public Label nation;
    public Label color;
    public Label nomPassport;
    public Label adminN;
    public Label admin;
    public Label form;
    public Label avMark;
    public Label stExp;
    public Label stCount;
    public Label coordLabel;
    public Label nameLabel;


    private ObservableList<String> formsOfEducation = FXCollections.observableArrayList("DISTANCE_EDUCATION", "FULL_TIME_EDUCATION", "EVENING_CLASSES");
    private ObservableList<String> colorBoxValues = FXCollections.observableArrayList("GREEN", "BLACK", "ORANGE", "BROWN");
    private ObservableList<String> nationality = FXCollections.observableArrayList("USA", "FRANCE", "SOUTH_KOREA", "NORTH_KOREA", "JAPAN");

    private static Stage startStage;

    public static void setPrevWindow(String prevWindow) {
        GroupWindowController.prevWindow = prevWindow;
    }

    private static ResourceBundle bundle;

    public static void setBundle(ResourceBundle bundle) {
        LanguageBundles.setCurrentLanguage(bundle.getString("language"));
        GroupWindowController.bundle = bundle;
    }

    private static String prevWindow;
    private static String commandName;
    private static String arg = "";
    private static List<StudyGroup> studyGroups;

    public static void setStage(Stage startStage) {
        GroupWindowController.startStage = startStage;
    }

    private static StudyGroup studyGroup;

    public static void setStudyGroup(StudyGroup studyGroup) {
        GroupWindowController.studyGroup = studyGroup;
    }

    public static void setCommandName(String name) {
        commandName = name;
    }

    public static String getCommandName() {
        return commandName;
    }

    public static String getArg() {
        return arg;
    }

    public static void setArg(String arg) {
        GroupWindowController.arg = arg;
    }

    FormOfEducation checkFormOfEducation(String data) throws GroupExeption {
        if (data == null) {
            throw new GroupExeption("noForm");
        }
        else switch (data) {
            case "DISTANCE_EDUCATION":
                return FormOfEducation.DISTANCE_EDUCATION;
            case "FULL_TIME_EDUCATION":
                return FormOfEducation.FULL_TIME_EDUCATION;
            case "EVENING_CLASSES":
                return FormOfEducation.EVENING_CLASSES;
            default:
                throw new GroupExeption("noForm");
        }
    }

    long checkStudentsCount(String data) throws GroupExeption {
        if (data.equals("")) {
            throw new GroupExeption("noCount");
        } else if (Long.parseLong(data) <= MIN_STUDENTS_COUNT) {
            throw new GroupExeption("noCount");
        }
        return Long.parseLong(data);
    }

    long checkShouldBeExpelled(String data) throws GroupExeption {
        if (data.equals("")) {
            throw new GroupExeption("noExp");
        } else if (Long.parseLong(data) <= MIN_SHOULD_BE_EXPELLED) throw new GroupExeption("noExp");
        return Long.parseLong(data);
    }

    int checkAverageMark(String data) throws GroupExeption {
        if (data.equals("")) {
            throw new GroupExeption("noMark");
        } else if (Integer.parseInt(data) <= MIN_AVERAGE_MARK) throw new GroupExeption("noMark");
        return Integer.parseInt(data);
    }

    String checkName(String data) throws GroupExeption {
        if (data.isEmpty()){
            throw new GroupExeption("noName");
        }
        return data;
    }

    Double checkY(String data) throws GroupExeption {
        if (data.isEmpty()) {
            throw new GroupExeption("noY");
        }
        return Double.parseDouble(data);
    }

    String checkPassport(String data) throws GroupExeption {
        if (data.equals("")) {
            throw new GroupExeption("noPass");
        } else if (data.length() <= MIN_PASSPORT_ID_LENGTH || data.length() >= MAX_PASSPORT_ID_LENGTH)
            throw new GroupExeption("noPass");
        return data;
    }

    @FXML
    private TextField xx;

    @FXML
    private TextField yy;

    @FXML
    private TextField zz;

    @FXML
    private TextField locationName;

    @FXML
    private TextField studentsShouldBeExpelled;

    @FXML
    private ChoiceBox<String> colorBox;


    @FXML
    private ChoiceBox<String> formOfEducation;

    @FXML
    private ChoiceBox<String> Nationality;

    @FXML
    private TextField adminName;

    @FXML
    private TextField studentsCount;

    @FXML
    private TextField averageMark;

    @FXML
    private TextField name;

    @FXML
    private TextField x;

    @FXML
    private TextField y;

    @FXML
    private TextField passportID;

    String getName(ActionEvent event) throws GroupExeption {
        String groupName = name.getText();
        return checkName(groupName);
    }

    double getX(ActionEvent event) {
        return Double.parseDouble(x.getText());
    }

    Double getY(ActionEvent event) throws GroupExeption {
        return checkY(y.getText());
    }

    long getStudentsCount(ActionEvent event) throws GroupExeption {
        return checkStudentsCount(studentsCount.getText());
    }

    long getShouldBeExpelled(ActionEvent event) throws GroupExeption {
        return checkShouldBeExpelled(studentsShouldBeExpelled.getText());
    }

    int getAverageMark(ActionEvent event) throws GroupExeption {
        return checkAverageMark(averageMark.getText());
    }

    FormOfEducation getFormOfEducation(ActionEvent event) throws GroupExeption {
        return checkFormOfEducation(formOfEducation.getValue());
    }

    String getAdminName(ActionEvent event) throws GroupExeption {
        return checkName(adminName.getText());
    }

    String getPassportID(ActionEvent event) throws GroupExeption {
        return checkPassport(passportID.getText());
    }

    Color getColor(ActionEvent event) {
        return Color.valueOf(colorBox.getValue());
    }

    Country getNationality(ActionEvent event) {
        return Country.valueOf(Nationality.getValue());
    }

    String getLocationName(ActionEvent event) {
        return locationName.getText();
    }

    Long getXX(ActionEvent event) {
        return Long.parseLong(xx.getText());
    }

    Float getYY(ActionEvent event) {
        return Float.parseFloat(yy.getText());
    }

    double getZZ(ActionEvent event) {
        return Double.parseDouble(zz.getText());
    }

    @FXML
    void initialize() {
        formOfEducation.setItems(formsOfEducation);
        colorBox.setItems(colorBoxValues);
        Nationality.setItems(nationality);
        setLanguage();
        if (studyGroup != null) {
            name.setText(studyGroup.getName());
            x.setText(String.valueOf(studyGroup.getCoordinates().getX()));
            y.setText(String.valueOf(studyGroup.getCoordinates().getY()));
            studentsCount.setText(String.valueOf(studyGroup.getStudentsCount()));
            studentsShouldBeExpelled.setText(String.valueOf(studyGroup.getShouldBeExpelled()));
            formOfEducation.setValue(String.valueOf(studyGroup.getFormOfEducation()));
            averageMark.setText(String.valueOf(studyGroup.getAverageMark()));
            adminName.setText(String.valueOf(studyGroup.getGroupAdmin().getName()));
            passportID.setText(String.valueOf(studyGroup.getGroupAdmin().getPassportID()));
            colorBox.setValue(String.valueOf(studyGroup.getGroupAdmin().getHairColor()));
            Nationality.setValue(String.valueOf(studyGroup.getGroupAdmin().getNationality()));
            locationName.setText(String.valueOf(studyGroup.getGroupAdmin().getLocation().getName()));
            xx.setText(String.valueOf(studyGroup.getGroupAdmin().getLocation().getX()));
            yy.setText(String.valueOf(studyGroup.getGroupAdmin().getLocation().getY()));
            zz.setText(String.valueOf(studyGroup.getGroupAdmin().getLocation().getZ()));
        }
    }

    private void setLanguage() {
        nameLabel.setText(bundle.getString("nameGroup"));
        coordLabel.setText(bundle.getString("coordinates"));
        coordLabell.setText(bundle.getString("coordinates"));
        group.setText(bundle.getString("group"));
        locName.setText(bundle.getString("locName"));
        loc.setText(bundle.getString("location"));
        nation.setText(bundle.getString("nationality"));
        color.setText(bundle.getString("color"));
        nomPassport.setText(bundle.getString("numberPassport"));
        adminN.setText(bundle.getString("name"));
        admin.setText(bundle.getString("admin"));
        form.setText(bundle.getString("formOfEducation"));
        avMark.setText(bundle.getString("averageMark"));
        stExp.setText(bundle.getString("dolgniki"));
        stCount.setText(bundle.getString("studentsCount"));
        back.setText(bundle.getString("back"));
        enter.setText(bundle.getString("enter"));
    }

    @FXML
    void enter(ActionEvent event) {
        StringBuilder errors = new StringBuilder();
        String name = null;
        double x = 0;
        Double y = 0d;
        long studentsCount = 0;
        long shouldBeExpelled = 0;
        int averageMark = 0;
        FormOfEducation formOfEducation = null;
        Person admin = null;
        String adminName = null;
        String adminPassword = null;
        Color color = null;
        Country nationality = null;
        Location location = null;
        Long xx = 0l;
        Float yy = 0f;
        double zz = 0;
        String locationName = null;

        try {
            name = getName(event);
        } catch (GroupExeption e) {
            errors.append(bundle.getString("eName") + "\n");
        }

        try {
            x = getX(event);
        } catch (NumberFormatException e) {
            errors.append(bundle.getString("eX") + "\n");
        }

        try {
            y = getY(event);
        } catch (NumberFormatException | GroupExeption e) {
            errors.append(bundle.getString("eY") + "\n");
        }

        try {
            studentsCount = getStudentsCount(event);
        } catch (GroupExeption e) {
            errors.append(bundle.getString("eCount") + "\n");
        }

        try {
            shouldBeExpelled = getShouldBeExpelled(event);
        } catch (GroupExeption e) {
            errors.append(bundle.getString("eExp") + "\n");
        }

        try {
            averageMark = getAverageMark(event);
        } catch (GroupExeption e) {
            errors.append(bundle.getString("eMark") + "\n");
        }

        try {
            formOfEducation = getFormOfEducation(event);
        } catch (GroupExeption e) {
            errors.append(bundle.getString("eForm") + "\n");
        }

        try {
            adminName = getAdminName(event);
        } catch (GroupExeption e) {
            errors.append(bundle.getString("eName") + "\n");
        }

        try {
            adminPassword = getPassportID(event);
        } catch (GroupExeption e) {
            errors.append(bundle.getString("ePass") + "\n");
        }

        try {
            color = getColor(event);
        } catch (Exception e) {
            errors.append(bundle.getString("eCol") + "\n");
        }

        try {
            nationality = getNationality(event);
        } catch (Exception e){
            errors.append(bundle.getString("eNat") + "\n");
        }

        try {
            locationName = getLocationName(event);
        } catch (Exception e) {
            errors.append(bundle.getString("eName") + "\n");
        }

        try {
            xx = getXX(event);
        } catch (Exception e) {
            errors.append(bundle.getString("eXx") + "\n");
        }

        try {
            yy = getYY(event);
        } catch (Exception e) {
            errors.append(bundle.getString("eYy") + "\n");
        }

        try {
            zz = getZZ(event);
        } catch (Exception e) {
            errors.append(bundle.getString("eZz") + "\n");
        }

        location = new Location(xx, yy, zz, locationName);
        admin = new Person(adminName, adminPassword, color, nationality, location);
        Request request;
        Response response;
        if (errors.length() == 0) {
            StudyGroupLite group = new StudyGroupLite(name, new Coordinates(x, y), studentsCount, shouldBeExpelled, averageMark, formOfEducation, admin);
            if (commandName.equals("remove_lower") || commandName.equals("remove_greater")) {
                request = new Request(commandName, "", group, Client.getUser());
            } else {
                request = new Request(commandName, arg, group, Client.getUser());
            }
        } else {
            Client.showWindow(400, 650, errors.toString(), javafx.scene.paint.Color.RED);
            return;
        }
        response = Client.execute(request);
        arg = "";
        if (prevWindow.contains("command")) {
            CommandsWindowController.setStartStage(Client.changeWindow(prevWindow, startStage, 400, 530));
        } else {
            if (prevWindow.contains("table")) {
                TableWindowController.setGroups(Client.getGroups());
                TableWindowController.setStage(Client.changeWindow(prevWindow, startStage, 500, 1000));
            } else {
            VisualizeWindowController.setGroups(Client.getGroups());
            VisualizeWindowController.setStartStage(Client.changeWindow(prevWindow, startStage, 500, 800));
        }
        }
        String ans = response.localize();
        Client.showWindow(200, 400, ans, javafx.scene.paint.Color.BLACK);
    }

    @FXML
    void back(ActionEvent event) {
        arg = "";
        if (prevWindow.contains("command")) {
            CommandsWindowController.setStartStage(Client.changeWindow(prevWindow, startStage, 400, 530));
        } else {
            if (prevWindow.contains("table")) {
                TableWindowController.setGroups(Client.getGroups());
                TableWindowController.setStage(Client.changeWindow(prevWindow, startStage, 500, 1000));
            } else {
                VisualizeWindowController.setGroups(Client.getGroups());
                VisualizeWindowController.setStartStage(Client.changeWindow(prevWindow, startStage, 500, 800));
            }
        }
    }
}

