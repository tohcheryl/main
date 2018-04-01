package seedu.address.model.session;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.commands.AddCommand;
import seedu.address.model.food.Name;

public class SessionAddCommandTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void parseInputForField_name_success() throws IllegalArgumentException, IllegalValueException {
        SessionAddCommand session = new SessionAddCommand(new AddCommand(null), null);
        session.parseInputForField(Name.class, "Some Name");
    }

    @Test
    public void parseInputForField_invalidClass_throwsIllegalArgumentException()
            throws IllegalArgumentException, IllegalValueException {
        thrown.expect(IllegalArgumentException.class);
        SessionAddCommand session = new SessionAddCommand(new AddCommand(null), null);
        session.parseInputForField(String.class, "Some Input");
    }
}
