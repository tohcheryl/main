package seedu.address.logic.orderer;

import static org.junit.Assert.assertNotNull;
import static seedu.address.testutil.TypicalFoods.getTypicalAddressBook;

import org.junit.Test;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;

//@@author samzx

public class EmailManagerTest {
    private static final int VALID_MODEL_FOOD_INDEX = 0;
    private static final String VALID_UUID = "f64f2940-fae4-11e7-8c5f-ef356f279131";
    private static final String VALID_MESSAGE = "Message";

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void constructor_withArguments_success() {
        EmailManager emailManager = new EmailManager(model.getUserProfile(),
                model.getAddressBook().getFoodList().get(VALID_MODEL_FOOD_INDEX), VALID_UUID, VALID_MESSAGE);
        assertNotNull(emailManager);
    }

    @Test
    public void email_execution_success() {
        EmailManager emailManager = new EmailManager(model.getUserProfile(),
                model.getAddressBook().getFoodList().get(VALID_MODEL_FOOD_INDEX), VALID_UUID, VALID_MESSAGE);
        assertEmailSuccess(emailManager);
    }

    /**
     * Executes the email method of the given {@code emailManager} and asserts success
     * @param emailManager to execute email
     */
    private static void assertEmailSuccess(EmailManager emailManager) {
        try {
            emailManager.email();
        } catch (Exception e) {
            throw new AssertionError("Email should not fail.");
        }
    }
}
