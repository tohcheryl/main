package seedu.address.model.food;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a Food's property in HackEat
 */
public abstract class FoodProperty {

    private static String MESSAGE_CONSTRAINT;
    private static String VALID_REGEX;
    private String value;

    /**
     * Constructs a {@code FoodProperty}.
     *
     * @param foodProperty A valid phone number.
     */
    public FoodProperty(String foodProperty) {
        requireNonNull(foodProperty);
        checkArgument(isValid(foodProperty), MESSAGE_CONSTRAINT);
        this.value = foodProperty;
    }

    /**
     * Returns true if a given string is a valid food phone number.
     */
    public static boolean isValid (String test) {
        return test.matches(VALID_REGEX);
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Phone // instanceof handles nulls
                && this.value.equals(((Phone) other).value)); // state check
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }


}
