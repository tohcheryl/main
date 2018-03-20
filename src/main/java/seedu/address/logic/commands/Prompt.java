package seedu.address.logic.commands;

/**
 * Contains a message and an expected response class.
 * Used for interactive user input for {@code Command}s.
 */
public class Prompt {
    private Class field;
    private String message;

    public Prompt(Class field, String message) {
        this.field = field;
        this.message = message;
    }

    public Class getField() {
        return field;
    }

    public String getMessage() {
        return message;
    }
}
