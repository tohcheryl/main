package seedu.address.logic.commands;

import java.util.List;
import java.util.Random;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.food.Food;

/**
 * Orders food in HackEat.
 */
public class OrderCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "order";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Orders a food "
            + "Parameters: INDEX (must be a positive integer) ";

    public static final String MESSAGE_SUCCESS = "Food ordered: %1$s";
    public static final String MESSAGE_ORDER_FAIL = "Ordering failed. Please try again later.";

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
            beginOrder();
            return new CommandResult(String.format(MESSAGE_SUCCESS, toOrder.getName()));
        } catch (Exception e) {
            throw new CommandException(MESSAGE_ORDER_FAIL);
        }

    }

    @Override
    protected void preprocessUndoableCommand() throws CommandException {
        List<Food> lastShownList = model.getFilteredFoodList();

        if (this.index == null) {
            this.index = selectFood();
        }

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_FOOD_DISPLAYED_INDEX);
        }

        toOrder = lastShownList.get(index.getZeroBased());
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof OrderCommand // instanceof handles nulls
                    && index.equals(((OrderCommand) other).index));
    }

    // This entails the food selection algorithm - TODO: BRING OUT TO NEW CLASS
    /**
     * Selects a {@code Food} based on the HackEat Algorithm
     */
    private Index selectFood() {
        List<Food> lastShownList = model.getFilteredFoodList();
        int listSize = lastShownList.size();
        int randomIndex = (new Random()).nextInt(listSize);
        return Index.fromZeroBased(randomIndex);
    }

    // Method responsible for beginning the phone call to order food. TODO: BRING OUT TO NEW CLASS
    /**
     * Begins phone call to order {@code Food}
     */
    private void beginOrder() {

    }
}
