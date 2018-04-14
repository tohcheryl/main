package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.Test;

import seedu.address.logic.commands.OrderCommand;
import seedu.address.logic.parser.exceptions.ParseException;

//@@author samzx

public class OrderCommandParserTest {
    private static final String EMPTY_STRING = "";
    private static final String VALID_INDEX = "1";
    private static final String INVALID_INDEX = "-1";

    private OrderCommandParser parser = new OrderCommandParser();

    @Test
    public void parse_emptySting() throws ParseException {
        OrderCommand expectedCommand = parser.parse(EMPTY_STRING);
        assertParseSuccess(parser, EMPTY_STRING, expectedCommand);
    }

    @Test
    public void parse_validIndex() throws ParseException {
        OrderCommand expectedCommand = parser.parse(VALID_INDEX);
        assertParseSuccess(parser, VALID_INDEX, expectedCommand);
    }

    @Test
    public void parse_invalidIndex_failure() throws ParseException {
        assertParseFailure(parser, INVALID_INDEX, String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                OrderCommand.MESSAGE_USAGE));
    }
}
