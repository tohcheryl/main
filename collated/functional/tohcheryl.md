# tohcheryl
###### /java/seedu/address/logic/parser/EditUserCommandParser.java
``` java
/**
 * Parses input arguments and creates a new EditUserCommand object
 */
public class EditUserCommandParser implements Parser<EditUserCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the EditCommand
     * and returns an EditCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public EditUserCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_PHONE, PREFIX_ADDRESS, PREFIX_ALLERGIES);

        EditUserCommand.EditUserDescriptor editUserDescriptor = new EditUserCommand.EditUserDescriptor();
        try {
            ParserUtil.parseName(argMultimap.getValue(PREFIX_NAME)).ifPresent(editUserDescriptor::setName);
            ParserUtil.parsePhone(argMultimap.getValue(PREFIX_PHONE)).ifPresent(editUserDescriptor::setPhone);
            ParserUtil.parseAddress(argMultimap.getValue(PREFIX_ADDRESS)).ifPresent(editUserDescriptor::setAddress);
            parseAllergiesForEdit(argMultimap.getAllValues(PREFIX_ALLERGIES))
                    .ifPresent(editUserDescriptor::setAllergies);
        } catch (IllegalValueException ive) {
            throw new ParseException(ive.getMessage(), ive);
        }

        if (!editUserDescriptor.isAnyFieldEdited()) {
            throw new ParseException(EditUserCommand.MESSAGE_NOT_EDITED);
        }

        return new EditUserCommand(editUserDescriptor);
    }

    /**
     * Parses {@code Collection<String> allergies} into a {@code Set<Allergy>} if {@code allergies} is non-empty.
     * If {@code allergies} contain only one element which is an empty string, it will be parsed into a
     * {@code Set<Allergy>} containing zero allergies.
     */
    private Optional<Set<Allergy>> parseAllergiesForEdit(Collection<String> allergies) throws IllegalValueException {
        assert allergies != null;

        if (allergies.isEmpty()) {
            return Optional.empty();
        }
        Collection<String> allergySet = allergies.size() == 1 && allergies.contains("")
                ? Collections.emptySet() : allergies;
        return Optional.of(ParserUtil.parseAllergies(allergySet));
    }
}
```
###### /java/seedu/address/logic/commands/EditUserCommand.java
``` java
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
    public static final String MESSAGE_DUPLICATE_USER = "New user profile is the same as the one set previously.";

    private final EditUserCommand.EditUserDescriptor editUserDescriptor;

    private UserProfile userToEdit;
    private UserProfile editedUser;

    /**
     * @param editUserDescriptor details to edit the food with
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
        Set<Food> recentFoods = userToEdit.getRecentFoods();
        editedUser.setRecentFoods(new UniqueFoodList(recentFoods));
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
         * Copy constructor.
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
         * Sets {@code allergies} to this object's {@code allergies}.
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
```
###### /java/seedu/address/model/user/UserProfile.java
``` java
    /**
     * Constructs a {@code UserProfile} object.
     *  @param name    Name of user
     * @param phone   Phone number of user
     * @param address Address of user for food delivery
     * @param recentFoods Food eaten recently
     */
    public UserProfile(Name name, Phone phone, Address address, Set<Allergy> allergies, Set<Food> recentFoods) {
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.allergies = new UniqueAllergyList(allergies);
        this.recentFoods = new UniqueFoodList(recentFoods);
    }

```
###### /java/seedu/address/model/user/UserProfile.java
``` java
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

```
###### /java/seedu/address/model/AddressBook.java
``` java
    /**
     * Initialises user profile with {@code profile}.
     */
    public void initUserProfile(UserProfile profile) {
        this.profile = profile;
    }

    /**
     * Replaces current user profile {@code target} with {@code editedProfile}.
     * @throws DuplicateUserException if there is no change in user profile
     */
    public void updateUserProfile(UserProfile editedProfile) throws DuplicateUserException {
        assert profile != null;
        if (!profile.equals(editedProfile)) {
            profile = editedProfile;
        } else {
            throw new DuplicateUserException();
        }
    }

```
###### /java/seedu/address/model/ModelManager.java
``` java
    @Override
    public UserProfile getUserProfile() throws NullPointerException {
        return addressBook.getUserProfile();
    }

    @Override
    public void initUserProfile(UserProfile userProfile) {
        addressBook.initUserProfile(userProfile);
        indicateAddressBookChanged();
    }

```
###### /java/seedu/address/model/food/Price.java
``` java
    /**
     * Returns price of Food as a BigDecimal.
     */
    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Price // instanceof handles nulls
                && this.value.equals(((Price) other).value)); // state check
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
```
###### /java/seedu/address/model/Model.java
``` java
    /**
     * Initialises user profile of address book with {@code target}.
     */
    void initUserProfile(UserProfile target);

    /**
     * Returns the current user profile.
     */
    UserProfile getUserProfile() throws NullPointerException;

    /**
     * Replaces the current user profile {@code target} with {@code editedProfile}.
     */
    void updateUserProfile(UserProfile editedProfile) throws DuplicateUserException;
}
```
