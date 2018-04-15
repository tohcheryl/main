# tohcheryl
###### \java\guitests\guihandles\UserProfilePanelHandle.java
``` java
/**
 * A handle to the {@code UserProfilePanel} in the GUI.
 */
public class UserProfilePanelHandle extends NodeHandle<Node> {

    private static final String NAME_FIELD_ID = "#name";
    private static final String ADDRESS_FIELD_ID = "#address";
    private static final String PHONE_FIELD_ID = "#phone";
    private static final String ALLERGIES_FIELD_ID = "#allergies";

    private final Label nameLabel;
    private final Label addressLabel;
    private final Label phoneLabel;
    private final List<Label> allergyLabels;

    public UserProfilePanelHandle(Node cardNode) {
        super(cardNode);

        this.nameLabel = getChildNode(NAME_FIELD_ID);
        this.addressLabel = getChildNode(ADDRESS_FIELD_ID);
        this.phoneLabel = getChildNode(PHONE_FIELD_ID);
        Region allergiesContainer = getChildNode(ALLERGIES_FIELD_ID);
        this.allergyLabels = allergiesContainer
                .getChildrenUnmodifiable()
                .stream()
                .map(Label.class::cast)
                .collect(Collectors.toList());
    }

    public String getName() {
        return nameLabel.getText();
    }

    public String getAddress() {
        return addressLabel.getText();
    }

    public String getPhone() {
        return phoneLabel.getText();
    }

    public List<String> getAllergies() {
        return allergyLabels
                .stream()
                .map(Label::getText)
                .collect(Collectors.toList());
    }
}
```
###### \java\seedu\address\logic\commands\AddCommandTest.java
``` java
        @Override
        public UserProfile getUserProfile() throws NullPointerException {
            fail("This method should not be called.");
            return null;
        }
    }

    /**
     * A Model stub that always throw a DuplicateFoodException when trying to add a food.
     */
    private class ModelStubThrowingDuplicateFoodException extends ModelStub {
        @Override
        public void addFood(Food food) throws DuplicateFoodException {
            throw new DuplicateFoodException();
        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            return new AddressBook();
        }
    }

    /**
     * A Model stub that always accept the food being added.
     */
    private class ModelStubAcceptingFoodAdded extends ModelStub {
        final ArrayList<Food> foodsAdded = new ArrayList<>();

        @Override
        public void addFood(Food food) throws DuplicateFoodException {
            requireNonNull(food);
            foodsAdded.add(food);
        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            return new AddressBook();
        }
    }

}
```
###### \java\seedu\address\logic\commands\ChangePicCommandTest.java
``` java
public class ChangePicCommandTest {

    private static String imageFilePath = "src/main/resources/images/defaultprofilepic.png";
    private ChangePicCommand changePicCommand = mock(ChangePicCommand.class);

    @Test
    public void execute_fileSelected_success() throws CommandException {
        File tempFile = new File(imageFilePath);
        when(changePicCommand.selectProfilePic()).thenReturn(tempFile);
        when(changePicCommand.execute()).thenCallRealMethod();
        CommandResult commandResult = changePicCommand.execute();
        assertEquals(ChangePicCommand.MESSAGE_PIC_CHANGED_ACKNOWLEDGEMENT, commandResult.feedbackToUser);
    }

    @Test
    public void execute_noFileSelected_failure() throws CommandException {
        File tempFile = null;
        when(changePicCommand.selectProfilePic()).thenReturn(tempFile);
        when(changePicCommand.execute()).thenCallRealMethod();
        assertCommandFailure(changePicCommand, MESSAGE_PIC_CHANGED_FAILURE);
    }
}
```
###### \java\seedu\address\logic\commands\CommandTestUtil.java
``` java
    /**
     * Executes the given {@code command} and confirms that <br>
     * - a {@code CommandException} is thrown <br>
     * - the CommandException message matches {@code expectedMessage}
     */
    public static void assertCommandFailure(Command command, String expectedMessage) {
        try {
            command.execute();
            fail("The expected CommandException was not thrown.");
        } catch (CommandException e) {
            assertEquals(expectedMessage, e.getMessage());
        }
    }

```
###### \java\seedu\address\logic\commands\EditUserCommandTest.java
``` java
public class EditUserCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_allFieldsSpecified_success() throws Exception {
        UserProfile editedUserProfile = new UserProfileBuilder().build();
        EditUserCommand.EditUserDescriptor descriptor = new EditUserDescriptorBuilder(editedUserProfile).build();
        EditUserCommand editUserCommand = prepareCommand(descriptor);

        String expectedMessage = String.format(EditUserCommand.MESSAGE_EDIT_USER_SUCCESS, editedUserProfile);

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.updateUserProfile(editedUserProfile);

        assertCommandSuccess(editUserCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_someFieldsSpecified_success() throws Exception {
        UserProfile currentProfile = model.getUserProfile();

        UserProfileBuilder userProfileSet = new UserProfileBuilder(currentProfile);
        UserProfile editedUserProfile = userProfileSet.withName(VALID_NAME_BANANA).withPhone(VALID_PHONE_BANANA)
                .build();

        EditUserCommand.EditUserDescriptor descriptor = new EditUserDescriptorBuilder().withName(VALID_NAME_BANANA)
                .withPhone(VALID_PHONE_BANANA).build();
        EditUserCommand editCommand = prepareCommand(descriptor);

        String expectedMessage = String.format(EditUserCommand.MESSAGE_EDIT_USER_SUCCESS, editedUserProfile);

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.updateUserProfile(editedUserProfile);

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_duplicateUserProfile_failure() {
        UserProfile currentUserProfile = model.getUserProfile();
        EditUserCommand.EditUserDescriptor descriptor = new EditUserDescriptorBuilder(currentUserProfile).build();
        EditUserCommand editUserCommand = prepareCommand(descriptor);

        assertCommandFailure(editUserCommand, model, EditUserCommand.MESSAGE_DUPLICATE_USER);
    }

    /**
     * Returns an {@code EditUserCommand} with parameter {@code descriptor}
     */
    private EditUserCommand prepareCommand(EditUserCommand.EditUserDescriptor descriptor) {
        EditUserCommand editUserCommand = new EditUserCommand(descriptor);
        editUserCommand.setData(model, new CommandHistory(), new UndoRedoStack());
        return editUserCommand;
    }

    /**
     * 1. Edits the {@code UserProfile}.
     * 2. Undo the edit.
     * 3. Redo the edit. This ensures {@code RedoCommand} edits the user profile object.
     */
    @Test
    public void executeUndoRedo_validUserProfile_sameUserProfileEdited() throws Exception {
        UndoRedoStack undoRedoStack = new UndoRedoStack();
        UndoCommand undoCommand = prepareUndoCommand(model, undoRedoStack);
        RedoCommand redoCommand = prepareRedoCommand(model, undoRedoStack);
        UserProfile editedUserProfile = new UserProfileBuilder().build();
        EditUserCommand.EditUserDescriptor descriptor = new EditUserDescriptorBuilder(editedUserProfile).build();
        EditUserCommand editUserCommand = prepareCommand(descriptor);
        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());

        UserProfile userProfileToEdit = model.getUserProfile();
        // edit -> edits user profile
        editUserCommand.execute();
        undoRedoStack.push(editUserCommand);

        // undo -> reverts addressbook back to previous state
        assertCommandSuccess(undoCommand, model, UndoCommand.MESSAGE_SUCCESS, expectedModel);

        assertNotEquals(userProfileToEdit, editedUserProfile);
        // redo -> edits user profile
        assertCommandSuccess(redoCommand, model, RedoCommand.MESSAGE_SUCCESS, expectedModel);
    }
}
```
###### \java\seedu\address\logic\commands\UserConfigCommandTest.java
``` java
public class UserConfigCommandTest {

    private Model model = new ModelManager(new AddressBook(), new UserPrefs());
    private UserProfile validUserProfile = new UserProfileBuilder().build();

    @Test
    public void execute_validUserProfile_setSuccessfully() throws Exception {
        CommandResult commandResult = getUserConfigCommand(validUserProfile, model).execute();
        assertEquals(UserConfigCommand.MESSAGE_SUCCESS, commandResult.feedbackToUser);
        assertEquals(validUserProfile, model.getUserProfile());
    }

    @Test
    public void execute_duplicateUserProfile_setSuccessfully() throws Exception {
        UserProfile currentUser = model.getUserProfile();
        CommandResult commandResult = getUserConfigCommand(currentUser, model).execute();
        assertEquals(UserConfigCommand.MESSAGE_SUCCESS, commandResult.feedbackToUser);
        assertEquals(currentUser, model.getUserProfile());
    }

    /**
     * Generates a new UserConfigCommand.
     */
    private UserConfigCommand getUserConfigCommand(UserProfile userProfile, Model model) {
        UserConfigCommand command = new UserConfigCommand(userProfile);
        command.setData(model, new CommandHistory(), new UndoRedoStack());
        return command;
    }
}
```
###### \java\seedu\address\logic\parser\EditUserCommandParserTest.java
``` java
public class EditUserCommandParserTest {

    private static final String ALLERGY_EMPTY = " " + PREFIX_ALLERGIES;

    private static final String MESSAGE_INVALID_FORMAT =
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditUserCommand.MESSAGE_USAGE);

    private EditUserCommandParser parser = new EditUserCommandParser();

    @Test
    public void parse_missingParts_failure() {
        // no field specified
        assertParseFailure(parser, "", EditUserCommand.MESSAGE_NOT_EDITED);
    }

    @Test
    public void parse_invalidValue_failure() {
        assertParseFailure(parser, INVALID_NAME_DESC, Name.MESSAGE_NAME_CONSTRAINTS); // invalid name
        assertParseFailure(parser, INVALID_PHONE_DESC, Phone.MESSAGE_PHONE_CONSTRAINTS); // invalid phone
        assertParseFailure(parser, INVALID_ADDRESS_DESC, Address.MESSAGE_ADDRESS_CONSTRAINTS); // invalid address
        assertParseFailure(parser, INVALID_ALLERGY_DESC, Allergy.MESSAGE_ALLERGY_CONSTRAINTS); // invalid allergy

        // invalid phone followed by valid address
        assertParseFailure(parser, INVALID_PHONE_DESC + ADDRESS_DESC_APPLE, Phone.MESSAGE_PHONE_CONSTRAINTS);

        // valid phone followed by invalid phone. The test case for invalid phone followed by valid phone
        // is tested at {@code parse_invalidValueFollowedByValidValue_success()}
        assertParseFailure(parser, PHONE_DESC_BANANA + INVALID_PHONE_DESC, Phone.MESSAGE_PHONE_CONSTRAINTS);

        // while parsing {@code PREFIX_ALLERGY} alone will reset the allergies of the {@code UserProfile} being edited,
        // parsing it together with a valid allergy results in error
        assertParseFailure(parser, ALLERGY_DESC_LACTOSE + ALLERGY_DESC_POLLEN + ALLERGY_EMPTY,
                Allergy.MESSAGE_ALLERGY_CONSTRAINTS);
        assertParseFailure(parser, ALLERGY_DESC_LACTOSE + ALLERGY_EMPTY + ALLERGY_DESC_POLLEN,
                Allergy.MESSAGE_ALLERGY_CONSTRAINTS);
        assertParseFailure(parser, ALLERGY_EMPTY + ALLERGY_DESC_LACTOSE + ALLERGY_DESC_POLLEN,
                Allergy.MESSAGE_ALLERGY_CONSTRAINTS);

        // multiple invalid values, but only the first invalid value is captured
        assertParseFailure(parser, INVALID_NAME_DESC + INVALID_PHONE_DESC + VALID_ADDRESS_APPLE,
                Name.MESSAGE_NAME_CONSTRAINTS);
    }

    @Test
    public void parse_allFieldsSpecified_success() {
        String userInput = PHONE_DESC_BANANA + ALLERGY_DESC_POLLEN + ADDRESS_DESC_APPLE + NAME_DESC_APPLE;

        EditUserCommand.EditUserDescriptor descriptor = new EditUserDescriptorBuilder().withName(VALID_NAME_APPLE)
                .withPhone(VALID_PHONE_BANANA).withAddress(VALID_ADDRESS_APPLE)
                .withAllergies(VALID_ALLERGY_POLLEN).build();
        EditUserCommand expectedCommand = new EditUserCommand(descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_someFieldsSpecified_success() {
        String userInput = PHONE_DESC_BANANA + NAME_DESC_APPLE;

        EditUserCommand.EditUserDescriptor descriptor = new EditUserDescriptorBuilder().withPhone(VALID_PHONE_BANANA)
                .withName(VALID_NAME_APPLE).build();
        EditUserCommand expectedCommand = new EditUserCommand(descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_oneFieldSpecified_success() {
        // name
        String userInput = NAME_DESC_APPLE;
        EditUserCommand.EditUserDescriptor descriptor = new EditUserDescriptorBuilder().withName(VALID_NAME_APPLE)
                .build();
        EditUserCommand expectedCommand = new EditUserCommand(descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // phone
        userInput = PHONE_DESC_APPLE;
        descriptor = new EditUserDescriptorBuilder().withPhone(VALID_PHONE_APPLE).build();
        expectedCommand = new EditUserCommand(descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // address
        userInput = ADDRESS_DESC_APPLE;
        descriptor = new EditUserDescriptorBuilder().withAddress(VALID_ADDRESS_APPLE).build();
        expectedCommand = new EditUserCommand(descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // allergies
        userInput = ALLERGY_DESC_LACTOSE;
        descriptor = new EditUserDescriptorBuilder().withAllergies(VALID_ALLERGY_LACTOSE).build();
        expectedCommand = new EditUserCommand(descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_multipleRepeatedFields_acceptsLast() {
        String userInput = PHONE_DESC_APPLE + ADDRESS_DESC_APPLE + ALLERGY_DESC_POLLEN + PHONE_DESC_APPLE
                + ADDRESS_DESC_BANANA + ALLERGY_DESC_LACTOSE + PHONE_DESC_BANANA;

        EditUserCommand.EditUserDescriptor descriptor = new EditUserDescriptorBuilder().withPhone(VALID_PHONE_BANANA)
                .withAddress(VALID_ADDRESS_BANANA).withAllergies(VALID_ALLERGY_POLLEN, VALID_ALLERGY_LACTOSE).build();
        EditUserCommand expectedCommand = new EditUserCommand(descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_invalidValueFollowedByValidValue_success() {
        // no other valid values specified
        String userInput = INVALID_PHONE_DESC + PHONE_DESC_BANANA;
        EditUserCommand.EditUserDescriptor descriptor = new EditUserDescriptorBuilder().withPhone(VALID_PHONE_BANANA)
                .build();
        EditUserCommand expectedCommand = new EditUserCommand(descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // other valid values specified
        userInput = INVALID_PHONE_DESC + ADDRESS_DESC_BANANA + PHONE_DESC_BANANA;
        descriptor = new EditUserDescriptorBuilder().withPhone(VALID_PHONE_BANANA).withAddress(VALID_ADDRESS_BANANA)
                .build();
        expectedCommand = new EditUserCommand(descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_resetAllergies_success() {
        String userInput = ALLERGY_EMPTY;

        EditUserCommand.EditUserDescriptor descriptor = new EditUserDescriptorBuilder().withAllergies().build();
        EditUserCommand expectedCommand = new EditUserCommand(descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }
}
```
###### \java\seedu\address\logic\parser\UserConfigCommandParserTest.java
``` java
public class UserConfigCommandParserTest {

    private UserConfigCommandParser parser = new UserConfigCommandParser();

    @Test
    public void parse_allFieldsPresent_success() {
        UserProfile expectedUserProfile = new UserProfile(new Name(VALID_NAME_APPLE), new Phone(VALID_PHONE_APPLE),
                new Address(VALID_ADDRESS_APPLE), getAllergySet("lactose"));

        assertParseSuccess(parser, NAME_DESC_APPLE + PHONE_DESC_APPLE + ADDRESS_DESC_APPLE + ALLERGY_DESC_LACTOSE,
                new UserConfigCommand(expectedUserProfile));
    }

    @Test
    public void parse_compulsoryFieldMissing_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, UserConfigCommand.MESSAGE_USAGE);

        // missing name prefix
        assertParseFailure(parser, VALID_NAME_BANANA + PHONE_DESC_BANANA + ADDRESS_DESC_BANANA,
                expectedMessage);

        // missing phone prefix
        assertParseFailure(parser, NAME_DESC_BANANA + VALID_PHONE_BANANA + ADDRESS_DESC_BANANA,
                expectedMessage);

        // missing address prefix
        assertParseFailure(parser, NAME_DESC_BANANA + PHONE_DESC_BANANA + VALID_ADDRESS_BANANA,
                expectedMessage);

        // all prefixes missing
        assertParseFailure(parser, VALID_NAME_BANANA + VALID_PHONE_BANANA + VALID_ADDRESS_BANANA,
                expectedMessage);
    }

    @Test
    public void parse_invalidValue_failure() {
        // invalid name
        assertParseFailure(parser, INVALID_NAME_DESC + PHONE_DESC_BANANA + ADDRESS_DESC_BANANA,
                Name.MESSAGE_NAME_CONSTRAINTS);

        // invalid phone
        assertParseFailure(parser, NAME_DESC_BANANA + INVALID_PHONE_DESC + ADDRESS_DESC_BANANA,
                Phone.MESSAGE_PHONE_CONSTRAINTS);

        // invalid address
        assertParseFailure(parser, NAME_DESC_BANANA + PHONE_DESC_BANANA + INVALID_ADDRESS_DESC,
                Address.MESSAGE_ADDRESS_CONSTRAINTS);

        // invalid allergies
        assertParseFailure(parser, NAME_DESC_BANANA + PHONE_DESC_BANANA + ADDRESS_DESC_BANANA
                + INVALID_ALLERGY_DESC, Allergy.MESSAGE_ALLERGY_CONSTRAINTS);
    }
}
```
###### \java\seedu\address\model\AddressBookTest.java
``` java
        @Override
        public UserProfile getUserProfile() {
            return profile;
        }
    }

}
```
###### \java\seedu\address\model\food\allergy\AllergyTest.java
``` java
public class AllergyTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> new Allergy(null));
    }

    @Test
    public void constructor_invalidAllergyName_throwsIllegalArgumentException() {
        String invalidAllergyName = "";
        Assert.assertThrows(IllegalArgumentException.class, () -> new Allergy(invalidAllergyName));
    }

    @Test
    public void isValidAllergyName() {
        // null tag name
        Assert.assertThrows(NullPointerException.class, () -> Allergy.isValidAllergyName(null));
    }
}
```
###### \java\seedu\address\model\food\allergy\UniqueAllergyListTest.java
``` java
public class UniqueAllergyListTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void asObservableList_modifyList_throwsUnsupportedOperationException() {
        UniqueAllergyList uniqueAllergyList = new UniqueAllergyList();
        thrown.expect(UnsupportedOperationException.class);
        uniqueAllergyList.asObservableList().remove(0);
    }
}
```
###### \java\seedu\address\model\food\PriceTest.java
``` java
public class PriceTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> new Price(null));
    }

    @Test
    public void constructor_invalidPrice_throwsIllegalArgumentException() {
        String invalidPrice = "";
        Assert.assertThrows(IllegalArgumentException.class, () -> new Price(invalidPrice));
    }

    @Test
    public void isValidPrice() {
        // null price
        Assert.assertThrows(NullPointerException.class, () -> Price.isValidPrice(null));

        // invalid prices
        assertFalse(Price.isValidPrice("12a.45"));
        assertFalse(Price.isValidPrice("$12a.45"));
        assertFalse(Price.isValidPrice("$1.p0"));
        assertFalse(Price.isValidPrice("203$0"));
        assertFalse(Price.isValidPrice("10$"));
        assertFalse(Price.isValidPrice("12.40$"));
        assertFalse(Price.isValidPrice("€2,0"));
        assertFalse(Price.isValidPrice("20000¥"));

        // valid prices in US
        assertTrue(Price.isValidPrice("$20"));
        assertTrue(Price.isValidPrice("$90.30"));
        assertTrue(Price.isValidPrice("$20.590"));
    }

    @Test
    public void setPrice() {
        Price p = new Price("$23.40");
        p.setPrice("$40.00");
        assertEquals("40.00", p.getValue());
    }

    @Test
    public void getValue() {
        Price p = new Price("$23.40");
        assertEquals("23.40", p.getValue());
    }

    @Test
    public void toString_validPrice_returnsString() {
        Price p = new Price("$23.40");
        assertEquals("23.40", p.toString());
    }

    @Test
    public void equals_validPrice_returnsEqual() {
        Price p = new Price("$23.40");
        Price p2 = new Price("$23.40");
        assertEquals(p, p2);
    }

    @Test
    public void hashCode_validPrice_returnsHashCode() {
        Price p = new Price("$23.40");
        Price p2 = new Price("$23.40");
        assertEquals(p.hashCode(), p2.hashCode());
    }
}
```
###### \java\seedu\address\storage\XmlAdaptedUserProfileTest.java
``` java
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
```
###### \java\seedu\address\testutil\EditUserDescriptorBuilder.java
``` java
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
```
###### \java\seedu\address\testutil\UserProfileBuilder.java
``` java
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
```
###### \java\seedu\address\testutil\UserProfileUtil.java
``` java
/**
 * A utility class for UserProfile.
 */
public class UserProfileUtil {
    /**
     * Returns an add command string for adding the {@code userProfile}.
     */
    public static String getUserConfigCommand(UserProfile userProfile) {
        return UserConfigCommand.COMMAND_WORD + " " + getUserDetails(userProfile);
    }

    /**
     * Returns the part of command string for the given {@code userProfile}'s details.
     */
    public static String getUserDetails(UserProfile userProfile) {
        StringBuilder sb = new StringBuilder();
        sb.append(PREFIX_NAME + userProfile.getName().fullName + " ");
        sb.append(PREFIX_PHONE + userProfile.getPhone().value + " ");
        sb.append(PREFIX_ADDRESS + userProfile.getAddress().value + " ");
        userProfile.getAllergies().stream().forEach(s -> sb.append(PREFIX_ALLERGIES + s.allergyName + " ")
        );
        return sb.toString();
    }
}
```
###### \java\seedu\address\ui\testutil\GuiTestAssert.java
``` java
    /**
     * Asserts that {@code actualPanel} displays the details of {@code expectedUser}.
     */
    public static void assertPanelDisplaysUser(UserProfile userProfile, UserProfilePanelHandle actualPanel) {
        assertEquals(userProfile.getName().fullName, actualPanel.getName());
        assertEquals(userProfile.getPhone().value, actualPanel.getPhone());
        assertEquals(userProfile.getAddress().value, actualPanel.getAddress());
        assertEquals(userProfile.getAllergies().stream().map(allergy -> allergy.allergyName)
                        .collect(Collectors.toList()),
                actualPanel.getAllergies());
    }
}
```
###### \java\seedu\address\ui\UserProfilePanelTest.java
``` java
public class UserProfilePanelTest extends GuiUnitTest {

    private static final ReadOnlyAddressBook ADDRESS_BOOK = SampleDataUtil.getSampleAddressBook();
    private static final Logger logger = LogsCenter.getLogger(UserProfilePanelTest.class);

    @Test
    public void display() {
        // no allergies
        UserProfile userWithNoAllergy = new UserProfileBuilder().withAllergies(new String[0]).build();
        UserProfilePanel userProfilePanel = new UserProfilePanel(ADDRESS_BOOK);
        userProfilePanel.setUserProfile(userWithNoAllergy);
        uiPartRule.setUiPart(userProfilePanel);
        assertPanelDisplay(userProfilePanel, userWithNoAllergy);

        // with allergies
        UserProfile userWithAllergies = new UserProfileBuilder().build();
        userProfilePanel = new UserProfilePanel(ADDRESS_BOOK);
        userProfilePanel.setUserProfile(userWithAllergies);
        uiPartRule.setUiPart(userProfilePanel);
        assertPanelDisplay(userProfilePanel, userWithAllergies);
    }

    @Test
    public void getSquareImage() {
        Image testImage = new Image("file:docs/images/StorageClassDiagram.png");
        UserProfilePanel userProfilePanel = new UserProfilePanel(ADDRESS_BOOK);
        Image outputImage = userProfilePanel.getSquareImage(testImage);
        int width = (int) outputImage.getWidth();
        int height = (int) outputImage.getHeight();
        Assert.assertEquals(width, height);
    }

    /**
     * Asserts that {@code userProfilePanel} displays the details of {@code userProfile} correctly
     */
    private void assertPanelDisplay(UserProfilePanel userProfilePanel, UserProfile userProfile) {
        guiRobot.pauseForHuman();

        UserProfilePanelHandle userProfilePanelHandle = new UserProfilePanelHandle(userProfilePanel.getRoot());

        // verify user details are displayed correctly
        assertPanelDisplaysUser(userProfile, userProfilePanelHandle);
    }
}
```
