package seedu.address.logic.commands;

//@@author {tohcheryl}

import java.io.File;

import javafx.stage.FileChooser;

/**
 * Changes the profile picture of the user
 */
public class ChangePicCommand extends Command {
    public static final String COMMAND_WORD = "changepic";

    public static final String MESSAGE_PIC_CHANGED_ACKNOWLEDGEMENT = "Profile picture has been changed!";

    @Override
    public CommandResult execute() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files",
                "*.png", "*.jpg", "*.gif"));
        File selectedFile = fileChooser.showOpenDialog(null);
        //EventsCenter.getInstance().post(new ExitAppRequestEvent());
        return new CommandResult(MESSAGE_PIC_CHANGED_ACKNOWLEDGEMENT);
    }

}
