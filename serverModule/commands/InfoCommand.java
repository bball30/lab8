package serverModule.commands;

import java.time.LocalDateTime;

import common.exceptions.NonAuthorizedUserException;
import common.exceptions.WrongAmountOfArgumentsException;
import common.utility.User;
import serverModule.util.CollectionManager;
import serverModule.util.ResponseOutputer;

/**
 * 'info' command. Prints information about the collection.
 */
public class InfoCommand extends Command {
    private final CollectionManager collectionManager;

    public InfoCommand(CollectionManager collectionManager) {
        super("info", "вывести информацию о коллекции");
        this.collectionManager = collectionManager;
    }

    /**
     * Executes the command.
     * @return Command execute status.
     */
    @Override
    public boolean execute(String argument, Object objectArgument, User user) {
        try {
            if (user == null) throw new NonAuthorizedUserException();
            if (!argument.isEmpty() || objectArgument != null) throw new WrongAmountOfArgumentsException();
            LocalDateTime lastInitTime = collectionManager.getLastInitTime();
            String lastInitTimeString = (lastInitTime == null) ? "infoCommandText1" :
                    lastInitTime.toString();

            LocalDateTime lastSaveTime = collectionManager.getLastSaveTime();
            String lastSaveTimeString = (lastSaveTime == null) ? "\"infoCommandText2\"" :
                    lastSaveTime.toString();

            ResponseOutputer.append("infoCommandText3" + "\n");
            ResponseOutputer.append("infoCommandText4:" + collectionManager.collectionType() + "\n");
            ResponseOutputer.append("infoCommandText5:" + collectionManager.collectionSize() + "\n");
            ResponseOutputer.append("infoCommandText6:" + lastSaveTimeString + "\n");
            ResponseOutputer.append("infoCommandText7:" + lastInitTimeString + "\n");
            System.out.println(ResponseOutputer.getString());
            return true;
        } catch (WrongAmountOfArgumentsException exception) {
            ResponseOutputer.append("Использование: '" + getName() + "'\n");
        } catch (NonAuthorizedUserException e) {
            ResponseOutputer.append("Необходимо авторизоваться!\n");
        }
        return false;
    }
}