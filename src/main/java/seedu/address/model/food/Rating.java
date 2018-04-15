package seedu.address.model.food;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

//@@author samzx

/**
 * Represents a Food's rating in HackEat.
 */
public class Rating {

    public static final String DEFAULT_RATING = "0";
    public static final int MAX_RATING = 5;
    public static final String MESSAGE_RATING_CONSTRAINTS =
            "Please enter a number between 0 to " + MAX_RATING;

    /**
     * Users must enter only a single digit.
     */
    public static final String RATING_VALIDATION_REGEX = "\\b\\d\\b";
    public static final String CLASS_NAME = "Rating";

    private static final String UNFILLED_RATING_SYMBOL = "☆";
    private static final String FILLED_RATING_SYMBOL = "★";

    public final String value;

    /**
     * Constructs a {@code Rating}.
     *
     * @param rating A valid rating.
     */
    public Rating(String rating) {
        requireNonNull(rating);
        checkArgument(isValidRating(rating), MESSAGE_RATING_CONSTRAINTS);
        this.value = rating;
    }

    /**
     * Returns true if a given string is a valid food rating.
     */
    public static boolean isValidRating(String test) {
        if (test.matches(RATING_VALIDATION_REGEX)) {
            int rating = Integer.parseInt(test);
            if (rating >= 0 && rating <= MAX_RATING) {
                return true;
            }
        }
        return false;
    }

    /**
     * Method to display ratings as stars instead of a number
     * @return a string of colored or uncolored stars
     */
    public static String displayString(String value) {
        int count = Integer.parseInt(value);

        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < MAX_RATING; i++) {
            if (count > 0) {
                stringBuilder.append(FILLED_RATING_SYMBOL);
            } else {
                stringBuilder.append(UNFILLED_RATING_SYMBOL);
            }
            count--;
        }

        return stringBuilder.toString();
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Rating // instanceof handles nulls
                && this.value.equals(((Rating) other).value)); // state check
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

}
