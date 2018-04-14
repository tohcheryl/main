package seedu.address.ui;

import static seedu.address.ui.testutil.GuiTestAssert.assertPanelDisplaysUser;

import java.util.logging.Logger;

import org.junit.Assert;
import org.junit.Test;

import guitests.guihandles.UserProfilePanelHandle;
import javafx.scene.image.Image;
import seedu.address.commons.core.LogsCenter;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.user.UserProfile;
import seedu.address.model.util.SampleDataUtil;
import seedu.address.testutil.UserProfileBuilder;

//@@author tohcheryl
public class UserProfilePanelTest extends GuiUnitTest {

    private static final ReadOnlyAddressBook ADDRESS_BOOK = SampleDataUtil.getSampleAddressBook();
    private static final Logger logger = LogsCenter.getLogger(UserProfilePanelTest.class);

    @Test
    public void display() {
        // no allergies
        UserProfile userWithNoAllergy = new UserProfileBuilder().withAllergies(new String[0]).build();
        UserProfilePanel userProfilePanel = new UserProfilePanel(ADDRESS_BOOK);
        userProfilePanel.setUserProfile(userWithNoAllergy);
        uiPartRule.setUiPart(userProfilePanel);
        assertPanelDisplay(userProfilePanel, userWithNoAllergy);

        // with allergies
        UserProfile userWithAllergies = new UserProfileBuilder().build();
        userProfilePanel = new UserProfilePanel(ADDRESS_BOOK);
        userProfilePanel.setUserProfile(userWithAllergies);
        uiPartRule.setUiPart(userProfilePanel);
        assertPanelDisplay(userProfilePanel, userWithAllergies);
    }

    @Test
    public void getSquareImage() {
        Image testImage = new Image("file:docs/images/StorageClassDiagram.png");
        UserProfilePanel userProfilePanel = new UserProfilePanel(ADDRESS_BOOK);
        Image outputImage = userProfilePanel.getSquareImage(testImage);
        int width = (int) outputImage.getWidth();
        int height = (int) outputImage.getHeight();
        Assert.assertEquals(width, height);
    }

    /**
     * Asserts that {@code userProfilePanel} displays the details of {@code userProfile} correctly
     */
    private void assertPanelDisplay(UserProfilePanel userProfilePanel, UserProfile userProfile) {
        guiRobot.pauseForHuman();

        UserProfilePanelHandle userProfilePanelHandle = new UserProfilePanelHandle(userProfilePanel.getRoot());

        // verify user details are displayed correctly
        assertPanelDisplaysUser(userProfile, userProfilePanelHandle);
    }
}
