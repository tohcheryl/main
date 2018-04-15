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

//@@author samzx

/**
 * Orders command which starts the selection and ordering food process in HackEat.
 */
public class OrderCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "order";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Orders a food "
            + "Parameters: INDEX (must be a positive integer) ";

    public static final String MESSAGE_SUCCESS = "%1$s has been requested to be ordered.";
    public static final String MESSAGE_SELECT_FAIL = "You seem to be allergic to all the foods listed here.";
    public static final String MESSAGE_SELECT_INDEX_FAIL = "Sorry, can't order that, you seem to be allergic to %s";
    public static final String MESSAGE_FAIL_FOOD = "Something went wrong, we could not order %s";
    public static final String MESSAGE_CHECK_INTERNET_CONNECTION = "Failed to contact our servers. "
            + "Please check your internet connection.";
    public static final String MESSAGE_EMAIL_FAIL_FOOD = "%1$s has failed to be ordered via email. "
            + MESSAGE_CHECK_INTERNET_CONNECTION;
    public static final String MESSAGE_DIAL_FAIL_FOOD = "%1$s has failed to be ordered via phone. "
            + MESSAGE_CHECK_INTERNET_CONNECTION;

    private Food toOrder;
    private Index index;

    /**
     * Creates an Order command to the specified index of {@code Food}
     */
    public OrderCommand(Index index) {
        this.index = index;
    }

    /**
     * Selects a index based on {@code FoodSelector} algorithm if not selected yet
     * @throws CommandException if unable to selectIndex food
     */
    private void getIndexIfNull() throws CommandException {
        if (this.index == null) {
            FoodSelector fs = new FoodSelector();
            this.index = fs.selectIndex(model);
        }
    }

    /**
     * Verifies that the index is smaller than the size of a list
     * @param list which the index can not exceed the size
     * @throws CommandException if index exceeds list size
     */
    private void verifyIndex(Index index, List list) throws CommandException {
        if (index.getZeroBased() >= list.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_FOOD_DISPLAYED_INDEX);
        }
    }

    /**
     * Checks a food for allergies
     * @param food to check for allergy
     * @throws CommandException is thrown if food contains an allergy same as user
     */
    private void checkForAllergy(Food food) throws CommandException {
        for (Allergy allergy : food.getAllergies()) {
            if (model.getUserProfile().getAllergies().contains(allergy)) {
                throw new CommandException(String.format(MESSAGE_SELECT_INDEX_FAIL, food.getName()));
            }
        }
    }

    @Override
    public CommandResult executeUndoableCommand() throws CommandException {
        try {
            if (!OrderManager.netIsAvailable(OrderManager.REMOTE_SERVER)) {
                throw new CommandException(String.format(MESSAGE_CHECK_INTERNET_CONNECTION));
            } else {
                OrderManager manager = new OrderManager(model.getAddressBook().getUserProfile(), toOrder);
                manager.order();
            }
            return new CommandResult(String.format(MESSAGE_SUCCESS, toOrder.getName()));
        } catch (CommandException e) {
            throw e;
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

        getIndexIfNull();
        verifyIndex(this.index, lastShownList);

        Food aboutToOrder = lastShownList.get(index.getZeroBased());
        checkForAllergy(aboutToOrder);

        toOrder = aboutToOrder;
    }

    @Override
    public boolean equals(Object other) {
        try {
            return other == this
                    || (other instanceof OrderCommand
                    && index.equals(((OrderCommand) other).index));

        } catch (NullPointerException npe) {
            return other == this
                    || (other instanceof OrderCommand
                    && index == (((OrderCommand) other).index));
        }

    }

}
