package seedu.address.commons.events.model;

import seedu.address.commons.events.BaseEvent;
import seedu.address.model.user.UserProfile;

//@@author {tohcheryl}
/**
 * Indicates profile details of user has changed
 */
public class UserProfileChangedEvent extends BaseEvent {

    public final UserProfile userProfile;

    public UserProfileChangedEvent(UserProfile userProfile) {
        this.userProfile = userProfile;
    }

    @Override
    public String toString() {
        return userProfile.toString();
    }
}
