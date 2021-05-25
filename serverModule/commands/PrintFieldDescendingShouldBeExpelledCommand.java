package serverModule.commands;

import common.exceptions.NonAuthorizedUserException;
import common.exceptions.WrongAmountOfArgumentsException;
import common.utility.User;
import serverModule.util.CollectionManager;
import serverModule.util.ResponseOutputer;

public class PrintFieldDescendingShouldBeExpelledCommand extends Command {
    private CollectionManager collectionManager;

    public PrintFieldDescendingShouldBeExpelledCommand(CollectionManager collectionManager) {
        super("print_field_descending_should_be_expelled", " вывести значения поля shouldBeExpelled всех элементов в порядке убывания");
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
            ResponseOutputer.append("shouldCommand:" + collectionManager.sortedByShouldBeExpelled());
            return true;
        } catch (WrongAmountOfArgumentsException exception) {
            ResponseOutputer.append("У этой команды нет параметров!\n");
        } catch (NonAuthorizedUserException e) {
            ResponseOutputer.append("Необходимо авторизоваться!\n");
        }
        return false;
    }
}
