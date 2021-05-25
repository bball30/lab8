package clientModule.util;

import clientModule.Client;
import common.data.Coordinates;
import common.data.FormOfEducation;
import common.data.Person;
import common.exceptions.IncorrectInputInScriptException;
import common.exceptions.ScriptRecursionException;
import common.exceptions.WrongAmountOfParametersException;
import common.utility.Request;
import common.utility.ResponseCode;
import common.utility.StudyGroupLite;
import common.utility.User;
import javafx.scene.control.ScrollPane;
import javafx.scene.paint.Color;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.Stack;

/**
 * Operates command input.
 */
public class Console {
    private Scanner userScanner;
    private Stack<File> scriptFileNames = new Stack<>();
    private Stack<Scanner> scannerStack = new Stack<>();
    private StringBuilder stringBuilder;

    public Console(Scanner userScanner, File script, StringBuilder str) {
        this.userScanner = userScanner;
        this.scriptFileNames.add(script);
        stringBuilder = str;
    }

    private boolean fileMode() {
        return !scannerStack.isEmpty();
    }

    private String userInput;

    public void setUserInput(String userInput) {
        this.userInput = userInput;
    }

    /**
     * Mode for catching serverModule.commands from user input.
     */
    public Request interactiveMode(ResponseCode serverResponseCode, User user) throws IncorrectInputInScriptException {
        userInput = "";
        String[] userCommand = {"", ""};
        ProcessCode processCode = null;
        try {
            do {
                try {
                    if (serverResponseCode == ResponseCode.SERVER_EXIT || serverResponseCode == ResponseCode.ERROR) {
                        throw new IncorrectInputInScriptException();
                    }
                    while (!userScanner.hasNextLine()) {
                        userScanner.close();
                        userScanner = scannerStack.pop();
                         stringBuilder.append("Возвращаюсь из скрипта '" + scriptFileNames.pop().getName() + "'!");
                    }
                    userInput = userScanner.nextLine();
                    if (!userInput.isEmpty()) {
                        stringBuilder.append(userInput + "\n");
                    }
                    userCommand = (userInput.trim() + " ").split(" ", 2);
                    userCommand[1] = userCommand[1].trim();
                } catch (NoSuchElementException | IllegalStateException exception) {
                    stringBuilder.append("Произошла ошибка при вводе команды!\n");
                    userCommand = new String[]{"", ""};
                }
                processCode = checkCommand(userCommand[0], userCommand[1]);
            } while (processCode == ProcessCode.ERROR && !fileMode() || userCommand[0].isEmpty());
            try {
                switch (processCode) {
                    case OBJECT:
                        StudyGroupLite groupToInsert = generateGroupToInsert();
                        if (groupToInsert != null) return new Request(userCommand[0], userCommand[1], groupToInsert, user);
                    case UPDATE_OBJECT:
                        StudyGroupLite groupToUpdate = generateGroupToUpdate();
                        return new Request(userCommand[0], userCommand[1], groupToUpdate, user);
                    case SCRIPT:
                        File scriptFile = new File(userCommand[1]);
                        if (!scriptFile.exists()) throw new FileNotFoundException();
                        if (!scriptFileNames.isEmpty() && scriptFileNames.search(scriptFile) != -1) {
                            throw new ScriptRecursionException();
                        }
                        scannerStack.push(userScanner);
                        scriptFileNames.push(scriptFile);
                        userScanner = new Scanner(scriptFile);
                        stringBuilder.append("Выполняю скрипт '" + scriptFile.getName() + "'!");
                        break;
                }
            } catch (FileNotFoundException exception) {
                Client.showWindow(200, 400, "Файл со скриптом не найден!", Color.RED);
            } catch (ScriptRecursionException exception) {
                Client.showWindow(200, 400, "Скрипты не могут вызываться рекурсивно!", Color.RED);
                throw new IncorrectInputInScriptException();
            }
        } catch (IncorrectInputInScriptException exception) {
            Client.showWindow(200, 400, "Выполнение скрипта прервано!", Color.RED);
            while (!scannerStack.isEmpty()) {
                userScanner.close();
                userScanner = scannerStack.pop();
            }
        }
        return new Request(userCommand[0], userCommand[1], null, user);
    }

    /**
     * Launches the command.
     * @param command Command to launch.
     * @return Exit code.
     */
    private ProcessCode checkCommand(String command, String argument) {
        try {
            switch (command) {
                case "":
                    return ProcessCode.ERROR;
                case "help":
                case "info":
                case "show":
                case "clear":
                case "history":
                case "print_unique_group_admin":
                case "print_field_descending_should_be_expelled":
                case "exit":
                case "log_out":
                    if (!argument.isEmpty()) throw new WrongAmountOfParametersException();
                    return ProcessCode.OK;
                case "log_in":
                    if (!argument.isEmpty()) throw new WrongAmountOfParametersException();
                    return ProcessCode.LOG_IN;
                case "add":
                case "remove_greater":
                case "remove_lower":
                    if (!argument.isEmpty()) throw new WrongAmountOfParametersException();
                    return ProcessCode.OBJECT;
                case "remove_any_by_form_of_education":
                case "remove_by_id":
                    if (argument.isEmpty()) throw new WrongAmountOfParametersException();
                    return ProcessCode.OK;
                case "update":
                    if (argument.isEmpty()) throw new WrongAmountOfParametersException();
                    return ProcessCode.UPDATE_OBJECT;
                case "execute_script":
                    if (argument.isEmpty()) throw new WrongAmountOfParametersException();
                    return ProcessCode.SCRIPT;
                case "save":
                    System.out.println("Эта команда недоступна клиентам!");
                    return ProcessCode.ERROR;
                default:
                    System.out.println("Команда '" + command + "' не найдена. Наберите 'help' для справки.");
                    return ProcessCode.ERROR;
            }
        } catch (WrongAmountOfParametersException e) {
            System.out.println("Проверьте правильность ввода аргументов!");
        }
        return ProcessCode.OK;
    }

    private StudyGroupLite generateGroupToInsert() throws IncorrectInputInScriptException {
        Interactor builder = new Interactor(userScanner);
        if (fileMode()) {
            builder.setFileMode();
        } else {
            builder.setUserMode();
        }
        return new StudyGroupLite(
                builder.askGroupName(),
                builder.askCoordinates(),
                builder.askStudentsCount(),
                builder.askShouldBeExpelled(),
                builder.askAverageMark(),
                builder.askFormOfEducation(),
                builder.askGroupAdmin()
        );
    }

    private StudyGroupLite generateGroupToUpdate() throws IncorrectInputInScriptException{
        Interactor builder = new Interactor(userScanner);
        if (fileMode()) {
            builder.setFileMode();
        } else {
            builder.setUserMode();
        }
        String name = builder.askQuestion("Хотите изменить имя группы?") ?
                builder.askGroupName() : null;
        Coordinates coordinates = builder.askQuestion("Хотите изменить координаты группы?") ?
                builder.askCoordinates() : null;
        long studentsCount = builder.askQuestion("Хотите изменить количество студентов?") ?
                builder.askStudentsCount() : -1;
        long shouldBeExpelled  = builder.askQuestion("Хотите изменить количество студентов, которые должны быть отчислены?") ?
                builder.askShouldBeExpelled() : -1;
        int averageMark  = builder.askQuestion("Хотите изменить средний балл студентов?") ?
                builder.askAverageMark() : -1;
        FormOfEducation formOfEducation = builder.askQuestion("Хотите изменить форму обучения?") ?
                builder.askFormOfEducation() : null;
        Person groupAdmin  = builder.askQuestion("Хотите изменить админа группы?") ?
                builder.askGroupAdmin() : null;
        return new StudyGroupLite(
                name,
                coordinates,
                studentsCount,
                shouldBeExpelled,
                averageMark,
                formOfEducation,
                groupAdmin
        );
    }

    /**
     * Prints toOut.toString() to Console
     * @param toOut Object to print
     */
    public static void print(Object toOut) {
        System.out.print(toOut);
    }

    /**
     * Prints toOut.toString() + \n to Console
     * @param toOut Object to print
     */
    public static void println(Object toOut) {
        System.out.println(toOut);
    }

    /**
     * Prints error: toOut.toString() to Console
     * @param toOut Error to print
     */
    public static void printerror(Object toOut) {
        System.out.println("error: " + toOut);
    }

    /**
     * Prints formatted 2-element table to Console
     * @param element1 Left element of the row.
     * @param element2 Right element of the row.
     */
    public static void printtable(Object element1, Object element2) {
        System.out.printf("%-60s%-1s%n", element1, element2);
    }
}