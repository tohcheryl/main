package seedu.address.logic.commands;

import static org.junit.Assert.assertEquals;
import static seedu.address.model.util.SampleDataUtil.getAllergySet;

import org.junit.Test;

import seedu.address.model.AddressBook;
import seedu.address.model.food.Address;
import seedu.address.model.food.Name;
import seedu.address.model.food.Phone;
import seedu.address.model.user.UserProfile;

public class UserConfigCommandTest {


    private AddressBook addressBook = new AddressBook();
    private UserProfile validUserProfile = new UserProfile(new Name("Wei wei"), new Phone("92304333"),
                new Address("Blk 343 Serangoon Ave 3 #23-23 Singapore 349343"), getAllergySet("banana"));

    @Test
    public void execute_validUserProfile_setSuccessfully() {
        addressBook.setUserProfile(validUserProfile);
        assertEquals(validUserProfile, addressBook.getUserProfile());
    }

}
