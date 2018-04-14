package seedu.address.model.session;

import java.util.ArrayList;
import java.util.List;

import com.google.common.eventbus.Subscribe;

import seedu.address.commons.core.ComponentManager;
import seedu.address.commons.events.model.EndActiveSessionEvent;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;

//@@author jaxony
/**
 * Manages sessions (interactions) between user and system for chat-like
 * experience.
 */
public class SessionManager extends ComponentManager implements SessionInterface {

    private final List<Session> sessionHistory;
    private Session activeSession;
    private boolean isUserInActiveSession;

    public SessionManager() {
        sessionHistory = new ArrayList<>();
        isUserInActiveSession = false;
        activeSession = null;
    }

    /**
     * Makes a new Session
     * @param command Must support interactive mode.
     */
    public void createNewSession(Command command) {
        assert command.getClass().getSimpleName().equals("AddCommand");
        activeSession = new SessionAddCommand(command, eventsCenter);
        isUserInActiveSession = true;
    }

    @Override
    public CommandResult startSession() throws CommandException {
        return activeSession.start();
    }

    @Override
    public CommandResult interpretUserInput(String userInput) throws CommandException {
        return activeSession.interpretUserInput(userInput);
    }

    @Subscribe
    public void handleEndActiveSessionEvent(EndActiveSessionEvent e) throws CommandException {
        endActiveSession();
    }

    /**
     * Ends the current active Session and saves it
     * to the session history.
     */
    private void endActiveSession() {
        // replace with a NoSessionException later
        if (activeSession == null) {
            return;
        }
        sessionHistory.add(activeSession);
        activeSession = null;
    }

    @Override
    public boolean isUserInActiveSession() {
        return activeSession != null;
    }
}
