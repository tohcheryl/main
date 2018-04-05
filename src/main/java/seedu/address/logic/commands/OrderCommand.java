package seedu.address.logic.commands;

import java.util.List;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.orderer.FoodSelector;
import seedu.address.logic.orderer.OrderManager;
import seedu.address.model.food.Food;

/**
 * Orders food in HackEat.
 */
public class OrderCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "order";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Orders a food "
            + "Parameters: INDEX (must be a positive integer) ";

    public static final String MESSAGE_SUCCESS = "Beginning to order %1$s. Ringing %2$s. Please wait.";
    public static final String MESSAGE_SELECT_FAIL = "Unable to select food at random.";
    public static final String MESSAGE_DIAL_FAIL_FOOD = "Order failure for: %s";
    public static final String MESSAGE_DIAL_FAIL_PHONE = "Failed to dial %s";
    public static final String MESSAGE_DIAL_FAIL = MESSAGE_DIAL_FAIL_FOOD + ". " + MESSAGE_DIAL_FAIL_PHONE + ".";

    private Food toOrder;
    private Index index;

    /**
     * Creates an Order command to the specified index of {@code Food}
     */
    public OrderCommand(Index index) {
        this.index = index;
    }

    @Override
    public CommandResult executeUndoableCommand() throws CommandException {
        try {
            OrderManager manager = new OrderManager(model.getAddressBook().getUserProfile(), toOrder);
            manager.order();

            return new CommandResult(String.format(MESSAGE_SUCCESS, toOrder.getName(), toOrder.getPhone()));
        } catch (Exception e) {
            throw new CommandException(String.format(MESSAGE_DIAL_FAIL, toOrder.getName(), toOrder.getPhone()));
        }
    }

    @Override
    protected void preprocessUndoableCommand() throws CommandException {
        List<Food> lastShownList = model.getFilteredFoodList();

        if (this.index == null) {
            FoodSelector fs = new FoodSelector();
            this.index = fs.select(model);
        }

        if (this.index == null) {
            throw new CommandException(MESSAGE_SELECT_FAIL);
        }

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_FOOD_DISPLAYED_INDEX);
        }

        toOrder = lastShownList.get(index.getZeroBased());
    }

    @Override
    public boolean equals(Object other) {
        try {
            return other == this // short circuit if same object
                    || (other instanceof OrderCommand // instanceof handles nulls
                    && index.equals(((OrderCommand) other).index));

        } catch (NullPointerException npe) {
            return other == this // short circuit if same object
                    || (other instanceof OrderCommand // instanceof handles nulls
                    && index == (((OrderCommand) other).index));
        }

    }

}
