package seedu.address.model.food;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

import java.math.BigDecimal;
import java.util.Locale;

import org.apache.commons.validator.routines.BigDecimalValidator;
import org.apache.commons.validator.routines.CurrencyValidator;

//@@author tohcheryl
/**
 * Represents a Food's price in HackEat.
 */
public class Price {

    public static final String DEFAULT_PRICE = "0";
    public static final String MESSAGE_PRICE_CONSTRAINTS =
            "Price should contain only numbers and a single decimal point if necessary."
                    + " Prices that have more than 2 decimal places will be truncated."
                    + " A $ sign can be prefixed to the price but it is not required.";
    public static final String CLASS_NAME = "Price";
    private static Locale currentLocale = Locale.US;
    private String value;

    /**
     * Constructs a {@code Price}.
     *
     * @param price A valid price given as a String.
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
     * Truncates given price to the appropriate number of decimal places.
     */
    public void setPrice(String inputPrice) {
        BigDecimalValidator validator = CurrencyValidator.getInstance();
        value = validator.validate(inputPrice, currentLocale).toString();
    }

    //@@author samzx

    /**
     * Parses price value into more recognisable price format with $.
     * @param value of price
     * @return price value with $ prefixed
     */
    public static String displayString(String value) {
        return "$" + value;
    }

    //@@author tohcheryl
    /**
     * Returns price of Food as a String.
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
