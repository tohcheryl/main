package seedu.address.logic.commands;

//@@author jaxony
/**
 * Contains a field and an associated message that the system will send to the user
 * for this field when interactively asking the user for input.
 */
public class Prompt {
    public final boolean isMultiValued;
    public final boolean isOptional;

    private final String fieldName;
    private final String message;

    public Prompt(String fieldName, String message, boolean isMultiValued, boolean isOptional) {
        this.fieldName = fieldName;
        this.message = message;
        this.isMultiValued = isMultiValued;
        this.isOptional = isOptional;
    }

    public Prompt(String fieldName, String message, boolean isMultiValued) {
        this.fieldName = fieldName;
        this.message = message;
        this.isMultiValued = isMultiValued;
        this.isOptional = false;
    }

    public String getFieldName() {
        return fieldName;
    }

    public String getMessage() {
        return message;
    }
}
