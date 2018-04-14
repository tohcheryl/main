package seedu.address.model.food;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import seedu.address.testutil.Assert;

//@@author samzx

public class RatingTest {
    private static final String VALID_RATING = "0";
    private static final String INVALID_RATING = "6";
    @Test
    public void constructor_null_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> new Rating(null));
    }

    @Test
    public void constructor_invalidRating_throwsIllegalArgumentException() {

        Assert.assertThrows(IllegalArgumentException.class, () -> new Rating(INVALID_RATING));
    }

    @Test
    public void isValidRating() {
        // null rating
        Assert.assertThrows(NullPointerException.class, () -> Rating.isValidRating(null));

        // invalid rating
        assertFalse(Rating.isValidRating("-1"));
        assertFalse(Rating.isValidRating("6"));

        // valid rating
        assertTrue(Rating.isValidRating("0"));
        assertTrue(Rating.isValidRating("5"));
    }

    @Test
    public void displayString_withStars_displaysStars() {
        assertEquals(Rating.displayString("0"), "☆☆☆☆☆");
        assertEquals(Rating.displayString("3"), "★★★☆☆");
        assertEquals(Rating.displayString("5"), "★★★★★");

    }

    @Test
    public void getValue() {

        Rating rating = new Rating(VALID_RATING);
        assertEquals(VALID_RATING, rating.value);
    }

    @Test
    public void toString_validRating_returnsString() {
        Rating rating = new Rating(VALID_RATING);
        assertEquals(VALID_RATING, rating.toString());
    }

    @Test
    public void equals_validRating_returnsEqual() {
        Rating rating = new Rating(VALID_RATING);
        Rating rating2 = new Rating(VALID_RATING);
        assertEquals(rating, rating2);
    }

    @Test
    public void hashCode_validRating_returnsHashCode() {
        Rating p = new Rating(VALID_RATING);
        Rating p2 = new Rating(VALID_RATING);
        assertEquals(p.hashCode(), p2.hashCode());
    }
}
