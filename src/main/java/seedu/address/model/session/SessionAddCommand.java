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

//@@author {jaxony}
/**
 * Session controlling the interaction for the AddCommand
 */
public class SessionAddCommand extends Session {

    protected Name name;
    protected Phone phone;
    protected Email email;
    protected Address address;
    protected Price price;
    protected Rating rating;
    protected Set<Tag> tagSet;
    protected Set<Allergy> allergySet;

    public SessionAddCommand(Command command, EventsCenter eventsCenter) {
        super(command, eventsCenter);
    }

    @Override
    public void parseInputForMultivaluedField(Class field) throws IllegalValueException, IllegalArgumentException {
        switch (field.getSimpleName()) {
        case "Tag":
            tagSet = ParserUtil.parseTags(stringBuffer);
            break;
        case "Allergy":
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
     * Parses the {@code userInput} for a specific {@code field}
     *
     * @param classObj Class used to parse the {@code userInput}
     * @param userInput Test input from the user
     * @throws IllegalValueException If parsing of {@code userInput} causes an error
     * @throws IllegalArgumentException If {@code classObj} is not allowed
     */
    public void parseInputForField(Class classObj, String userInput)
            throws IllegalValueException, IllegalArgumentException {
        switch (classObj.getSimpleName()) {
        case "Name":
            name = ParserUtil.parseName(Optional.of(userInput)).get();
            break;
        case "Phone":
            phone = ParserUtil.parsePhone(Optional.of(userInput)).get();
            break;
        case "Email":
            email = ParserUtil.parseEmail(Optional.of(userInput))
                    .orElse(new Email(Email.DEFAULT_EMAIL));
            break;
        case "Address":
            address = ParserUtil.parseAddress(Optional.of(userInput))
                      .orElse(new Address(Address.DEFAULT_ADDRESS));
            break;
        case "Price":
            price = ParserUtil.parsePrice(Optional.of(userInput))
                    .orElse(new Price(Price.DEFAULT_PRICE));
            break;
        case "Rating":
            rating = ParserUtil.parseRating(Optional.of(userInput))
                     .orElse(new Rating(Rating.DEFAULT_RATING));
            break;
        default:
            throw new IllegalArgumentException();
        }
    }
}
