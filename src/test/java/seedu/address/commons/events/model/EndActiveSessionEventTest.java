package seedu.address.commons.events.model;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;

import org.junit.Test;

import seedu.address.commons.core.EventsCenter;
import seedu.address.logic.commands.AddCommand;
import seedu.address.model.session.SessionInterface;
import seedu.address.model.session.SessionManager;

//@@author jaxony
public class EndActiveSessionEventTest {

    @Test
    public void endActiveSession_success() {
        SessionInterface sessionManager = new SessionManager();
        sessionManager.createNewSession(new AddCommand(null));
        assertTrue(sessionManager.isUserInActiveSession());
        EventsCenter.getInstance().post(new EndActiveSessionEvent());
        assertFalse(sessionManager.isUserInActiveSession());
    }
}
