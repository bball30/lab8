package clientModule.controllers;

import clientModule.Client;
import clientModule.localization.LanguageBundles;
import common.data.StudyGroup;
import common.utility.Request;
import common.utility.Response;
import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.*;
import java.util.stream.Collectors;

public class VisualizeWindowController {
    private static Stage startStage;

    private Thread thread;

    private static List<StudyGroup> groups;

    public static void setGroups(List<StudyGroup> groups) {
        VisualizeWindowController.groups = groups;
    }

    private static ResourceBundle bundle;

    public static void setBundle(ResourceBundle bundle) {
        LanguageBundles.setCurrentLanguage(bundle.getString("language"));
        VisualizeWindowController.bundle = bundle;

    }

    public static void setStartStage(Stage startStage) {
        VisualizeWindowController.startStage = startStage;
    }

    @FXML
    private GridPane field;

    @FXML
    public Button backButton;

    @FXML
    void back(ActionEvent event) {
        thread.interrupt();
        CommandsWindowController.setStartStage(Client.changeWindow("/clientModule/scenes/commands.fxml", startStage, 400, 530));
    }

    private boolean compare(List<StudyGroup> list1, List<StudyGroup> list2) {
        if (list1.size() != list2.size())
            return false;
        for (int i = 0; i < list1.size(); i++) {
            boolean check = false;
            for (int j = 0; j < list1.size(); j++) {
                if (list1.get(i).equals(list2.get(j))) {
                    check = true;
                    break;
                }
            }
            if (!check) return false;
        }
        return true;
    }

    @FXML
    public void initialize() {
        showGroups();
        backButton.setText(bundle.getString("back"));
        thread = new Thread(() -> {
            try {
                while (true) {
                    List<StudyGroup> list = Client.getGroups();
                    if (!compare(list, groups)) {
                        groups = list;
                        Platform.runLater(this::showGroups);
                    }
                    Thread.sleep(10000);
                }
            } catch(InterruptedException ignored) {
            }
        });
        thread.start();
    }

    public static String convertStringToHex(String str) {

        StringBuffer hex = new StringBuffer();

        // loop chars one by one
        for (char temp : str.toCharArray()) {

            // convert char to int, for char `a` decimal 97
            int decimal = (int) temp;

            // convert int to hex, for decimal 97 hex 61
            hex.append(Integer.toHexString(decimal));
        }

        return hex.toString();

    }

    private void showGroups() {
        field.getChildren().clear();
        List<ViewGroup> viewGroups = getPositions();

        for (int i = 0; i < viewGroups.size(); i++) {
            Rectangle rectangle = new Rectangle();
            rectangle.setX(20);
            rectangle.setY(10);
            rectangle.setWidth(50 + viewGroups.get(i).getSize() * 5);
            rectangle.setHeight(25 + viewGroups.get(i).getSize() * 5);

            String hex = convertStringToHex(viewGroups.get(i).getGroup().getOwner().getLogin());
            hex = (hex.length() > 6 ? hex.substring(0, 6) : hex);
            while (hex.length() < 6) {
                hex += "0";
            }

            Color color = Color.web("#" + hex);
            rectangle.setFill(color);
            Text text = new Text(viewGroups.get(i).getGroup().getName());
            text.setFill(Color.WHITE);

            int finalI = i;
            rectangle.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    String arg = "";
                    arg = String.valueOf(viewGroups.get(finalI).getGroup().getId());
                    Response response = Client.execute(new Request("findId", arg, null, Client.getUser()));
                    String ans = response.getResponseBody();
                    if (ans.equals("true")) {
                        GroupWindowController.setCommandName("update");
                        GroupWindowController.setBundle(bundle);
                        GroupWindowController.setPrevWindow("/clientModule/scenes/visualize.fxml");
                        GroupWindowController.setArg(arg);
                        GroupWindowController.setStudyGroup(viewGroups.get(finalI).getGroup());
                        GroupWindowController.setStage(Client.changeWindow("/clientModule/scenes/group.fxml", startStage, 400, 500));
                    } else {
                        ans = response.localize();
                        Client.showWindow(200, 500, ans, Color.BLACK);
                    }
                }
            });
            ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(1000));
            scaleTransition.setNode(rectangle);
            //scaleTransition.setNode(text);
            scaleTransition.setCycleCount(50);
            scaleTransition.setByX(0.25);
            scaleTransition.setByY(0.25);
            scaleTransition.setAutoReverse(true);
            scaleTransition.play();
            StackPane pane = new StackPane();
            pane.getChildren().addAll(rectangle, text);
            field.add(pane, viewGroups.get(i).getX(), viewGroups.get(i).getY());
        }
    }

    private List<ViewGroup> getPositions() {
        List<StudyGroup> sortX = groups.stream().sorted((t1, t2) -> Double.compare(t1.getX(), t2.getX())).collect(Collectors.toList());
        List<StudyGroup> sortY = groups.stream().sorted(Comparator.comparingDouble(StudyGroup::getY)).collect(Collectors.toList());
        List<ViewGroup> view = new ArrayList<>();
        Set<Long> sizeSet = new TreeSet<>();

        for (int i = 0; i < groups.size(); i++) {
            StudyGroup group = groups.get(i);
            view.add(new ViewGroup(find(sortX, group), find(sortY, group), group, group.getStudentsCount()));
            sizeSet.add(group.getStudentsCount());
        }

        List<Long> sizeList = new ArrayList<>(sizeSet);
        for (int i = 0; i < view.size(); i++) {
            ViewGroup group = view.get(i);
            for(int j = 0; j < sizeList.size(); j++) {
                if (group.getSize() == sizeList.get(j)) {
                    group.setSize(j+1);
                }
            }
        }
        return view;
    }

    private int find(List<StudyGroup> tickets, StudyGroup ticket) {
        for (int i = 0; i < tickets.size(); i++) {
            if (tickets.get(i).equals(ticket)) return i;
        }
        return 0;
    }

    class ViewGroup {
        int x;
        int y;
        long size;
        StudyGroup group;

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public long getSize() {
            return size;
        }

        public void setSize(long size) {
            this.size = size;
        }

        public ViewGroup(int x, int y, StudyGroup group) {
            this.x = x;
            this.y = y;
            this.group = group;
        }

        public ViewGroup(long s) {
            size = s;
        }

        public ViewGroup(int x, int y, StudyGroup group, long size) {
            this.x = x;
            this.y = y;
            this.group = group;
            this.size = size;
        }

        public StudyGroup getGroup() {
            return group;
        }
    }

    private List<ViewGroup> getSize() {
        List<ViewGroup> view = new ArrayList<>();
        List<StudyGroup> sortSize = groups.stream().sorted(Comparator.comparingLong(StudyGroup::getStudentsCount)).collect(Collectors.toList());
        long size = 1;
        view.add(new ViewGroup(1));
        for (int i = 1; i < sortSize.size(); i++) {
            if (groups.get(i).getStudentsCount() == groups.get(i-1).getStudentsCount()) {
                view.add(new ViewGroup(size));
            }
            view.add(new ViewGroup(size + 1));
            size += 1;
        }
        return view;
    }
}
