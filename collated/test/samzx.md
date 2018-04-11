# samzx
###### /java/seedu/address/logic/orderer/FoodSelectorTest.java
``` java

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
```
###### /java/seedu/address/logic/orderer/OrderManagerTest.java
``` java

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
```
###### /java/seedu/address/logic/parser/OrderCommandParserTest.java
``` java

public class OrderCommandParserTest {
    private OrderCommandParser parser = new OrderCommandParser();

    @Test
    public void parse_emptySting() throws ParseException {
        OrderCommand expectedCommand = parser.parse("");
        assertParseSuccess(parser, "", expectedCommand);
    }

    @Test
    public void parse_validIndex() throws ParseException {
        OrderCommand expectedCommand = parser.parse("1");
        assertParseSuccess(parser, "1", expectedCommand);
    }

    @Test
    public void parse_invalidIndex_failure() throws ParseException {
        assertParseFailure(parser, "-1", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                OrderCommand.MESSAGE_USAGE));
    }
}
```
###### /java/seedu/address/logic/commands/OrderCommandTest.java
``` java

public class OrderCommandTest {
    private static final Index VALID_INDEX = Index.fromZeroBased(1);
    private static final Index NULL_INDEX = null;

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void constructor_index_success() {
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
        try {
            CommandResult result = orderCommand.execute();
            assertThat(result.feedbackToUser, containsString(String.format(OrderCommand.MESSAGE_SUCCESS,
                    food.getName())));
        } catch (Exception e) {
            assertThat(e.getMessage(), containsString(String.format(OrderCommand.MESSAGE_SELECT_INDEX_FAIL,
                    food.getName())));
        }
    }

    @Test
    public void execute_orderWithoutIndex_success() throws CommandException {
        OrderCommand orderCommand = getOrderCommandForIndex(NULL_INDEX, model);
        try {
            CommandResult result = orderCommand.execute();
            assertThat(result.feedbackToUser, containsString(String.format(OrderCommand.MESSAGE_SUCCESS,
                    "", "")));
        } catch (Exception e) {
            assertThat(e.getMessage(), containsString(String.format(OrderCommand.MESSAGE_EMAIL_FAIL_FOOD, "")));
            assertThat(e.getMessage(), containsString(String.format(OrderCommand.MESSAGE_EMAIL_FAIL_FOOD, "")));
        }
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
     * Executes the given {@code command}, confirms that <br>
     * - the result message matches {@code expectedMessage} <br>
     * - the {@code actualModel} matches {@code expectedModel}
     */
    public static void assertCommandSuccess(Command command, Model actualModel, String expectedMessage) {
        try {
            CommandResult result = command.execute();
            assertEquals(expectedMessage, result.feedbackToUser);
        } catch (CommandException ce) {
            throw new AssertionError("Execution of command should not fail.", ce);
        }
    }

}
```
###### /java/seedu/address/model/food/RatingTest.java
``` java

public class RatingTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> new Rating(null));
    }

    @Test
    public void constructor_invalidRating_throwsIllegalArgumentException() {
        String invalidRating = "10";
        Assert.assertThrows(IllegalArgumentException.class, () -> new Rating(invalidRating));
    }

    @Test
    public void isValidRating() {
        // null rating
        Assert.assertThrows(NullPointerException.class, () -> Rating.isValidRating(null));

        // invalid rating
        assertFalse(Rating.isValidRating("-1"));
        assertFalse(Rating.isValidRating("10"));
        assertFalse(Rating.isValidRating("6"));

        // valid rating
        assertTrue(Rating.isValidRating("1"));
        assertTrue(Rating.isValidRating("5"));
        assertTrue(Rating.isValidRating("0"));
    }

    @Test
    public void getValue() {
        Rating rating = new Rating("0");
        assertEquals("0", rating.value);
    }

    @Test
    public void toString_validRating_returnsString() {
        Rating rating = new Rating("0");
        assertEquals("0", rating.toString());
    }

    @Test
    public void equals_validRating_returnsEqual() {
        Rating rating = new Rating("3");
        Rating rating2 = new Rating("3");
        assertEquals(rating, rating2);
    }

    @Test
    public void hashCode_validRating_returnsHashCode() {
        Rating p = new Rating("5");
        Rating p2 = new Rating("5");
        assertEquals(p.hashCode(), p2.hashCode());
    }
}
```
