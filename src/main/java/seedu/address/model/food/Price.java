package seedu.address.model.food;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Locale;

import org.apache.commons.validator.routines.BigDecimalValidator;
import org.apache.commons.validator.routines.CurrencyValidator;

/**
 * Represents a Food's price in HackEat.
 * Guarantees: immutable; is valid as declared in {@link #isValidPrice(String)}
 */
public class Price {

    public static final String MESSAGE_PRICE_CONSTRAINTS =
            "Price can only contain currency symbol and numbers";
    private static Locale currentLocale = Locale.getDefault();
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
     * Returns true if a given string begins/ends with the correct currency symbol.
     */
    private static boolean isValidSymbol(String inputPrice) {
        Currency currency = Currency.getInstance(currentLocale);
        String symbol = currency.getSymbol();
        if (inputPrice.startsWith(symbol) || inputPrice.endsWith(symbol)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Returns true if a given string is a valid price.
     */
    public static boolean isValidPrice(String inputPrice) {
        BigDecimal amount = null;
        BigDecimalValidator validator = CurrencyValidator.getInstance();
        if (isValidSymbol(inputPrice)) {
            amount = validator.validate(inputPrice, currentLocale);
        } else {
            amount = null;
        }

        if (amount != null) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Sets price of Food without currency symbol.
     * Truncates given price if it is given to more than 2 decimal places(if applicable).
     */
    public void setPrice(String inputPrice) {
        BigDecimalValidator validator = CurrencyValidator.getInstance();
        value = validator.validate(inputPrice, currentLocale).toString();
    }

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
