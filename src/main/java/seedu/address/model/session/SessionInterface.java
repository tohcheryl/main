package seedu.address.model.session;

import seedu.address.logic.commands.Command;

/**
 *
 */
public interface SessionInterface {
    Session getActiveSession();

    boolean isUserInActiveSession();

    void createNewSession(Command command);
}
