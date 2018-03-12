package seedu.address.logic.commands;

import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.Before;
import org.junit.Test;

import seedu.address.logic.CommandHistory;
import seedu.address.logic.UndoRedoStack;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Food;
import seedu.address.testutil.PersonBuilder;

/**
 * Contains integration tests (interaction with the Model) for {@code AddCommand}.
 */
public class AddCommandIntegrationTest {

    private Model model;

    @Before
    public void setUp() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    }

    @Test
    public void execute_newPerson_success() throws Exception {
        Food validFood = new PersonBuilder().build();

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.addPerson(validFood);

        assertCommandSuccess(prepareCommand(validFood, model), model,
                String.format(AddCommand.MESSAGE_SUCCESS, validFood), expectedModel);
    }

    @Test
    public void execute_duplicatePerson_throwsCommandException() {
        Food foodInList = model.getAddressBook().getPersonList().get(0);
        assertCommandFailure(prepareCommand(foodInList, model), model, AddCommand.MESSAGE_DUPLICATE_PERSON);
    }

    /**
     * Generates a new {@code AddCommand} which upon execution, adds {@code food} into the {@code model}.
     */
    private AddCommand prepareCommand(Food food, Model model) {
        AddCommand command = new AddCommand(food);
        command.setData(model, new CommandHistory(), new UndoRedoStack());
        return command;
    }
}
