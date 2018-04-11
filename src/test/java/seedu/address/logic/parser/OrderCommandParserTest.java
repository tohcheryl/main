package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.Test;

import seedu.address.logic.commands.OrderCommand;
import seedu.address.logic.parser.exceptions.ParseException;

//@@author {samzx}

public class OrderCommandParserTest {
    private OrderCommandParser parser = new OrderCommandParser();

    @Test
    public void parse_emptySting() throws ParseException {
        OrderCommand expectedCommand = parser.parse("");
        assertParseSuccess(parser, "", expectedCommand);
    }

    @Test
    public void parse_validIndex() throws ParseException {
        OrderCommand expectedCommand = parser.parse("1");
        assertParseSuccess(parser, "1", expectedCommand);
    }

    @Test
    public void parse_invalidIndex_failure() throws ParseException {
        assertParseFailure(parser, "-1", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                OrderCommand.MESSAGE_USAGE));
    }
}
