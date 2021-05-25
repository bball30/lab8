package serverModule.commands;

import common.data.StudyGroup;
import common.exceptions.*;
import common.utility.User;
import serverModule.util.CollectionManager;
import serverModule.util.DatabaseCollectionManager;
import serverModule.util.ResponseOutputer;

/**
 *  'clear' command. Clears the collection.
 */
public class ClearCommand extends Command {
    private final CollectionManager collectionManager;
    private DatabaseCollectionManager databaseCollectionManager;

    public ClearCommand(CollectionManager collectionManager, DatabaseCollectionManager databaseCollectionManager) {
        super("clear", "очистить коллекцию");
        this.collectionManager = collectionManager;
        this.databaseCollectionManager = databaseCollectionManager;

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

            databaseCollectionManager.clearCollection();
            collectionManager.clearCollection();
            ResponseOutputer.append("clearOK");
            return true;
        } catch (WrongAmountOfArgumentsException exception) {
            ResponseOutputer.append("У этой команды нет параметров!\n");
        } catch (DatabaseManagerException exception) {
            ResponseOutputer.append("Произошла ошибка при обращении к базе данных!\n");
        } catch (NonAuthorizedUserException e) {
            ResponseOutputer.append("Необходимо авторизоваться!\n");
        }
        return false;
    }
}
