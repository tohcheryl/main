package seedu.address.logic.commands;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.DESC_AARON;
import static seedu.address.logic.commands.CommandTestUtil.DESC_BERNICE;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_BERNICE;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ALLERGY_POLLEN;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BERNICE;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_BERNICE;

import org.junit.Test;

import seedu.address.testutil.EditUserDescriptorBuilder;

public class EditUserDescriptorTest {

    @Test
    public void equals() {
        // same values -> returns true
        EditUserCommand.EditUserDescriptor descriptorWithSameValues =
                new EditUserCommand.EditUserDescriptor(DESC_AARON);
        assertTrue(DESC_AARON.equals(descriptorWithSameValues));

        // same object -> returns true
        assertTrue(DESC_AARON.equals(DESC_AARON));

        // null -> returns false
        assertFalse(DESC_AARON.equals(null));

        // different types -> returns false
        assertFalse(DESC_AARON.equals(5));

        // different values -> returns false
        assertFalse(DESC_AARON.equals(DESC_BERNICE));

        // different name -> returns false
        EditUserCommand.EditUserDescriptor editedAaron = new EditUserDescriptorBuilder(DESC_AARON)
                .withName(VALID_NAME_BERNICE).build();
        assertFalse(DESC_AARON.equals(editedAaron));

        // different phone -> returns false
        editedAaron = new EditUserDescriptorBuilder(DESC_AARON).withPhone(VALID_PHONE_BERNICE).build();
        assertFalse(DESC_AARON.equals(editedAaron));

        // different address -> returns false
        editedAaron = new EditUserDescriptorBuilder(DESC_AARON).withAddress(VALID_ADDRESS_BERNICE).build();
        assertFalse(DESC_AARON.equals(editedAaron));

        // different allergies -> returns false
        editedAaron = new EditUserDescriptorBuilder(DESC_AARON).withAllergies(VALID_ALLERGY_POLLEN).build();
        assertFalse(DESC_AARON.equals(editedAaron));
    }
}
