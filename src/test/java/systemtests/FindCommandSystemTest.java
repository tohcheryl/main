package systemtests;

import static org.junit.Assert.assertFalse;
import static seedu.address.commons.core.Messages.MESSAGE_FOODS_LISTED_OVERVIEW;
import static seedu.address.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;
import static seedu.address.testutil.TypicalFoods.BACON;
import static seedu.address.testutil.TypicalFoods.CAKE;
import static seedu.address.testutil.TypicalFoods.DUMPLING;
import static seedu.address.testutil.TypicalFoods.KEYWORD_MATCHING_MOUSSE;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.DeleteCommand;
import seedu.address.logic.commands.FindCommand;
import seedu.address.logic.commands.RedoCommand;
import seedu.address.logic.commands.UndoCommand;
import seedu.address.model.Model;
import seedu.address.model.tag.Tag;

public class FindCommandSystemTest extends AddressBookSystemTest {

    @Test
    public void find() {
        /* Case: find multiple foods in address book, command with leading spaces and trailing spaces
         * -> 2 foods found
         */
        String command = "   " + FindCommand.COMMAND_WORD + " " + KEYWORD_MATCHING_MOUSSE + "   ";
        Model expectedModel = getModel();
        ModelHelper.setFilteredList(expectedModel, BACON, DUMPLING); // first names of Benson and Daniel are "Meier"
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: repeat previous find command where food list is displaying the foods we are finding
         * -> 2 foods found
         */
        command = FindCommand.COMMAND_WORD + " " + KEYWORD_MATCHING_MOUSSE;
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find food where food list is not displaying the food we are finding -> 1 food found */
        command = FindCommand.COMMAND_WORD + " Cake";
        ModelHelper.setFilteredList(expectedModel, CAKE);
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find multiple foods in address book, 2 keywords -> 2 foods found */
        command = FindCommand.COMMAND_WORD + " Bacon Dumpling";
        ModelHelper.setFilteredList(expectedModel, BACON, DUMPLING);
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find multiple foods in address book, 2 keywords in reversed order -> 2 foods found */
        command = FindCommand.COMMAND_WORD + " Dumpling Bacon";
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find multiple foods in address book, 2 keywords with 1 repeat -> 2 foods found */
        command = FindCommand.COMMAND_WORD + " Dumpling Bacon Dumpling";
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find multiple foods in address book, 2 matching keywords and 1 non-matching keyword
         * -> 2 foods found
         */
        command = FindCommand.COMMAND_WORD + " Dumpling Bacon NonMatchingKeyWord";
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: undo previous find command -> rejected */
        command = UndoCommand.COMMAND_WORD;
        String expectedResultMessage = UndoCommand.MESSAGE_FAILURE;
        assertCommandFailure(command, expectedResultMessage);

        /* Case: redo previous find command -> rejected */
        command = RedoCommand.COMMAND_WORD;
        expectedResultMessage = RedoCommand.MESSAGE_FAILURE;
        assertCommandFailure(command, expectedResultMessage);

        /* Case: find same foods in address book after deleting 1 of them -> 1 food found */
        executeCommand(DeleteCommand.COMMAND_WORD + " 1");
        assertFalse(getModel().getAddressBook().getFoodList().contains(BACON));
        command = FindCommand.COMMAND_WORD + " " + KEYWORD_MATCHING_MOUSSE;
        expectedModel = getModel();
        ModelHelper.setFilteredList(expectedModel, DUMPLING);
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find food in address book, keyword is same as name but of different case -> 1 food found */
        command = FindCommand.COMMAND_WORD + " MoUsSe";
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find food in address book, keyword is substring of name -> 0 foods found */
        command = FindCommand.COMMAND_WORD + " Mou";
        ModelHelper.setFilteredList(expectedModel);
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find food in address book, name is substring of keyword -> 0 foods found */
        command = FindCommand.COMMAND_WORD + " Mousses";
        ModelHelper.setFilteredList(expectedModel);
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find food not in address book -> 0 foods found */
        command = FindCommand.COMMAND_WORD + " Mouse";
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find phone number of food in address book -> 0 foods found */
        command = FindCommand.COMMAND_WORD + " " + DUMPLING.getPhone().value;
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find address of food in address book -> 0 foods found */
        command = FindCommand.COMMAND_WORD + " " + DUMPLING.getAddress().value;
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find email of food in address book -> 0 foods found */
        command = FindCommand.COMMAND_WORD + " " + DUMPLING.getEmail().value;
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find tags of food in address book -> 0 foods found */
        List<Tag> tags = new ArrayList<>(DUMPLING.getTags());
        command = FindCommand.COMMAND_WORD + " " + tags.get(0).tagName;
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find while a food is selected -> selected card deselected */
        showAllFoods();
        selectFood(Index.fromOneBased(1));
        assertFalse(getFoodListPanel().getHandleToSelectedCard().getName().equals(DUMPLING.getName().fullName));
        command = FindCommand.COMMAND_WORD + " Dumpling";
        ModelHelper.setFilteredList(expectedModel, DUMPLING);
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardDeselected();

        /* Case: find food in empty address book -> 0 foods found */
        deleteAllFoods();
        command = FindCommand.COMMAND_WORD + " " + KEYWORD_MATCHING_MOUSSE;
        expectedModel = getModel();
        ModelHelper.setFilteredList(expectedModel, DUMPLING);
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: mixed case command word -> rejected */
        command = "FiNd Mousse";
        assertCommandFailure(command, MESSAGE_UNKNOWN_COMMAND);
    }

    /**
     * Executes {@code command} and verifies that the command box displays an empty string, the result display
     * box displays {@code Messages#MESSAGE_FOODS_LISTED_OVERVIEW} with the number of people in the filtered list,
     * and the model related components equal to {@code expectedModel}.
     * These verifications are done by
     * {@code AddressBookSystemTest#assertApplicationDisplaysExpected(String, String, Model)}.<br>
     * Also verifies that the status bar remains unchanged, and the command box has the default style class, and the
     * selected card updated accordingly, depending on {@code cardStatus}.
     * @see AddressBookSystemTest#assertApplicationDisplaysExpected(String, String, Model)
     */
    private void assertCommandSuccess(String command, Model expectedModel) {
        String expectedResultMessage = String.format(
                MESSAGE_FOODS_LISTED_OVERVIEW, expectedModel.getFilteredFoodList().size());

        executeCommand(command);
        assertApplicationDisplaysExpected("", expectedResultMessage, expectedModel);
        assertCommandBoxShowsDefaultStyle();
        assertStatusBarUnchanged();
    }

    /**
     * Executes {@code command} and verifies that the command box displays {@code command}, the result display
     * box displays {@code expectedResultMessage} and the model related components equal to the current model.
     * These verifications are done by
     * {@code AddressBookSystemTest#assertApplicationDisplaysExpected(String, String, Model)}.<br>
     * Also verifies that the browser url, selected card and status bar remain unchanged, and the command box has the
     * error style.
     * @see AddressBookSystemTest#assertApplicationDisplaysExpected(String, String, Model)
     */
    private void assertCommandFailure(String command, String expectedResultMessage) {
        Model expectedModel = getModel();

        executeCommand(command);
        assertApplicationDisplaysExpected(command, expectedResultMessage, expectedModel);
        assertSelectedCardUnchanged();
        assertCommandBoxShowsErrorStyle();
        assertStatusBarUnchanged();
    }
}
