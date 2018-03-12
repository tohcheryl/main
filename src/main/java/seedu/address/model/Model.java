package seedu.address.model;

import java.util.function.Predicate;

import javafx.collections.ObservableList;
import seedu.address.model.person.Food;
import seedu.address.model.person.exceptions.DuplicatePersonException;
import seedu.address.model.person.exceptions.PersonNotFoundException;

/**
 * The API of the Model component.
 */
public interface Model {
    /** {@code Predicate} that always evaluate to true */
    Predicate<Food> PREDICATE_SHOW_ALL_PERSONS = unused -> true;

    /** Clears existing backing model and replaces with the provided new data. */
    void resetData(ReadOnlyAddressBook newData);

    /** Returns the AddressBook */
    ReadOnlyAddressBook getAddressBook();

    /** Deletes the given food. */
    void deletePerson(Food target) throws PersonNotFoundException;

    /** Adds the given food */
    void addPerson(Food food) throws DuplicatePersonException;

    /**
     * Replaces the given food {@code target} with {@code editedFood}.
     *
     * @throws DuplicatePersonException if updating the food's details causes the food to be equivalent to
     *      another existing food in the list.
     * @throws PersonNotFoundException if {@code target} could not be found in the list.
     */
    void updatePerson(Food target, Food editedFood)
            throws DuplicatePersonException, PersonNotFoundException;

    /** Returns an unmodifiable view of the filtered food list */
    ObservableList<Food> getFilteredPersonList();

    /**
     * Updates the filter of the filtered food list to filter by the given {@code predicate}.
     * @throws NullPointerException if {@code predicate} is null.
     */
    void updateFilteredPersonList(Predicate<Food> predicate);

}
