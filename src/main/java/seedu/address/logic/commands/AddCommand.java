package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ALLERGIES;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PRICE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_RATING;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.Arrays;
import java.util.List;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.food.Address;
import seedu.address.model.food.Email;
import seedu.address.model.food.Food;
import seedu.address.model.food.Name;
import seedu.address.model.food.Phone;
import seedu.address.model.food.Price;
import seedu.address.model.food.Rating;
import seedu.address.model.food.allergy.Allergy;
import seedu.address.model.food.exceptions.DuplicateFoodException;
import seedu.address.model.tag.Tag;

/**
 * Adds a food to HackEat.
 */
public class AddCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "add";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a food to HackEat. "
            + "Parameters: "
            + PREFIX_NAME + "NAME "
            + PREFIX_PHONE + "PHONE "
            + PREFIX_EMAIL + "EMAIL "
            + PREFIX_ADDRESS + "ADDRESS "
            + PREFIX_RATING + "RATING "
            + PREFIX_PRICE + "PRICE "
            + "[" + PREFIX_TAG + "TAG]... "
            + "[" + PREFIX_ALLERGIES + "ALLERGY]...\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_NAME + "Tissue Prata "
            + PREFIX_PHONE + "62895379 "
            + PREFIX_EMAIL + "rkeatinghouse@gmail.com "
            + PREFIX_ADDRESS + "1 Kensington Park Rd "
            + PREFIX_RATING + "5 "
            + PREFIX_PRICE + "3.50 "
            + PREFIX_TAG + "sweet "
            + PREFIX_TAG + "crispy "
            + PREFIX_ALLERGIES + "lactose";

    public static final String MESSAGE_SUCCESS = "New food added: %1$s";
    public static final String MESSAGE_DUPLICATE_FOOD = "This food already exists in HackEat";

    //@@author jaxony
    public static final List<Prompt> PROMPTS = Arrays.asList(
            new Prompt(Name.CLASS_NAME, "What's the food called?", false),
            new Prompt(Phone.CLASS_NAME, "Restaurant phone number?", false),
            new Prompt(Email.CLASS_NAME, "And their email?", false, true),
            new Prompt(Address.CLASS_NAME, "Where they located @ fam?", false, true),
            new Prompt(Price.CLASS_NAME, "$$$?", false, true),
            new Prompt(Rating.CLASS_NAME, "U rate or what?", false, true),
            new Prompt(Tag.CLASS_NAME, "Where those tags at?", true, true),
            new Prompt(Allergy.CLASS_NAME, "Does this food have any allergies?", true, true));
    //@@author
    private Food toAdd;

    /**
     * Creates an AddCommand to add the specified {@code Food}
     */
    public AddCommand(Food food) {
        toAdd = food;
    }

    //@@author jaxony
    @Override
    public List<Prompt> getPrompts() {
        return PROMPTS;
    }
    //@@author

    @Override
    public CommandResult executeUndoableCommand() throws CommandException {
        requireNonNull(model);
        requireNonNull(toAdd);
        try {
            model.addFood(toAdd);
            return new CommandResult(String.format(MESSAGE_SUCCESS, toAdd));
        } catch (DuplicateFoodException e) {
            throw new CommandException(MESSAGE_DUPLICATE_FOOD);
        }

    }

    //@@author jaxony
    public void setFood(Food foodToAdd) {
        toAdd = foodToAdd;
    }
    //@@author

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof AddCommand // instanceof handles nulls
                && toAdd.equals(((AddCommand) other).toAdd));
    }
}
