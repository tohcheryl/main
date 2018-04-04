package seedu.address.model.user;

/**
 * Represents file path to profile picture of the user
 */
public class ProfilePicturePath {

    public final String filePath;

    public ProfilePicturePath() {
        filePath = "docs/images/tohcheryl.png";
    }

    public ProfilePicturePath(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public String toString() {
        return filePath;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof ProfilePicturePath // instanceof handles nulls
                && this.filePath.equals(((ProfilePicturePath) other).filePath)); // state check
    }

    @Override
    public int hashCode() {
        return filePath.hashCode();
    }
}
