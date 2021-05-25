package clientModule.util;

import clientModule.util.Console;
import common.data.*;
import common.exceptions.IncorrectInputInScriptException;
import common.exceptions.MustBeNotEmptyException;
import common.exceptions.NotInBoundsException;



import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * Asks a user a studyGroup's value.
 */
public class Interactor {
    private final int MIN_PASSPORT_ID_LENGTH = 7;
    private final int MAX_PASSPORT_ID_LENGTH = 39;
    private final int MIN_STUDENTS_COUNT = 0;
    private final int MIN_SHOULD_BE_EXPELLED = 0;
    private final int MIN_AVERAGE_MARK = 0;
    private Pattern patternNumber = Pattern.compile("-?\\d+(\\.\\d+)?");

    private Scanner userScanner;
    private boolean fileMode;

    public Interactor(Scanner userScanner) {
        this.userScanner = userScanner;
        fileMode = true;
    }

    /**
     * Sets a scanner to scan user input.
     *
     * @param userScanner Scanner to set.
     */
    public void setUserScanner(Scanner userScanner) {
        this.userScanner = userScanner;
    }

    /**
     * @return Scanner, which uses for user input.
     */
    public Scanner getUserScanner() {
        return userScanner;
    }

    /**
     * Sets studyGroup asker mode to 'File Mode'.
     */
    public void setFileMode() {
        fileMode = true;
    }

    /**
     * Sets studyGroup asker mode to 'User Mode'.
     */
    public void setUserMode() {
        fileMode = false;
    }

    /**
     * Asks a user the studyGroup's name.
     *
     * @return name
     */
    public String askName() {
        String name = userScanner.nextLine().trim();;
        if (name.isEmpty()) name = null;
        return name;
    }


    /**
     * Asks a user the studyGroup's X coordinate.
     *
     * @return studyGroup's X coordinate.
     * @throws IncorrectInputInScriptException If script is running and something goes wrong.
     */
    public double askX() throws IncorrectInputInScriptException {
        String strX = userScanner.nextLine().trim();
        return Double.parseDouble(strX);
    }

    /**
     * Asks a user the studyGroup's Y coordinate.
     *
     * @return StudyGroup's Y coordinate.
     * @throws IncorrectInputInScriptException If script is running and something goes wrong.
     */
    public Double askY() throws IncorrectInputInScriptException {
        String strY = userScanner.nextLine().trim();
        return Double.parseDouble(strY);
    }

    /**
     * Asks a user the studyGroup's Z coordinate.
     *
     * @return StudyGroup's Z coordinate.
     * @throws IncorrectInputInScriptException If script is running and something goes wrong.
     */
    public Double askZ() throws IncorrectInputInScriptException {
        String strZ = userScanner.nextLine().trim();
        return Double.parseDouble(strZ);
    }

    /**
     * Asks a user the studyGroup's coordinates.
     *
     * @return StudyGroup's coordinates.
     * @throws IncorrectInputInScriptException If script is running and something goes wrong.
     */
    public Coordinates askCoordinates() throws IncorrectInputInScriptException {
        double x = askX();
        Double y = askY();
        return new Coordinates(x, y);
    }

    /**
     * Asks a user the studyGroup's form of education
     *
     * @return StudyGroup's form of education
     * @throws IncorrectInputInScriptException if script is running and something goes wrong.
     */
    public FormOfEducation askFormOfEducation() throws IncorrectInputInScriptException {
        String strFormOfEducation;
        strFormOfEducation = userScanner.nextLine().trim();
        FormOfEducation formOfEducation;
        formOfEducation = FormOfEducation.valueOf(strFormOfEducation.toUpperCase());
        return formOfEducation;
    }

    /**
     * Asks a user the studyGroup's student count
     *
     * @return StudyGroup's students count
     * @throws IncorrectInputInScriptException if script is running and something goes wrong.
     */
    public long askStudentsCount() throws IncorrectInputInScriptException {
        String strStudentsCount = userScanner.nextLine().trim();
        return Long.parseLong(strStudentsCount);
    }

    /**
     * Asks a user the studyGroup's average mark
     *
     * @return StudyGroup's average mark
     * @throws IncorrectInputInScriptException if script is running and something goes wrong.
     */
    public int askAverageMark() throws IncorrectInputInScriptException {
        String strAverageMark = userScanner.nextLine().trim();
        
        return Integer.parseInt(strAverageMark);
    }

    /**
     * Asks a user the studyGroup's admin
     *
     * @return Person [Admin]
     * @throws IncorrectInputInScriptException if script is running and something goes wrong.
     */
    public Person askGroupAdmin() throws IncorrectInputInScriptException {
        String name = askAdminName();
        String passportID = askPassportID();
        Color hairColor = askHairColor();
        Country country = askCountry();
        Location location = askLocation();
        return new Person(name, passportID, hairColor, country, location);
    }

    /**
     * Asks a user the studyGroup's count of should be expelled students
     *
     * @return StudyGroup's count of should be expelled students
     * @throws IncorrectInputInScriptException if script is running and something goes wrong.
     */
    public long askShouldBeExpelled() throws IncorrectInputInScriptException {
        String strShouldBeExpelled = userScanner.nextLine().trim();
        return Long.parseLong(strShouldBeExpelled);
    }

    /**
     * Asks a user the admin's name
     *
     * @return Person's name
     * @throws IncorrectInputInScriptException if script is running and something goes wrong.
     */
    public String askAdminName() throws IncorrectInputInScriptException {
        return userScanner.nextLine().trim();
    }

    /**
     * Asks a user the location's name
     *
     * @return Location's name
     * @throws IncorrectInputInScriptException if script is running and something goes wrong.
     */
    public String askLocationName() throws IncorrectInputInScriptException {
        return userScanner.nextLine().trim();
    }

    /**
     * Asks a user the studyGroup's name
     *
     * @return StudyGroup's name
     * @throws IncorrectInputInScriptException if script is running and something goes wrong.
     */
    public String askGroupName() throws IncorrectInputInScriptException {
        return userScanner.nextLine().trim();
    }

    /**
     * Asks a user the admin's passport ID
     *
     * @return Person's passportID
     * @throws IncorrectInputInScriptException if script is running and something goes wrong.
     */
    public String askPassportID() throws IncorrectInputInScriptException {
        return userScanner.nextLine().trim();
    }

    /**
     * converts Double to Float
     *
     * @param f Double value
     * @return Float value
     */
    public static Float convertToFloat(Double f) {
        return f == null ? null : f.floatValue();
    }

    /**
     * Asks a user the admin's location
     *
     * @return Person's location
     * @throws IncorrectInputInScriptException if script is running and something goes wrong.
     */
    public Location askLocation() throws IncorrectInputInScriptException {
        Long x = (long) askX();
        Float y = convertToFloat(askY());
        double z = askZ();
        String name = askLocationName();
        return new Location(x, y, z, name);
    }

    /**
     * Asks a user the admin's hairColor
     *
     * @return Person's hairColor
     * @throws IncorrectInputInScriptException if script is running and something goes wrong.
     */
    public Color askHairColor() throws IncorrectInputInScriptException {
        String strColor = userScanner.nextLine().trim();
        return Color.valueOf(strColor.toUpperCase());
    }

    /**
     * Asks a user the admin's country
     *
     * @return Person's country
     * @throws IncorrectInputInScriptException if script is running and something goes wrong.
     */
    public Country askCountry() throws IncorrectInputInScriptException {
        String strCountry = userScanner.nextLine().trim();
        Country country = Country.valueOf(strCountry.toUpperCase());
        return country;
    }

    /**
     * Asks a user a question.
     *
     * @param question A question.
     * @return Answer (true/false).
     * @throws IncorrectInputInScriptException If script is running and something goes wrong.
     */
    public boolean askQuestion(String question) throws IncorrectInputInScriptException {
        String finalQuestion = question + " (+/-):";
        String answer;
        while (true) {
            try {
                Console.println(finalQuestion);
                Console.print(">");
                answer = userScanner.nextLine().trim();
                if (fileMode) Console.println(answer);
                if (!answer.equals("+") && !answer.equals("-")) throw new NotInBoundsException();
                break;
            } catch (NoSuchElementException exception) {
                Console.printerror("Ответ не распознан!");
                if (fileMode) throw new IncorrectInputInScriptException();
            } catch (NotInBoundsException exception) {
                Console.printerror("Ответ должен быть представлен знаками '+' или '-'!");
                if (fileMode) throw new IncorrectInputInScriptException();
            } catch (IllegalStateException exception) {
                Console.printerror("Непредвиденная ошибка!");
                System.exit(0);
            }
        }
        return answer.equals("+");
    }
}