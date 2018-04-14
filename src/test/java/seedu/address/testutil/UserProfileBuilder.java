package seedu.address.testutil;

import java.util.HashSet;
import java.util.Set;

import seedu.address.model.food.Address;
import seedu.address.model.food.Name;
import seedu.address.model.food.Phone;
import seedu.address.model.food.allergy.Allergy;
import seedu.address.model.user.UserProfile;
import seedu.address.model.util.SampleDataUtil;

//@@author tohcheryl
/**
 * A utility class to help with building User Profile objects.
 */

public class UserProfileBuilder {
    public static final String DEFAULT_USER_NAME = "John Doe";
    public static final String DEFAULT_USER_PHONE = "83449232";
    public static final String DEFAULT_USER_ADDRESS = "1 Neo Tiew Road";
    public static final String DEFAULT_USER_ALLERGIES = "pollen";

    private Name name;
    private Phone phone;
    private Address address;
    private Set<Allergy> allergies;

    public UserProfileBuilder() {
        name = new Name(DEFAULT_USER_NAME);
        phone = new Phone(DEFAULT_USER_PHONE);
        address = new Address(DEFAULT_USER_ADDRESS);
        allergies = SampleDataUtil.getAllergySet(DEFAULT_USER_ALLERGIES);
    }

    /**
     * Initializes the UserProfileBuilder with the data of {@code userProfileToCopy}.
     */
    public UserProfileBuilder(UserProfile userProfileToCopy) {
        name = userProfileToCopy.getName();
        phone = userProfileToCopy.getPhone();
        address = userProfileToCopy.getAddress();
        allergies = new HashSet<>(userProfileToCopy.getAllergies());
    }

    /**
     * Sets the {@code Name} of the {@code UserProfile} that we are building.
     */
    public UserProfileBuilder withName(String name) {
        this.name = new Name(name);
        return this;
    }

    /**
     * Sets the {@code Address} of the {@code UserProfile} that we are building.
     */
    public UserProfileBuilder withAddress(String address) {
        this.address = new Address(address);
        return this;
    }

    /**
     * Sets the {@code Phone} of the {@code UserProfile} that we are building.
     */
    public UserProfileBuilder withPhone(String phone) {
        this.phone = new Phone(phone);
        return this;
    }

    /**
     * Parses the {@code allergies} into a {@code Set<Allergy>} and set it to the {@code UserProfile}
     * that we are building.
     */
    public UserProfileBuilder withAllergies(String ... allergies) {
        this.allergies = SampleDataUtil.getAllergySet(allergies);
        return this;
    }

    public UserProfile build() {
        return new UserProfile(name, phone, address, allergies);
    }
}
