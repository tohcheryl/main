package seedu.address.logic.commands;

//@@author jaxony
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
