package seedu.address.ui;

import java.io.File;
import java.util.logging.Logger;

import com.google.common.eventbus.Subscribe;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Region;

import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.events.model.UserProfileChangedEvent;
import seedu.address.commons.events.ui.ProfilePictureChangedEvent;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.user.UserProfile;

//@@author {tohcheryl}
/**
 * The User Profile panel of the App.
 */
public class UserProfilePanel extends UiPart<Region> {

    private static final String FXML = "UserProfilePanel.fxml";

    private static final Logger logger = LogsCenter.getLogger(UserProfilePanel.class);

    private static final String PROFILE_PICTURE_PATH = "src/main/resources/images/profilepic.png";

    private static final String DEFAULT_PROFILE_PICTURE_PATH = "src/main/resources/images/defaultprofilepic.png";

    private ReadOnlyAddressBook addressBook;

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
        setUserProfile(addressBook.getUserProfile());
        setProfilePicture();
        registerAsAnEventHandler(this);
    }

    public void setUserProfile(UserProfile userProfile) {
        name.setText(userProfile.getName().fullName);
        phone.setText(userProfile.getPhone().value);
        address.setText(userProfile.getAddress().value);
        allergies.getChildren().clear();
        userProfile.getAllergies().forEach(allergy -> allergies.getChildren().add(new Label(allergy.allergyName)));
    }

    public void setProfilePicture() {
        if (profilePicPresent()) {
            profilepic.setImage(new Image("file:" + PROFILE_PICTURE_PATH));
        } else {
            profilepic.setImage(new Image("file:" + DEFAULT_PROFILE_PICTURE_PATH));
        }
    }

    /**
     * Checks if user has his/her own profile picture saved.
     */
    private boolean profilePicPresent() {
        File file = new File(PROFILE_PICTURE_PATH);
        return file.exists();
    }

    @Subscribe
    public void handleUserProfileChangedEvent(UserProfileChangedEvent abce) {
        UserProfile newUserProfile = addressBook.getUserProfile();
        logger.info(LogsCenter.getEventHandlingLogMessage(abce, "User Profile updated to: " + newUserProfile));
        setUserProfile(newUserProfile);
    }

    @Subscribe
    public void handleProfilePictureChangedEvent(ProfilePictureChangedEvent ppce) {
        setProfilePicture();
    }
}
