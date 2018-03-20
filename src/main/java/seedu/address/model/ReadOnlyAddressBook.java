package seedu.address.model;

import javafx.collections.ObservableList;
import seedu.address.model.food.Food;
import seedu.address.model.tag.Tag;
import seedu.address.model.user.UserProfile;

/**
 * Unmodifiable view of an address book
 */
public interface ReadOnlyAddressBook {

    /**
     * Returns an unmodifiable view of the foods list.
     * This list will not contain any duplicate foods.
     */
    ObservableList<Food> getFoodList();

    /**
     * Returns an unmodifiable view of the tags list.
     * This list will not contain any duplicate tags.
     */
    ObservableList<Tag> getTagList();

    /**
     * Returns an unmodifiable view of the User Profile.
     */
    UserProfile getUserProfile();
}
