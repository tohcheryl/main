package seedu.address.logic.orderer;

import static seedu.address.testutil.TypicalFoods.getTypicalAddressBook;

import org.junit.Test;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;


public class OrderManagerTest {
    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void constructor_withArguments_success() {
        OrderManager orderManager = new OrderManager(
                model.getAddressBook().getUserProfile(),
                model.getAddressBook().getFoodList().get(0)
        );
    }
}
