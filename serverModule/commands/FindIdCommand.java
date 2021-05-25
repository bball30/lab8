package serverModule.commands;

import common.data.StudyGroup;
import common.exceptions.*;
import common.utility.User;
import serverModule.util.CollectionManager;
import serverModule.util.DatabaseCollectionManager;
import serverModule.util.ResponseOutputer;

public class FindIdCommand extends Command {
    private CollectionManager collectionManager;

    public FindIdCommand(CollectionManager collectionManager) {
        super("find_id <ID>", "проверить ID");
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
            if (argument.isEmpty() || objectArgument != null) throw new WrongAmountOfArgumentsException();
            if (collectionManager.collectionSize() == 0) throw new CollectionIsEmptyException();
            Integer id = Integer.parseInt(argument);
            StudyGroup studyGroup = collectionManager.getById(id);
            if (studyGroup == null) throw new StudyGroupNotFoundException();
            if (!studyGroup.getOwner().equals(user)) throw new PermissionDeniedException();
            ResponseOutputer.append("true");
            return true;
        } catch (WrongAmountOfArgumentsException exception) {
            ResponseOutputer.append("Использование: '" + getName() + "'\n");
        } catch (CollectionIsEmptyException exception) {
            ResponseOutputer.append("empty");
        } catch (NumberFormatException exception) {
            ResponseOutputer.append("incorrectID");
        } catch (StudyGroupNotFoundException exception) {
            ResponseOutputer.append("notFound");
        } catch (PermissionDeniedException exception) {
            ResponseOutputer.append("notYour");
        } catch (NonAuthorizedUserException e) {
            ResponseOutputer.append("Необходимо авторизоваться!\n");
        }
        return false;
    }
}
