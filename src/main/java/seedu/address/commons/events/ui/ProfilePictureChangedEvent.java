package seedu.address.commons.events.ui;

import seedu.address.commons.events.BaseEvent;

//@@author tohcheryl
/**
 * Indicates profile picture of user has changed
 */
public class ProfilePictureChangedEvent extends BaseEvent {

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
