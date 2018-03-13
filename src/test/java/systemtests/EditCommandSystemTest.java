package systemtests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.ADDRESS_DESC_APPLE;
import static seedu.address.logic.commands.CommandTestUtil.ADDRESS_DESC_BANANA;
import static seedu.address.logic.commands.CommandTestUtil.EMAIL_DESC_APPLE;
import static seedu.address.logic.commands.CommandTestUtil.EMAIL_DESC_BANANA;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_ADDRESS_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_EMAIL_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_NAME_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_PHONE_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_TAG_DESC;
import static seedu.address.logic.commands.CommandTestUtil.NAME_DESC_APPLE;
import static seedu.address.logic.commands.CommandTestUtil.NAME_DESC_BANANA;
import static seedu.address.logic.commands.CommandTestUtil.PHONE_DESC_APPLE;
import static seedu.address.logic.commands.CommandTestUtil.PHONE_DESC_BANANA;
import static seedu.address.logic.commands.CommandTestUtil.TAG_DESC_FRIED;
import static seedu.address.logic.commands.CommandTestUtil.TAG_DESC_NUTS;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_BANANA;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_BANANA;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BANANA;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_BANANA;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_FRIED;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_NUTS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_FOODS;
import static seedu.address.testutil.TypicalFoods.APPLE;
import static seedu.address.testutil.TypicalFoods.BANANA;
import static seedu.address.testutil.TypicalFoods.KEYWORD_MATCHING_MOUSSE;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_FOOD;

import org.junit.Test;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.EditCommand;
import seedu.address.logic.commands.RedoCommand;
import seedu.address.logic.commands.UndoCommand;
import seedu.address.model.Model;
import seedu.address.model.food.Address;
import seedu.address.model.food.Email;
import seedu.address.model.food.Food;
import seedu.address.model.food.Name;
import seedu.address.model.food.Phone;
import seedu.address.model.food.exceptions.DuplicateFoodException;
import seedu.address.model.food.exceptions.FoodNotFoundException;
import seedu.address.model.tag.Tag;
import seedu.address.testutil.FoodBuilder;
import seedu.address.testutil.FoodUtil;

public class EditCommandSystemTest extends AddressBookSystemTest {

    @Test
    public void edit() throws Exception {
        Model model = getModel();

        /* ----------------- Performing edit operation while an unfiltered list is being shown ---------------------- */

        /* Case: edit all fields, command with leading spaces, trailing spaces and multiple spaces between each field
         * -> edited
         */
        Index index = INDEX_FIRST_FOOD;
        String command = " " + EditCommand.COMMAND_WORD + "  " + index.getOneBased() + "  " + NAME_DESC_BANANA + "  "
                + PHONE_DESC_BANANA + " " + EMAIL_DESC_BANANA + "  " + ADDRESS_DESC_BANANA + " " + TAG_DESC_NUTS + " ";
        Food editedFood = new FoodBuilder().withName(VALID_NAME_BANANA).withPhone(VALID_PHONE_BANANA)
                .withEmail(VALID_EMAIL_BANANA).withAddress(VALID_ADDRESS_BANANA).withTags(VALID_TAG_NUTS).build();
        assertCommandSuccess(command, index, editedFood);

        /* Case: undo editing the last food in the list -> last food restored */
        command = UndoCommand.COMMAND_WORD;
        String expectedResultMessage = UndoCommand.MESSAGE_SUCCESS;
        assertCommandSuccess(command, model, expectedResultMessage);

        /* Case: redo editing the last food in the list -> last food edited again */
        command = RedoCommand.COMMAND_WORD;
        expectedResultMessage = RedoCommand.MESSAGE_SUCCESS;
        model.updateFood(
                getModel().getFilteredFoodList().get(INDEX_FIRST_FOOD.getZeroBased()), editedFood);
        assertCommandSuccess(command, model, expectedResultMessage);

        /* Case: edit a food with new values same as existing values -> edited */
        command = EditCommand.COMMAND_WORD + " " + index.getOneBased() + NAME_DESC_BANANA + PHONE_DESC_BANANA
                + EMAIL_DESC_BANANA + ADDRESS_DESC_BANANA + TAG_DESC_FRIED + TAG_DESC_NUTS;
        assertCommandSuccess(command, index, BANANA);

        /* Case: edit some fields -> edited */
        index = INDEX_FIRST_FOOD;
        command = EditCommand.COMMAND_WORD + " " + index.getOneBased() + TAG_DESC_FRIED;
        Food foodToEdit = getModel().getFilteredFoodList().get(index.getZeroBased());
        editedFood = new FoodBuilder(foodToEdit).withTags(VALID_TAG_FRIED).build();
        assertCommandSuccess(command, index, editedFood);

        /* Case: clear tags -> cleared */
        index = INDEX_FIRST_FOOD;
        command = EditCommand.COMMAND_WORD + " " + index.getOneBased() + " " + PREFIX_TAG.getPrefix();
        editedFood = new FoodBuilder(foodToEdit).withTags().build();
        assertCommandSuccess(command, index, editedFood);

        /* ------------------ Performing edit operation while a filtered list is being shown ------------------------ */

        /* Case: filtered food list, edit index within bounds of address book and food list -> edited */
        showFoodsWithName(KEYWORD_MATCHING_MOUSSE);
        index = INDEX_FIRST_FOOD;
        assertTrue(index.getZeroBased() < getModel().getFilteredFoodList().size());
        command = EditCommand.COMMAND_WORD + " " + index.getOneBased() + " " + NAME_DESC_BANANA;
        foodToEdit = getModel().getFilteredFoodList().get(index.getZeroBased());
        editedFood = new FoodBuilder(foodToEdit).withName(VALID_NAME_BANANA).build();
        assertCommandSuccess(command, index, editedFood);

        /* Case: filtered food list, edit index within bounds of address book but out of bounds of food list
         * -> rejected
         */
        showFoodsWithName(KEYWORD_MATCHING_MOUSSE);
        int invalidIndex = getModel().getAddressBook().getFoodList().size();
        assertCommandFailure(EditCommand.COMMAND_WORD + " " + invalidIndex + NAME_DESC_BANANA,
                Messages.MESSAGE_INVALID_FOOD_DISPLAYED_INDEX);

        /* --------------------- Performing edit operation while a food card is selected -------------------------- */

        /* Case: selects first card in the food list, edit a food -> edited, card selection remains unchanged but
         * browser url changes
         */
        showAllFoods();
        index = INDEX_FIRST_FOOD;
        selectFood(index);
        command = EditCommand.COMMAND_WORD + " " + index.getOneBased() + NAME_DESC_APPLE + PHONE_DESC_APPLE
                + EMAIL_DESC_APPLE + ADDRESS_DESC_APPLE + TAG_DESC_FRIED;
        // this can be misleading: card selection actually remains unchanged but the
        // browser's url is updated to reflect the new food's name
        assertCommandSuccess(command, index, APPLE, index);

        /* --------------------------------- Performing invalid edit operation -------------------------------------- */

        /* Case: invalid index (0) -> rejected */
        assertCommandFailure(EditCommand.COMMAND_WORD + " 0" + NAME_DESC_BANANA,
                String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE));

        /* Case: invalid index (-1) -> rejected */
        assertCommandFailure(EditCommand.COMMAND_WORD + " -1" + NAME_DESC_BANANA,
                String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE));

        /* Case: invalid index (size + 1) -> rejected */
        invalidIndex = getModel().getFilteredFoodList().size() + 1;
        assertCommandFailure(EditCommand.COMMAND_WORD + " " + invalidIndex + NAME_DESC_BANANA,
                Messages.MESSAGE_INVALID_FOOD_DISPLAYED_INDEX);

        /* Case: missing index -> rejected */
        assertCommandFailure(EditCommand.COMMAND_WORD + NAME_DESC_BANANA,
                String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE));

        /* Case: missing all fields -> rejected */
        assertCommandFailure(EditCommand.COMMAND_WORD + " " + INDEX_FIRST_FOOD.getOneBased(),
                EditCommand.MESSAGE_NOT_EDITED);

        /* Case: invalid name -> rejected */
        assertCommandFailure(EditCommand.COMMAND_WORD + " " + INDEX_FIRST_FOOD.getOneBased() + INVALID_NAME_DESC,
                Name.MESSAGE_NAME_CONSTRAINTS);

        /* Case: invalid phone -> rejected */
        assertCommandFailure(EditCommand.COMMAND_WORD + " " + INDEX_FIRST_FOOD.getOneBased() + INVALID_PHONE_DESC,
                Phone.MESSAGE_PHONE_CONSTRAINTS);

        /* Case: invalid email -> rejected */
        assertCommandFailure(EditCommand.COMMAND_WORD + " " + INDEX_FIRST_FOOD.getOneBased() + INVALID_EMAIL_DESC,
                Email.MESSAGE_EMAIL_CONSTRAINTS);

        /* Case: invalid address -> rejected */
        assertCommandFailure(EditCommand.COMMAND_WORD + " " + INDEX_FIRST_FOOD.getOneBased() + INVALID_ADDRESS_DESC,
                Address.MESSAGE_ADDRESS_CONSTRAINTS);

        /* Case: invalid tag -> rejected */
        assertCommandFailure(EditCommand.COMMAND_WORD + " " + INDEX_FIRST_FOOD.getOneBased() + INVALID_TAG_DESC,
                Tag.MESSAGE_TAG_CONSTRAINTS);

        /* Case: edit a food with new values same as another food's values -> rejected */
        executeCommand(FoodUtil.getAddCommand(BANANA));
        assertTrue(getModel().getAddressBook().getFoodList().contains(BANANA));
        index = INDEX_FIRST_FOOD;
        assertFalse(getModel().getFilteredFoodList().get(index.getZeroBased()).equals(BANANA));
        command = EditCommand.COMMAND_WORD + " " + index.getOneBased() + NAME_DESC_BANANA + PHONE_DESC_BANANA
                + EMAIL_DESC_BANANA + ADDRESS_DESC_BANANA + TAG_DESC_FRIED + TAG_DESC_NUTS;
        assertCommandFailure(command, EditCommand.MESSAGE_DUPLICATE_FOOD);

        /* Case: edit a food with new values same as another food's values but with different tags -> rejected */
        command = EditCommand.COMMAND_WORD + " " + index.getOneBased() + NAME_DESC_BANANA + PHONE_DESC_BANANA
                + EMAIL_DESC_BANANA + ADDRESS_DESC_BANANA + TAG_DESC_NUTS;
        assertCommandFailure(command, EditCommand.MESSAGE_DUPLICATE_FOOD);
    }

    /**
     * Performs the same verification as {@code assertCommandSuccess(String, Index, Food, Index)} except that
     * the browser url and selected card remain unchanged.
     *
     * @param toEdit the index of the current model's filtered list
     * @see EditCommandSystemTest#assertCommandSuccess(String, Index, Food, Index)
     */
    private void assertCommandSuccess(String command, Index toEdit, Food editedFood) {
        assertCommandSuccess(command, toEdit, editedFood, null);
    }

    /**
     * Performs the same verification as {@code assertCommandSuccess(String, Model, String, Index)} and in addition,<br>
     * 1. Asserts that result display box displays the success message of executing {@code EditCommand}.<br>
     * 2. Asserts that the model related components are updated to reflect the food at index {@code toEdit} being
     * updated to values specified {@code editedFood}.<br>
     *
     * @param toEdit the index of the current model's filtered list.
     * @see EditCommandSystemTest#assertCommandSuccess(String, Model, String, Index)
     */
    private void assertCommandSuccess(String command, Index toEdit, Food editedFood,
                                      Index expectedSelectedCardIndex) {
        Model expectedModel = getModel();
        try {
            expectedModel.updateFood(
                    expectedModel.getFilteredFoodList().get(toEdit.getZeroBased()), editedFood);
            expectedModel.updateFilteredFoodList(PREDICATE_SHOW_ALL_FOODS);
        } catch (DuplicateFoodException | FoodNotFoundException e) {
            throw new IllegalArgumentException(
                    "editedFood is a duplicate in expectedModel, or it isn't found in the model.");
        }

        assertCommandSuccess(command, expectedModel,
                String.format(EditCommand.MESSAGE_EDIT_FOOD_SUCCESS, editedFood), expectedSelectedCardIndex);
    }

    /**
     * Performs the same verification as {@code assertCommandSuccess(String, Model, String, Index)} except that the
     * browser url and selected card remain unchanged.
     *
     * @see EditCommandSystemTest#assertCommandSuccess(String, Model, String, Index)
     */
    private void assertCommandSuccess(String command, Model expectedModel, String expectedResultMessage) {
        assertCommandSuccess(command, expectedModel, expectedResultMessage, null);
    }

    /**
     * Executes {@code command} and in addition,<br>
     * 1. Asserts that the command box displays an empty string.<br>
     * 2. Asserts that the result display box displays {@code expectedResultMessage}.<br>
     * 3. Asserts that the model related components equal to {@code expectedModel}.<br>
     * 4. Asserts that the browser url and selected card update accordingly depending on the card at
     * {@code expectedSelectedCardIndex}.<br>
     * 5. Asserts that the status bar's sync status changes.<br>
     * 6. Asserts that the command box has the default style class.<br>
     * Verifications 1 to 3 are performed by
     * {@code AddressBookSystemTest#assertApplicationDisplaysExpected(String, String, Model)}.<br>
     *
     * @see AddressBookSystemTest#assertApplicationDisplaysExpected(String, String, Model)
     * @see AddressBookSystemTest#assertSelectedCardChanged(Index)
     */
    private void assertCommandSuccess(String command, Model expectedModel, String expectedResultMessage,
                                      Index expectedSelectedCardIndex) {
        executeCommand(command);
        expectedModel.updateFilteredFoodList(PREDICATE_SHOW_ALL_FOODS);
        assertApplicationDisplaysExpected("", expectedResultMessage, expectedModel);
        assertCommandBoxShowsDefaultStyle();
        if (expectedSelectedCardIndex != null) {
            assertSelectedCardChanged(expectedSelectedCardIndex);
        } else {
            assertSelectedCardUnchanged();
        }
        assertStatusBarUnchangedExceptSyncStatus();
    }

    /**
     * Executes {@code command} and in addition,<br>
     * 1. Asserts that the command box displays {@code command}.<br>
     * 2. Asserts that result display box displays {@code expectedResultMessage}.<br>
     * 3. Asserts that the model related components equal to the current model.<br>
     * 4. Asserts that the browser url, selected card and status bar remain unchanged.<br>
     * 5. Asserts that the command box has the error style.<br>
     * Verifications 1 to 3 are performed by
     * {@code AddressBookSystemTest#assertApplicationDisplaysExpected(String, String, Model)}.<br>
     *
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
