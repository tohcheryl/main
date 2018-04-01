package seedu.address.model.session;

import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;

/**
 *
 */
public interface SessionInterface {
    Session getActiveSession();

    boolean isUserInActiveSession();

    void createNewSession(Command command);

    CommandResult startSession() throws CommandException;
}
