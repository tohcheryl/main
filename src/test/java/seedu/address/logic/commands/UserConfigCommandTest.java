package seedu.address.logic.commands;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import seedu.address.logic.CommandHistory;
import seedu.address.logic.UndoRedoStack;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.user.UserProfile;
import seedu.address.testutil.UserProfileBuilder;

//@@author tohcheryl
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
