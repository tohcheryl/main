# tohcheryl
###### \java\seedu\address\commons\events\ui\ProfilePictureChangedEvent.java
``` java
/**
 * Indicates profile picture of user has changed
 */
public class ProfilePictureChangedEvent extends BaseEvent {

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
```
###### \java\seedu\address\logic\commands\ChangePicCommand.java
``` java
/**
 * Changes the profile picture of the user
 */
public class ChangePicCommand extends Command {

    public static final String COMMAND_WORD = "changepic";

    public static final String MESSAGE_PIC_CHANGED_ACKNOWLEDGEMENT = "Profile picture has been changed!";

    public static final String MESSAGE_PIC_CHANGED_FAILURE = "Unable to set profile picture";

    /**
     * Allows user to select a profile picture
     */
    public File selectProfilePic() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files",
                "*.png", "*.jpg", "*.gif"));
        File selectedFile = fileChooser.showOpenDialog(null);
        return selectedFile;
    }

    @Override
    public CommandResult execute() throws CommandException {
        File outputFile = new File("profilepic.png");
        File selectedFile = selectProfilePic();
        try {
            FileUtils.copyFile(selectedFile, outputFile);
        } catch (IOException | NullPointerException e) {
            throw new CommandException(MESSAGE_PIC_CHANGED_FAILURE);
        }
        EventsCenter.getInstance().post(new ProfilePictureChangedEvent());
        return new CommandResult(MESSAGE_PIC_CHANGED_ACKNOWLEDGEMENT);
    }
}
```
###### \java\seedu\address\logic\commands\EditUserCommand.java
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
```
###### \java\seedu\address\logic\LogicManager.java
``` java
    @Override
    public ReadOnlyAddressBook getAddressBook() {
        return model.getAddressBook();
    }
}
```
###### \java\seedu\address\logic\parser\EditUserCommandParser.java
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
###### \java\seedu\address\MainApp.java
``` java
    /**
     * Sets up a default profile picture if a picture has not been set
     */
    private void initProfilePic() {
        File profilePicFile = new File("profilepic.png");
        if (!profilePicFile.exists()) {
            initDefaultProfilePic();
        }
    }

    /**
     * Saves default profile picture to profilepic.png
     */
    private void initDefaultProfilePic() {
        try {
            File profilePicFile = new File("profilepic.png");
            URL defaultPicUrl = new URL("http://i64.tinypic.com/vo385x.png");
            FileUtils.copyURLToFile(defaultPicUrl, profilePicFile);
        } catch (IOException e) {
            logger.warning("Unable to download default profile picture. "
                    + "Starting HackEat without a profile picture.");
        }
    }

```
###### \java\seedu\address\model\AddressBook.java
``` java
    /**
     * Sets the current user profile to the input {@code profile}.
     */
    public void initUserProfile(UserProfile profile) {
        this.profile = profile;
    }

    /**
     * Replaces the current user profile with {@code editedProfile}.
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
###### \java\seedu\address\model\food\Price.java
``` java
/**
 * Represents a Food's price in HackEat.
 */
public class Price {

    public static final String DEFAULT_PRICE = "0";
    public static final String MESSAGE_PRICE_CONSTRAINTS =
            "Price should contain only numbers and a single decimal point if necessary."
                    + " Prices that have more than 2 decimal places will be truncated."
                    + " A $ sign can be prefixed to the price but it is not required.";
    public static final String CLASS_NAME = "Price";
    private static Locale currentLocale = Locale.US;
    private String value;

    /**
     * Constructs a {@code Price}.
     *
     * @param price A valid price given as a String.
     */
    public Price(String price) {
        requireNonNull(price);
        checkArgument(isValidPrice(price), MESSAGE_PRICE_CONSTRAINTS);
        setPrice(price);
    }

    /**
     * Returns true if a given string is a valid price.
     */
    public static boolean isValidPrice(String inputPrice) {
        if (inputPrice == null) {
            throw new NullPointerException();
        }

        BigDecimalValidator validator = CurrencyValidator.getInstance();
        BigDecimal amount = validator.validate(inputPrice, currentLocale);

        if (amount != null) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Sets price of Food without currency symbol.
     * Truncates given price to the appropriate number of decimal places.
     */
    public void setPrice(String inputPrice) {
        BigDecimalValidator validator = CurrencyValidator.getInstance();
        value = validator.validate(inputPrice, currentLocale).toString();
    }

```
###### \java\seedu\address\model\food\Price.java
``` java
    /**
     * Returns price of Food as a String.
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
###### \java\seedu\address\model\Model.java
``` java
    /**
     * Sets the user profile of address book to {@code target}.
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
###### \java\seedu\address\model\ModelManager.java
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
###### \java\seedu\address\model\user\exceptions\DuplicateUserException.java
``` java
/**
 * Signals that the operation will result in duplicate Food objects.
 */
public class DuplicateUserException extends DuplicateDataException {
    public DuplicateUserException() {
        super("Operation would result in no change to user profile");
    }
}
```
###### \java\seedu\address\ui\UserProfilePanel.java
``` java
/**
 * The User Profile panel of the App.
 */
public class UserProfilePanel extends UiPart<Region> {

    private static final String FXML = "UserProfilePanel.fxml";

    private static final Logger logger = LogsCenter.getLogger(UserProfilePanel.class);

    private static final String PROFILE_PICTURE_PATH = "profilepic.png";

    final Circle clip = new Circle(75, 75, 75);

    private ReadOnlyAddressBook addressBook;

    @FXML
    private ScrollPane profilePane;

    @FXML
    private ImageView profilepic;

    @FXML
    private Label name;

    @FXML
    private Label phone;

    @FXML
    private Label address;

    @FXML
    private FlowPane allergies;

    public UserProfilePanel(ReadOnlyAddressBook addressBook) {
        super(FXML);
        this.addressBook = addressBook;
        profilePane.setFitToWidth(true);
        profilePane.setFitToHeight(true);
        profilePane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        profilePane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        name.setWrapText(true);
        phone.setWrapText(true);
        address.setWrapText(true);
        setUserProfile(addressBook.getUserProfile());
        setProfilePicture();
        registerAsAnEventHandler(this);
    }

    /**
     * Sets the labels to reflect the values of the current {@code UserProfile}
     */
    public void setUserProfile(UserProfile userProfile) {
        name.setText(userProfile.getName().fullName);
        phone.setText(userProfile.getPhone().value);
        address.setText(userProfile.getAddress().value);
        allergies.getChildren().clear();
        userProfile.getAllergies().forEach(allergy -> allergies.getChildren().add(new Label(allergy.allergyName)));
    }

    /**
     * Sets the profile picture to a square image and clips it
     */
    public void setProfilePicture() {
        Image image = new Image("file:" + PROFILE_PICTURE_PATH);
        Image squareImage = getSquareImage(image);
        profilepic.setImage(squareImage);
        profilepic.setClip(clip);
    }

    /**
     * Crops an image to make it square so that it can be displayed properly in the image view
     */
    public Image getSquareImage(Image image) {
        double width = image.getWidth();
        double height = image.getHeight();
        if (width == height) {
            return image;
        } else {
            double lengthOfSquare = width < height ? width : height;
            double centerX = width / 2;
            double centerY = height / 2;
            double startingX = centerX - lengthOfSquare / 2;
            double startingY = centerY - lengthOfSquare / 2;
            PixelReader reader = image.getPixelReader();
            WritableImage squareImage = new WritableImage(reader, (int) startingX,
                    (int) startingY, (int) lengthOfSquare, (int) lengthOfSquare);
            return squareImage;
        }
    }

    @Subscribe
    public void handleAddressBookChangedEvent(AddressBookChangedEvent abce) {
        UserProfile newUserProfile = addressBook.getUserProfile();
        logger.info(LogsCenter.getEventHandlingLogMessage(abce, "User Profile updated to: " + newUserProfile));
        Platform.runLater(() -> {
            setUserProfile(newUserProfile);
        });
    }

    @Subscribe
    public void handleProfilePictureChangedEvent(ProfilePictureChangedEvent ppce) {
        Platform.runLater(() -> {
            setProfilePicture();
        });
    }
}
```
###### \resources\view\UserProfilePanel.fxml
``` fxml
<?import javafx.scene.control.ScrollPane?>
<ScrollPane fx:id="profilePane" xmlns="http://javafx.com/javafx/8" xmlns:fx="http://javafx.com/fxml/1">
  <VBox fx:id="profileVBox" >
    <ImageView fx:id="profilepic" fitHeight="150.0" fitWidth="150.0" pickOnBounds="true" preserveRatio="true" />
    <Label fx:id="name" text="\$name" alignment="CENTER"/>
    <Label fx:id="phone" text="\$phone" alignment="CENTER"/>
    <Label fx:id="address" text="\$address" alignment="CENTER"/>
    <FlowPane fx:id="allergies" />
  </VBox>
</ScrollPane>
```
