package seedu.address.model.util;

/**
 *
 */
public interface SessionInterface {
    Session getActiveSession();

    boolean isUserInActiveSession();
}
