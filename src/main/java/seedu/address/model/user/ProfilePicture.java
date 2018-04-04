package seedu.address.model.user;

import java.awt.image.BufferedImage;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

/**
 * Profile picture of the user
 */
public class ProfilePicture {

    private Image profilePicture = new Image("file:docs/images/tohcheryl.png");

    public ProfilePicture() {}

    public ProfilePicture(BufferedImage image) {
        profilePicture = SwingFXUtils.toFXImage(image, null);
    }

    public Image getImage() {
        return this.profilePicture;
    }

    public BufferedImage toBufferedImage() {
        return SwingFXUtils.fromFXImage(profilePicture, null);
    }
}
