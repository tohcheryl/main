package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

/**
 * Represents the result of a command execution.
 */
public class CommandResult {

    public final String feedbackToUser;
    public final boolean isSuccessful;

    public CommandResult(String feedbackToUser) {
        this.feedbackToUser = requireNonNull(feedbackToUser);
        isSuccessful = true;
    }

    public CommandResult(String feedbackToUser, boolean isSuccessful) {
        this.feedbackToUser = requireNonNull(feedbackToUser);
        this.isSuccessful = isSuccessful;
    }

}
