package seedu.address.logic.commands;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static seedu.address.logic.commands.ChangePicCommand.MESSAGE_PIC_CHANGED_FAILURE;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;

import java.io.File;

import org.junit.Test;

import seedu.address.logic.commands.exceptions.CommandException;

//@@author tohcheryl
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
