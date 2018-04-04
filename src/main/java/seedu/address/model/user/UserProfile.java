package seedu.address.model.user;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;

import seedu.address.model.food.Address;
import seedu.address.model.food.Food;
import seedu.address.model.food.Name;
import seedu.address.model.food.Phone;
import seedu.address.model.food.UniqueFoodList;
import seedu.address.model.food.allergy.Allergy;
import seedu.address.model.food.allergy.UniqueAllergyList;

//@@author {jaxony}
/**
 * Represents the profile of the HackEat user and contains
 * personal information such as name, phone and physical address.
 */
public class UserProfile {
    private Name name;
    private Phone phone;
    private Address address;
    private final UniqueAllergyList allergies;
    private UniqueFoodList recentFoods;
    private ProfilePicturePath profilePicturePath;


    /**
     * Constructs a {@code UserProfile} object.
     *  @param name    Name of user
     * @param phone   Phone number of user
     * @param address Address of user for food delivery
     */
    public UserProfile(Name name, Phone phone, Address address, Set<Allergy> allergies) {
        this.profilePicturePath = new ProfilePicturePath();
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.allergies = new UniqueAllergyList(allergies);
        this.recentFoods = new UniqueFoodList();
    }

    //@@author {tohcheryl}
    /**
     * Constructs a {@code UserProfile} object.
     * @param name    Name of user
     * @param phone   Phone number of user
     * @param address Address of user for food delivery
     * @param recentFoods Food eaten recently
     * @param profilePicturePath Path to profile picture
     */
    public UserProfile(Name name, Phone phone, Address address, Set<Allergy> allergies, Set<Food> recentFoods,
                       ProfilePicturePath profilePicturePath) {
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.allergies = new UniqueAllergyList(allergies);
        this.recentFoods = new UniqueFoodList(recentFoods);
        this.profilePicturePath = profilePicturePath;
    }

    //@@author {jaxony}
    public Name getName() {
        return name;
    }

    public Phone getPhone() {
        return phone;
    }

    public Address getAddress() {
        return address;
    }

    /**
     * Returns an immutable allergy set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Set<Allergy> getAllergies() {
        return Collections.unmodifiableSet(allergies.toSet());
    }

    //@@author {tohcheryl}
    /**
     * Returns an immutable Food set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Set<Food> getRecentFoods() {
        return Collections.unmodifiableSet(recentFoods.toSet());
    }

    /**
     * Sets recentFoods to the UniqueFoodList provided
     */
    public void setRecentFoods(UniqueFoodList recentFoodsList) {
        this.recentFoods = recentFoodsList;
    }

    public ProfilePicturePath getProfilePicturePath() {
        return this.profilePicturePath;
    }

    //@@author {jaxony}
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof UserProfile)) {
            return false;
        }

        UserProfile otherUserProfile = (UserProfile) other;
        return otherUserProfile.getName().equals(this.getName())
                && otherUserProfile.getPhone().equals(this.getPhone())
                && otherUserProfile.getAddress().equals(this.getAddress())
                && otherUserProfile.getAllergies().equals(this.getAllergies())
                && otherUserProfile.getRecentFoods().equals(this.getRecentFoods())
                && otherUserProfile.getProfilePicturePath().equals(this.getProfilePicturePath());
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(name, phone, address, allergies, recentFoods, profilePicturePath);
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append(getName())
                .append(" Phone: ")
                .append(getPhone())
                .append(" Address: ")
                .append(getAddress())
                .append(" Allergies: ");
        getAllergies().forEach(builder::append);
        builder.append("Recently ordered: ");
        getRecentFoods().forEach(builder::append);
        return builder.toString();
    }
}
