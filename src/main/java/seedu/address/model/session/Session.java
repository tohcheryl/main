package seedu.address.model.session;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Logger;

import seedu.address.commons.core.EventsCenter;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.events.ui.EndActiveSessionEvent;
import seedu.address.commons.events.ui.NewResultAvailableEvent;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.commands.AddCommand;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.Prompt;
import seedu.address.logic.parser.ParserUtil;
import seedu.address.model.food.Address;
import seedu.address.model.food.Email;
import seedu.address.model.food.Food;
import seedu.address.model.food.Name;
import seedu.address.model.food.Phone;
import seedu.address.model.food.Price;
import seedu.address.model.food.Rating;
import seedu.address.model.tag.Tag;

/**
 *
 */
public class Session {
    public static final String END_MULTIVALUE_FIELD = "n";
    private Command command;
    private int promptIndex;
    private final List<Prompt> prompts;
    private final Map<String, Object> results;
    private final Logger logger = LogsCenter.getLogger(Session.class);
    private final EventsCenter eventsCenter;
    private boolean isParsingMultivaluedField;
    private Collection<String> temporaryStrings;

    public Session(Command command, EventsCenter eventsCenter) {
        this.command = command;
        promptIndex = 0;
        prompts = this.command.getPrompts();
        this.eventsCenter = eventsCenter;
        results = new HashMap<>();
        isParsingMultivaluedField = false;
    }

    /**
     *
     */
    private void showPrompt() {
        try {
            Prompt p = prompts.get(promptIndex);
            eventsCenter.post(new NewResultAvailableEvent(p.getMessage(), true));
        } catch (IndexOutOfBoundsException e) {
            eventsCenter.post(new NewResultAvailableEvent("Thanks!", true));
        }
    }

    /**
     *
     */
    public void end() {
        assert command.getClass().getName().equals("AddCommand");
        AddCommand addCommand = (AddCommand) command;

        Name name = (Name) results.get("Name");
        Email email = (Email) results.get("Email");
        Phone phone = (Phone) results.get("Phone");
        Address address = (Address) results.get("Address");
        Rating rating = (Rating) results.get("Rating");
        Price price = (Price) results.get("Price");
        Set<Tag> tags = (Set<Tag>) results.get("Tag");

        addCommand.setFood(new Food(name, phone, email, address, price, rating, tags));
        eventsCenter.post(new EndActiveSessionEvent());
    }

    /**
     *
     * @param userInput
     */
    public void interpretUserInput(String userInput) {
        logger.info("Received user input in current Session: " + userInput);
        Prompt p = prompts.get(promptIndex);
        try {
            if (p.isMultiValued) {
                if (isParsingMultivaluedField) {
                    if (userInput.toLowerCase().equals(END_MULTIVALUE_FIELD)) {
                        results.put(p.getField().getSimpleName(), ParserUtil.parseTags(temporaryStrings));
                        // end here
                        isParsingMultivaluedField = false;
                        promptIndex++;
                        showPrompt();
                    } else {
                        temporaryStrings.add(userInput);
                        askForNextMultivalue();
                    }
                } else {
                    // start multivalue parsing
                    temporaryStrings = new HashSet<>();
                    isParsingMultivaluedField = true;
                }
            } else {
                results.put(p.getField().getName(), parseText(p.getField(), userInput));
                promptIndex++;
                showPrompt();
            }
        } catch (IllegalValueException ive) {
            eventsCenter.post(new NewResultAvailableEvent("Please try again: " + ive.getMessage(), false));
        } catch (Exception e) {

        }
    }

    private void askForNextMultivalue() {
        eventsCenter.post(new NewResultAvailableEvent("And anything else? Type (n/N) to stop here.", true));
    }

    /**
     * Parses user input based on the type of command the interaction is using.
     *
     * @param userInput
     * @return
     */
    private Object parseText(Class field, String userInput) throws IllegalValueException, Exception {
        assert command.getClass().getName().equals("AddCommand");
        switch (field.getSimpleName()) {
        case "Name":
            return ParserUtil.parseName(Optional.of(userInput));
        case "Phone":
            return ParserUtil.parsePhone(Optional.of(userInput));
        case "Email":
            return ParserUtil.parseEmail(Optional.of(userInput));
        case "Address":
            return ParserUtil.parseAddress(Optional.of(userInput));
        case "Price":
            return ParserUtil.parsePrice(Optional.of(userInput));
        case "Rating":
            return ParserUtil.parseRating(Optional.of(userInput));
        default:
            throw new Exception("Incorrect class");
        }
    }

    /**
     *
     */
    public void start() {
        showPrompt();
    }
}
