package seedu.address.testutil;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import seedu.address.logic.commands.EditUserCommand;
import seedu.address.model.food.Address;
import seedu.address.model.food.Name;
import seedu.address.model.food.Phone;
import seedu.address.model.food.allergy.Allergy;
import seedu.address.model.user.UserProfile;

//@@author tohcheryl
/**
 * A utility class to help with building EditUserDescriptor objects.
 */
public class EditUserDescriptorBuilder {

    private EditUserCommand.EditUserDescriptor descriptor;

    public EditUserDescriptorBuilder() {
        descriptor = new EditUserCommand.EditUserDescriptor();
    }

    public EditUserDescriptorBuilder(EditUserCommand.EditUserDescriptor descriptor) {
        this.descriptor = new EditUserCommand.EditUserDescriptor(descriptor);
    }

    /**
     * Returns an {@code EditUserDescriptor} with fields containing {@code userProfile}'s details
     */
    public EditUserDescriptorBuilder(UserProfile userProfile) {
        descriptor = new EditUserCommand.EditUserDescriptor();
        descriptor.setName(userProfile.getName());
        descriptor.setPhone(userProfile.getPhone());
        descriptor.setAddress(userProfile.getAddress());
        descriptor.setAllergies(userProfile.getAllergies());
    }

    /**
     * Sets the {@code Name} of the {@code EditUserDescriptor} that we are building.
     */
    public EditUserDescriptorBuilder withName(String name) {
        descriptor.setName(new Name(name));
        return this;
    }

    /**
     * Sets the {@code Phone} of the {@code EditUserDescriptor} that we are building.
     */
    public EditUserDescriptorBuilder withPhone(String phone) {
        descriptor.setPhone(new Phone(phone));
        return this;
    }

    /**
     * Sets the {@code Address} of the {@code EditUserDescriptor} that we are building.
     */
    public EditUserDescriptorBuilder withAddress(String address) {
        descriptor.setAddress(new Address(address));
        return this;
    }

    /**
     * Parses the {@code allergies} into a {@code Set<Allergy>} and set it to the {@code EditUserDescriptor}
     * that we are building.
     */
    public EditUserDescriptorBuilder withAllergies(String... allergies) {
        Set<Allergy> allergySet = Stream.of(allergies).map(Allergy::new).collect(Collectors.toSet());
        descriptor.setAllergies(allergySet);
        return this;
    }

    public EditUserCommand.EditUserDescriptor build() {
        return descriptor;
    }
}
