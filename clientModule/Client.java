package clientModule;

import clientModule.util.AuthManager;
import clientModule.util.Console;
import common.data.StudyGroup;
import common.exceptions.IncorrectInputInScriptException;
import common.utility.Request;
import common.utility.Response;
import common.utility.ResponseCode;
import common.utility.User;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.List;

public class Client {
    private static String host;
    private static int port;
    private Console console;
    private AuthManager authManager;
    private static User user;
    private static String login;

    private static SocketChannel socketChannel;
    private static SocketAddress address;
    private static ByteBuffer byteBuffer = ByteBuffer.allocate(65536);

    public static String getLogin() {
        return login;
    }

    public static void setLogin(String login) {
        Client.login = login;
    }

    public static User getUser() {
        return user;
    }

    public static void setUser(User user) {
        Client.user = user;
    }

    public Client(String host, int port) {
        this.host = host;
        this.port = port;
    }

    private static void makeByteBufferToRequest(Request request) throws IOException {
        byteBuffer.clear();
        byteBuffer.put(serialize(request));
        byteBuffer.flip();
    }

    private static byte[] serialize(Request request) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(request);
        byte[] buffer = byteArrayOutputStream.toByteArray();
        objectOutputStream.flush();
        byteArrayOutputStream.flush();
        byteArrayOutputStream.close();
        objectOutputStream.close();
        return buffer;
    }

    private static Response deserialize() throws IOException, ClassNotFoundException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteBuffer.array());
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        Response response = (Response) objectInputStream.readObject();
        byteArrayInputStream.close();
        objectInputStream.close();
        byteBuffer.clear();
        return response;
    }

    public static Response execute(Request r) {
        Request requestToServer = r;
        Response serverResponse = null;
        try {
            //socketChannel = SocketChannel.open();
            address = new InetSocketAddress("localhost", port);
            makeByteBufferToRequest(requestToServer);
            socketChannel = SocketChannel.open();
            socketChannel.connect(address);
            socketChannel.write(byteBuffer);
            byteBuffer.clear();
            socketChannel.read(byteBuffer);
            byteBuffer.flip();
            serverResponse = deserialize();
            if (serverResponse.getResponseCode().equals(ResponseCode.OK) && (requestToServer.getCommandName().equals("sign_in") || requestToServer.getCommandName().equals("sign_up")))
                user = requestToServer.getUser();
            if (serverResponse.getResponseCode().equals(ResponseCode.OK) && requestToServer.getCommandName().equals("log_out"))
                user = null;
            socketChannel.close();
        } catch (IOException | ClassNotFoundException | ClassCastException exception) {
            showWindow(200, 400, "К сожалению, сервер не найден!\nДавайте подождем 5 секунд пока сервер оживет!", Color.BLACK);
            try {
                Thread.sleep(5 * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            execute(r);
        }
        return serverResponse;
    }

    public static List<StudyGroup> getGroups() {
        List<StudyGroup> resp = null;
        try {
            Request requestToServer = new Request("get", "", null, getUser());
            Response serverResponse;
            //socketChannel = SocketChannel.open();
            address = new InetSocketAddress("localhost", port);
            makeByteBufferToRequest(requestToServer);
            socketChannel = SocketChannel.open();
            socketChannel.connect(address);
            socketChannel.write(byteBuffer);
            byteBuffer.clear();
            socketChannel.read(byteBuffer);
            byteBuffer.flip();
            serverResponse = deserialize();
            resp = serverResponse.getList();
            if (serverResponse.getResponseCode().equals(ResponseCode.OK) && (requestToServer.getCommandName().equals("sign_in") || requestToServer.getCommandName().equals("sign_up")))
                user = requestToServer.getUser();
            if (serverResponse.getResponseCode().equals(ResponseCode.OK) && requestToServer.getCommandName().equals("log_out"))
                user = null;
            socketChannel.close();
            return resp;
        } catch (IOException | ClassNotFoundException | ClassCastException exception) {
            showWindow(200, 400, "К сожалению, сервер не найден!\nДавайте подождем 5 секунд пока сервер оживет!", Color.RED);
            try {
                Thread.sleep(5 * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            getGroups();
        }
        return resp;
    }

    public void run(){
        try {
            socketChannel = SocketChannel.open();
            address = new InetSocketAddress("localhost", this.port);
            //socketChannel.connect(address);
            Request requestToServer;
            Response serverResponse = null;
            do {
                requestToServer = serverResponse != null ? console.interactiveMode(serverResponse.getResponseCode(), user) :
                        console.interactiveMode(null, user);
                if (requestToServer.isEmpty()) continue;
                makeByteBufferToRequest(requestToServer);
                socketChannel = SocketChannel.open();
                socketChannel.connect(address);
                socketChannel.write(byteBuffer);
                byteBuffer.clear();
                socketChannel.read(byteBuffer);
                byteBuffer.flip();
                serverResponse = deserialize();
                if (serverResponse.getResponseCode().equals(ResponseCode.OK) && (requestToServer.getCommandName().equals("sign_in") || requestToServer.getCommandName().equals("sign_up")))
                    user = requestToServer.getUser();
                if (serverResponse.getResponseCode().equals(ResponseCode.OK) && requestToServer.getCommandName().equals("log_out"))
                    user = null;
                System.out.print(serverResponse.getResponseBody());
                socketChannel.close();
            } while(!requestToServer.getCommandName().equals("exit"));

        } catch (IOException | ClassNotFoundException | IncorrectInputInScriptException | ClassCastException exception) {
            System.out.println("К сожалению, сервер не найден!");
            System.out.println("Давайте подождем 5 секунд пока сервер оживет!");
            try {
                Thread.sleep(5 * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            run();
        }
    }

    public static void showWindow(double height, double width, String msg, Color color) {
        Label label = new Label(msg);
        label.setWrapText(true);
        label.setTextFill(color);
        label.setFont(new Font(20));
        BorderPane pane = new BorderPane(label);
        Scene scene = new Scene(pane);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setMinWidth(width);
        stage.setMinHeight(height);
        stage.setHeight(height);
        stage.setWidth(width);
        stage.show();
    }

    public static Stage changeWindow(String window, Stage startStage, double minHeight, double minWidth) {
        try {
            FXMLLoader root = new FXMLLoader();
            root.setLocation(Client.class.getResource(window));
            Parent xml = root.load();
            Scene scene = new Scene(xml);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setMinHeight(minHeight);
            stage.setMinWidth(minWidth);
            stage.setHeight(minHeight);
            stage.setWidth(minWidth);
            startStage.close();
            stage.show();
            return stage;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
