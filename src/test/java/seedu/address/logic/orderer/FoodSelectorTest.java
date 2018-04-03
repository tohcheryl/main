package seedu.address.logic.orderer;

import static seedu.address.testutil.TypicalFoods.getTypicalAddressBook;

import org.junit.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;


public class FoodSelectorTest {
    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void constructor_withArguments_success() {
        Index index = FoodSelector.select(model);
    }
}
