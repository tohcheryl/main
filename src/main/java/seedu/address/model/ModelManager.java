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
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.food.Food;
import seedu.address.model.food.exceptions.DuplicateFoodException;
import seedu.address.model.food.exceptions.FoodNotFoundException;
import seedu.address.model.session.SessionInterface;
import seedu.address.model.session.SessionManager;
import seedu.address.model.user.UserProfile;
import seedu.address.model.user.exceptions.DuplicateUserException;

/**
 * Represents the in-memory model of HackEat data.
 * All changes to any model should be synchronized.
 */
public class ModelManager extends ComponentManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private final AddressBook addressBook;
    private final FilteredList<Food> filteredFoods;
    private final SessionInterface sessionManager;

    /**
     * Initializes a ModelManager with the given addressBook and userPrefs.
     */
    public ModelManager(ReadOnlyAddressBook addressBook, UserPrefs userPrefs) {
        super();
        requireAllNonNull(addressBook, userPrefs);

        logger.fine("Initializing with address book: " + addressBook + " and user prefs " + userPrefs);

        this.addressBook = new AddressBook(addressBook);
        filteredFoods = new FilteredList<>(this.addressBook.getFoodList());
        sessionManager = new SessionManager();
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

    //@@author tohcheryl
    @Override
    public UserProfile getUserProfile() throws NullPointerException {
        return addressBook.getUserProfile();
    }

    @Override
    public void initUserProfile(UserProfile userProfile) {
        addressBook.initUserProfile(userProfile);
        indicateAddressBookChanged();
    }

    //@@author
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

    //@@author jaxony
    @Override
    public void updateUserProfile(UserProfile toAdd) throws DuplicateUserException {
        addressBook.updateUserProfile(toAdd);
        indicateAddressBookChanged();
    }

    @Override
    public boolean isUserInActiveSession() {
        return sessionManager.isUserInActiveSession();
    }

    @Override
    public void createNewSession(Command interactiveCommand) {
        sessionManager.createNewSession(interactiveCommand);
    }

    @Override
    public CommandResult startSession() throws CommandException {
        return sessionManager.startSession();
    }

    @Override
    public CommandResult interpretInteractiveUserInput(String commandText) throws CommandException {
        return sessionManager.interpretUserInput(commandText);
    }
    //@@author

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
