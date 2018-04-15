package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ALLERGIES;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import seedu.address.commons.util.CollectionUtil;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.food.Address;
import seedu.address.model.food.Name;
import seedu.address.model.food.Phone;
import seedu.address.model.food.allergy.Allergy;
import seedu.address.model.user.UserProfile;
import seedu.address.model.user.exceptions.DuplicateUserException;

//@@author tohcheryl
/**
 * Edits the details of a user
 */
public class EditUserCommand extends UndoableCommand {


    public static final String COMMAND_WORD = "edituser";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits the details of the user "
            + "Existing values will be overwritten by the input values.\n"
            + "Parameters: "
            + "[" + PREFIX_NAME + "NAME] "
            + "[" + PREFIX_PHONE + "PHONE] "
            + "[" + PREFIX_ADDRESS + "ADDRESS] "
            + "[" + PREFIX_ALLERGIES + "ALLERGY]...\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_PHONE + "91234567";

    public static final String MESSAGE_EDIT_USER_SUCCESS = "Edited User: %1$s";
    public static final String MESSAGE_NOT_EDITED = "At least one field to edit must be provided.";
    public static final String MESSAGE_DUPLICATE_USER = "Edited user profile is the same as the one set previously.";

    private final EditUserCommand.EditUserDescriptor editUserDescriptor;

    private UserProfile userToEdit;
    private UserProfile editedUser;

    /**
     * Creates a new EditUserCommand
     * @param editUserDescriptor An EditUserDescriptor object which contains the updated attribute values
     */
    public EditUserCommand(EditUserCommand.EditUserDescriptor editUserDescriptor) {
        requireNonNull(editUserDescriptor);

        this.editUserDescriptor = new EditUserCommand.EditUserDescriptor(editUserDescriptor);
    }

    @Override
    public CommandResult executeUndoableCommand() throws CommandException {
        try {
            model.updateUserProfile(editedUser);
        } catch (DuplicateUserException dpe) {
            throw new CommandException(MESSAGE_DUPLICATE_USER);
        }
        return new CommandResult(String.format(MESSAGE_EDIT_USER_SUCCESS, editedUser));
    }

    @Override
    protected void preprocessUndoableCommand() {
        userToEdit = model.getUserProfile();
        editedUser = createEditedUser(userToEdit, editUserDescriptor);
    }

    /**
     * Creates and returns a {@code UserProfile} with the details of {@code userToEdit}
     * edited with {@code editProfileDescriptor}.
     */
    private static UserProfile createEditedUser(UserProfile userToEdit,
                                                EditUserCommand.EditUserDescriptor editUserDescriptor) {
        assert userToEdit != null;

        Name updatedName = editUserDescriptor.getName().orElse(userToEdit.getName());
        Phone updatedPhone = editUserDescriptor.getPhone().orElse(userToEdit.getPhone());
        Address updatedAddress = editUserDescriptor.getAddress().orElse(userToEdit.getAddress());
        Set<Allergy> updatedAllergies = editUserDescriptor.getAllergies().orElse(userToEdit.getAllergies());

        UserProfile editedUser = new UserProfile(updatedName, updatedPhone, updatedAddress, updatedAllergies);
        return editedUser;
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof EditUserCommand)) {
            return false;
        }

        // state check
        EditUserCommand e = (EditUserCommand) other;
        return editUserDescriptor.equals(e.editUserDescriptor)
                && Objects.equals(userToEdit, e.userToEdit);
    }

    /**
     * Stores the details to edit the user with. Each non-empty field value will replace the
     * corresponding field value of the user.
     */
    public static class EditUserDescriptor {
        private Name name;
        private Phone phone;
        private Address address;
        private Set<Allergy> allergies;

        public EditUserDescriptor() {}

        /**
         * Creates a new EditUserDescriptor.
         * A defensive copy of {@code allergies} is used internally.
         */
        public EditUserDescriptor(EditUserCommand.EditUserDescriptor toCopy) {
            setName(toCopy.name);
            setPhone(toCopy.phone);
            setAddress(toCopy.address);
            setAllergies(toCopy.allergies);
        }

        /**
         * Returns true if at least one field is edited.
         */
        public boolean isAnyFieldEdited() {
            return CollectionUtil.isAnyNonNull(this.name, this.phone, this.address, this.allergies);
        }

        public void setName(Name name) {
            this.name = name;
        }

        public Optional<Name> getName() {
            return Optional.ofNullable(name);
        }

        public void setPhone(Phone phone) {
            this.phone = phone;
        }

        public Optional<Phone> getPhone() {
            return Optional.ofNullable(phone);
        }

        public void setAddress(Address address) {
            this.address = address;
        }

        public Optional<Address> getAddress() {
            return Optional.ofNullable(address);
        }

        /**
         * Sets the current set of {@code allergies} to the {@code allergies} set provided
         * A defensive copy of {@code allergies} is used internally.
         */
        public void setAllergies(Set<Allergy> allergies) {
            this.allergies = (allergies != null) ? new HashSet<>(allergies) : null;
        }

        /**
         * Returns an unmodifiable allergy set, which throws {@code UnsupportedOperationException}
         * if modification is attempted.
         * Returns {@code Optional#empty()} if {@code allergies} is null.
         */
        public Optional<Set<Allergy>> getAllergies() {
            return (allergies != null) ? Optional.of(Collections.unmodifiableSet(allergies)) : Optional.empty();
        }

        @Override
        public boolean equals(Object other) {
            // short circuit if same object
            if (other == this) {
                return true;
            }

            // instanceof handles nulls
            if (!(other instanceof EditUserCommand.EditUserDescriptor)) {
                return false;
            }

            // state check
            EditUserCommand.EditUserDescriptor e = (EditUserCommand.EditUserDescriptor) other;

            return getName().equals(e.getName())
                    && getPhone().equals(e.getPhone())
                    && getAddress().equals(e.getAddress())
                    && getAllergies().equals(e.getAllergies());
        }
    }
}
