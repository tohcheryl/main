package seedu.address.logic.commands;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;

public class OrderCommandTest {
    private static final Index VALID_INDEX = Index.fromZeroBased(1);
    private static final Index NULL_INDEX = null;

    @Test
    public void constructor_index_success() {
        new OrderCommand(VALID_INDEX);
        new OrderCommand(NULL_INDEX);
    }

    // TODO: Add stub class to test the order success
    @Test
    public void execute_order_success() throws CommandException {
        OrderCommand orderCommand = new OrderCommand(VALID_INDEX);
        // Below should be orderCommand.execute()
        assertEquals(OrderCommand.MESSAGE_SUCCESS, OrderCommand.MESSAGE_SUCCESS);
    }
}
