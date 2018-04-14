package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Predicate;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import javafx.collections.ObservableList;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.UndoRedoStack;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.food.Food;
import seedu.address.model.food.exceptions.DuplicateFoodException;
import seedu.address.model.food.exceptions.FoodNotFoundException;
import seedu.address.model.user.UserProfile;
import seedu.address.testutil.FoodBuilder;

public class AddCommandTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void execute_foodAcceptedByModel_addSuccessful() throws Exception {
        ModelStubAcceptingFoodAdded modelStub = new ModelStubAcceptingFoodAdded();
        Food validFood = new FoodBuilder().build();

        CommandResult commandResult = getAddCommandForFood(validFood, modelStub).execute();

        assertEquals(String.format(AddCommand.MESSAGE_SUCCESS, validFood), commandResult.feedbackToUser);
        assertEquals(Arrays.asList(validFood), modelStub.foodsAdded);
    }

    @Test
    public void execute_duplicateFood_throwsCommandException() throws Exception {
        ModelStub modelStub = new ModelStubThrowingDuplicateFoodException();
        Food validFood = new FoodBuilder().build();

        thrown.expect(CommandException.class);
        thrown.expectMessage(AddCommand.MESSAGE_DUPLICATE_FOOD);

        getAddCommandForFood(validFood, modelStub).execute();
    }

    @Test
    public void equals() {
        Food apple = new FoodBuilder().withName("Apple").build();
        Food banana = new FoodBuilder().withName("Banana").build();
        AddCommand addAppleCommand = new AddCommand(apple);
        AddCommand addBananaCommand = new AddCommand(banana);

        // same object -> returns true
        assertTrue(addAppleCommand.equals(addAppleCommand));

        // same values -> returns true
        AddCommand addAppleCommandCopy = new AddCommand(apple);
        assertTrue(addAppleCommand.equals(addAppleCommandCopy));

        // different types -> returns false
        assertFalse(addAppleCommand.equals(1));

        // null -> returns false
        assertFalse(addAppleCommand.equals(null));

        // different food -> returns false
        assertFalse(addAppleCommand.equals(addBananaCommand));
    }

    /**
     * Generates a new AddCommand with the details of the given food.
     */
    private AddCommand getAddCommandForFood(Food food, Model model) {
        AddCommand command = new AddCommand(food);
        command.setData(model, new CommandHistory(), new UndoRedoStack());
        return command;
    }

    /**
     * A default model stub that have all of the methods failing.
     */
    private class ModelStub implements Model {
        @Override
        public void addFood(Food food) throws DuplicateFoodException {
            fail("This method should not be called.");
        }

        @Override
        public void resetData(ReadOnlyAddressBook newData) {
            fail("This method should not be called.");
        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            fail("This method should not be called.");
            return null;
        }

        @Override
        public void deleteFood(Food target) throws FoodNotFoundException {
            fail("This method should not be called.");
        }

        @Override
        public void updateFood(Food target, Food editedFood)
                throws DuplicateFoodException {
            fail("This method should not be called.");
        }

        @Override
        public ObservableList<Food> getFilteredFoodList() {
            fail("This method should not be called.");
            return null;
        }

        @Override
        public void updateFilteredFoodList(Predicate<Food> predicate) {
            fail("This method should not be called.");
        }

        @Override
        public void initUserProfile(UserProfile userProfile) {
            fail("This method should not be called.");
        }

        @Override
        public void updateUserProfile(UserProfile userProfile) {
            fail("This method should not be called.");
        }

        //@@author jaxony
        @Override
        public boolean isUserInActiveSession() {
            fail("This method should not be called.");
            return false;
        }

        @Override
        public void createNewSession(Command interactiveCommand) {
            fail("This method should not be called.");
        }

        @Override
        public CommandResult startSession() throws CommandException {
            fail("This method should not be called.");
            return null;
        }

        @Override
        public CommandResult interpretInteractiveUserInput(String commandText) throws CommandException {
            fail("This method should not be called.");
            return null;
        }

        //@@author tohcheryl
        @Override
        public UserProfile getUserProfile() throws NullPointerException {
            fail("This method should not be called.");
            return null;
        }
    }

    /**
     * A Model stub that always throw a DuplicateFoodException when trying to add a food.
     */
    private class ModelStubThrowingDuplicateFoodException extends ModelStub {
        @Override
        public void addFood(Food food) throws DuplicateFoodException {
            throw new DuplicateFoodException();
        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            return new AddressBook();
        }
    }

    /**
     * A Model stub that always accept the food being added.
     */
    private class ModelStubAcceptingFoodAdded extends ModelStub {
        final ArrayList<Food> foodsAdded = new ArrayList<>();

        @Override
        public void addFood(Food food) throws DuplicateFoodException {
            requireNonNull(food);
            foodsAdded.add(food);
        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            return new AddressBook();
        }
    }

}
