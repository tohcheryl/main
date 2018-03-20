package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.CommandTestUtil.ADDRESS_DESC_APPLE;
import static seedu.address.logic.commands.CommandTestUtil.ADDRESS_DESC_BANANA;
import static seedu.address.logic.commands.CommandTestUtil.EMAIL_DESC_APPLE;
import static seedu.address.logic.commands.CommandTestUtil.EMAIL_DESC_BANANA;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_ADDRESS_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_EMAIL_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_NAME_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_PHONE_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_PRICE_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_RATING_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_TAG_DESC;
import static seedu.address.logic.commands.CommandTestUtil.NAME_DESC_APPLE;
import static seedu.address.logic.commands.CommandTestUtil.NAME_DESC_BANANA;
import static seedu.address.logic.commands.CommandTestUtil.PHONE_DESC_APPLE;
import static seedu.address.logic.commands.CommandTestUtil.PHONE_DESC_BANANA;
import static seedu.address.logic.commands.CommandTestUtil.PREAMBLE_NON_EMPTY;
import static seedu.address.logic.commands.CommandTestUtil.PREAMBLE_WHITESPACE;
import static seedu.address.logic.commands.CommandTestUtil.PRICE_DESC_APPLE;
import static seedu.address.logic.commands.CommandTestUtil.PRICE_DESC_BANANA;
import static seedu.address.logic.commands.CommandTestUtil.RATING_DESC_APPLE;
import static seedu.address.logic.commands.CommandTestUtil.RATING_DESC_BANANA;
import static seedu.address.logic.commands.CommandTestUtil.TAG_DESC_FRIED;
import static seedu.address.logic.commands.CommandTestUtil.TAG_DESC_NUTS;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_APPLE;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_BANANA;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_APPLE;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_BANANA;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_APPLE;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BANANA;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_APPLE;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_BANANA;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PRICE_APPLE;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PRICE_BANANA;
import static seedu.address.logic.commands.CommandTestUtil.VALID_RATING_APPLE;
import static seedu.address.logic.commands.CommandTestUtil.VALID_RATING_BANANA;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_FRIED;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_NUTS;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.Test;

import seedu.address.logic.commands.AddCommand;
import seedu.address.model.food.*;
import seedu.address.model.tag.Tag;
import seedu.address.testutil.FoodBuilder;

public class AddCommandParserTest {
    private AddCommandParser parser = new AddCommandParser();

    @Test
    public void parse_allFieldsPresent_success() {
        Food expectedFood = new FoodBuilder().withName(VALID_NAME_BANANA).withPhone(VALID_PHONE_BANANA)
                .withEmail(VALID_EMAIL_BANANA).withAddress(VALID_ADDRESS_BANANA)
                .withPrice(VALID_PRICE_BANANA).withRating(VALID_RATING_BANANA).withTags(VALID_TAG_FRIED).build();

        // whitespace only preamble
        assertParseSuccess(parser, PREAMBLE_WHITESPACE + NAME_DESC_BANANA + PHONE_DESC_BANANA
                        + EMAIL_DESC_BANANA + ADDRESS_DESC_BANANA + PRICE_DESC_BANANA + RATING_DESC_BANANA
                        + TAG_DESC_FRIED, new AddCommand(expectedFood));

        // multiple names - last name accepted
        assertParseSuccess(parser, NAME_DESC_APPLE + NAME_DESC_BANANA + PHONE_DESC_BANANA + EMAIL_DESC_BANANA
                + ADDRESS_DESC_BANANA + PRICE_DESC_BANANA + RATING_DESC_BANANA + TAG_DESC_FRIED,
                new AddCommand(expectedFood));

        // multiple phones - last phone accepted
        assertParseSuccess(parser, NAME_DESC_BANANA + PHONE_DESC_APPLE + PHONE_DESC_BANANA + EMAIL_DESC_BANANA
                + ADDRESS_DESC_BANANA + PRICE_DESC_BANANA + RATING_DESC_BANANA + TAG_DESC_FRIED,
                new AddCommand(expectedFood));

        // multiple emails - last email accepted
        assertParseSuccess(parser, NAME_DESC_BANANA + PHONE_DESC_BANANA + EMAIL_DESC_APPLE + EMAIL_DESC_BANANA
                + ADDRESS_DESC_BANANA + PRICE_DESC_BANANA + RATING_DESC_BANANA + TAG_DESC_FRIED,
                new AddCommand(expectedFood));

        // multiple addresses - last address accepted
        assertParseSuccess(parser, NAME_DESC_BANANA + PHONE_DESC_BANANA + EMAIL_DESC_BANANA
                        + ADDRESS_DESC_APPLE + ADDRESS_DESC_BANANA + PRICE_DESC_BANANA + RATING_DESC_BANANA
                        + TAG_DESC_FRIED, new AddCommand(expectedFood));

        // multiple tags - all accepted
        Food expectedFoodMultipleTags = new FoodBuilder().withName(VALID_NAME_BANANA).withPhone(VALID_PHONE_BANANA)
                .withEmail(VALID_EMAIL_BANANA).withAddress(VALID_ADDRESS_BANANA).withPrice(VALID_PRICE_BANANA)
                .withRating(VALID_RATING_BANANA).withTags(VALID_TAG_FRIED, VALID_TAG_NUTS).build();
        assertParseSuccess(parser, NAME_DESC_BANANA + PHONE_DESC_BANANA + EMAIL_DESC_BANANA
                + ADDRESS_DESC_BANANA + PRICE_DESC_BANANA + RATING_DESC_BANANA + TAG_DESC_NUTS + TAG_DESC_FRIED,
                new AddCommand(expectedFoodMultipleTags));
    }

    @Test
    public void parse_optionalFieldsMissing_success() {
        // zero tags
        Food expectedFood = new FoodBuilder().withName(VALID_NAME_APPLE).withPhone(VALID_PHONE_APPLE)
                .withEmail(VALID_EMAIL_APPLE).withAddress(VALID_ADDRESS_APPLE).withPrice(VALID_PRICE_APPLE)
                .withRating(VALID_RATING_APPLE).withTags().build();
        assertParseSuccess(parser, NAME_DESC_APPLE + PHONE_DESC_APPLE + EMAIL_DESC_APPLE
                        + ADDRESS_DESC_APPLE + PRICE_DESC_APPLE + RATING_DESC_APPLE,
                new AddCommand(expectedFood));

        // missing email prefix
        Food expectedFoodNoEmail = new FoodBuilder().withName(VALID_NAME_APPLE).withPhone(VALID_PHONE_APPLE)
                .withAddress(VALID_ADDRESS_APPLE).withPrice(VALID_PRICE_APPLE).withTags().build();
        assertParseSuccess(parser, NAME_DESC_APPLE + PHONE_DESC_APPLE + ADDRESS_DESC_APPLE + PRICE_DESC_APPLE,
                new AddCommand(expectedFoodNoEmail));

        // missing address prefix
        Food expectedFoodNoAddress = new FoodBuilder().withName(VALID_NAME_APPLE).withPhone(VALID_PHONE_APPLE)
                .withEmail(VALID_EMAIL_APPLE).withPrice(VALID_PRICE_APPLE).withTags().build();
        assertParseSuccess(parser, NAME_DESC_APPLE + PHONE_DESC_APPLE + EMAIL_DESC_APPLE + PRICE_DESC_APPLE,
                new AddCommand(expectedFoodNoAddress));


        // missing rating
        Food expectedFoodNoRating = new FoodBuilder().withName(VALID_NAME_APPLE).withPhone(VALID_PHONE_APPLE)
                .withAddress(VALID_ADDRESS_APPLE).withEmail(VALID_EMAIL_APPLE).withPrice(VALID_PRICE_APPLE)
                .withTags().build();
        assertParseSuccess(parser, NAME_DESC_APPLE + PHONE_DESC_APPLE + EMAIL_DESC_APPLE + ADDRESS_DESC_APPLE
                        + PRICE_DESC_APPLE, new AddCommand(expectedFoodNoRating));
    }

    @Test
    public void parse_compulsoryFieldMissing_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE);

        // missing name prefix
        assertParseFailure(parser, VALID_NAME_BANANA + PHONE_DESC_BANANA + EMAIL_DESC_BANANA + ADDRESS_DESC_BANANA,
                expectedMessage);

        // missing phone prefix
        assertParseFailure(parser, NAME_DESC_BANANA + VALID_PHONE_BANANA + EMAIL_DESC_BANANA + ADDRESS_DESC_BANANA,
                expectedMessage);

        // all prefixes missing
        assertParseFailure(parser, VALID_NAME_BANANA + VALID_PHONE_BANANA + VALID_EMAIL_BANANA + VALID_ADDRESS_BANANA,
                expectedMessage);
    }

    @Test
    public void parse_invalidValue_failure() {
        // invalid name
        assertParseFailure(parser, INVALID_NAME_DESC + PHONE_DESC_BANANA + EMAIL_DESC_BANANA
                        + ADDRESS_DESC_BANANA + PRICE_DESC_BANANA + RATING_DESC_BANANA + TAG_DESC_NUTS
                        + TAG_DESC_FRIED, Name.MESSAGE_NAME_CONSTRAINTS);

        // invalid phone
        assertParseFailure(parser, NAME_DESC_BANANA + INVALID_PHONE_DESC + EMAIL_DESC_BANANA
                        + ADDRESS_DESC_BANANA + PRICE_DESC_BANANA + RATING_DESC_BANANA + TAG_DESC_NUTS
                + TAG_DESC_FRIED, Phone.MESSAGE_PHONE_CONSTRAINTS);

        // invalid email
        assertParseFailure(parser, NAME_DESC_BANANA + PHONE_DESC_BANANA + INVALID_EMAIL_DESC
                        + ADDRESS_DESC_BANANA + PRICE_DESC_BANANA + RATING_DESC_BANANA + TAG_DESC_NUTS
                + TAG_DESC_FRIED, Email.MESSAGE_EMAIL_CONSTRAINTS);

        // invalid address
        assertParseFailure(parser, NAME_DESC_BANANA + PHONE_DESC_BANANA + EMAIL_DESC_BANANA
                        + INVALID_ADDRESS_DESC + PRICE_DESC_BANANA + RATING_DESC_BANANA + TAG_DESC_NUTS
                + TAG_DESC_FRIED, Address.MESSAGE_ADDRESS_CONSTRAINTS);

        // invalid price
        assertParseFailure(parser, NAME_DESC_BANANA + PHONE_DESC_BANANA + EMAIL_DESC_BANANA
                        + ADDRESS_DESC_BANANA + INVALID_PRICE_DESC + RATING_DESC_BANANA + TAG_DESC_NUTS
                + TAG_DESC_FRIED, Price.MESSAGE_PRICE_CONSTRAINTS);

        // invalid rating
        assertParseFailure(parser, NAME_DESC_BANANA + PHONE_DESC_BANANA + EMAIL_DESC_BANANA
                        + ADDRESS_DESC_BANANA + VALID_PRICE_BANANA + INVALID_RATING_DESC + TAG_DESC_NUTS
                        + TAG_DESC_FRIED, Rating.MESSAGE_RATING_CONSTRAINTS);

        // invalid tag
        assertParseFailure(parser, NAME_DESC_BANANA + PHONE_DESC_BANANA + EMAIL_DESC_BANANA
                        + ADDRESS_DESC_BANANA + PRICE_DESC_BANANA + RATING_DESC_BANANA + INVALID_TAG_DESC
                        + VALID_TAG_FRIED, Tag.MESSAGE_TAG_CONSTRAINTS);

        // two invalid values, only first invalid value reported
        assertParseFailure(parser, INVALID_NAME_DESC + PHONE_DESC_BANANA + EMAIL_DESC_BANANA
                        + INVALID_ADDRESS_DESC + PRICE_DESC_BANANA + RATING_DESC_BANANA, Name.MESSAGE_NAME_CONSTRAINTS);

        // non-empty preamble
        assertParseFailure(parser, PREAMBLE_NON_EMPTY + NAME_DESC_BANANA + PHONE_DESC_BANANA
                        + EMAIL_DESC_BANANA + ADDRESS_DESC_BANANA + PRICE_DESC_BANANA + RATING_DESC_BANANA
                        + TAG_DESC_NUTS + TAG_DESC_FRIED,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));
    }
}
