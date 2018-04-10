package seedu.address.logic.commands;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;

import org.junit.Test;


public class ChangePicCommandTest {

    private static String imageFilePath = "src/main/resources/images/defaultprofilepic.png";
    private ChangePicCommand changePicCommand = mock(ChangePicCommand.class);

    @Test
    public void execute_fileSelected_success() {
        File tempFile = new File(imageFilePath);
        when(changePicCommand.selectProfilePic()).thenReturn(tempFile);
        when(changePicCommand.execute()).thenCallRealMethod();
        CommandResult commandResult = changePicCommand.execute();
        assertEquals(ChangePicCommand.MESSAGE_PIC_CHANGED_ACKNOWLEDGEMENT, commandResult.feedbackToUser);
    }
}
