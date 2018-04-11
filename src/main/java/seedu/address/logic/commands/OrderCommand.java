package seedu.address.logic.commands;

import java.io.IOException;
import java.util.List;

import javax.mail.MessagingException;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.orderer.FoodSelector;
import seedu.address.logic.orderer.OrderManager;
import seedu.address.model.food.Food;
import seedu.address.model.food.allergy.Allergy;

//@@author {samzx}

/**
 * Orders food in HackEat.
 */
public class OrderCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "order";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Orders a food "
            + "Parameters: INDEX (must be a positive integer) ";

    public static final String MESSAGE_SUCCESS = "%1$s has been successfully ordered.";
    public static final String MESSAGE_SELECT_FAIL = "You seem to be allergic to all the foods listed here.";
    public static final String MESSAGE_SELECT_INDEX_FAIL = "Sorry, can't order that, you seem to be allergic to %s";
    public static final String MESSAGE_FAIL_FOOD = "Order failure for: %s";
    public static final String MESSAGE_EMAIL_FAIL_FOOD = "%1$s has failed to be ordered via email";
    public static final String MESSAGE_DIAL_FAIL_FOOD = "%1$s has failed to be ordered via phone";

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
            return new CommandResult(String.format(MESSAGE_SUCCESS, toOrder.getName()));
        } catch (MessagingException e) {
            throw new CommandException(String.format(MESSAGE_EMAIL_FAIL_FOOD, toOrder.getName()));
        } catch (IOException e) {
            throw new CommandException(String.format(MESSAGE_DIAL_FAIL_FOOD, toOrder.getName()));
        } catch (Exception e) {
            throw new CommandException(String.format(MESSAGE_FAIL_FOOD, toOrder.getName()));
        }
    }

    @Override
    protected void preprocessUndoableCommand() throws CommandException {
        List<Food> lastShownList = model.getFilteredFoodList();

        if (this.index == null) {
            FoodSelector fs = new FoodSelector();
            this.index = fs.select(model);
        }

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_FOOD_DISPLAYED_INDEX);
        }
        Food aboutToOrder = lastShownList.get(index.getZeroBased());
        for (Allergy allergy : aboutToOrder.getAllergies()) {
            if (model.getUserProfile().getAllergies().contains(allergy)) {
                throw new CommandException(String.format(MESSAGE_SELECT_INDEX_FAIL, aboutToOrder.getName()));
            }
        }
        toOrder = aboutToOrder;
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
