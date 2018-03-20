package seedu.address.model.util;

import seedu.address.logic.commands.Command;

/**
 *
 */
public class Session {
    private Command command;
    private int promptIndex;

    public Session(Command command) {
        this.command = command;
        promptIndex = 0;
    }

    public void start() {

    }

    public void nextPrompt() {

    }

    public Command end() {
        return null;
    }

    public void interpret() {

    }
}
