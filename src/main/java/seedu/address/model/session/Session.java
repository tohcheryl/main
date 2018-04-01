package seedu.address.model.session;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.logging.Logger;

import seedu.address.commons.core.EventsCenter;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.events.ui.EndActiveSessionEvent;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.Prompt;
import seedu.address.logic.commands.exceptions.CommandException;

/**
 * Represents a continuous chat or interaction between the user
 * and the system.
 */
public abstract class Session {
    public static final String END_MULTI_VALUE_FIELD = "n";
    public static final String SUCCESS_MESSAGE = "Success!";
    public static final String ANYTHING_ELSE_MESSAGE = "And anything else? Type (n/N) to stop here.";
    protected final EventsCenter eventsCenter;
    protected Collection<String> temporaryStrings;
    protected Command command;
    private int promptIndex;
    private final List<Prompt> prompts;
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
    private CommandResult getNextPrompt() throws CommandException {
        if (promptIndex < prompts.size()) {
            Prompt p = prompts.get(promptIndex);
            return new CommandResult(p.getMessage());
        } else {
            end();
            return new CommandResult(SUCCESS_MESSAGE);
        }
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
        Prompt p = prompts.get(promptIndex);

        try {
            if (p.isMultiValued) {
                if (isParsingMultivaluedField) {
                    if (userInput.toLowerCase().equals(END_MULTI_VALUE_FIELD)) {
                        parseInputForMultivaluedField(p.getField());
                        isParsingMultivaluedField = false;
                        promptIndex++;
                        return getNextPrompt();
                    } else {
                        addAsMultiValue(userInput);
                        return askForNextMultivalue();
                    }
                } else {
                    // start multivalue parsing
                    temporaryStrings = new HashSet<>();
                    isParsingMultivaluedField = true;
                    addAsMultiValue(userInput);
                    return askForNextMultivalue();
                }
            } else {
                parseInputForField(p.getField(), userInput);
                promptIndex++;
                return getNextPrompt();
            }
        } catch (IllegalValueException ive) {
            return new CommandResult("Please try again: " + ive.getMessage());
        }
    }

    /**
     * Adds user input to a collection of strings for processing later
     * when all input has been collected from the user.
     *
     * @param userInput
     */
    private void addAsMultiValue(String userInput) {
        temporaryStrings.add(userInput);
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
        return getNextPrompt();
    }
}
