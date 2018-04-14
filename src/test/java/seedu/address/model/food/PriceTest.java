package seedu.address.model.food;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import seedu.address.testutil.Assert;

//@@author tohcheryl
public class PriceTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> new Price(null));
    }

    @Test
    public void constructor_invalidPrice_throwsIllegalArgumentException() {
        String invalidPrice = "";
        Assert.assertThrows(IllegalArgumentException.class, () -> new Price(invalidPrice));
    }

    @Test
    public void isValidPrice() {
        // null price
        Assert.assertThrows(NullPointerException.class, () -> Price.isValidPrice(null));

        // invalid prices
        assertFalse(Price.isValidPrice("12a.45"));
        assertFalse(Price.isValidPrice("$12a.45"));
        assertFalse(Price.isValidPrice("$1.p0"));
        assertFalse(Price.isValidPrice("203$0"));
        assertFalse(Price.isValidPrice("10$"));
        assertFalse(Price.isValidPrice("12.40$"));
        assertFalse(Price.isValidPrice("€2,0"));
        assertFalse(Price.isValidPrice("20000¥"));

        // valid prices in US
        assertTrue(Price.isValidPrice("$20"));
        assertTrue(Price.isValidPrice("$90.30"));
        assertTrue(Price.isValidPrice("$20.590"));
    }

    @Test
    public void setPrice() {
        Price p = new Price("$23.40");
        p.setPrice("$40.00");
        assertEquals("40.00", p.getValue());
    }

    @Test
    public void getValue() {
        Price p = new Price("$23.40");
        assertEquals("23.40", p.getValue());
    }

    @Test
    public void toString_validPrice_returnsString() {
        Price p = new Price("$23.40");
        assertEquals("23.40", p.toString());
    }

    @Test
    public void equals_validPrice_returnsEqual() {
        Price p = new Price("$23.40");
        Price p2 = new Price("$23.40");
        assertEquals(p, p2);
    }

    @Test
    public void hashCode_validPrice_returnsHashCode() {
        Price p = new Price("$23.40");
        Price p2 = new Price("$23.40");
        assertEquals(p.hashCode(), p2.hashCode());
    }
}
