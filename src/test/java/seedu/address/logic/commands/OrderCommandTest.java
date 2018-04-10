package seedu.address.logic.commands;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import static seedu.address.testutil.TypicalFoods.getTypicalAddressBook;

import org.junit.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.UndoRedoStack;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.food.Food;

//@@author {samzx}

public class OrderCommandTest {
    private static final Index VALID_INDEX = Index.fromZeroBased(1);
    private static final Index NULL_INDEX = null;

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void constructor_index_success() {
        OrderCommand indexedOrderCommand = new OrderCommand(VALID_INDEX);
        OrderCommand indexedOrderCommand2 = new OrderCommand(VALID_INDEX);

        OrderCommand nullOrderCommand = new OrderCommand(NULL_INDEX);
        OrderCommand nullOrderCommand2 = new OrderCommand(NULL_INDEX);

        assertEquals(indexedOrderCommand, indexedOrderCommand2);
        assertEquals(nullOrderCommand, nullOrderCommand2);
    }

    @Test
    public void execute_orderWithIndex_success() throws CommandException {
        OrderCommand orderCommand = getOrderCommandForIndex(VALID_INDEX, model);
        Food food = model.getAddressBook().getFoodList().get(VALID_INDEX.getZeroBased());
        try {
            CommandResult result = orderCommand.execute();
            assertThat(result.feedbackToUser, containsString(String.format(OrderCommand.MESSAGE_SUCCESS,
                    food.getName())));
        } catch (Exception e) {
            assertThat(e.getMessage(), containsString(String.format(OrderCommand.MESSAGE_EMAIL_FAIL_FOOD,
                    food.getName())));
        }
    }

    @Test
    public void execute_orderWithoutIndex_success() throws CommandException {
        OrderCommand orderCommand = getOrderCommandForIndex(NULL_INDEX, model);
        try {
            CommandResult result = orderCommand.execute();
            assertThat(result.feedbackToUser, containsString(String.format(OrderCommand.MESSAGE_SUCCESS,
                    "", "")));
        } catch (Exception e) {
            assertThat(e.getMessage(), containsString(String.format(OrderCommand.MESSAGE_EMAIL_FAIL_FOOD, "")));
            assertThat(e.getMessage(), containsString(String.format(OrderCommand.MESSAGE_EMAIL_FAIL_FOOD, "")));
        }
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
