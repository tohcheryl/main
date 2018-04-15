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

//@@author jaxony

/**
 * Represents a continuous chat or interaction between the user
 * and the system.
 */
public abstract class Session {
    public static final String END_FIELD = "";
    public static final String OPTIONAL_MESSAGE = "[Enter] to skip.";
    public static final String SUCCESS_MESSAGE = "Success!";
    public static final String ANYTHING_ELSE_MESSAGE = "And anything else?\nType [Enter] to stop.";
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

    public static String buildMessageFromPrompt(Prompt p) {
        return p.isOptional ? p.getMessage() + "\n" + OPTIONAL_MESSAGE : p.getMessage();
    }

    /**
     * Interprets user input in the CommandBox.
     * @param userInput Text typed in by the user in the CommandBox
     * @return Feedback to user as a {@code CommandResult}.
     * @throws CommandException If the executed command is invalid.
     */
    public CommandResult interpretUserInput(String userInput) throws CommandException {
        logger.info("Received user input in current Session: " + userInput);
        Prompt p = getCurrentPrompt();

        try {
            if (p.isMultiValued) {
                return handleInputForMultiValuedField(userInput);
            } else {
                return handleInputForField(userInput);
            }
        } catch (IllegalValueException ive) {
            if (p.isMultiValued) {
                resetStringBuffer();
            }
            return new CommandResult(TRY_AGAIN_MESSAGE + ive.getMessage(), false);
        }
    }

    /**
     * Starts the session by returning the first prompt in the interaction.
     * @return The message of the first prompt.
     */
    public CommandResult start() throws CommandException {
        return new CommandResult(getCurrentPrompt().getMessage());
    }

    protected abstract void parseInputForMultivaluedField(String fieldName) throws IllegalValueException;

    protected abstract void parseInputForField(String fieldName, String userInput) throws IllegalValueException;

    protected abstract void finishCommand() throws CommandException;

    private Prompt getCurrentPrompt() {
        return prompts.get(promptIndex);
    }

    /**
     * Processes user input for a single-value field, including optional fields.
     * @param userInput String input from user.
     * @return Feedback to the user as a {@code CommandResult}.
     * @throws CommandException If command execution leads to an error.
     * @throws IllegalValueException If input parsing leads to an error.
     */
    private CommandResult handleInputForField(String userInput) throws CommandException, IllegalValueException {
        Prompt p = getCurrentPrompt();
        boolean canUserSkipField = didUserEndPrompt(userInput) && p.isOptional;
        if (!canUserSkipField) {
            parseInputForField(p.getFieldName(), userInput);
        }
        return getNextPromptMessage();
    }

    private boolean sessionHasPromptsLeft() {
        return promptIndex < prompts.size();
    }

    /**
     * Ends the active {@code Session}.
     * @throws CommandException If command execution fails.
     */
    private void endSession() throws CommandException {
        eventsCenter.post(new EndActiveSessionEvent());
        finishCommand();
    }

    /**
     * Processes user input for a multi valued field, including optional fields.
     * @param userInput String input from user.
     * @return Feedback to the user.
     * @throws CommandException If command execution leads to an error.
     */
    private CommandResult handleInputForMultiValuedField(String userInput)
            throws CommandException, IllegalValueException {
        Prompt p = getCurrentPrompt();
        if (didUserEndPrompt(userInput)) {
            parseInputForMultivaluedField(p.getFieldName());
            isParsingMultivaluedField = false;
            return getNextPromptMessage();
        }
        addAsMultiValue(userInput);
        return askForNextMultivalue();

    }

    private void resetStringBuffer() {
        stringBuffer = new HashSet<>();
    }

    private boolean didUserEndPrompt(String userInput) {
        return userInput.equals(END_FIELD);
    }

    /**
     * Gets the next prompt message in the interactive session.
     * @return Feedback to the user as a {@code CommandResult}.
     * @throws CommandException If command execution fails.
     */
    private CommandResult getNextPromptMessage() throws CommandException {
        promptIndex++;
        if (!sessionHasPromptsLeft()) {
            endSession();
            return buildSuccessfulCommandResult();
        }
        Prompt prompt = getCurrentPrompt();
        if (prompt.isMultiValued) {
            setupForMultiValued();
        }
        return buildCommandResultFromPrompt(prompt);
    }

    private CommandResult buildSuccessfulCommandResult() {
        return new CommandResult(SUCCESS_MESSAGE);
    }

    /**
     * Establishes Session state for processing a field containing
     * one or more values.
     */
    private void setupForMultiValued() {
        isParsingMultivaluedField = true;
        resetStringBuffer();
    }

    /**
     * Constructs a CommandResult from a prompt.
     * @param prompt What the system is asking from the user. May be optional.
     * @return Feedback to user.
     */
    private static CommandResult buildCommandResultFromPrompt(Prompt prompt) {
        String message = buildMessageFromPrompt(prompt);
        return new CommandResult(message);
    }

    /**
     * Adds user input to a collection of strings for processing later
     * when all input has been collected from the user for the current command.
     * @param userInput String from user.
     */
    private void addAsMultiValue(String userInput) {
        stringBuffer.add(userInput);
        logger.info("Added " + userInput + " as a multi value field");
    }

    private CommandResult askForNextMultivalue() {
        return new CommandResult(ANYTHING_ELSE_MESSAGE);
    }
}
