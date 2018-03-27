package seedu.address.model.session;

import java.util.ArrayList;
import java.util.List;

import seedu.address.commons.core.ComponentManager;
import seedu.address.logic.commands.Command;

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
     * Makes a new Session.
     */
    public void createNewSession(Command command) {
        activeSession = new Session(command);
        isUserInActiveSession = true;
    }

    /**
     * Ends the current active Session and saves it
     * to the session history.
     */
    public void endActiveSession() {
        // replace with a NoSessionException later
        if (activeSession == null) {
            return;
        }
        sessionHistory.add(activeSession);
        activeSession = null;
    }

    @Override
    public Session getActiveSession() {
        return activeSession;
    }

    @Override
    public boolean isUserInActiveSession() {
        return activeSession != null;
    }
}
