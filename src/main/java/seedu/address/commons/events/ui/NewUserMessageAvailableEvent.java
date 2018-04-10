package seedu.address.commons.events.ui;

import seedu.address.commons.events.BaseEvent;

/**
 * Indicates that a new user message is available.
 */
public class NewUserMessageAvailableEvent extends BaseEvent {

    public final String message;

    public NewUserMessageAvailableEvent(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }

}
