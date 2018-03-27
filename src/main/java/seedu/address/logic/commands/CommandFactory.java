package seedu.address.logic.commands;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * Factory pattern for creating command objects
 */
public class CommandFactory {

    /**
     * CommandWord
     *
     * @param commandWord
     * @return
     */
    public static Command createCommand(String commandWord) throws NotImplementedException {
        switch (commandWord) {
        case AddCommand.COMMAND_WORD:
            return new AddCommand(null);
        default:
            throw new NotImplementedException();
        }
    }
}
