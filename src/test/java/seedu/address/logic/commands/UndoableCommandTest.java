package seedu.address.logic.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static seedu.address.logic.commands.CommandTestUtil.deleteFirstFood;
import static seedu.address.logic.commands.CommandTestUtil.showFoodAtIndex;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_FOOD;
import static seedu.address.testutil.TypicalFoods.getTypicalAddressBook;

import org.junit.Test;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Food;
import seedu.address.model.person.exceptions.FoodNotFoundException;

public class UndoableCommandTest {
    private final Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    private final DummyCommand dummyCommand = new DummyCommand(model);

    private Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void executeUndo() throws Exception {
        dummyCommand.execute();
        deleteFirstFood(expectedModel);
        assertEquals(expectedModel, model);

        showFoodAtIndex(model, INDEX_FIRST_FOOD);

        // undo() should cause the model's filtered list to show all foods
        dummyCommand.undo();
        expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        assertEquals(expectedModel, model);
    }

    @Test
    public void redo() {
        showFoodAtIndex(model, INDEX_FIRST_FOOD);

        // redo() should cause the model's filtered list to show all foods
        dummyCommand.redo();
        deleteFirstFood(expectedModel);
        assertEquals(expectedModel, model);
    }

    /**
     * Deletes the first food in the model's filtered list.
     */
    class DummyCommand extends UndoableCommand {
        DummyCommand(Model model) {
            this.model = model;
        }

        @Override
        public CommandResult executeUndoableCommand() throws CommandException {
            Food foodToDelete = model.getFilteredFoodList().get(0);
            try {
                model.deleteFood(foodToDelete);
            } catch (FoodNotFoundException pnfe) {
                fail("Impossible: foodToDelete was retrieved from model.");
            }
            return new CommandResult("");
        }
    }
}
