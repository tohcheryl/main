package seedu.address.model.food;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import seedu.address.testutil.Assert;

//@@author {samzx}

public class RatingTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> new Rating(null));
    }

    @Test
    public void constructor_invalidRating_throwsIllegalArgumentException() {
        String invalidRating = "10";
        Assert.assertThrows(IllegalArgumentException.class, () -> new Rating(invalidRating));
    }

    @Test
    public void isValidRating() {
        // null rating
        Assert.assertThrows(NullPointerException.class, () -> Rating.isValidRating(null));

        // invalid rating
        assertFalse(Rating.isValidRating("-1"));
        assertFalse(Rating.isValidRating("10"));
        assertFalse(Rating.isValidRating("6"));

        // valid rating
        assertTrue(Rating.isValidRating("1"));
        assertTrue(Rating.isValidRating("5"));
        assertTrue(Rating.isValidRating("0"));
    }

    @Test
    public void getValue() {
        Rating rating = new Rating("0");
        assertEquals("0", rating.value);
    }

    @Test
    public void toString_validRating_returnsString() {
        Rating rating = new Rating("0");
        assertEquals("0", rating.toString());
    }

    @Test
    public void equals_validRating_returnsEqual() {
        Rating rating = new Rating("3");
        Rating rating2 = new Rating("3");
        assertEquals(rating, rating2);
    }

    @Test
    public void hashCode_validRating_returnsHashCode() {
        Rating p = new Rating("5");
        Rating p2 = new Rating("5");
        assertEquals(p.hashCode(), p2.hashCode());
    }
}
