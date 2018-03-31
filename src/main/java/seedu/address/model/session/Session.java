package seedu.address.model.session;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import seedu.address.commons.core.EventsCenter;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.events.ui.EndActiveSessionEvent;
import seedu.address.commons.events.ui.NewResultAvailableEvent;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.Prompt;
import seedu.address.logic.commands.exceptions.CommandException;

/**
 *
 */
public abstract class Session {
    public static final String END_MULTIVALUE_FIELD = "n";
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
     *
     */
    private void showPrompt() throws CommandException {
        if (promptIndex < prompts.size()) {
            Prompt p = prompts.get(promptIndex);
            eventsCenter.post(new NewResultAvailableEvent(p.getMessage(), true));
        } else {
            eventsCenter.post(new NewResultAvailableEvent("Thanks!", true));
            end();
        }
    }

    /**
     *
     */
    public void end() throws CommandException {
        finishCommand();
        eventsCenter.post(new EndActiveSessionEvent());
    }

    protected abstract void finishCommand() throws CommandException;

    /**
     * @param userInput
     */
    public void interpretUserInput(String userInput) throws CommandException {
        logger.info("Received user input in current Session: " + userInput);
        Prompt p = prompts.get(promptIndex);

        try {
            if (p.isMultiValued) {
                if (isParsingMultivaluedField) {
                    if (userInput.toLowerCase().equals(END_MULTIVALUE_FIELD)) {
                        parseInputForMultivaluedField(p.getField());
                        isParsingMultivaluedField = false;
                        promptIndex++;
                        showPrompt();
                    } else {
                        addAsMultivalue(userInput);
                        askForNextMultivalue();
                    }
                } else {
                    // start multivalue parsing
                    temporaryStrings = new HashSet<>();
                    isParsingMultivaluedField = true;

                    addAsMultivalue(userInput);
                }
            } else {
                parseInputForField(p.getField(), userInput);
                promptIndex++;
                showPrompt();
            }
        } catch (IllegalValueException ive) {
            eventsCenter.post(
                    new NewResultAvailableEvent("Please try again: " + ive.getMessage(), false));
        }
    }

    private void addAsMultivalue(String userInput) {
        temporaryStrings.add(userInput);
        logger.info("Added " + userInput + " as a multivalue field");
    }

    protected abstract void parseInputForMultivaluedField(Class field) throws IllegalValueException;

    protected abstract void parseInputForField(Class field, String userInput) throws IllegalValueException;

    private void askForNextMultivalue() {
        eventsCenter.post(new NewResultAvailableEvent("And anything else? Type (n/N) to stop here.", true));
    }

    /**
     *
     */
    public void start() throws CommandException {
        showPrompt();
    }
}
