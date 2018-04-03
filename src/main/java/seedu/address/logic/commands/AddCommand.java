package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
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
            + PREFIX_RATING + "RATING"
            + PREFIX_PRICE + "PRICE"
            + "[" + PREFIX_TAG + "TAG]...\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_NAME + "John Doe "
            + PREFIX_PHONE + "98765432 "
            + PREFIX_EMAIL + "johnd@example.com "
            + PREFIX_ADDRESS + "311, Clementi Ave 2, #02-25 "
            + PREFIX_RATING + "5"
            + PREFIX_PRICE + "3.50"
            + PREFIX_TAG + "friends "
            + PREFIX_TAG + "owesMoney";

    public static final String MESSAGE_SUCCESS = "New food added: %1$s";
    public static final String MESSAGE_DUPLICATE_FOOD = "This food already exists in HackEat";

    //@@author {jaxony}
    public static final List<Prompt> prompts = Arrays.asList(
            new Prompt(Name.class, "What's the food called?", false),
            new Prompt(Phone.class, "Restaurant phone number?", false),
            new Prompt(Email.class, "And their email?", false),
            new Prompt(Address.class, "Where they located @ fam?.", false),
            new Prompt(Price.class, "$$$?", false),
            new Prompt(Rating.class, "U rate or what?", false),
            new Prompt(Tag.class, "Where those tags at?", true),
            new Prompt(Allergy.class, "Does this food have any allergies?", true));
    //@@author
    private Food toAdd;

    /**
     * Creates an AddCommand to add the specified {@code Food}
     */
    public AddCommand(Food food) {
        toAdd = food;
    }

    //@@author {jaxony}
    @Override
    public List<Prompt> getPrompts() {
        return prompts;
    }
    //@author

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

    //@@author {jaxony}
    public void setFood(Food food) {
        toAdd = food;
    }
    //@@author

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof AddCommand // instanceof handles nulls
                && toAdd.equals(((AddCommand) other).toAdd));
    }
}
