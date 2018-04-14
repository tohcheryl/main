package seedu.address.model.food.allergy;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents an Allergy in HackEat.
 * Guarantees: immutable; name is valid as declared in {@link #isValidAllergyName(String)}
 */
public class Allergy {

    public static final String MESSAGE_ALLERGY_CONSTRAINTS = "Allergy names should be alphanumeric and"
            + " should have at most 20 characters";
    public static final String ALLERGY_VALIDATION_REGEX = "\\p{Alnum}+";
    public static final String CLASS_NAME = "Allergy";

    public final String allergyName;

    /**
     * Constructs a {@code Allergy}.
     *
     * @param allergyName A valid allergy name.
     */
    public Allergy(String allergyName) {
        requireNonNull(allergyName);
        checkArgument(isValidAllergyName(allergyName), MESSAGE_ALLERGY_CONSTRAINTS);
        this.allergyName = allergyName;
    }

    /**
     * Returns true if a given string is a valid allergy name.
     */
    public static boolean isValidAllergyName(String test) {
        return test.matches(ALLERGY_VALIDATION_REGEX) && test.length() <= 20;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Allergy // instanceof handles nulls
                && this.allergyName.equals(((Allergy) other).allergyName)); // state check
    }

    @Override
    public int hashCode() {
        return allergyName.hashCode();
    }

    /**
     * Format state as text for viewing.
     */
    public String toString() {
        return '[' + allergyName + ']';
    }

}

