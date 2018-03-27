package seedu.address.logic.commands;

import static org.junit.Assert.assertEquals;
import static seedu.address.testutil.TypicalFoods.getTypicalAddressBook;

import org.junit.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.UndoRedoStack;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;

public class OrderCommandTest {
    private static final Index VALID_INDEX = Index.fromZeroBased(1);
    private static final Index NULL_INDEX = null;

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void constructor_index_success() {
        new OrderCommand(VALID_INDEX);
        new OrderCommand(NULL_INDEX);
    }

    @Test
    public void execute_order_success() throws CommandException {
        OrderCommand orderCommand = getOrderCommandForIndex(VALID_INDEX, model);
        String expectedMessage = String.format(OrderCommand.MESSAGE_SUCCESS,
                model.getAddressBook().getFoodList().get(VALID_INDEX.getZeroBased()).getName());
        assertCommandSuccess(orderCommand, model, expectedMessage);
    }

    /**
     * Generates a new AddCommand with the details of the given food.
     */
    private OrderCommand getOrderCommandForIndex(Index index, Model model) {
        OrderCommand command = new OrderCommand(index);
        command.setData(model, new CommandHistory(), new UndoRedoStack());
        return command;
    }

    /**
     * Executes the given {@code command}, confirms that <br>
     * - the result message matches {@code expectedMessage} <br>
     * - the {@code actualModel} matches {@code expectedModel}
     */
    public static void assertCommandSuccess(Command command, Model actualModel, String expectedMessage) {
        try {
            CommandResult result = command.execute();
            assertEquals(expectedMessage, result.feedbackToUser);
        } catch (CommandException ce) {
            throw new AssertionError("Execution of command should not fail.", ce);
        }
    }

}
