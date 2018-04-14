package systemtests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.address.ui.StatusBarFooter.SYNC_STATUS_INITIAL;
import static seedu.address.ui.StatusBarFooter.SYNC_STATUS_UPDATED;
import static seedu.address.ui.testutil.GuiTestAssert.assertListMatching;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;

import guitests.guihandles.ChatPanelHandle;
import guitests.guihandles.CommandBoxHandle;
import guitests.guihandles.FoodListPanelHandle;
import guitests.guihandles.MainMenuHandle;
import guitests.guihandles.MainWindowHandle;
import guitests.guihandles.StatusBarFooterHandle;
import seedu.address.TestApp;
import seedu.address.commons.core.EventsCenter;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.ClearCommand;
import seedu.address.logic.commands.FindCommand;
import seedu.address.logic.commands.ListCommand;
import seedu.address.logic.commands.SelectCommand;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.testutil.TypicalFoods;
import seedu.address.ui.CommandBox;

/**
 * A system test class for AddressBook, which provides access to handles of GUI components and helper methods
 * for test verification.
 */
public abstract class AddressBookSystemTest {
    @ClassRule
    public static ClockRule clockRule = new ClockRule();

    private static final List<String> COMMAND_BOX_DEFAULT_STYLE = Arrays.asList("text-input", "text-field");
    private static final List<String> COMMAND_BOX_ERROR_STYLE =
            Arrays.asList("text-input", "text-field", CommandBox.ERROR_STYLE_CLASS);

    private MainWindowHandle mainWindowHandle;
    private TestApp testApp;
    private SystemTestSetupHelper setupHelper;

    @BeforeClass
    public static void setupBeforeClass() {
        SystemTestSetupHelper.initialize();
    }

    @Before
    public void setUp() {
        setupHelper = new SystemTestSetupHelper();
        testApp = setupHelper.setupApplication(this::getInitialData, getDataFileLocation());
        mainWindowHandle = setupHelper.setupMainWindowHandle();

        assertApplicationStartingStateIsCorrect();
    }

    @After
    public void tearDown() throws Exception {
        setupHelper.tearDownStage();
        EventsCenter.clearSubscribers();
    }

    /**
     * Returns the data to be loaded into the file in {@link #getDataFileLocation()}.
     */
    protected AddressBook getInitialData() {
        return TypicalFoods.getTypicalAddressBook();
    }

    /**
     * Returns the directory of the data file.
     */
    protected String getDataFileLocation() {
        return TestApp.SAVE_LOCATION_FOR_TESTING;
    }

    public MainWindowHandle getMainWindowHandle() {
        return mainWindowHandle;
    }

    public CommandBoxHandle getCommandBox() {
        return mainWindowHandle.getCommandBox();
    }

    public FoodListPanelHandle getFoodListPanel() {
        return mainWindowHandle.getFoodListPanel();
    }

    public MainMenuHandle getMainMenu() {
        return mainWindowHandle.getMainMenu();
    }

    public StatusBarFooterHandle getStatusBarFooter() {
        return mainWindowHandle.getStatusBarFooter();
    }

    public ChatPanelHandle getChatPanel() {
        return mainWindowHandle.getChatPanelHandle();
    }

    /**
     * Executes {@code command} in the application's {@code CommandBox}.
     * Method returns after UI components have been updated.
     */
    protected void executeCommand(String command) {
        rememberStates();
        // Injects a fixed clock before executing a command so that the time stamp shown in the status bar
        // after each command is predictable and also different from the previous command.
        clockRule.setInjectedClockToCurrentTime();

        mainWindowHandle.getCommandBox().run(command);
    }

    /**
     * Displays all foods in HackEat.
     */
    protected void showAllFoods() {
        executeCommand(ListCommand.COMMAND_WORD);
        assertEquals(getModel().getAddressBook().getFoodList().size(), getModel().getFilteredFoodList().size());
    }

    /**
     * Displays all foods with any parts of their names matching {@code keyword} (case-insensitive).
     */
    protected void showFoodsWithName(String keyword) {
        executeCommand(FindCommand.COMMAND_WORD + " " + keyword);
        assertTrue(getModel().getFilteredFoodList().size() < getModel().getAddressBook().getFoodList().size());
    }

    /**
     * Selects the food at {@code index} of the displayed list.
     */
    protected void selectFood(Index index) {
        executeCommand(SelectCommand.COMMAND_WORD + " " + index.getOneBased());
        assertEquals(index.getZeroBased(), getFoodListPanel().getSelectedCardIndex());
    }

    /**
     * Deletes all foods in HackEat.
     */
    protected void deleteAllFoods() {
        executeCommand(ClearCommand.COMMAND_WORD);
        assertEquals(0, getModel().getAddressBook().getFoodList().size());
    }

    /**
     * Asserts that the {@code CommandBox} displays {@code expectedCommandInput}, the {@code ResultDisplay} displays
     * {@code expectedResultMessage}, the model and storage contains the same food objects as {@code expectedModel}
     * and the food list panel displays the foods in the model correctly.
     */
    protected void assertApplicationDisplaysExpected(String expectedCommandInput, String expectedResultMessage,
                                                     Model expectedModel) {
        assertEquals(expectedCommandInput, getCommandBox().getInput());
        assertEquals(expectedResultMessage, getChatPanel().getText());
        assertEquals(expectedModel, getModel());
        assertEquals(expectedModel.getAddressBook(), testApp.readStorageAddressBook());
        assertListMatching(getFoodListPanel(), expectedModel.getFilteredFoodList());
    }

    /**
     * Calls {@code FoodListPanelHandle} and {@code StatusBarFooterHandle} to remember
     * their current state.
     */
    private void rememberStates() {
        StatusBarFooterHandle statusBarFooterHandle = getStatusBarFooter();
        statusBarFooterHandle.rememberSaveLocation();
        statusBarFooterHandle.rememberSyncStatus();
        getFoodListPanel().rememberSelectedFoodCard();
    }

    /**
     * Asserts that the previously selected card is now deselected.
     */
    protected void assertSelectedCardDeselected() {
        assertFalse(getFoodListPanel().isAnyCardSelected());
    }

    /**
     * Asserts that only the card at {@code expectedSelectedCardIndex} is selected.
     *
     * @see FoodListPanelHandle#isSelectedFoodCardChanged()
     */
    protected void assertSelectedCardChanged(Index expectedSelectedCardIndex) {
        assertEquals(expectedSelectedCardIndex.getZeroBased(), getFoodListPanel().getSelectedCardIndex());
    }

    /**
     * Asserts that the selected card in the food list panel remain unchanged.
     *
     * @see FoodListPanelHandle#isSelectedFoodCardChanged()
     */
    protected void assertSelectedCardUnchanged() {
        assertFalse(getFoodListPanel().isSelectedFoodCardChanged());
    }

    /**
     * Asserts that the command box's shows the default style.
     */
    protected void assertCommandBoxShowsDefaultStyle() {
        assertEquals(COMMAND_BOX_DEFAULT_STYLE, getCommandBox().getStyleClass());
    }

    /**
     * Asserts that the command box's shows the error style.
     */
    protected void assertCommandBoxShowsErrorStyle() {
        assertEquals(COMMAND_BOX_ERROR_STYLE, getCommandBox().getStyleClass());
    }

    /**
     * Asserts that the entire status bar remains the same.
     */
    protected void assertStatusBarUnchanged() {
        StatusBarFooterHandle handle = getStatusBarFooter();
        assertFalse(handle.isSaveLocationChanged());
        assertFalse(handle.isSyncStatusChanged());
    }

    /**
     * Asserts that only the sync status in the status bar was changed to the timing of
     * {@code ClockRule#getInjectedClock()}, while the save location remains the same.
     */
    protected void assertStatusBarUnchangedExceptSyncStatus() {
        StatusBarFooterHandle handle = getStatusBarFooter();
        String timestamp = new Date(clockRule.getInjectedClock().millis()).toString();
        String expectedSyncStatus = String.format(SYNC_STATUS_UPDATED, timestamp);
        assertEquals(expectedSyncStatus, handle.getSyncStatus());
        assertFalse(handle.isSaveLocationChanged());
    }

    /**
     * Asserts that the starting state of the application is correct.
     */
    private void assertApplicationStartingStateIsCorrect() {
        try {
            assertEquals("", getCommandBox().getInput());
            assertEquals(null, getChatPanel().getText());
            assertListMatching(getFoodListPanel(), getModel().getFilteredFoodList());
            assertEquals("./" + testApp.getStorageSaveLocation(), getStatusBarFooter().getSaveLocation());
            assertEquals(SYNC_STATUS_INITIAL, getStatusBarFooter().getSyncStatus());
        } catch (Exception e) {
            throw new AssertionError("Starting state is wrong.", e);
        }
    }

    /**
     * Returns a defensive copy of the current model.
     */
    protected Model getModel() {
        return testApp.getModel();
    }
}
