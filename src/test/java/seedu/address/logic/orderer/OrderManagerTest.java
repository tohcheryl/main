package seedu.address.logic.orderer;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_BANANA;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_BANANA;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BANANA;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_BANANA;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PRICE_BANANA;
import static seedu.address.logic.commands.CommandTestUtil.VALID_RATING_BANANA;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_FRIED;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashSet;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

import seedu.address.model.food.Address;
import seedu.address.model.food.Food;
import seedu.address.model.food.Name;
import seedu.address.model.food.Phone;
import seedu.address.model.food.allergy.Allergy;
import seedu.address.model.user.UserProfile;
import seedu.address.testutil.Assert;
import seedu.address.testutil.FoodBuilder;

//@@author samzx

public class OrderManagerTest {
    private static final String INVALID_URL = "";
    private static final String LOOSE_CONNECTION = "http://www.120391820938109231023.com/";

    private static final String USER_NAME = "Alice";
    private static final String USER_PHONE = "12345678";
    private static final String USER_ADDRESS = Address.DEFAULT_ADDRESS;
    private static final String USER_ALLERGY = "lactose";
    private static final String USER_NOT_ALLERGIC = "peanut";
    private static final String MESSAGE_HTTP_POST_FAILED =
            "Order Manager failed to update server with POST request";
    private static final String MESSAGE_SHOULD_NOT_THROW_ERROR =
            "Order Manager should not have thrown error";
    private UserProfile validUser = new UserProfile(
            new Name(USER_NAME),
            new Phone(USER_PHONE),
            new Address(USER_ADDRESS),
            new HashSet<>(
                    Arrays.asList(new Allergy(USER_ALLERGY))
            )
    );

    @Test
    public void order_withModel_success() throws Exception {
        Food validFood = new FoodBuilder().withName(VALID_NAME_BANANA).withPhone(VALID_PHONE_BANANA)
                .withEmail(VALID_EMAIL_BANANA).withAddress(VALID_ADDRESS_BANANA)
                .withPrice(VALID_PRICE_BANANA).withRating(VALID_RATING_BANANA).withTags(VALID_TAG_FRIED)
                .withAllergies(USER_NOT_ALLERGIC).build();

        OrderManager orderManager = new OrderManager(validUser, validFood);

        assertOrderResolvesCorrectly(orderManager, MESSAGE_HTTP_POST_FAILED);

    }

    @Test
    public void netIsAvailable_invalidUrl_failure() throws MalformedURLException {
        Assert.assertThrows(RuntimeException.class, () -> OrderManager.netIsAvailable(INVALID_URL));
    }

    @Test
    public void netIsAvailable_badConnection_failure() throws MalformedURLException {
        assertFalse(OrderManager.netIsAvailable(LOOSE_CONNECTION));
    }

    @Test
    public void netIsAvailable_validUrl_success() {
        assertTrue(OrderManager.netIsAvailable(OrderManager.REMOTE_SERVER));
    }

    /**
     * Sends a HTTP POST Request to the server responsible for exposing message to public APIs
     * @return whether the message was successfully posted and exposed
     */
    private boolean verifyPostConfirmation(String orderId) throws Exception {
        URL url = new URL(OrderManager.REMOTE_SERVER + OrderManager.ORDER_PATH + orderId);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        InputStream inputStream = con.getInputStream();
        String incomingString = IOUtils.toString(inputStream, OrderManager.CHARSET_ENCODING);
        con.disconnect();

        String expectedContents = String.format(OrderManager.CANNED_SPEECH_MESSAGE,
                USER_NAME, VALID_NAME_BANANA, USER_ADDRESS);
        return incomingString.contains(expectedContents);
    }

    /**
     * Executes order method for order manager and checks the correct message is thrown
     * @param orderManager to execute order method
     * @param httpPostFail message if unable to contact server with http post
     * @throws Exception
     */
    private void assertOrderResolvesCorrectly(OrderManager orderManager, String httpPostFail) throws Exception {
        try {
            orderManager.order();
            assert(verifyPostConfirmation(orderManager.getOrderId()));
        } catch (AssertionError e) {
            assert(e.getMessage().isEmpty());
            throw new Exception(httpPostFail);
        } catch (Exception e) {
            throw new Exception(MESSAGE_SHOULD_NOT_THROW_ERROR);
        }
    }
}
