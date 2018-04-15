package seedu.address.logic.commands;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import javafx.stage.FileChooser;
import seedu.address.commons.core.EventsCenter;
import seedu.address.commons.events.ui.ProfilePictureChangedEvent;
import seedu.address.logic.commands.exceptions.CommandException;

//@@author tohcheryl
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
