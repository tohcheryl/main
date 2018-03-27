package seedu.address.model.session;

import java.util.List;
import java.util.logging.Logger;

import seedu.address.commons.core.EventsCenter;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.events.ui.NewResultAvailableEvent;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.Prompt;
import seedu.address.logic.parser.ParserUtil;

/**
 *
 */
public class Session {
    private Command command;
    private int promptIndex;
    private final List<Prompt> prompts;
    private final Logger logger = LogsCenter.getLogger(Session.class);
    private final EventsCenter eventsCenter;

    public Session(Command command, EventsCenter eventsCenter) {
        this.command = command;
        promptIndex = 0;
        prompts = this.command.getPrompts();
        this.eventsCenter = eventsCenter;
    }

    /**
     *
     */
    public void nextPrompt() {
        Prompt p = prompts.get(promptIndex);
        eventsCenter.post(new NewResultAvailableEvent(p.getMessage(), true));
    }

    public Command end() {
        return null;
    }

    /**
     *
     * @param userInput
     */
    public void interpretUserInput(String userInput) {
        logger.info("Received user input in current Session: " + userInput);
        Prompt p = prompts.get(promptIndex);
        try {
            ParserUtil.parseClass(p.getField(), userInput);
        } catch (IllegalValueException ive) {
            eventsCenter.post(new NewResultAvailableEvent(ive.getMessage(), false));
        }
    }

    /**
     *
     */
    public void start() {
        promptIndex = 0;
        nextPrompt();
    }
}
