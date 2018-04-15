package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.commands.OrderCommand;
import seedu.address.logic.parser.exceptions.ParseException;

//@@author samzx

/**
 * Parses input arguments and creates a new Order Command object
 */
public class OrderCommandParser implements Parser<OrderCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the OrderCommand
     * and returns an OrderCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public OrderCommand parse(String args) throws ParseException {

        if (withIndexArgument(args)) {
            return orderCommandWithIndex(args);
        } else {
            return orderCommandWithoutIndex();
        }

    }

    /**
     * Returns a default order command without a specific index to order
     */
    private OrderCommand orderCommandWithoutIndex() {
        return new OrderCommand(null);
    }

    /**
     * Returns an OrderCommand object for execution when given a {@code String} of
     * arguments in the context of the OrderCommand.
     * @throws ParseException if the user input does not conform the expected format
     */
    private OrderCommand orderCommandWithIndex(String args) throws ParseException {
        Index index;
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args);
        try {
            index = ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (IllegalValueException ive) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, OrderCommand.MESSAGE_USAGE));
        }
        return new OrderCommand(index);
    }

    private boolean withIndexArgument(String args) {
        return !args.equals("");
    }
}


