package seedu.address.logic.commands;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import javafx.stage.FileChooser;
import seedu.address.commons.core.EventsCenter;
import seedu.address.commons.events.ui.ProfilePictureChangedEvent;

//@@author {tohcheryl}
/**
 * Changes the profile picture of the user
 */
public class ChangePicCommand extends Command {

    public static final String COMMAND_WORD = "changepic";

    public static final String MESSAGE_PIC_CHANGED_ACKNOWLEDGEMENT = "Profile picture has been changed!";

    /**
     * Copies profile image file set by user and saves it into images folder
     */
    public void copyFile(File file) throws IOException {
        int numBytes;
        FileInputStream fis = null;
        FileOutputStream fos = null;
        File outputFile = new File("src/main/resources/images/profilepic.png");
        if (!outputFile.exists()) {
            outputFile.createNewFile();
        }
        try {
            fis = new FileInputStream(file);
            fos = new FileOutputStream(outputFile);
            while ((numBytes = fis.read()) != -1) {
                fos.write(numBytes);
            }
            fis.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public CommandResult execute() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files",
                "*.png", "*.jpg", "*.gif"));
        File selectedFile = fileChooser.showOpenDialog(null);
        try {
            copyFile(selectedFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        EventsCenter.getInstance().post(new ProfilePictureChangedEvent());
        return new CommandResult(MESSAGE_PIC_CHANGED_ACKNOWLEDGEMENT);
    }
}
