package serverModule.commands;

import common.data.Person;
import common.exceptions.NonAuthorizedUserException;
import common.exceptions.WrongAmountOfArgumentsException;
import common.utility.User;
import serverModule.util.CollectionManager;
import serverModule.util.ResponseOutputer;

import java.util.Set;

public class PrintUniqueGroupAdminCommand extends Command{
    private CollectionManager collectionManager;

    public PrintUniqueGroupAdminCommand(CollectionManager collectionManager) {
        super("print_unique_group_admin", " вывести уникальные значения поля groupAdmin всех элементов в коллекции");
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
            ResponseOutputer.append("uniqueCommand:");
            for (Person person : collectionManager.uniqueGroupAdmin()) {
                ResponseOutputer.append(person.toString());
            }
            return true;
        } catch (WrongAmountOfArgumentsException exception) {
            ResponseOutputer.append("У этой команды нет параметров!\n");
        } catch (NonAuthorizedUserException e) {
            ResponseOutputer.append("Необходимо авторизоваться!\n");
        }
        return false;
    }
}
