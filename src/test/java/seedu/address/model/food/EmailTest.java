package seedu.address.model.food;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import seedu.address.testutil.Assert;

public class EmailTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> new Email(null));
    }

    @Test
    public void constructor_invalidEmail_throwsIllegalArgumentException() {
        String invalidEmail = "";
        Assert.assertThrows(IllegalArgumentException.class, () -> new Email(invalidEmail));
    }

    @Test
    public void isValidEmail() {
        // null email
        Assert.assertThrows(NullPointerException.class, () -> Email.isValidEmail(null));

        // blank email
        assertFalse(Email.isValidEmail("")); // empty string
        assertFalse(Email.isValidEmail(" ")); // spaces only

        // missing parts
        assertFalse(Email.isValidEmail("@example.com")); // missing local part
        assertFalse(Email.isValidEmail("pancakeexample.com")); // missing '@' symbol
        assertFalse(Email.isValidEmail("pancake@")); // missing domain name

        // invalid parts
        assertFalse(Email.isValidEmail("pancake@-")); // invalid domain name
        assertFalse(Email.isValidEmail("pancake@exam_ple.com")); // underscore in domain name
        assertFalse(Email.isValidEmail("pan cake@example.com")); // spaces in local part
        assertFalse(Email.isValidEmail("pancake@exam ple.com")); // spaces in domain name
        assertFalse(Email.isValidEmail(" pancake@example.com")); // leading space
        assertFalse(Email.isValidEmail("pancake@example.com ")); // trailing space
        assertFalse(Email.isValidEmail("pancake@@example.com")); // double '@' symbol
        assertFalse(Email.isValidEmail("pan@cake@example.com")); // '@' symbol in local part
        assertFalse(Email.isValidEmail("pancake@example@com")); // '@' symbol in domain name
        assertFalse(Email.isValidEmail("pancake@.example.com")); // domain name starts with a period
        assertFalse(Email.isValidEmail("pancake@example.com.")); // domain name ends with a period
        assertFalse(Email.isValidEmail("pancake@-example.com")); // domain name starts with a hyphen
        assertFalse(Email.isValidEmail("pancake@example.com-")); // domain name ends with a hyphen

        // valid email
        assertTrue(Email.isValidEmail("PanCake_1190@example.com"));
        assertTrue(Email.isValidEmail("a@bc"));  // minimal
        assertTrue(Email.isValidEmail("test@localhost"));   // alphabets only
        assertTrue(Email.isValidEmail("!#$%&'*+/=?`{|}~^.-@example.org")); // special characters local part
        assertTrue(Email.isValidEmail("123@145"));  // numeric local part and domain name
        assertTrue(Email.isValidEmail("a1+be!@example1.com")); // mixture of alphanumeric and special characters
        assertTrue(Email.isValidEmail("pan_cake@very-very-very-long-example.com"));   // long domain name
        assertTrue(Email.isValidEmail("if.you.dream.it_you.can.do.it@example.com"));    // long local part
    }
}
