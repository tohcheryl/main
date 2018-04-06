package seedu.address.logic.commands;

import java.io.File;

import javafx.stage.FileChooser;
import seedu.address.commons.core.EventsCenter;
import seedu.address.commons.events.ui.ProfilePictureChangedEvent;
import seedu.address.commons.util.FileUtil;

//@@author {tohcheryl}
/**
 * Changes the profile picture of the user
 */
public class ChangePicCommand extends Command {

    public static final String COMMAND_WORD = "changepic";

    public static final String MESSAGE_PIC_CHANGED_ACKNOWLEDGEMENT = "Profile picture has been changed!";

    /**
     * Selects a profile picture
     */
    public File selectProfilePic() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files",
                "*.png", "*.jpg", "*.gif"));
        File selectedFile = fileChooser.showOpenDialog(null);
        return selectedFile;
    }

    @Override
    public CommandResult execute() {
        File outputFile = new File("profilepic.png");
        File selectedFile = selectProfilePic();
        FileUtil.copyFile(selectedFile, outputFile);
        EventsCenter.getInstance().post(new ProfilePictureChangedEvent());
        return new CommandResult(MESSAGE_PIC_CHANGED_ACKNOWLEDGEMENT);
    }
}
