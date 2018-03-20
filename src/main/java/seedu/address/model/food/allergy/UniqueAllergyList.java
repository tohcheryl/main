package seedu.address.model.food.allergy;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.commons.exceptions.DuplicateDataException;
import seedu.address.commons.util.CollectionUtil;

/**
 * A list of allergies that enforces no nulls and uniqueness between its elements.
 *
 * Supports minimal set of list operations for the app's features.
 *
 * @see Allergy#equals(Object)
 */
public class UniqueAllergyList implements Iterable<Allergy> {

    private final ObservableList<Allergy> internalList = FXCollections.observableArrayList();

    /**
     * Constructs empty AllergyList.
     */
    public UniqueAllergyList() {}

    /**
     * Creates a UniqueAllergyList using given allergies.
     * Enforces no nulls.
     */
    public UniqueAllergyList(Set<Allergy> allergies) {
        requireAllNonNull(allergies);
        internalList.addAll(allergies);

        assert CollectionUtil.elementsAreUnique(internalList);
    }

    /**
     * Returns all allergies in this list as a Set.
     * This set is mutable and change-insulated against the internal list.
     */
    public Set<Allergy> toSet() {
        assert CollectionUtil.elementsAreUnique(internalList);
        return new HashSet<>(internalList);
    }

    /**
     * Replaces the Allergies in this list with those in the argument allergy list.
     */
    public void setAllergies(Set<Allergy> allergies) {
        requireAllNonNull(allergies);
        internalList.setAll(allergies);
        assert CollectionUtil.elementsAreUnique(internalList);
    }

    /**
     * Ensures every allergy in the argument list exists in this object.
     */
    public void mergeFrom(UniqueAllergyList from) {
        final Set<Allergy> alreadyInside = this.toSet();
        from.internalList.stream()
                .filter(allergy -> !alreadyInside.contains(allergy))
                .forEach(internalList::add);

        assert CollectionUtil.elementsAreUnique(internalList);
    }

    /**
     * Returns true if the list contains an equivalent Allergy as the given argument.
     */
    public boolean contains(Allergy toCheck) {
        requireNonNull(toCheck);
        return internalList.contains(toCheck);
    }

    /**
     * Adds a Allergy to the list.
     *
     * @throws DuplicateAllergyException if the Allergy to add is a duplicate of an existing Allergy in the list.
     */
    public void add(Allergy toAdd) throws DuplicateAllergyException {
        requireNonNull(toAdd);
        if (contains(toAdd)) {
            throw new DuplicateAllergyException();
        }
        internalList.add(toAdd);

        assert CollectionUtil.elementsAreUnique(internalList);
    }

    @Override
    public Iterator<Allergy> iterator() {
        assert CollectionUtil.elementsAreUnique(internalList);
        return internalList.iterator();
    }

    /**
     * Returns the backing list as an unmodifiable {@code ObservableList}.
     */
    public ObservableList<Allergy> asObservableList() {
        assert CollectionUtil.elementsAreUnique(internalList);
        return FXCollections.unmodifiableObservableList(internalList);
    }

    @Override
    public boolean equals(Object other) {
        assert CollectionUtil.elementsAreUnique(internalList);
        return other == this // short circuit if same object
                || (other instanceof UniqueAllergyList // instanceof handles nulls
                && this.internalList.equals(((UniqueAllergyList) other).internalList));
    }

    /**
     * Returns true if the element in this list is equal to the elements in {@code other}.
     * The elements do not have to be in the same order.
     */
    public boolean equalsOrderInsensitive(UniqueAllergyList other) {
        assert CollectionUtil.elementsAreUnique(internalList);
        assert CollectionUtil.elementsAreUnique(other.internalList);
        return this == other || new HashSet<>(this.internalList).equals(new HashSet<>(other.internalList));
    }

    @Override
    public int hashCode() {
        assert CollectionUtil.elementsAreUnique(internalList);
        return internalList.hashCode();
    }

    /**
     * Signals that an operation would have violated the 'no duplicates' property of the list.
     */
    public static class DuplicateAllergyException extends DuplicateDataException {
        protected DuplicateAllergyException() {
            super("Operation would result in duplicate allergies");
        }
    }

}

