package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Iterator;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.commons.util.CollectionUtil;
import seedu.address.model.person.exceptions.DuplicatePersonException;
import seedu.address.model.person.exceptions.PersonNotFoundException;

/**
 * A list of persons that enforces uniqueness between its elements and does not allow nulls.
 *
 * Supports a minimal set of list operations.
 *
 * @see Food#equals(Object)
 * @see CollectionUtil#elementsAreUnique(Collection)
 */
public class UniquePersonList implements Iterable<Food> {

    private final ObservableList<Food> internalList = FXCollections.observableArrayList();

    /**
     * Returns true if the list contains an equivalent food as the given argument.
     */
    public boolean contains(Food toCheck) {
        requireNonNull(toCheck);
        return internalList.contains(toCheck);
    }

    /**
     * Adds a food to the list.
     *
     * @throws DuplicatePersonException if the food to add is a duplicate of an existing food in the list.
     */
    public void add(Food toAdd) throws DuplicatePersonException {
        requireNonNull(toAdd);
        if (contains(toAdd)) {
            throw new DuplicatePersonException();
        }
        internalList.add(toAdd);
    }

    /**
     * Replaces the food {@code target} in the list with {@code editedFood}.
     *
     * @throws DuplicatePersonException if the replacement is equivalent to another existing food in the list.
     * @throws PersonNotFoundException if {@code target} could not be found in the list.
     */
    public void setPerson(Food target, Food editedFood)
            throws DuplicatePersonException, PersonNotFoundException {
        requireNonNull(editedFood);

        int index = internalList.indexOf(target);
        if (index == -1) {
            throw new PersonNotFoundException();
        }

        if (!target.equals(editedFood) && internalList.contains(editedFood)) {
            throw new DuplicatePersonException();
        }

        internalList.set(index, editedFood);
    }

    /**
     * Removes the equivalent food from the list.
     *
     * @throws PersonNotFoundException if no such food could be found in the list.
     */
    public boolean remove(Food toRemove) throws PersonNotFoundException {
        requireNonNull(toRemove);
        final boolean personFoundAndDeleted = internalList.remove(toRemove);
        if (!personFoundAndDeleted) {
            throw new PersonNotFoundException();
        }
        return personFoundAndDeleted;
    }

    public void setPersons(UniquePersonList replacement) {
        this.internalList.setAll(replacement.internalList);
    }

    public void setPersons(List<Food> foods) throws DuplicatePersonException {
        requireAllNonNull(foods);
        final UniquePersonList replacement = new UniquePersonList();
        for (final Food food : foods) {
            replacement.add(food);
        }
        setPersons(replacement);
    }

    /**
     * Returns the backing list as an unmodifiable {@code ObservableList}.
     */
    public ObservableList<Food> asObservableList() {
        return FXCollections.unmodifiableObservableList(internalList);
    }

    @Override
    public Iterator<Food> iterator() {
        return internalList.iterator();
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof UniquePersonList // instanceof handles nulls
                        && this.internalList.equals(((UniquePersonList) other).internalList));
    }

    @Override
    public int hashCode() {
        return internalList.hashCode();
    }
}
