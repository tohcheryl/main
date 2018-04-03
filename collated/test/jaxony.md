# jaxony
###### /java/systemtests/AddCommandSystemTest.java
``` java
    @Test
    public void addInteractive() {
        /* -------------------------- Perform add in interactive mode ------------------------------ */
        assertCommandSuccessWithoutSync(AddCommand.COMMAND_WORD, getModel(), AddCommand.prompts.get(0).getMessage());
    }
```
###### /java/systemtests/AddCommandSystemTest.java
``` java
    /**
     * Performs the same verification as {@code assertCommandSuccess(String, Food)} except asserts that
     * the,<br>
     * 1. Result display box displays {@code expectedResultMessage}.<br>
     * 2. {@code Model}, {@code Storage} and {@code FoodListPanel} equal to the corresponding components in
     * {@code expectedModel}.<br>
     * 3. Status bar does not change.<br>
     * @see AddCommandSystemTest#assertCommandSuccess(String, Food)
     */
    private void assertCommandSuccessWithoutSync(String command, Model expectedModel, String expectedResultMessage) {
        executeCommand(command);
        assertApplicationDisplaysExpected("", expectedResultMessage, expectedModel);
        assertSelectedCardUnchanged();
        assertCommandBoxShowsDefaultStyle();
        assertStatusBarUnchanged();
    }

```
###### /java/seedu/address/logic/LogicManagerTest.java
``` java
    @Test
    public void createNewSession_editCommand_throwsIllegalArgumentException() {
        thrown.expect(IllegalArgumentException.class);
        logic.createNewSession("edit");
    }

    @Test
    public void createNewSession_addCommand_success() {
        logic.createNewSession("add");
    }

    @Test
    public void isCommandInteractive_validCommand_true() throws ParseException {
        assertTrue(logic.isCommandInteractive("add"));
    }

    @Test
    public void isCommandInteractive_validCommand_false() throws ParseException {
        assertFalse(logic.isCommandInteractive("edit"));
    }

    @Test
    public void isCommandInteractive_invalidCommand_throwsParseException() throws ParseException {
        thrown.expect(ParseException.class);
        logic.isCommandInteractive("asdad");
    }
```
###### /java/seedu/address/logic/commands/AddCommandTest.java
``` java
        @Override
        public boolean isUserInActiveSession() {
            fail("This method should not be called.");
            return false;
        }

        @Override
        public void createNewSession(Command interactiveCommand) {
            fail("This method should not be called.");
        }

        @Override
        public CommandResult startSession() throws CommandException {
            fail("This method should not be called.");
            return null;
        }

        @Override
        public CommandResult interpretInteractiveUserInput(String commandText) throws CommandException {
            fail("This method should not be called.");
            return null;
        }

```
###### /java/seedu/address/model/session/SessionAddCommandTest.java
``` java
public class SessionAddCommandTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void parseInputForField_name_success() throws IllegalArgumentException, IllegalValueException {
        SessionAddCommand session = new SessionAddCommand(new AddCommand(null), null);
        session.parseInputForField(Name.class, "Some Name");
    }

    @Test
    public void parseInputForField_address_success() throws IllegalArgumentException, IllegalValueException {
        SessionAddCommand session = new SessionAddCommand(new AddCommand(null), null);
        session.parseInputForField(Address.class, "Some Address");
    }

    @Test
    public void parseInputForField_phone_success() throws IllegalArgumentException, IllegalValueException {
        SessionAddCommand session = new SessionAddCommand(new AddCommand(null), null);
        session.parseInputForField(Phone.class, "123124913");
    }

    @Test
    public void parseInputForField_email_success() throws IllegalArgumentException, IllegalValueException {
        SessionAddCommand session = new SessionAddCommand(new AddCommand(null), null);
        session.parseInputForField(Email.class, "email@email.com");
    }

    @Test
    public void parseInputForField_price_success() throws IllegalArgumentException, IllegalValueException {
        SessionAddCommand session = new SessionAddCommand(new AddCommand(null), null);
        session.parseInputForField(Price.class, "12");
    }

    @Test
    public void parseInputForField_rating_success() throws IllegalArgumentException, IllegalValueException {
        SessionAddCommand session = new SessionAddCommand(new AddCommand(null), null);
        session.parseInputForField(Rating.class, "5");
    }

    @Test
    public void parseInputForField_invalidClass_throwsIllegalArgumentException()
            throws IllegalArgumentException, IllegalValueException {
        thrown.expect(IllegalArgumentException.class);
        SessionAddCommand session = new SessionAddCommand(new AddCommand(null), null);
        session.parseInputForField(String.class, "Some Input");
    }

    @Test
    public void parseInputForMultivaluedField_tag_success() throws IllegalValueException {
        SessionAddCommandStub session = new SessionAddCommandStub(new AddCommand(null), null);
        session.parseInputForMultivaluedField(Tag.class);
    }

    @Test
    public void parseInputForMultivaluedField_allergy_success() throws IllegalValueException {
        SessionAddCommandStub session = new SessionAddCommandStub(new AddCommand(null), null);
        session.parseInputForMultivaluedField(Allergy.class);
    }

    @Test
    public void parseInputForMultivaluedField_string_throwsIllegalArgumentException()
            throws IllegalArgumentException, IllegalValueException {
        thrown.expect(IllegalArgumentException.class);
        SessionAddCommandStub session = new SessionAddCommandStub(new AddCommand(null), null);
        session.parseInputForMultivaluedField(String.class);
    }

    @Test
    public void finishCommand_success() throws CommandException {
        Command command = new AddCommand(null);
        command.setData(new ModelManager(), null, null);
        SessionAddCommandStub session = new SessionAddCommandStub(command, null);
        session.finishCommand();
    }

    /**
     * A stub used to test SessionAddCommand with hardcoded values
     */
    private class SessionAddCommandStub extends SessionAddCommand {

        public SessionAddCommandStub(Command command, EventsCenter eventsCenter) {
            super(command, eventsCenter);
            temporaryStrings = Arrays.asList("peruvian", "seafood");

            name = TypicalFoods.BACON.getName();
            phone = TypicalFoods.BACON.getPhone();
            email = TypicalFoods.BACON.getEmail();
            address = TypicalFoods.BACON.getAddress();
            price = TypicalFoods.BACON.getPrice();
            rating = TypicalFoods.BACON.getRating();
            tagSet = TypicalFoods.BACON.getTags();
            allergySet = TypicalFoods.BACON.getAllergies();
        }
    }
}
```
###### /java/seedu/address/model/session/SessionTest.java
``` java
public class SessionTest {
    private static final String BACON_NAME = TypicalFoods.BACON.getName().toString();
    private static final String BACON_WRONG_PHONE = "asdadn";
    private static final String BACON_PHONE = TypicalFoods.BACON.getPhone().toString();
    private static final String BACON_EMAIL = TypicalFoods.BACON.getEmail().toString();
    private static final String BACON_ADDRESS = TypicalFoods.BACON.getAddress().toString();
    private static final String BACON_RATING = TypicalFoods.BACON.getRating().toString();
    private static final String BACON_PRICE = TypicalFoods.BACON.getPrice().toString();

    private static final int INDEX_AFTER_NAME = 1;
    private static final String SUCCESS_MESSAGE_AFTER_NAME =
            AddCommand.prompts.get(INDEX_AFTER_NAME).getMessage();
    private static final int INDEX_AFTER_PHONE = 2;
    private static final String FAILURE_MESSAGE_AFTER_WRONG_PHONE =
            Session.TRY_AGAIN_MESSAGE + Phone.MESSAGE_PHONE_CONSTRAINTS;
    private static final String SUCCESS_MESSAGE_AFTER_PHONE =
            AddCommand.prompts.get(INDEX_AFTER_PHONE).getMessage();
    private static final int INDEX_AFTER_EMAIL = 3;
    private static final String SUCCESS_MESSAGE_AFTER_EMAIL =
            AddCommand.prompts.get(INDEX_AFTER_EMAIL).getMessage();
    private static final int INDEX_AFTER_ADDRESS = 4;
    private static final String SUCCESS_MESSAGE_AFTER_ADDRESS =
            AddCommand.prompts.get(INDEX_AFTER_ADDRESS).getMessage();
    private static final int INDEX_AFTER_PRICE = 5;
    private static final String SUCCESS_MESSAGE_AFTER_PRICE =
            AddCommand.prompts.get(INDEX_AFTER_PRICE).getMessage();
    private static final int INDEX_AFTER_RATING = 6;
    private static final String SUCCESS_MESSAGE_AFTER_RATING =
            AddCommand.prompts.get(INDEX_AFTER_RATING).getMessage();

    private static final String SUCCESS_MESSAGE_AFTER_FIRST_TAG =
            Session.ANYTHING_ELSE_MESSAGE;
    private static final String SUCCESS_MESSAGE_AFTER_SECOND_TAG =
            Session.ANYTHING_ELSE_MESSAGE;

    private static final int INDEX_AFTER_TAGS = 7;
    private static final String SUCCESS_MESSAGE_AFTER_TAGS =
            AddCommand.prompts.get(INDEX_AFTER_TAGS).getMessage();

    private static final String SUCCESS_MESSAGE_AFTER_FIRST_ALLERGY =
            Session.ANYTHING_ELSE_MESSAGE;


    @Test
    public void interpretUserInput_success() throws CommandException {
        // Session class is abstract so need to use a subclass
        // to test non-abstract methods in Session
        Session session = new SessionAddCommandStub(new AddCommand(null), EventsCenter.getInstance());
        assertEquals(SUCCESS_MESSAGE_AFTER_NAME, session.interpretUserInput(BACON_NAME).feedbackToUser);
        assertEquals(FAILURE_MESSAGE_AFTER_WRONG_PHONE, session.interpretUserInput(BACON_WRONG_PHONE).feedbackToUser);
        assertEquals(SUCCESS_MESSAGE_AFTER_PHONE, session.interpretUserInput(BACON_PHONE).feedbackToUser);
        assertEquals(SUCCESS_MESSAGE_AFTER_EMAIL, session.interpretUserInput(BACON_EMAIL).feedbackToUser);
        assertEquals(SUCCESS_MESSAGE_AFTER_ADDRESS, session.interpretUserInput(BACON_ADDRESS).feedbackToUser);
        assertEquals(SUCCESS_MESSAGE_AFTER_PRICE, session.interpretUserInput(BACON_PRICE).feedbackToUser);
        assertEquals(SUCCESS_MESSAGE_AFTER_RATING, session.interpretUserInput(BACON_RATING).feedbackToUser);

        // adding multi value fields
        assertEquals(SUCCESS_MESSAGE_AFTER_FIRST_TAG, session.interpretUserInput("meat").feedbackToUser);
        assertEquals(SUCCESS_MESSAGE_AFTER_SECOND_TAG, session.interpretUserInput("other").feedbackToUser);
        assertEquals(SUCCESS_MESSAGE_AFTER_TAGS, session.interpretUserInput(
                Session.END_MULTI_VALUE_FIELD).feedbackToUser);

        // adding multi value fields
        assertEquals(SUCCESS_MESSAGE_AFTER_FIRST_ALLERGY, session.interpretUserInput("animals").feedbackToUser);
        assertEquals(Session.SUCCESS_MESSAGE, session.interpretUserInput(Session.END_MULTI_VALUE_FIELD).feedbackToUser);
    }

    /**
     * A stub used to test SessionAddCommand with hardcoded values
     */
    private class SessionAddCommandStub extends SessionAddCommand {

        public SessionAddCommandStub(Command command, EventsCenter eventsCenter) {
            super(command, eventsCenter);
            command.setData(new ModelManager(), null, null);
        }
    }
}
```
