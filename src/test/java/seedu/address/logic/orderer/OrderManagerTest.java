package seedu.address.logic.orderer;

import static org.junit.Assert.assertEquals;

import static seedu.address.testutil.TypicalFoods.getTypicalAddressBook;

import java.io.IOException;
import javax.mail.MessagingException;

import org.junit.Test;

import seedu.address.logic.commands.OrderCommand;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.food.Food;

//@@author {samzx}

public class OrderManagerTest {
    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void constructor_withArguments_success() {
        Food validFood = model.getAddressBook().getFoodList().get(0);
        OrderManager orderManager = new OrderManager(
                model.getAddressBook().getUserProfile(),
                validFood
        );
        try {
            orderManager.order();
        } catch (MessagingException e) {
            assertEquals(e, String.format(OrderCommand.MESSAGE_EMAIL_FAIL_FOOD, validFood.getName()));
        } catch (IOException e) {
            assertEquals(e, String.format(OrderCommand.MESSAGE_DIAL_FAIL_FOOD, validFood.getName()));
        } catch (Exception e) {
            assertEquals(e, String.format(OrderCommand.MESSAGE_FAIL_FOOD, validFood.getName()));
        }
    }
}
