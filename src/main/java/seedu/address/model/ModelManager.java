package seedu.address.model;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.function.Predicate;
import java.util.logging.Logger;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import seedu.address.commons.core.ComponentManager;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.events.model.AddressBookChangedEvent;
import seedu.address.model.food.Food;
import seedu.address.model.food.exceptions.DuplicateFoodException;
import seedu.address.model.food.exceptions.FoodNotFoundException;

/**
 * Represents the in-memory model of HackEat data.
 * All changes to any model should be synchronized.
 */
public class ModelManager extends ComponentManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private final AddressBook addressBook;
    private final FilteredList<Food> filteredFoods;

    /**
     * Initializes a ModelManager with the given addressBook and userPrefs.
     */
    public ModelManager(ReadOnlyAddressBook addressBook, UserPrefs userPrefs) {
        super();
        requireAllNonNull(addressBook, userPrefs);

        logger.fine("Initializing with address book: " + addressBook + " and user prefs " + userPrefs);

        this.addressBook = new AddressBook(addressBook);
        filteredFoods = new FilteredList<>(this.addressBook.getFoodList());
    }

    public ModelManager() {
        this(new AddressBook(), new UserPrefs());
    }

    @Override
    public void resetData(ReadOnlyAddressBook newData) {
        addressBook.resetData(newData);
        indicateAddressBookChanged();
    }

    @Override
    public ReadOnlyAddressBook getAddressBook() {
        return addressBook;
    }

    /** Raises an event to indicate the model has changed */
    private void indicateAddressBookChanged() {
        raise(new AddressBookChangedEvent(addressBook));
    }

    @Override
    public synchronized void deleteFood(Food target) throws FoodNotFoundException {
        addressBook.removeFood(target);
        indicateAddressBookChanged();
    }

    @Override
    public synchronized void addFood(Food food) throws DuplicateFoodException {
        addressBook.addFood(food);
        updateFilteredFoodList(PREDICATE_SHOW_ALL_FOODS);
        indicateAddressBookChanged();
    }

    @Override
    public void updateFood(Food target, Food editedFood)
            throws DuplicateFoodException, FoodNotFoundException {
        requireAllNonNull(target, editedFood);

        addressBook.updateFood(target, editedFood);
        indicateAddressBookChanged();
    }

    //=========== Filtered Food List Accessors =============================================================

    /**
     * Returns an unmodifiable view of the list of {@code Food} backed by the internal list of
     * {@code addressBook}
     */
    @Override
    public ObservableList<Food> getFilteredFoodList() {
        return FXCollections.unmodifiableObservableList(filteredFoods);
    }

    @Override
    public void updateFilteredFoodList(Predicate<Food> predicate) {
        requireNonNull(predicate);
        filteredFoods.setPredicate(predicate);
    }

    @Override
    public boolean equals(Object obj) {
        // short circuit if same object
        if (obj == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(obj instanceof ModelManager)) {
            return false;
        }

        // state check
        ModelManager other = (ModelManager) obj;
        return addressBook.equals(other.addressBook)
                && filteredFoods.equals(other.filteredFoods);
    }

}
