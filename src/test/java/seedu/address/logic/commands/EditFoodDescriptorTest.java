package seedu.address.logic.commands;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.DESC_APPLE;
import static seedu.address.logic.commands.CommandTestUtil.DESC_BANANA;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_BANANA;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_BANANA;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BANANA;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_BANANA;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_NUTS;

import org.junit.Test;

import seedu.address.logic.commands.EditCommand.EditFoodDescriptor;
import seedu.address.testutil.EditFoodDescriptorBuilder;

public class EditFoodDescriptorTest {

    @Test
    public void equals() {
        // same values -> returns true
        EditFoodDescriptor descriptorWithSameValues = new EditFoodDescriptor(DESC_APPLE);
        assertTrue(DESC_APPLE.equals(descriptorWithSameValues));

        // same object -> returns true
        assertTrue(DESC_APPLE.equals(DESC_APPLE));

        // null -> returns false
        assertFalse(DESC_APPLE.equals(null));

        // different types -> returns false
        assertFalse(DESC_APPLE.equals(5));

        // different values -> returns false
        assertFalse(DESC_APPLE.equals(DESC_BANANA));

        // different name -> returns false
        EditFoodDescriptor editedApple = new EditFoodDescriptorBuilder(DESC_APPLE).withName(VALID_NAME_BANANA).build();
        assertFalse(DESC_APPLE.equals(editedApple));

        // different phone -> returns false
        editedApple = new EditFoodDescriptorBuilder(DESC_APPLE).withPhone(VALID_PHONE_BANANA).build();
        assertFalse(DESC_APPLE.equals(editedApple));

        // different email -> returns false
        editedApple = new EditFoodDescriptorBuilder(DESC_APPLE).withEmail(VALID_EMAIL_BANANA).build();
        assertFalse(DESC_APPLE.equals(editedApple));

        // different address -> returns false
        editedApple = new EditFoodDescriptorBuilder(DESC_APPLE).withAddress(VALID_ADDRESS_BANANA).build();
        assertFalse(DESC_APPLE.equals(editedApple));

        // different tags -> returns false
        editedApple = new EditFoodDescriptorBuilder(DESC_APPLE).withTags(VALID_TAG_NUTS).build();
        assertFalse(DESC_APPLE.equals(editedApple));
    }
}
