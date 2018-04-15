package seedu.address.ui;

import java.util.logging.Logger;

import com.google.common.eventbus.Subscribe;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Region;

import javafx.scene.shape.Circle;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.events.model.AddressBookChangedEvent;
import seedu.address.commons.events.ui.ProfilePictureChangedEvent;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.user.UserProfile;

//@@author tohcheryl
/**
 * The User Profile panel of the App.
 */
public class UserProfilePanel extends UiPart<Region> {

    private static final String FXML = "UserProfilePanel.fxml";

    private static final Logger logger = LogsCenter.getLogger(UserProfilePanel.class);

    private static final String PROFILE_PICTURE_PATH = "profilepic.png";

    final Circle clip = new Circle(75, 75, 75);

    private ReadOnlyAddressBook addressBook;

    @FXML
    private ScrollPane profilePane;

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
        profilePane.setFitToWidth(true);
        profilePane.setFitToHeight(true);
        profilePane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        profilePane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        name.setWrapText(true);
        phone.setWrapText(true);
        address.setWrapText(true);
        setUserProfile(addressBook.getUserProfile());
        setProfilePicture();
        registerAsAnEventHandler(this);
    }

    /**
     * Sets the labels to reflect the values of the current {@code UserProfile}
     */
    public void setUserProfile(UserProfile userProfile) {
        name.setText(userProfile.getName().fullName);
        phone.setText(userProfile.getPhone().value);
        address.setText(userProfile.getAddress().value);
        allergies.getChildren().clear();
        userProfile.getAllergies().forEach(allergy -> allergies.getChildren().add(new Label(allergy.allergyName)));
    }

    /**
     * Sets the profile picture to a square image and clips it
     */
    public void setProfilePicture() {
        Image image = new Image("file:" + PROFILE_PICTURE_PATH);
        Image squareImage = getSquareImage(image);
        profilepic.setImage(squareImage);
        profilepic.setClip(clip);
    }

    /**
     * Crops an image to make it square so that it can be displayed properly in the image view
     */
    public Image getSquareImage(Image image) {
        double width = image.getWidth();
        double height = image.getHeight();
        if (width == height) {
            return image;
        } else {
            double lengthOfSquare = width < height ? width : height;
            double centerX = width / 2;
            double centerY = height / 2;
            double startingX = centerX - lengthOfSquare / 2;
            double startingY = centerY - lengthOfSquare / 2;
            PixelReader reader = image.getPixelReader();
            WritableImage squareImage = new WritableImage(reader, (int) startingX,
                    (int) startingY, (int) lengthOfSquare, (int) lengthOfSquare);
            return squareImage;
        }
    }

    @Subscribe
    public void handleAddressBookChangedEvent(AddressBookChangedEvent abce) {
        UserProfile newUserProfile = addressBook.getUserProfile();
        logger.info(LogsCenter.getEventHandlingLogMessage(abce, "User Profile updated to: " + newUserProfile));
        Platform.runLater(() -> {
            setUserProfile(newUserProfile);
        });
    }

    @Subscribe
    public void handleProfilePictureChangedEvent(ProfilePictureChangedEvent ppce) {
        Platform.runLater(() -> {
            setProfilePicture();
        });
    }
}
