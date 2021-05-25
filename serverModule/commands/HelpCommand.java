package serverModule.commands;

import common.exceptions.WrongAmountOfArgumentsException;
import common.utility.User;
import serverModule.util.ResponseOutputer;

/**
 * 'help' command. Just for logical structure. Does nothing.
 */
public class HelpCommand extends Command{
    public HelpCommand(){
        super("help", "вывести справку по доступным командам");
    }

    /**
     * Executes the command.
     * @return Command execute status.
     */
    @Override
    public boolean execute(String argument, Object objectArgument, User user) {
        try {
            if (!argument.isEmpty() || objectArgument != null) throw new WrongAmountOfArgumentsException();
            return true;
        } catch (WrongAmountOfArgumentsException exception) {
            ResponseOutputer.append("Использование: '" + getName() + "'\n");
        }
        return false;
    }
}
