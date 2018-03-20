package seedu.address.model;

import java.util.function.Predicate;

import javafx.collections.ObservableList;
import seedu.address.model.food.Food;
import seedu.address.model.food.exceptions.DuplicateFoodException;
import seedu.address.model.food.exceptions.FoodNotFoundException;
import seedu.address.model.user.UserProfile;
import seedu.address.model.util.Session;

/**
 * The API of the Model component.
 */
public interface Model {
    /** {@code Predicate} that always evaluate to true */
    Predicate<Food> PREDICATE_SHOW_ALL_FOODS = unused -> true;

    /** Clears existing backing model and replaces with the provided new data. */
    void resetData(ReadOnlyAddressBook newData);

    /** Returns the AddressBook */
    ReadOnlyAddressBook getAddressBook();

    /** Deletes the given food. */
    void deleteFood(Food target) throws FoodNotFoundException;

    /** Adds the given food */
    void addFood(Food food) throws DuplicateFoodException;

    /**
     * Replaces the given food {@code target} with {@code editedFood}.
     *
     * @throws DuplicateFoodException if updating the food's details causes the food to be equivalent to
     *      another existing food in the list.
     * @throws FoodNotFoundException if {@code target} could not be found in the list.
     */
    void updateFood(Food target, Food editedFood)
            throws DuplicateFoodException, FoodNotFoundException;

    /** Returns an unmodifiable view of the filtered food list */
    ObservableList<Food> getFilteredFoodList();

    /**
     * Updates the filter of the filtered food list to filter by the given {@code predicate}.
     * @throws NullPointerException if {@code predicate} is null.
     */
    void updateFilteredFoodList(Predicate<Food> predicate);

    /**
     * Update user's profile
     * @param profile UserProfile
     */
    void updateUserProfile(UserProfile profile);

    Session getActiveSession();

    boolean isUserInActiveSession();
}
