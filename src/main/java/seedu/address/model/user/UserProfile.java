package seedu.address.model.user;

import seedu.address.model.food.Address;
import seedu.address.model.food.Name;
import seedu.address.model.food.Phone;

/**
 * Represents the profile of the HackEat user and contains
 * personal information such as name, phone and physical address.
 */
public class UserProfile {
    private Name name;
    private Phone phone;
    private Address address;

    /**
     * Constructs a {@code UserProfile} object.
     *
     * @param name    Name of user
     * @param phone   Phone number of user
     * @param address Address of user for food delivery
     */
    public UserProfile(String name, String phone, String address) {
        this.name = new Name(name);
        this.phone = new Phone(phone);
        this.address = new Address(address);
    }

    public Name getName() {
        return name;
    }

    public Phone getPhone() {
        return phone;
    }

    public Address getAddress() {
        return address;
    }
}
