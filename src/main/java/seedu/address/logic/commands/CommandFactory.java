package seedu.address.logic.commands;

//@@author jaxony
/**
 * Factory pattern for creating command objects
 */
public class CommandFactory {

    /**
     * Creates a Command given a command word.
     * @param commandWord Word for a command.
     * @return A new Command object.
     * @throws IllegalArgumentException If the command word is not supported.
     */
    public static Command createCommand(String commandWord) throws IllegalArgumentException {
        switch (commandWord) {
        case AddCommand.COMMAND_WORD:
            return new AddCommand(null);
        default:
            throw new IllegalArgumentException();
        }
    }
}
