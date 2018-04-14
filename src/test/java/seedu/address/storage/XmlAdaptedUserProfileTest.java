package seedu.address.storage;

import static org.junit.Assert.assertEquals;
import static seedu.address.storage.XmlAdaptedUserProfile.MISSING_FIELD_MESSAGE_FORMAT;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.food.Address;
import seedu.address.model.food.Name;
import seedu.address.model.food.Phone;
import seedu.address.model.user.UserProfile;
import seedu.address.testutil.Assert;
import seedu.address.testutil.UserProfileBuilder;

//@@author tohcheryl
public class XmlAdaptedUserProfileTest {
    private static final String INVALID_NAME = "R@nch";
    private static final String INVALID_PHONE = "+651234";
    private static final String INVALID_ADDRESS = " ";
    private static final String INVALID_ALLERGY = "#lactose";

    private static final UserProfile JOHN_DOE = new UserProfileBuilder().build();

    private static final String VALID_NAME = JOHN_DOE.getName().toString();
    private static final String VALID_PHONE = JOHN_DOE.getPhone().toString();
    private static final String VALID_ADDRESS = JOHN_DOE.getAddress().toString();
    private static final List<XmlAdaptedAllergy> VALID_ALLERGIES = JOHN_DOE.getAllergies().stream()
            .map(XmlAdaptedAllergy::new)
            .collect(Collectors.toList());

    @Test
    public void toModelType_validFoodDetails_returnsFood() throws Exception {
        XmlAdaptedUserProfile johnDoe = new XmlAdaptedUserProfile(JOHN_DOE);
        assertEquals(JOHN_DOE, johnDoe.toModelType());
    }

    @Test
    public void toModelType_invalidName_throwsIllegalValueException() {
        XmlAdaptedUserProfile johnDoe =
                new XmlAdaptedUserProfile(INVALID_NAME, VALID_PHONE, VALID_ADDRESS, VALID_ALLERGIES);
        String expectedMessage = Name.MESSAGE_NAME_CONSTRAINTS;
        Assert.assertThrows(IllegalValueException.class, expectedMessage, johnDoe::toModelType);
    }

    @Test
    public void toModelType_nullName_throwsIllegalValueException() {
        XmlAdaptedUserProfile johnDoe = new XmlAdaptedUserProfile(null, VALID_PHONE, VALID_ADDRESS, VALID_ALLERGIES);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Name.class.getSimpleName());
        Assert.assertThrows(IllegalValueException.class, expectedMessage, johnDoe::toModelType);
    }

    @Test
    public void toModelType_invalidPhone_throwsIllegalValueException() {
        XmlAdaptedUserProfile johnDoe =
                new XmlAdaptedUserProfile(VALID_NAME, INVALID_PHONE, VALID_ADDRESS, VALID_ALLERGIES);
        String expectedMessage = Phone.MESSAGE_PHONE_CONSTRAINTS;
        Assert.assertThrows(IllegalValueException.class, expectedMessage, johnDoe::toModelType);
    }

    @Test
    public void toModelType_nullPhone_throwsIllegalValueException() {
        XmlAdaptedUserProfile johnDoe = new XmlAdaptedUserProfile(VALID_NAME, null, VALID_ADDRESS, VALID_ALLERGIES);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Phone.class.getSimpleName());
        Assert.assertThrows(IllegalValueException.class, expectedMessage, johnDoe::toModelType);
    }

    @Test
    public void toModelType_invalidAddress_throwsIllegalValueException() {
        XmlAdaptedUserProfile johnDoe =
                new XmlAdaptedUserProfile(VALID_NAME, VALID_PHONE, INVALID_ADDRESS, VALID_ALLERGIES);
        String expectedMessage = Address.MESSAGE_ADDRESS_CONSTRAINTS;
        Assert.assertThrows(IllegalValueException.class, expectedMessage, johnDoe::toModelType);
    }

    @Test
    public void toModelType_nullAddress_throwsIllegalValueException() {
        XmlAdaptedUserProfile johnDoe = new XmlAdaptedUserProfile(VALID_NAME, VALID_PHONE, null, VALID_ALLERGIES);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Address.class.getSimpleName());
        Assert.assertThrows(IllegalValueException.class, expectedMessage, johnDoe::toModelType);
    }

    @Test
    public void toModelType_invalidAllergies_throwsIllegalValueException() {
        List<XmlAdaptedAllergy> invalidAllergies = new ArrayList<>(VALID_ALLERGIES);
        invalidAllergies.add(new XmlAdaptedAllergy(INVALID_ALLERGY));
        XmlAdaptedUserProfile johnDoe =
                new XmlAdaptedUserProfile(VALID_NAME, VALID_PHONE, VALID_ADDRESS, invalidAllergies);
        Assert.assertThrows(IllegalValueException.class, johnDoe::toModelType);
    }
}
