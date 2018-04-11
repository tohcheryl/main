package seedu.address.logic.orderer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import static seedu.address.testutil.TypicalFoods.getTypicalAddressBook;

import org.junit.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.OrderCommand;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;

//@@author {samzx}

public class FoodSelectorTest {
    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void constructor_withArguments_success() {
        FoodSelector fs = new FoodSelector();
        try {
            Index index = fs.select(model);
            assertNotNull(index);
        } catch (CommandException ce) {
            assertEquals(ce, OrderCommand.MESSAGE_SELECT_FAIL);
        }
    }
}
