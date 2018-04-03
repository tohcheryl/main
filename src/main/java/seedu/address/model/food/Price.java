package seedu.address.model.food;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

import java.math.BigDecimal;
import java.util.Locale;

import org.apache.commons.validator.routines.BigDecimalValidator;
import org.apache.commons.validator.routines.CurrencyValidator;

//@@ author {tohcheryl}
/**
 * Represents a Food's price in HackEat.
 * Guarantees: immutable; is valid as declared in {@link #isValidPrice(String)}
 */
public class Price {

    public static final String DEFAULT_PRICE = "0";
    public static final String MESSAGE_PRICE_CONSTRAINTS =
            "Price can only contain currency symbol and numbers";
    private static Locale currentLocale = Locale.US;
    private String value;

    /**
     * Constructs a {@code Price}.
     *
     * @param price A valid price.
     */
    public Price(String price) {
        requireNonNull(price);
        checkArgument(isValidPrice(price), MESSAGE_PRICE_CONSTRAINTS);
        setPrice(price);
    }

    /**
     * Returns true if a given string is a valid price.
     */
    public static boolean isValidPrice(String inputPrice) {
        if (inputPrice == null) {
            throw new NullPointerException();
        }

        BigDecimalValidator validator = CurrencyValidator.getInstance();
        BigDecimal amount = validator.validate(inputPrice, currentLocale);

        if (amount != null) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Sets price of Food without currency symbol.
     * Truncates given price to the appropriate number of dp.
     */
    public void setPrice(String inputPrice) {
        BigDecimalValidator validator = CurrencyValidator.getInstance();
        value = validator.validate(inputPrice, currentLocale).toString();
    }

    //@@author {samzx}
    /**
     * Method to display price with pre-fix symbol '$'
     * @return a string with '$' prefix
     */
    public static String displayString(String value) {
        return "$" + value;
    }

    //@@author {tohcheryl}
    /**
     * Returns price of Food as a BigDecimal.
     */
    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Price // instanceof handles nulls
                && this.value.equals(((Price) other).value)); // state check
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
