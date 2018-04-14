package seedu.address.model.session;

import java.util.Optional;
import java.util.Set;

import seedu.address.commons.core.EventsCenter;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.commands.AddCommand;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.ParserUtil;
import seedu.address.model.food.Address;
import seedu.address.model.food.Email;
import seedu.address.model.food.Food;
import seedu.address.model.food.Name;
import seedu.address.model.food.Phone;
import seedu.address.model.food.Price;
import seedu.address.model.food.Rating;
import seedu.address.model.food.allergy.Allergy;
import seedu.address.model.tag.Tag;

//@@author jaxony
/**
 * Controls the interaction for the interactive AddCommand
 */
public class SessionAddCommand extends Session {

    protected Name name;
    protected Phone phone;
    protected Email email = new Email(Email.DEFAULT_EMAIL);
    protected Address address = new Address(Address.DEFAULT_ADDRESS);
    protected Price price = new Price(Price.DEFAULT_PRICE);
    protected Rating rating = new Rating(Rating.DEFAULT_RATING);
    protected Set<Tag> tagSet;
    protected Set<Allergy> allergySet;

    public SessionAddCommand(Command command, EventsCenter eventsCenter) {
        super(command, eventsCenter);
    }

    @Override
    public void parseInputForMultivaluedField(String fieldName) throws IllegalValueException, IllegalArgumentException {
        switch (fieldName) {
        case Tag.CLASS_NAME:
            tagSet = ParserUtil.parseTags(stringBuffer);
            break;
        case Allergy.CLASS_NAME:
            allergySet = ParserUtil.parseAllergies(stringBuffer);
            break;
        default:
            throw new IllegalArgumentException();
        }
    }

    @Override
    protected void finishCommand() throws CommandException {
        AddCommand addCommand = (AddCommand) command;
        Food food = new Food(name, phone, email, address, price, rating, tagSet, allergySet);
        addCommand.setFood(food);
        addCommand.execute();
    }

    /**
     * Parses the {@code userInput} for a specific field.
     * @param fieldName Class name of the field that will be used to parse {@code userInput}
     * @param userInput Test input from the user.
     * @throws IllegalValueException If parsing of {@code userInput} causes an error.
     * @throws IllegalArgumentException If {@code fieldName} is invalid.
     */
    public void parseInputForField(String fieldName, String userInput)
            throws IllegalValueException, IllegalArgumentException {
        switch (fieldName) {
        case Name.CLASS_NAME:
            name = ParserUtil.parseName(Optional.of(userInput)).get();
            break;
        case Phone.CLASS_NAME:
            phone = ParserUtil.parsePhone(Optional.of(userInput)).get();
            break;
        case Email.CLASS_NAME:
            email = ParserUtil.parseEmail(Optional.of(userInput)).get();
            break;
        case Address.CLASS_NAME:
            address = ParserUtil.parseAddress(Optional.of(userInput)).get();
            break;
        case Price.CLASS_NAME:
            price = ParserUtil.parsePrice(Optional.of(userInput)).get();
            break;
        case Rating.CLASS_NAME:
            rating = ParserUtil.parseRating(Optional.of(userInput)).get();
            break;
        default:
            throw new IllegalArgumentException();
        }
    }
}
