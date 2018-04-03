package seedu.address.model.session;

import java.util.Arrays;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.address.commons.core.EventsCenter;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.commands.AddCommand;
import seedu.address.logic.commands.Command;
import seedu.address.model.food.Address;
import seedu.address.model.food.Email;
import seedu.address.model.food.Name;
import seedu.address.model.food.Phone;
import seedu.address.model.food.Price;
import seedu.address.model.food.Rating;
import seedu.address.model.food.allergy.Allergy;
import seedu.address.model.tag.Tag;

public class SessionAddCommandTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void parseInputForField_name_success() throws IllegalArgumentException, IllegalValueException {
        SessionAddCommand session = new SessionAddCommand(new AddCommand(null), null);
        session.parseInputForField(Name.class, "Some Name");
    }

    @Test
    public void parseInputForField_address_success() throws IllegalArgumentException, IllegalValueException {
        SessionAddCommand session = new SessionAddCommand(new AddCommand(null), null);
        session.parseInputForField(Address.class, "Some Address");
    }

    @Test
    public void parseInputForField_phone_success() throws IllegalArgumentException, IllegalValueException {
        SessionAddCommand session = new SessionAddCommand(new AddCommand(null), null);
        session.parseInputForField(Phone.class, "123124913");
    }

    @Test
    public void parseInputForField_email_success() throws IllegalArgumentException, IllegalValueException {
        SessionAddCommand session = new SessionAddCommand(new AddCommand(null), null);
        session.parseInputForField(Email.class, "email@email.com");
    }

    @Test
    public void parseInputForField_price_success() throws IllegalArgumentException, IllegalValueException {
        SessionAddCommand session = new SessionAddCommand(new AddCommand(null), null);
        session.parseInputForField(Price.class, "12");
    }

    @Test
    public void parseInputForField_rating_success() throws IllegalArgumentException, IllegalValueException {
        SessionAddCommand session = new SessionAddCommand(new AddCommand(null), null);
        session.parseInputForField(Rating.class, "5");
    }

    @Test
    public void parseInputForField_invalidClass_throwsIllegalArgumentException()
            throws IllegalArgumentException, IllegalValueException {
        thrown.expect(IllegalArgumentException.class);
        SessionAddCommand session = new SessionAddCommand(new AddCommand(null), null);
        session.parseInputForField(String.class, "Some Input");
    }

    @Test
    public void parseInputForMultivaluedField_tag_success() throws IllegalValueException {
        SessionAddCommandStub session = new SessionAddCommandStub(new AddCommand(null), null);
        session.parseInputForMultivaluedField(Tag.class);
    }

    @Test
    public void parseInputForMultivaluedField_allergy_success() throws IllegalValueException {
        SessionAddCommandStub session = new SessionAddCommandStub(new AddCommand(null), null);
        session.parseInputForMultivaluedField(Allergy.class);
    }

    @Test
    public void parseInputForMultivaluedField_tag_throwsIllegalArgumentException()
            throws IllegalArgumentException, IllegalValueException {
        thrown.expect(IllegalArgumentException.class);
        SessionAddCommandStub session = new SessionAddCommandStub(new AddCommand(null), null);
        session.parseInputForMultivaluedField(String.class);
    }

    public class SessionAddCommandStub extends SessionAddCommand {

        public SessionAddCommandStub(Command command, EventsCenter eventsCenter) {
            super(command, eventsCenter);
            temporaryStrings = Arrays.asList("peruvian", "seafood");
        }

        public void setPromptIndex() {
            promptIndex = 6;
            assert prompts.get(promptIndex).getField().getSimpleName().equals(Tag.class.getSimpleName());
        }
    }
}
