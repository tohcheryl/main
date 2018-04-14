package seedu.address.logic.commands;

import static org.junit.Assert.assertNotEquals;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BANANA;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_BANANA;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.prepareRedoCommand;
import static seedu.address.logic.commands.CommandTestUtil.prepareUndoCommand;
import static seedu.address.testutil.TypicalFoods.getTypicalAddressBook;

import org.junit.Test;

import seedu.address.logic.CommandHistory;
import seedu.address.logic.UndoRedoStack;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.user.UserProfile;
import seedu.address.testutil.EditUserDescriptorBuilder;
import seedu.address.testutil.UserProfileBuilder;

//@@author tohcheryl
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
