package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.CommandTestUtil.ADDRESS_DESC_APPLE;
import static seedu.address.logic.commands.CommandTestUtil.ADDRESS_DESC_BANANA;
import static seedu.address.logic.commands.CommandTestUtil.ALLERGY_DESC_LACTOSE;
import static seedu.address.logic.commands.CommandTestUtil.ALLERGY_DESC_POLLEN;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_ADDRESS_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_ALLERGY_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_NAME_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_PHONE_DESC;
import static seedu.address.logic.commands.CommandTestUtil.NAME_DESC_APPLE;
import static seedu.address.logic.commands.CommandTestUtil.PHONE_DESC_APPLE;
import static seedu.address.logic.commands.CommandTestUtil.PHONE_DESC_BANANA;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_APPLE;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_BANANA;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ALLERGY_LACTOSE;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ALLERGY_POLLEN;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_APPLE;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_APPLE;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_BANANA;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ALLERGIES;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.Test;

import seedu.address.logic.commands.EditUserCommand;
import seedu.address.model.food.Address;
import seedu.address.model.food.Name;
import seedu.address.model.food.Phone;
import seedu.address.model.food.allergy.Allergy;
import seedu.address.testutil.EditUserDescriptorBuilder;

//@@author tohcheryl
public class EditUserCommandParserTest {

    private static final String ALLERGY_EMPTY = " " + PREFIX_ALLERGIES;

    private static final String MESSAGE_INVALID_FORMAT =
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditUserCommand.MESSAGE_USAGE);

    private EditUserCommandParser parser = new EditUserCommandParser();

    @Test
    public void parse_missingParts_failure() {
        // no field specified
        assertParseFailure(parser, "", EditUserCommand.MESSAGE_NOT_EDITED);
    }

    @Test
    public void parse_invalidValue_failure() {
        assertParseFailure(parser, INVALID_NAME_DESC, Name.MESSAGE_NAME_CONSTRAINTS); // invalid name
        assertParseFailure(parser, INVALID_PHONE_DESC, Phone.MESSAGE_PHONE_CONSTRAINTS); // invalid phone
        assertParseFailure(parser, INVALID_ADDRESS_DESC, Address.MESSAGE_ADDRESS_CONSTRAINTS); // invalid address
        assertParseFailure(parser, INVALID_ALLERGY_DESC, Allergy.MESSAGE_ALLERGY_CONSTRAINTS); // invalid allergy

        // invalid phone followed by valid address
        assertParseFailure(parser, INVALID_PHONE_DESC + ADDRESS_DESC_APPLE, Phone.MESSAGE_PHONE_CONSTRAINTS);

        // valid phone followed by invalid phone. The test case for invalid phone followed by valid phone
        // is tested at {@code parse_invalidValueFollowedByValidValue_success()}
        assertParseFailure(parser, PHONE_DESC_BANANA + INVALID_PHONE_DESC, Phone.MESSAGE_PHONE_CONSTRAINTS);

        // while parsing {@code PREFIX_ALLERGY} alone will reset the allergies of the {@code UserProfile} being edited,
        // parsing it together with a valid allergy results in error
        assertParseFailure(parser, ALLERGY_DESC_LACTOSE + ALLERGY_DESC_POLLEN + ALLERGY_EMPTY,
                Allergy.MESSAGE_ALLERGY_CONSTRAINTS);
        assertParseFailure(parser, ALLERGY_DESC_LACTOSE + ALLERGY_EMPTY + ALLERGY_DESC_POLLEN,
                Allergy.MESSAGE_ALLERGY_CONSTRAINTS);
        assertParseFailure(parser, ALLERGY_EMPTY + ALLERGY_DESC_LACTOSE + ALLERGY_DESC_POLLEN,
                Allergy.MESSAGE_ALLERGY_CONSTRAINTS);

        // multiple invalid values, but only the first invalid value is captured
        assertParseFailure(parser, INVALID_NAME_DESC + INVALID_PHONE_DESC + VALID_ADDRESS_APPLE,
                Name.MESSAGE_NAME_CONSTRAINTS);
    }

    @Test
    public void parse_allFieldsSpecified_success() {
        String userInput = PHONE_DESC_BANANA + ALLERGY_DESC_POLLEN + ADDRESS_DESC_APPLE + NAME_DESC_APPLE;

        EditUserCommand.EditUserDescriptor descriptor = new EditUserDescriptorBuilder().withName(VALID_NAME_APPLE)
                .withPhone(VALID_PHONE_BANANA).withAddress(VALID_ADDRESS_APPLE)
                .withAllergies(VALID_ALLERGY_POLLEN).build();
        EditUserCommand expectedCommand = new EditUserCommand(descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_someFieldsSpecified_success() {
        String userInput = PHONE_DESC_BANANA + NAME_DESC_APPLE;

        EditUserCommand.EditUserDescriptor descriptor = new EditUserDescriptorBuilder().withPhone(VALID_PHONE_BANANA)
                .withName(VALID_NAME_APPLE).build();
        EditUserCommand expectedCommand = new EditUserCommand(descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_oneFieldSpecified_success() {
        // name
        String userInput = NAME_DESC_APPLE;
        EditUserCommand.EditUserDescriptor descriptor = new EditUserDescriptorBuilder().withName(VALID_NAME_APPLE)
                .build();
        EditUserCommand expectedCommand = new EditUserCommand(descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // phone
        userInput = PHONE_DESC_APPLE;
        descriptor = new EditUserDescriptorBuilder().withPhone(VALID_PHONE_APPLE).build();
        expectedCommand = new EditUserCommand(descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // address
        userInput = ADDRESS_DESC_APPLE;
        descriptor = new EditUserDescriptorBuilder().withAddress(VALID_ADDRESS_APPLE).build();
        expectedCommand = new EditUserCommand(descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // allergies
        userInput = ALLERGY_DESC_LACTOSE;
        descriptor = new EditUserDescriptorBuilder().withAllergies(VALID_ALLERGY_LACTOSE).build();
        expectedCommand = new EditUserCommand(descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_multipleRepeatedFields_acceptsLast() {
        String userInput = PHONE_DESC_APPLE + ADDRESS_DESC_APPLE + ALLERGY_DESC_POLLEN + PHONE_DESC_APPLE
                + ADDRESS_DESC_BANANA + ALLERGY_DESC_LACTOSE + PHONE_DESC_BANANA;

        EditUserCommand.EditUserDescriptor descriptor = new EditUserDescriptorBuilder().withPhone(VALID_PHONE_BANANA)
                .withAddress(VALID_ADDRESS_BANANA).withAllergies(VALID_ALLERGY_POLLEN, VALID_ALLERGY_LACTOSE).build();
        EditUserCommand expectedCommand = new EditUserCommand(descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_invalidValueFollowedByValidValue_success() {
        // no other valid values specified
        String userInput = INVALID_PHONE_DESC + PHONE_DESC_BANANA;
        EditUserCommand.EditUserDescriptor descriptor = new EditUserDescriptorBuilder().withPhone(VALID_PHONE_BANANA)
                .build();
        EditUserCommand expectedCommand = new EditUserCommand(descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // other valid values specified
        userInput = INVALID_PHONE_DESC + ADDRESS_DESC_BANANA + PHONE_DESC_BANANA;
        descriptor = new EditUserDescriptorBuilder().withPhone(VALID_PHONE_BANANA).withAddress(VALID_ADDRESS_BANANA)
                .build();
        expectedCommand = new EditUserCommand(descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_resetAllergies_success() {
        String userInput = ALLERGY_EMPTY;

        EditUserCommand.EditUserDescriptor descriptor = new EditUserDescriptorBuilder().withAllergies().build();
        EditUserCommand expectedCommand = new EditUserCommand(descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }
}
