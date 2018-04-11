# jaxony
###### /java/seedu/address/logic/Logic.java
``` java
    /**
     * Creates a new Session for chat-like interaction with system.
     * @param userInput Text input from user.
     */
    void createNewSession(String userInput);

    /**
     * Starts the active Session.
     * @return Feedback to user.
     * @throws CommandException If command execution fails.
     */
    CommandResult startSession() throws CommandException;

    /**
     * Checks if command is an interactive command.
     * @param commandText Text input from user.
     * @return Feedback to user.
     * @throws ParseException If {@code commandText} is not a valid command.
     */
    boolean isCommandInteractive(String commandText) throws ParseException;

    /**
     * Returns ReadOnlyAddressBook
     */
    ReadOnlyAddressBook getAddressBook();
}
```
###### /java/seedu/address/logic/parser/AddressBookParser.java
``` java
    /**
     * Checks whether userInput specifies a command that is interactive.
     * Currently only AddCommand supports interactive mode.
     * @param userInput Text input from user.
     * @return True if the command is interactive, false if the command is valid but not interactive.
     * @throws ParseException If the command is invalid.
     */
    public boolean isCommandInteractive(String userInput) throws ParseException {
        Matcher matcher = match(userInput);
        final String arguments = matcher.group("arguments");
        // command must be interactive type if no arguments are provided
        // only AddCommand is interactive right now
        switch (matcher.group("commandWord")) {
        case AddCommand.COMMAND_WORD:
            break;
        case EditCommand.COMMAND_WORD:
        case SelectCommand.COMMAND_WORD:
        case DeleteCommand.COMMAND_WORD:
        case ClearCommand.COMMAND_WORD:
        case FindCommand.COMMAND_WORD:
        case ListCommand.COMMAND_WORD:
        case OrderCommand.COMMAND_WORD:
        case HistoryCommand.COMMAND_WORD:
        case ExitCommand.COMMAND_WORD:
        case HelpCommand.COMMAND_WORD:
        case UndoCommand.COMMAND_WORD:
        case RedoCommand.COMMAND_WORD:
        case ChangePicCommand.COMMAND_WORD:
        case EditUserCommand.COMMAND_WORD:
        case UserConfigCommand.COMMAND_WORD:
            return false;
        default:
            throw new ParseException(MESSAGE_UNKNOWN_COMMAND);
        }
        return arguments.equals("");
    }

    /**
     * Matches user input string with a basic command regex.
     *
     * @param userInput Text input from user.
     * @return Matcher object produced from regex pattern.
     * @throws ParseException If error arises during parsing of {@code userInput}.
     */
    private Matcher match(String userInput) throws ParseException {
        final Matcher matcher = BASIC_COMMAND_FORMAT.matcher(userInput.trim());
        if (!matcher.matches()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE));
        }
        return matcher;
    }
```
###### /java/seedu/address/logic/parser/AddressBookParser.java
``` java
    /**
     * Create a new command object.
     * @param userInput Text input from user.
     * @return New Command object.
     * @throws IllegalArgumentException If the command in {@code userInput} is not supported.
     */
    public Command getCommand(String userInput) throws IllegalArgumentException {
        return CommandFactory.createCommand(userInput.trim());
    }
```
###### /java/seedu/address/logic/commands/Command.java
``` java
    public List<Prompt> getPrompts() {
        return null;
    }
}
```
###### /java/seedu/address/logic/commands/AddCommand.java
``` java
    public static final List<Prompt> prompts = Arrays.asList(
            new Prompt(Name.class, "What's the food called?", false),
            new Prompt(Phone.class, "Restaurant phone number?", false),
            new Prompt(Email.class, "And their email?", false),
            new Prompt(Address.class, "Where they located @ fam?", false),
            new Prompt(Price.class, "$$$?", false),
            new Prompt(Rating.class, "U rate or what?", false),
            new Prompt(Tag.class, "Where those tags at?", true, true),
            new Prompt(Allergy.class, "Does this food have any allergies?", true, true));
```
###### /java/seedu/address/logic/commands/AddCommand.java
``` java
    @Override
    public List<Prompt> getPrompts() {
        return prompts;
    }
    //@author

    @Override
    public CommandResult executeUndoableCommand() throws CommandException {
        requireNonNull(model);
        requireNonNull(toAdd);
        try {
            model.addFood(toAdd);
            return new CommandResult(String.format(MESSAGE_SUCCESS, toAdd));
        } catch (DuplicateFoodException e) {
            throw new CommandException(MESSAGE_DUPLICATE_FOOD);
        }

    }

```
###### /java/seedu/address/logic/commands/AddCommand.java
``` java
    public void setFood(Food food) {
        toAdd = food;
    }
```
###### /java/seedu/address/logic/commands/UserConfigCommand.java
``` java
/**
 * Sets User Profile of HackEat user.
 */
public class UserConfigCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "userconfig";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a user's personal details to HackEat. "
            + "Parameters: "
            + PREFIX_NAME + "NAME "
            + PREFIX_PHONE + "PHONE "
            + PREFIX_ADDRESS + "ADDRESS "
            + "[" + PREFIX_ALLERGIES + "ALLERGY]...\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_NAME + "John Doe "
            + PREFIX_PHONE + "98765432 "
            + PREFIX_ADDRESS + "311, Clementi Ave 2, #02-25 "
            + PREFIX_ALLERGIES + "lactose "
            + PREFIX_ALLERGIES + "gluten";

    public static final String MESSAGE_SUCCESS = "User profile updated";

    private final UserProfile toAdd;

    /**
     * Creates a UserConfigCommand to add the specified {@code UserProfile}
     */
    public UserConfigCommand(UserProfile profile) {
        requireNonNull(profile);
        toAdd = profile;
    }

    @Override
    public CommandResult executeUndoableCommand() {
        requireNonNull(model);
        model.initUserProfile(toAdd);
        return new CommandResult(MESSAGE_SUCCESS);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof UserConfigCommand // instanceof handles nulls
                && toAdd.equals(((UserConfigCommand) other).toAdd));
    }
}
```
###### /java/seedu/address/logic/commands/Prompt.java
``` java
/**
 * Contains a message and an expected response class.
 * Used for interactive user input for {@code Command}s.
 */
public class Prompt {
    public final boolean isMultiValued;
    public final boolean isOptional;

    private Class field;
    private String message;

    public Prompt(Class field, String message, boolean isMultiValued, boolean isOptional) {
        this.field = field;
        this.message = message;
        this.isMultiValued = isMultiValued;
        this.isOptional = isOptional;
    }

    public Prompt(Class field, String message, boolean isMultiValued) {
        this.field = field;
        this.message = message;
        this.isMultiValued = isMultiValued;
        this.isOptional = false;
    }

    public Class getField() {
        return field;
    }

    public String getMessage() {
        return message;
    }
}
```
###### /java/seedu/address/logic/commands/CommandFactory.java
``` java
/**
 * Factory pattern for creating command objects
 */
public class CommandFactory {

    /**
     * Creates a Command given a command word.
     * @param commandWord Word for a command.
     * @return A new Command object.
     * @throws IllegalArgumentException If the command word is not supported.
     */
    public static Command createCommand(String commandWord) throws IllegalArgumentException {
        switch (commandWord) {
        case AddCommand.COMMAND_WORD:
            return new AddCommand(null);
        default:
            throw new IllegalArgumentException();
        }
    }
}
```
###### /java/seedu/address/logic/LogicManager.java
``` java
            CommandResult result;
            if (model.isUserInActiveSession()) {
                logger.info("User is in an active session with the system.");
                result = model.interpretInteractiveUserInput(commandText);
            } else if (isCommandInteractive(commandText)) {
                logger.info("Command is interactive.");
                // start a new session
                createNewSession(commandText);
                result = startSession();
            } else {
```
###### /java/seedu/address/logic/LogicManager.java
``` java
    @Override
    public void createNewSession(String userInput) throws IllegalArgumentException {
        Command interactiveCommand = addressBookParser.getCommand(userInput);
        interactiveCommand.setData(model, null, null);
        model.createNewSession(interactiveCommand);
    }

    @Override
    public CommandResult startSession() throws CommandException {
        return model.startSession();
    }

    @Override
    public boolean isCommandInteractive(String commandText) throws ParseException {
        return addressBookParser.isCommandInteractive(commandText);
    }

```
###### /java/seedu/address/model/user/UserProfile.java
``` java
/**
 * Represents the profile of the HackEat user and contains
 * personal information such as name, phone and physical address.
 */
public class UserProfile {
    private Name name;
    private Phone phone;
    private Address address;
    private final UniqueAllergyList allergies;
    private UniqueFoodList recentFoods;


    /**
     * Constructs a {@code UserProfile} object.
     *  @param name    Name of user
     * @param phone   Phone number of user
     * @param address Address of user for food delivery
     */
    public UserProfile(Name name, Phone phone, Address address, Set<Allergy> allergies) {
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.allergies = new UniqueAllergyList(allergies);
        this.recentFoods = new UniqueFoodList();
    }

```
###### /java/seedu/address/model/user/UserProfile.java
``` java
    public Name getName() {
        return name;
    }

    public Phone getPhone() {
        return phone;
    }

    public Address getAddress() {
        return address;
    }

    /**
     * Returns an immutable allergy set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Set<Allergy> getAllergies() {
        return Collections.unmodifiableSet(allergies.toSet());
    }

```
###### /java/seedu/address/model/user/UserProfile.java
``` java
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof UserProfile)) {
            return false;
        }

        UserProfile otherUserProfile = (UserProfile) other;
        return otherUserProfile.getName().equals(this.getName())
                && otherUserProfile.getPhone().equals(this.getPhone())
                && otherUserProfile.getAddress().equals(this.getAddress())
                && otherUserProfile.getAllergies().equals(this.getAllergies())
                && otherUserProfile.getRecentFoods().equals(this.getRecentFoods());
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(name, phone, address, allergies, recentFoods);
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append(getName())
                .append(" Phone: ")
                .append(getPhone())
                .append(" Address: ")
                .append(getAddress())
                .append(" Allergies: ");
        getAllergies().forEach(builder::append);
        builder.append("Recently ordered: ");
        getRecentFoods().forEach(builder::append);
        return builder.toString();
    }
}
```
###### /java/seedu/address/model/ModelManager.java
``` java
    @Override
    public void updateUserProfile(UserProfile toAdd) throws DuplicateUserException {
        addressBook.updateUserProfile(toAdd);
        indicateAddressBookChanged();
    }

    @Override
    public boolean isUserInActiveSession() {
        return sessionManager.isUserInActiveSession();
    }

    @Override
    public void createNewSession(Command interactiveCommand) {
        sessionManager.createNewSession(interactiveCommand);
    }

    @Override
    public CommandResult startSession() throws CommandException {
        return sessionManager.startSession();
    }

    @Override
    public CommandResult interpretInteractiveUserInput(String commandText) throws CommandException {
        return sessionManager.interpretUserInput(commandText);
    }
```
###### /java/seedu/address/model/Model.java
``` java
    /**
     * Checks if the user is engaged in an interactive session.
     * @return boolean
     */
    boolean isUserInActiveSession();

    /**
     * Creates a new interactive session.
     * @param interactiveCommand A Command that supports interactive mode.
     */
    void createNewSession(Command interactiveCommand);

    /**
     * Starts the new session by prompting the user.
     * @return feedback or messages to the user in the form of a CommandResult.
     */
    CommandResult startSession() throws CommandException;

    /**
     * Processes the user input for an interactive session.
     *
     * @param commandText user input string
     * @return feedback to the user
     */
    CommandResult interpretInteractiveUserInput(String commandText) throws CommandException;

```
###### /java/seedu/address/model/session/SessionAddCommand.java
``` java
/**
 * Session controlling the interaction for the AddCommand
 */
public class SessionAddCommand extends Session {

    protected Name name;
    protected Phone phone;
    protected Email email;
    protected Address address;
    protected Price price;
    protected Rating rating;
    protected Set<Tag> tagSet;
    protected Set<Allergy> allergySet;

    public SessionAddCommand(Command command, EventsCenter eventsCenter) {
        super(command, eventsCenter);
    }

    @Override
    public void parseInputForMultivaluedField(Class field) throws IllegalValueException, IllegalArgumentException {
        switch (field.getSimpleName()) {
        case "Tag":
            tagSet = ParserUtil.parseTags(stringBuffer);
            break;
        case "Allergy":
            allergySet = ParserUtil.parseAllergies(stringBuffer);
            break;
        default:
            throw new IllegalArgumentException();
        }
    }

    @Override
    protected void finishCommand() throws CommandException {
        AddCommand addCommand = (AddCommand) command;
        Food food = new Food(name, phone, email, address, price, rating, tagSet, allergySet);
        addCommand.setFood(food);
        addCommand.execute();
    }

    /**
     * Parses the {@code userInput} for a specific {@code field}
     *
     * @param field class used to parse the {@code userInput}
     * @param userInput test input from the user
     * @throws IllegalValueException parsing of {@code userInput} causes an error
     * @throws IllegalArgumentException {@code field} is not allowed
     */
    public void parseInputForField(Class field, String userInput)
            throws IllegalValueException, IllegalArgumentException {
        switch (field.getSimpleName()) {
        case "Name":
            name = ParserUtil.parseName(Optional.of(userInput)).get();
            break;
        case "Phone":
            phone = ParserUtil.parsePhone(Optional.of(userInput)).get();
            break;
        case "Email":
            email = ParserUtil.parseEmail(Optional.of(userInput))
                    .orElse(new Email(Email.DEFAULT_EMAIL));
            break;
        case "Address":
            address = ParserUtil.parseAddress(Optional.of(userInput))
                      .orElse(new Address(Address.DEFAULT_ADDRESS));
            break;
        case "Price":
            price = ParserUtil.parsePrice(Optional.of(userInput))
                    .orElse(new Price(Price.DEFAULT_PRICE));
            break;
        case "Rating":
            rating = ParserUtil.parseRating(Optional.of(userInput))
                     .orElse(new Rating(Rating.DEFAULT_RATING));
            break;
        default:
            throw new IllegalArgumentException();
        }
    }
}
```
###### /java/seedu/address/model/session/SessionInterface.java
``` java
/**
 * The API of the Session sub-component.
 */
public interface SessionInterface {
    /**
     * Checks if user is engaged in an active session.
     * @return boolean
     */
    boolean isUserInActiveSession();

    /**
     * Creates a new session with a command.
     * @param command Must support interactive mode.
     */
    void createNewSession(Command command);

    /**
     * Starts the new session.
     * @return feedback as a CommandResult
     * @throws CommandException
     */
    CommandResult startSession() throws CommandException;

    /**
     * Processes the user's text input.
     * @param userInput string input in the CommandBox
     * @return feedback as a CommandResult
     */
    CommandResult interpretUserInput(String userInput) throws CommandException;
}
```
###### /java/seedu/address/model/session/SessionManager.java
``` java
/**
 * Manages sessions (interactions) between user and system for chat-like
 * experience.
 */
public class SessionManager extends ComponentManager implements SessionInterface {

    private final List<Session> sessionHistory;
    private Session activeSession;
    private boolean isUserInActiveSession;

    public SessionManager() {
        sessionHistory = new ArrayList<>();
        isUserInActiveSession = false;
        activeSession = null;
    }

    /**
     * Makes a new Session
     * @param command Must support interactive mode.
     */
    public void createNewSession(Command command) {
        assert command.getClass().getSimpleName().equals("AddCommand");
        activeSession = new SessionAddCommand(command, eventsCenter);
        isUserInActiveSession = true;
    }

    @Override
    public CommandResult startSession() throws CommandException {
        return activeSession.start();
    }

    @Override
    public CommandResult interpretUserInput(String userInput) throws CommandException {
        return activeSession.interpretUserInput(userInput);
    }

    @Subscribe
    public void handleEndActiveSessionEvent(EndActiveSessionEvent e) throws CommandException {
        endActiveSession();
    }

    /**
     * Ends the current active Session and saves it
     * to the session history.
     */
    private void endActiveSession() {
        // replace with a NoSessionException later
        if (activeSession == null) {
            return;
        }
        sessionHistory.add(activeSession);
        activeSession = null;
    }

    @Override
    public boolean isUserInActiveSession() {
        return activeSession != null;
    }
}
```
###### /java/seedu/address/model/session/Session.java
``` java

/**
 * Represents a continuous chat or interaction between the user
 * and the system.
 */
public abstract class Session {
    public static final String END_MULTI_VALUE_FIELD = "";
    public static final String OPTIONAL_MESSAGE = "Press [Enter] to skip this optional field.";
    public static final String SUCCESS_MESSAGE = "Success!";
    public static final String ANYTHING_ELSE_MESSAGE = "And anything else? Type [Enter] to stop.";
    public static final String TRY_AGAIN_MESSAGE = "Please try again: ";
    protected final EventsCenter eventsCenter;
    protected Collection<String> stringBuffer;
    protected Command command;
    protected int promptIndex;
    protected final List<Prompt> prompts;
    private final Logger logger = LogsCenter.getLogger(Session.class);
    private boolean isParsingMultivaluedField;

    public Session(Command command, EventsCenter eventsCenter) {
        this.command = command;
        promptIndex = 0;
        prompts = this.command.getPrompts();
        this.eventsCenter = eventsCenter;
        isParsingMultivaluedField = false;
    }

    /**
     * Gets the next prompt message in the interactive session.
     *
     * @return feedback to the user
     * @throws CommandException If end() throws exception
     */
    private CommandResult getNextPromptMessage() throws CommandException {
        promptIndex++;
        if (!sessionHasPromptsLeft()) {
            end();
            return new CommandResult(SUCCESS_MESSAGE);
        }
        Prompt prompt = getCurrentPrompt();
        if (prompt.isMultiValued) {
            setupForMultiValued();
        }
        return buildCommandResultFromPrompt(prompt);
    }

    /**
     * Sets up Session state for processing multi valued field.
     */
    private void setupForMultiValued() {
        isParsingMultivaluedField = true;
        resetStringBuffer();
    }

    /**
     * Constructs a CommandResult from a prompt.
     *
     * @param prompt What the system is asking from the user. May be optional.
     * @return Feedback to user.
     */
    private CommandResult buildCommandResultFromPrompt(Prompt prompt) {
        String message = prompt.getMessage();
        if (prompt.isOptional) {
            message += " " + OPTIONAL_MESSAGE;
        }
        return new CommandResult(message);
    }

    private boolean sessionHasPromptsLeft() {
        return promptIndex < prompts.size();
    }

    /**
     * Ends the active Session.
     *
     * @throws CommandException If finishCommand() throws exception
     */
    private void end() throws CommandException {
        finishCommand();
        eventsCenter.post(new EndActiveSessionEvent());
    }

    private Prompt getCurrentPrompt() {
        return prompts.get(promptIndex);
    }

    protected abstract void finishCommand() throws CommandException;

    /**
     * Interprets user input in the CommandBox.
     *
     * @param userInput Text typed in by the user in the CommandBox
     * @return feedback to user
     * @throws CommandException
     */
    public CommandResult interpretUserInput(String userInput) throws CommandException {
        logger.info("Received user input in current Session: " + userInput);
        Prompt p = getCurrentPrompt();

        try {
            if (p.isMultiValued) {
                return handleInputForMultiValuedField(userInput);
            } else {
                parseInputForField(p.getField(), userInput);
                return getNextPromptMessage();
            }
        } catch (IllegalValueException ive) {
            if (p.isMultiValued) {
                // a multi value thingo failed during parsing, need to refresh
                resetStringBuffer();
            }
            return new CommandResult(TRY_AGAIN_MESSAGE + ive.getMessage());
        }
    }

    /**
     * Processes and responds to user input when processing a multi valued field.
     *
     * @param userInput String input from user.
     * @return Feedback to the user.
     * @throws CommandException If parsing goes wrong.
     */
    private CommandResult handleInputForMultiValuedField(String userInput)
            throws CommandException, IllegalValueException {
        Prompt p = getCurrentPrompt();
        if (didUserEndPrompt(userInput)) {
            // user wants to go to the next prompt now
            parseInputForMultivaluedField(p.getField());
            isParsingMultivaluedField = false;
            return getNextPromptMessage();
        }
        // user entered input that can be processed
        addAsMultiValue(userInput);
        return askForNextMultivalue();

    }

    private void resetStringBuffer() {
        stringBuffer = new HashSet<>();
    }

    private boolean didUserEndPrompt(String userInput) {
        return userInput.equals(END_MULTI_VALUE_FIELD);
    }

    /**
     * Adds user input to a collection of strings for processing later
     * when all input has been collected from the user.
     * @param userInput String from user.
     */
    private void addAsMultiValue(String userInput) {
        stringBuffer.add(userInput);
        logger.info("Added " + userInput + " as a multi value field");
    }

    protected abstract void parseInputForMultivaluedField(Class field) throws IllegalValueException;

    protected abstract void parseInputForField(Class field, String userInput) throws IllegalValueException;

    private CommandResult askForNextMultivalue() {
        return new CommandResult(ANYTHING_ELSE_MESSAGE);
    }

    /**
     * Start the session by giving the first prompt in the interaction.
     */
    public CommandResult start() throws CommandException {
        return new CommandResult(getCurrentPrompt().getMessage());
    }
}
```
