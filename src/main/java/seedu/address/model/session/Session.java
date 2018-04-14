package seedu.address.model.session;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.logging.Logger;

import seedu.address.commons.core.EventsCenter;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.events.model.EndActiveSessionEvent;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.Prompt;
import seedu.address.logic.commands.exceptions.CommandException;

//@@author {jaxony}

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
            return new CommandResult(TRY_AGAIN_MESSAGE + ive.getMessage(), false);
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
