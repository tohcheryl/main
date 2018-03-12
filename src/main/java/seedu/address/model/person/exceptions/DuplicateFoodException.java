package seedu.address.model.person.exceptions;

import seedu.address.commons.exceptions.DuplicateDataException;

/**
 * Signals that the operation will result in duplicate Food objects.
 */
public class DuplicateFoodException extends DuplicateDataException {
    public DuplicateFoodException() {
        super("Operation would result in duplicate foods");
    }
}
