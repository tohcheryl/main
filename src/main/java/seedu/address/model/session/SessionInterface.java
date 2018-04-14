package seedu.address.model.session;

import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;

//@@author jaxony
/**
 * The API of the Session sub-component.
 */
public interface SessionInterface {
    /**
     * Checks if user is engaged in an active session.
     * @return boolean
     */
    boolean isUserInActiveSession();

    /**
     * Creates a new session with a command.
     * @param command Must support interactive mode.
     */
    void createNewSession(Command command);

    /**
     * Starts the new session.
     * @return feedback as a CommandResult
     * @throws CommandException
     */
    CommandResult startSession() throws CommandException;

    /**
     * Processes the user's text input.
     * @param userInput string input in the CommandBox
     * @return feedback as a CommandResult
     */
    CommandResult interpretUserInput(String userInput) throws CommandException;
}
