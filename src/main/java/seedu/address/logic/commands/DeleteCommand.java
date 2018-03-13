package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.Objects;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.food.Food;
import seedu.address.model.food.exceptions.FoodNotFoundException;

/**
 * Deletes a food identified using it's last displayed index from HackEat.
 */
public class DeleteCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "delete";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes the food identified by the index number used in the last food listing.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_DELETE_FOOD_SUCCESS = "Deleted Food: %1$s";

    private final Index targetIndex;

    private Food foodToDelete;

    public DeleteCommand(Index targetIndex) {
        this.targetIndex = targetIndex;
    }


    @Override
    public CommandResult executeUndoableCommand() {
        requireNonNull(foodToDelete);
        try {
            model.deleteFood(foodToDelete);
        } catch (FoodNotFoundException pnfe) {
            throw new AssertionError("The target food cannot be missing");
        }

        return new CommandResult(String.format(MESSAGE_DELETE_FOOD_SUCCESS, foodToDelete));
    }

    @Override
    protected void preprocessUndoableCommand() throws CommandException {
        List<Food> lastShownList = model.getFilteredFoodList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_FOOD_DISPLAYED_INDEX);
        }

        foodToDelete = lastShownList.get(targetIndex.getZeroBased());
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof DeleteCommand // instanceof handles nulls
                && this.targetIndex.equals(((DeleteCommand) other).targetIndex) // state check
                && Objects.equals(this.foodToDelete, ((DeleteCommand) other).foodToDelete));
    }
}
