package seedu.address.model.user.exceptions;

import seedu.address.commons.exceptions.DuplicateDataException;

/**
 * Signals that the operation will result in duplicate Food objects.
 */
public class DuplicateUserException extends DuplicateDataException {
    public DuplicateUserException() {
        super("Operation would result in no change to user profile");
    }
}
