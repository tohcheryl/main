package seedu.address.logic.commands;

//@@author {jaxony}
/**
 * Contains a message and an expected response class.
 * Used for interactive user input for {@code Command}s.
 */
public class Prompt {
    public final boolean isMultiValued;

    private Class field;
    private String message;

    public Prompt(Class field, String message, boolean isMultiValued) {
        this.field = field;
        this.message = message;
        this.isMultiValued = isMultiValued;
    }

    public Class getField() {
        return field;
    }

    public String getMessage() {
        return message;
    }
}
