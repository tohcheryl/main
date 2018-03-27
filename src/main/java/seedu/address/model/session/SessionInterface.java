package seedu.address.model.session;

/**
 *
 */
public interface SessionInterface {
    Session getActiveSession();

    boolean isUserInActiveSession();
}
