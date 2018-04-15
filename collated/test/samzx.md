# samzx
###### \java\seedu\address\logic\commands\OrderCommandTest.java
``` java

public class OrderCommandTest {
    private static final Index VALID_INDEX = Index.fromZeroBased(1);
    private static final Index NULL_INDEX = null;
    private static final String EMPTY_STRING = "";

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void constructor_index_success() {
        OrderCommand indexedOrderCommand = new OrderCommand(VALID_INDEX);
        OrderCommand nullOrderCommand = new OrderCommand(NULL_INDEX);

        assertNotNull(indexedOrderCommand);
        assertNotNull(nullOrderCommand);
    }

    @Test
    public void equals_duplicate_success() {
        OrderCommand indexedOrderCommand = new OrderCommand(VALID_INDEX);
        OrderCommand indexedOrderCommand2 = new OrderCommand(VALID_INDEX);

        OrderCommand nullOrderCommand = new OrderCommand(NULL_INDEX);
        OrderCommand nullOrderCommand2 = new OrderCommand(NULL_INDEX);

        assertEquals(indexedOrderCommand, indexedOrderCommand2);
        assertEquals(nullOrderCommand, nullOrderCommand2);
    }

    @Test
    public void execute_orderWithIndex_success() throws CommandException {
        OrderCommand orderCommand = getOrderCommandForIndex(VALID_INDEX, model);
        Food food = model.getAddressBook().getFoodList().get(VALID_INDEX.getZeroBased());
        assertExecuteResolvesCorrectly(orderCommand,
                String.format(OrderCommand.MESSAGE_SUCCESS, food.getName()),
                String.format(OrderCommand.MESSAGE_SELECT_INDEX_FAIL, food.getName()));
    }

    @Test
    public void execute_orderWithoutIndex_success() throws CommandException {
        OrderCommand orderCommand = getOrderCommandForIndex(NULL_INDEX, model);
        assertExecuteResolvesCorrectly(orderCommand,
                String.format(OrderCommand.MESSAGE_SUCCESS, "", ""),
                String.format(OrderCommand.MESSAGE_EMAIL_FAIL_FOOD, EMPTY_STRING));
    }

    /**
     * Generates a new AddCommand with the details of the given food.
     */
    private OrderCommand getOrderCommandForIndex(Index index, Model model) {
        OrderCommand command = new OrderCommand(index);
        command.setData(model, new CommandHistory(), new UndoRedoStack());
        return command;
    }

    /**
     * Execute order command and ensures that the correct response is met when succeeding or failing
     * @param orderCommand to execute
     * @param success message if execute success
     * @param failure message if execute fails
     */
    private void assertExecuteResolvesCorrectly(OrderCommand orderCommand, String success, String failure) {
        try {
            CommandResult result = orderCommand.execute();
            assertThat(result.feedbackToUser, containsString(success));
        } catch (Exception e) {
            assertThat(e.getMessage(), containsString(failure));
        }
    }
}
```
###### \java\seedu\address\logic\orderer\EmailManagerTest.java
``` java

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
```
###### \java\seedu\address\logic\orderer\FoodSelectorTest.java
``` java

public class FoodSelectorTest {
    private static final String USER_NAME = "Alice";
    private static final String USER_PHONE = "12345678";
    private static final String USER_ALLERGY = "lactose";
    private static final String USER_NOT_ALLERGIC = "peanut";
    private static final String MESSAGE_SHOULD_AVOID_ALLERGIC =
            "Food selector should have avoided a food the user is allergic to!";
    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    private UserProfile validUser = new UserProfile(
            new Name(USER_NAME),
            new Phone(USER_PHONE),
            new Address(Address.DEFAULT_ADDRESS),
            new HashSet<>(
                    Arrays.asList(new Allergy(USER_ALLERGY))
            )
    );

    @Test
    public void constructor_withoutArguments_success() {
        FoodSelector foodSelector = new FoodSelector();
        assertNotNull(foodSelector);
    }

    @Test
    public void selectIndex_withModel_validIndex() throws Exception {
        FoodSelector fs = new FoodSelector();
        Index index = fs.selectIndex(model);
        assertNotNull(index);
    }

    @Test
    public void selectIndex_withAllAllergy_invalidIndex() throws Exception {
        AddressBook allAllergicAddressBook = new AddressBook();
        allAllergicAddressBook.initUserProfile(validUser);

        Food allergicFood = new FoodBuilder().withName(VALID_NAME_BANANA).withPhone(VALID_PHONE_BANANA)
                .withEmail(VALID_EMAIL_BANANA).withAddress(VALID_ADDRESS_BANANA)
                .withPrice(VALID_PRICE_BANANA).withRating(VALID_RATING_BANANA).withTags(VALID_TAG_FRIED)
                .withAllergies(USER_ALLERGY).build();

        allAllergicAddressBook.addFood(allergicFood);
        Model model = new ModelManager(allAllergicAddressBook, new UserPrefs());

        FoodSelector foodSelector = new FoodSelector();

        assertAvoidsAllergies(foodSelector, model, OrderCommand.MESSAGE_SELECT_FAIL);
    }

    @Test
    public void selectIndex_nonAllergy_indexOne() throws Exception {
        AddressBook allAllergicAddressBook = new AddressBook();
        allAllergicAddressBook.initUserProfile(validUser);

        Food foodIsAllergic = new FoodBuilder().withName(VALID_NAME_BANANA).withPhone(VALID_PHONE_BANANA)
                .withEmail(VALID_EMAIL_BANANA).withAddress(VALID_ADDRESS_BANANA)
                .withPrice(VALID_PRICE_BANANA).withRating(VALID_RATING_BANANA).withTags(VALID_TAG_FRIED)
                .withAllergies(USER_ALLERGY).build();
        Food foodIsNotAllergic = new FoodBuilder().withName(VALID_NAME_APPLE).withPhone(VALID_PHONE_BANANA)
                .withEmail(VALID_EMAIL_BANANA).withAddress(VALID_ADDRESS_BANANA)
                .withPrice(VALID_PRICE_BANANA).withRating(VALID_RATING_BANANA).withTags(VALID_TAG_FRIED)
                .withAllergies(USER_NOT_ALLERGIC).build();

        allAllergicAddressBook.addFood(foodIsAllergic);
        allAllergicAddressBook.addFood(foodIsNotAllergic);

        Model model = new ModelManager(allAllergicAddressBook, new UserPrefs());

        FoodSelector fs = new FoodSelector();

        final int expectedSelectedIndex = 1;

        assertEquals(expectedSelectedIndex, fs.selectIndex(model).getZeroBased());
    }

    /**
     * Executes selectIndex method for food selector. Given a model with all allergic, makes sure that an exception
     * is thrown is the desired message, to avoid ordering a food that the user is allergic to, and to notify them
     * @param foodSelector that will have selectIndex executed
     * @param model which must have all foods be allergic by the userProfile
     * @param expectedMessage when unable to order food
     * @throws Exception when an allergic food is ordered
     */
    private static void assertAvoidsAllergies(FoodSelector foodSelector, Model model, String expectedMessage)
            throws Exception {
        try {
            foodSelector.selectIndex(model);
            throw new AssertionError(MESSAGE_SHOULD_AVOID_ALLERGIC);
        } catch (AssertionError e) {
            throw new Exception(MESSAGE_SHOULD_AVOID_ALLERGIC);
        } catch (Exception e) {
            assertEquals(e.getMessage(), expectedMessage);
        }
    }
}
```
###### \java\seedu\address\logic\orderer\OrderManagerTest.java
``` java

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
```
###### \java\seedu\address\logic\parser\OrderCommandParserTest.java
``` java

public class OrderCommandParserTest {
    private static final String EMPTY_STRING = "";
    private static final String VALID_INDEX = "1";
    private static final String INVALID_INDEX = "-1";

    private OrderCommandParser parser = new OrderCommandParser();

    @Test
    public void parse_emptySting() throws ParseException {
        OrderCommand expectedCommand = parser.parse(EMPTY_STRING);
        assertParseSuccess(parser, EMPTY_STRING, expectedCommand);
    }

    @Test
    public void parse_validIndex() throws ParseException {
        OrderCommand expectedCommand = parser.parse(VALID_INDEX);
        assertParseSuccess(parser, VALID_INDEX, expectedCommand);
    }

    @Test
    public void parse_invalidIndex_failure() throws ParseException {
        assertParseFailure(parser, INVALID_INDEX, String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                OrderCommand.MESSAGE_USAGE));
    }
}
```
###### \java\seedu\address\model\food\RatingTest.java
``` java

public class RatingTest {
    private static final String VALID_RATING = "0";
    private static final String INVALID_RATING = "6";
    @Test
    public void constructor_null_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> new Rating(null));
    }

    @Test
    public void constructor_invalidRating_throwsIllegalArgumentException() {

        Assert.assertThrows(IllegalArgumentException.class, () -> new Rating(INVALID_RATING));
    }

    @Test
    public void isValidRating() {
        // null rating
        Assert.assertThrows(NullPointerException.class, () -> Rating.isValidRating(null));

        // invalid rating
        assertFalse(Rating.isValidRating("-1"));
        assertFalse(Rating.isValidRating("6"));

        // valid rating
        assertTrue(Rating.isValidRating("0"));
        assertTrue(Rating.isValidRating("5"));
    }

    @Test
    public void displayString_withStars_displaysStars() {
        assertEquals(Rating.displayString("0"), "☆☆☆☆☆");
        assertEquals(Rating.displayString("3"), "★★★☆☆");
        assertEquals(Rating.displayString("5"), "★★★★★");

    }

    @Test
    public void getValue() {

        Rating rating = new Rating(VALID_RATING);
        assertEquals(VALID_RATING, rating.value);
    }

    @Test
    public void toString_validRating_returnsString() {
        Rating rating = new Rating(VALID_RATING);
        assertEquals(VALID_RATING, rating.toString());
    }

    @Test
    public void equals_validRating_returnsEqual() {
        Rating rating = new Rating(VALID_RATING);
        Rating rating2 = new Rating(VALID_RATING);
        assertEquals(rating, rating2);
    }

    @Test
    public void hashCode_validRating_returnsHashCode() {
        Rating p = new Rating(VALID_RATING);
        Rating p2 = new Rating(VALID_RATING);
        assertEquals(p.hashCode(), p2.hashCode());
    }
}
```
