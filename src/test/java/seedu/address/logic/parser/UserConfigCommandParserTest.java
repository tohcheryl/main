package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.CommandTestUtil.ADDRESS_DESC_APPLE;
import static seedu.address.logic.commands.CommandTestUtil.ADDRESS_DESC_BANANA;
import static seedu.address.logic.commands.CommandTestUtil.ALLERGY_DESC_LACTOSE;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_ADDRESS_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_ALLERGY_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_NAME_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_PHONE_DESC;
import static seedu.address.logic.commands.CommandTestUtil.NAME_DESC_APPLE;
import static seedu.address.logic.commands.CommandTestUtil.NAME_DESC_BANANA;
import static seedu.address.logic.commands.CommandTestUtil.PHONE_DESC_APPLE;
import static seedu.address.logic.commands.CommandTestUtil.PHONE_DESC_BANANA;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_APPLE;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_BANANA;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_APPLE;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BANANA;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_APPLE;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_BANANA;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.model.util.SampleDataUtil.getAllergySet;

import org.junit.Test;

import seedu.address.logic.commands.UserConfigCommand;
import seedu.address.model.food.Address;
import seedu.address.model.food.Name;
import seedu.address.model.food.Phone;
import seedu.address.model.food.allergy.Allergy;
import seedu.address.model.user.UserProfile;

//@@author tohcheryl
public class UserConfigCommandParserTest {

    private UserConfigCommandParser parser = new UserConfigCommandParser();

    @Test
    public void parse_allFieldsPresent_success() {
        UserProfile expectedUserProfile = new UserProfile(new Name(VALID_NAME_APPLE), new Phone(VALID_PHONE_APPLE),
                new Address(VALID_ADDRESS_APPLE), getAllergySet("lactose"));

        assertParseSuccess(parser, NAME_DESC_APPLE + PHONE_DESC_APPLE + ADDRESS_DESC_APPLE + ALLERGY_DESC_LACTOSE,
                new UserConfigCommand(expectedUserProfile));
    }

    @Test
    public void parse_compulsoryFieldMissing_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, UserConfigCommand.MESSAGE_USAGE);

        // missing name prefix
        assertParseFailure(parser, VALID_NAME_BANANA + PHONE_DESC_BANANA + ADDRESS_DESC_BANANA,
                expectedMessage);

        // missing phone prefix
        assertParseFailure(parser, NAME_DESC_BANANA + VALID_PHONE_BANANA + ADDRESS_DESC_BANANA,
                expectedMessage);

        // missing address prefix
        assertParseFailure(parser, NAME_DESC_BANANA + PHONE_DESC_BANANA + VALID_ADDRESS_BANANA,
                expectedMessage);

        // all prefixes missing
        assertParseFailure(parser, VALID_NAME_BANANA + VALID_PHONE_BANANA + VALID_ADDRESS_BANANA,
                expectedMessage);
    }

    @Test
    public void parse_invalidValue_failure() {
        // invalid name
        assertParseFailure(parser, INVALID_NAME_DESC + PHONE_DESC_BANANA + ADDRESS_DESC_BANANA,
                Name.MESSAGE_NAME_CONSTRAINTS);

        // invalid phone
        assertParseFailure(parser, NAME_DESC_BANANA + INVALID_PHONE_DESC + ADDRESS_DESC_BANANA,
                Phone.MESSAGE_PHONE_CONSTRAINTS);

        // invalid address
        assertParseFailure(parser, NAME_DESC_BANANA + PHONE_DESC_BANANA + INVALID_ADDRESS_DESC,
                Address.MESSAGE_ADDRESS_CONSTRAINTS);

        // invalid allergies
        assertParseFailure(parser, NAME_DESC_BANANA + PHONE_DESC_BANANA + ADDRESS_DESC_BANANA
                + INVALID_ALLERGY_DESC, Allergy.MESSAGE_ALLERGY_CONSTRAINTS);
    }
}
