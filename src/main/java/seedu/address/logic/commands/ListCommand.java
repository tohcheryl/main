package seedu.address.logic.commands;

import static seedu.address.model.Model.PREDICATE_SHOW_ALL_FOODS;

/**
 * Lists all foods in HackEat to the user.
 */
public class ListCommand extends Command {

    public static final String COMMAND_WORD = "list";

    public static final String MESSAGE_SUCCESS = "Listed all foods";


    @Override
    public CommandResult execute() {
        model.updateFilteredFoodList(PREDICATE_SHOW_ALL_FOODS);
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
