package seedu.address.model.session;

import java.util.Arrays;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.address.commons.core.EventsCenter;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.commands.AddCommand;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.ModelManager;
import seedu.address.model.food.Address;
import seedu.address.model.food.Email;
import seedu.address.model.food.Name;
import seedu.address.model.food.Phone;
import seedu.address.model.food.Price;
import seedu.address.model.food.Rating;
import seedu.address.model.food.allergy.Allergy;
import seedu.address.model.tag.Tag;
import seedu.address.testutil.TypicalFoods;

//@@author jaxony
public class SessionAddCommandTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void parseInputForField_name_success() throws IllegalArgumentException, IllegalValueException {
        SessionAddCommand session = new SessionAddCommand(new AddCommand(null), null);
        session.parseInputForField(Name.CLASS_NAME, "Some Name");
    }

    @Test
    public void parseInputForField_address_success() throws IllegalArgumentException, IllegalValueException {
        SessionAddCommand session = new SessionAddCommand(new AddCommand(null), null);
        session.parseInputForField(Address.CLASS_NAME, "Some Address");
    }

    @Test
    public void parseInputForField_phone_success() throws IllegalArgumentException, IllegalValueException {
        SessionAddCommand session = new SessionAddCommand(new AddCommand(null), null);
        session.parseInputForField(Phone.CLASS_NAME, "123124913");
    }

    @Test
    public void parseInputForField_email_success() throws IllegalArgumentException, IllegalValueException {
        SessionAddCommand session = new SessionAddCommand(new AddCommand(null), null);
        session.parseInputForField(Email.CLASS_NAME, "email@email.com");
    }

    @Test
    public void parseInputForField_price_success() throws IllegalArgumentException, IllegalValueException {
        SessionAddCommand session = new SessionAddCommand(new AddCommand(null), null);
        session.parseInputForField(Price.CLASS_NAME, "12");
    }

    @Test
    public void parseInputForField_rating_success() throws IllegalArgumentException, IllegalValueException {
        SessionAddCommand session = new SessionAddCommand(new AddCommand(null), null);
        session.parseInputForField(Rating.CLASS_NAME, "5");
    }

    @Test
    public void parseInputForField_invalidClass_throwsIllegalArgumentException()
            throws IllegalArgumentException, IllegalValueException {
        thrown.expect(IllegalArgumentException.class);
        SessionAddCommand session = new SessionAddCommand(new AddCommand(null), null);
        session.parseInputForField(String.class.getName(), "Some Input");
    }

    @Test
    public void parseInputForMultivaluedField_tag_success() throws IllegalValueException {
        SessionAddCommandStub session = new SessionAddCommandStub(new AddCommand(null), null);
        session.parseInputForMultivaluedField(Tag.CLASS_NAME);
    }

    @Test
    public void parseInputForMultivaluedField_allergy_success() throws IllegalValueException {
        SessionAddCommandStub session = new SessionAddCommandStub(new AddCommand(null), null);
        session.parseInputForMultivaluedField(Allergy.CLASS_NAME);
    }

    @Test
    public void parseInputForMultivaluedField_string_throwsIllegalArgumentException()
            throws IllegalArgumentException, IllegalValueException {
        thrown.expect(IllegalArgumentException.class);
        SessionAddCommandStub session = new SessionAddCommandStub(new AddCommand(null), null);
        session.parseInputForMultivaluedField(String.class.getName());
    }

    @Test
    public void finishCommand_success() throws CommandException {
        Command command = new AddCommand(null);
        command.setData(new ModelManager(), null, null);
        SessionAddCommandStub session = new SessionAddCommandStub(command, null);
        session.finishCommand();
    }

    /**
     * A stub used to test SessionAddCommand with hardcoded values
     */
    private class SessionAddCommandStub extends SessionAddCommand {

        public SessionAddCommandStub(Command command, EventsCenter eventsCenter) {
            super(command, eventsCenter);
            stringBuffer = Arrays.asList("peruvian", "seafood");

            name = TypicalFoods.BACON.getName();
            phone = TypicalFoods.BACON.getPhone();
            email = TypicalFoods.BACON.getEmail();
            address = TypicalFoods.BACON.getAddress();
            price = TypicalFoods.BACON.getPrice();
            rating = TypicalFoods.BACON.getRating();
            tagSet = TypicalFoods.BACON.getTags();
            allergySet = TypicalFoods.BACON.getAllergies();
        }
    }
}
