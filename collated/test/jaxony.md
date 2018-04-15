# jaxony
###### \java\guitests\guihandles\ChatPanelHandle.java
``` java
/**
 * A handler for the {@code ChatPanel} of the UI
 */
public class ChatPanelHandle extends NodeHandle<VBox> {

    public static final String CHAT_PANEL_ID = "#chatPanel";

    public ChatPanelHandle(VBox chatPanelNode) {
        super(chatPanelNode);
    }

    /**
     * Returns the last result text in the chat panel.
     */
    public String getText() {
        ObservableList<Node> messageContainers = getRootNode().getChildrenUnmodifiable();
        int numResults = messageContainers.size();
        if (numResults == 0) {
            return null;
        }
        HBox lastResultMessageHBox = (HBox) messageContainers.get(messageContainers.size() - 1);
        Label lastResultLabel = (Label) lastResultMessageHBox.getChildren().get(0);
        return lastResultLabel.getText();
    }
}
```
###### \java\seedu\address\commons\events\model\EndActiveSessionEventTest.java
``` java
public class EndActiveSessionEventTest {

    @Test
    public void endActiveSession_success() {
        SessionInterface sessionManager = new SessionManager();
        sessionManager.createNewSession(new AddCommand(null));
        assertTrue(sessionManager.isUserInActiveSession());
        EventsCenter.getInstance().post(new EndActiveSessionEvent());
        assertFalse(sessionManager.isUserInActiveSession());
    }
}
```
###### \java\seedu\address\logic\commands\AddCommandTest.java
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
###### \java\seedu\address\logic\LogicManagerTest.java
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
###### \java\seedu\address\model\session\SessionAddCommandTest.java
``` java
public class SessionAddCommandTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void parseInputForField_name_success() throws IllegalArgumentException, IllegalValueException {
        SessionAddCommand session = new SessionAddCommand(new AddCommand(null), null);
        session.parseInputForField(Name.CLASS_NAME, "Some Name");
    }

    @Test
    public void parseInputForField_address_success() throws IllegalArgumentException, IllegalValueException {
        SessionAddCommand session = new SessionAddCommand(new AddCommand(null), null);
        session.parseInputForField(Address.CLASS_NAME, "Some Address");
    }

    @Test
    public void parseInputForField_phone_success() throws IllegalArgumentException, IllegalValueException {
        SessionAddCommand session = new SessionAddCommand(new AddCommand(null), null);
        session.parseInputForField(Phone.CLASS_NAME, "123124913");
    }

    @Test
    public void parseInputForField_email_success() throws IllegalArgumentException, IllegalValueException {
        SessionAddCommand session = new SessionAddCommand(new AddCommand(null), null);
        session.parseInputForField(Email.CLASS_NAME, "email@email.com");
    }

    @Test
    public void parseInputForField_price_success() throws IllegalArgumentException, IllegalValueException {
        SessionAddCommand session = new SessionAddCommand(new AddCommand(null), null);
        session.parseInputForField(Price.CLASS_NAME, "12");
    }

    @Test
    public void parseInputForField_rating_success() throws IllegalArgumentException, IllegalValueException {
        SessionAddCommand session = new SessionAddCommand(new AddCommand(null), null);
        session.parseInputForField(Rating.CLASS_NAME, "5");
    }

    @Test
    public void parseInputForField_invalidClass_throwsIllegalArgumentException()
            throws IllegalArgumentException, IllegalValueException {
        thrown.expect(IllegalArgumentException.class);
        SessionAddCommand session = new SessionAddCommand(new AddCommand(null), null);
        session.parseInputForField(String.class.getName(), "Some Input");
    }

    @Test
    public void parseInputForMultivaluedField_tag_success() throws IllegalValueException {
        SessionAddCommandStub session = new SessionAddCommandStub(new AddCommand(null), null);
        session.parseInputForMultivaluedField(Tag.CLASS_NAME);
    }

    @Test
    public void parseInputForMultivaluedField_allergy_success() throws IllegalValueException {
        SessionAddCommandStub session = new SessionAddCommandStub(new AddCommand(null), null);
        session.parseInputForMultivaluedField(Allergy.CLASS_NAME);
    }

    @Test
    public void parseInputForMultivaluedField_string_throwsIllegalArgumentException()
            throws IllegalArgumentException, IllegalValueException {
        thrown.expect(IllegalArgumentException.class);
        SessionAddCommandStub session = new SessionAddCommandStub(new AddCommand(null), null);
        session.parseInputForMultivaluedField(String.class.getName());
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
            stringBuffer = Arrays.asList("peruvian", "seafood");

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
###### \java\seedu\address\model\session\SessionTest.java
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
    private static final int INDEX_AFTER_PHONE = 2;
    private static final int INDEX_AFTER_EMAIL = 3;
    private static final int INDEX_AFTER_ADDRESS = 4;
    private static final int INDEX_AFTER_PRICE = 5;
    private static final int INDEX_AFTER_RATING = 6;
    private static final int INDEX_AFTER_TAGS = 7;

    private static final String SUCCESS_MESSAGE_AFTER_NAME =
            Session.buildMessageFromPrompt(AddCommand.PROMPTS.get(INDEX_AFTER_NAME));
    private static final String FAILURE_MESSAGE_AFTER_WRONG_PHONE =
            Session.TRY_AGAIN_MESSAGE + Phone.MESSAGE_PHONE_CONSTRAINTS;
    private static final String SUCCESS_MESSAGE_AFTER_PHONE =
            Session.buildMessageFromPrompt(AddCommand.PROMPTS.get(INDEX_AFTER_PHONE));
    private static final String SUCCESS_MESSAGE_AFTER_EMAIL =
            Session.buildMessageFromPrompt(AddCommand.PROMPTS.get(INDEX_AFTER_EMAIL));
    private static final String SUCCESS_MESSAGE_AFTER_ADDRESS =
            Session.buildMessageFromPrompt(AddCommand.PROMPTS.get(INDEX_AFTER_ADDRESS));
    private static final String SUCCESS_MESSAGE_AFTER_PRICE =
            Session.buildMessageFromPrompt(AddCommand.PROMPTS.get(INDEX_AFTER_PRICE));
    private static final String SUCCESS_MESSAGE_AFTER_RATING =
            Session.buildMessageFromPrompt(AddCommand.PROMPTS.get(INDEX_AFTER_RATING));

    private static final String SUCCESS_MESSAGE_AFTER_FIRST_TAG =
            Session.ANYTHING_ELSE_MESSAGE;
    private static final String SUCCESS_MESSAGE_AFTER_SECOND_TAG =
            Session.ANYTHING_ELSE_MESSAGE;

    private static final String SUCCESS_MESSAGE_AFTER_TAGS =
            Session.buildMessageFromPrompt(AddCommand.PROMPTS.get(INDEX_AFTER_TAGS));

    private static final String SUCCESS_MESSAGE_AFTER_FIRST_ALLERGY =
            Session.ANYTHING_ELSE_MESSAGE;


    @Test
    public void interpretUserInput_success() throws CommandException {
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
                Session.END_FIELD).feedbackToUser);

        // adding multi value fields
        assertEquals(SUCCESS_MESSAGE_AFTER_FIRST_ALLERGY, session.interpretUserInput("animals").feedbackToUser);
        assertEquals(Session.SUCCESS_MESSAGE, session.interpretUserInput(Session.END_FIELD).feedbackToUser);
    }

    @Test
    public void interpretUserInput_emptyOptionalFields_success() throws CommandException {
        Session session = new SessionAddCommandStub(new AddCommand(null), EventsCenter.getInstance());
        assertEquals(SUCCESS_MESSAGE_AFTER_NAME, session.interpretUserInput(BACON_NAME).feedbackToUser);
        assertEquals(SUCCESS_MESSAGE_AFTER_PHONE, session.interpretUserInput(BACON_PHONE).feedbackToUser);
        assertEquals(SUCCESS_MESSAGE_AFTER_EMAIL, session.interpretUserInput(Session.END_FIELD).feedbackToUser);
        assertEquals(SUCCESS_MESSAGE_AFTER_ADDRESS, session.interpretUserInput(Session.END_FIELD).feedbackToUser);
        assertEquals(SUCCESS_MESSAGE_AFTER_PRICE, session.interpretUserInput(Session.END_FIELD).feedbackToUser);
        assertEquals(SUCCESS_MESSAGE_AFTER_RATING, session.interpretUserInput(Session.END_FIELD).feedbackToUser);

        // skipping multi value fields
        assertEquals(SUCCESS_MESSAGE_AFTER_TAGS, session.interpretUserInput(Session.END_FIELD).feedbackToUser);

        // adding multi value fields
        assertEquals(Session.SUCCESS_MESSAGE, session.interpretUserInput(Session.END_FIELD).feedbackToUser);
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
###### \java\systemtests\AddCommandSystemTest.java
``` java
    @Test
    public void addInteractive() {
        /* -------------------------- Perform add in interactive mode ------------------------------ */
        assertCommandSuccessWithoutSync(AddCommand.COMMAND_WORD, getModel(), AddCommand.PROMPTS.get(0).getMessage());
    }
```
###### \java\systemtests\AddCommandSystemTest.java
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
