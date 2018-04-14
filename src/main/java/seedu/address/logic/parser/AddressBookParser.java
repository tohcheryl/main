package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import seedu.address.logic.commands.AddCommand;
import seedu.address.logic.commands.ChangePicCommand;
import seedu.address.logic.commands.ClearCommand;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.CommandFactory;
import seedu.address.logic.commands.DeleteCommand;
import seedu.address.logic.commands.EditCommand;
import seedu.address.logic.commands.EditUserCommand;
import seedu.address.logic.commands.ExitCommand;
import seedu.address.logic.commands.FindCommand;
import seedu.address.logic.commands.HelpCommand;
import seedu.address.logic.commands.HistoryCommand;
import seedu.address.logic.commands.ListCommand;
import seedu.address.logic.commands.OrderCommand;
import seedu.address.logic.commands.RedoCommand;
import seedu.address.logic.commands.SelectCommand;
import seedu.address.logic.commands.UndoCommand;
import seedu.address.logic.commands.UserConfigCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses user input.
 */
public class AddressBookParser {

    /**
     * Used for initial separation of command word and args.
     */
    private static final Pattern BASIC_COMMAND_FORMAT = Pattern.compile("(?<commandWord>\\S+)(?<arguments>.*)");

    //@@author jaxony
    /**
     * Checks whether userInput specifies a command that is interactive.
     * Currently only AddCommand supports interactive mode.
     * @param userInput Text input from user.
     * @return True if the command is interactive, false if the command is valid but not interactive.
     * @throws ParseException If the command is invalid.
     */
    public boolean isCommandInteractive(String userInput) throws ParseException {
        Matcher matcher = match(userInput);
        final String arguments = matcher.group("arguments");
        switch (matcher.group("commandWord")) {
        case AddCommand.COMMAND_WORD:
            break;
        case EditCommand.COMMAND_WORD:
        case SelectCommand.COMMAND_WORD:
        case DeleteCommand.COMMAND_WORD:
        case ClearCommand.COMMAND_WORD:
        case FindCommand.COMMAND_WORD:
        case ListCommand.COMMAND_WORD:
        case OrderCommand.COMMAND_WORD:
        case HistoryCommand.COMMAND_WORD:
        case ExitCommand.COMMAND_WORD:
        case HelpCommand.COMMAND_WORD:
        case UndoCommand.COMMAND_WORD:
        case RedoCommand.COMMAND_WORD:
        case ChangePicCommand.COMMAND_WORD:
        case EditUserCommand.COMMAND_WORD:
        case UserConfigCommand.COMMAND_WORD:
            return false;
        default:
            throw new ParseException(MESSAGE_UNKNOWN_COMMAND);
        }
        return arguments.equals("");
    }

    /**
     * Matches user input string with a basic command regex.
     *
     * @param userInput Text input from user.
     * @return Matcher object produced from regex pattern.
     * @throws ParseException If error arises during parsing of {@code userInput}.
     */
    private Matcher match(String userInput) throws ParseException {
        final Matcher matcher = BASIC_COMMAND_FORMAT.matcher(userInput.trim());
        if (!matcher.matches()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE));
        }
        return matcher;
    }
    //@@author

    /**
     * Parses user input into command for execution.
     * @param userInput full user input string.
     * @return The command based on the user input.
     * @throws ParseException If the user input does not conform the expected format.
     */
    public Command parseCommand(String userInput) throws ParseException {
        Matcher matcher = match(userInput);

        final String commandWord = matcher.group("commandWord");
        final String arguments = matcher.group("arguments");
        switch (commandWord) {

        case AddCommand.COMMAND_WORD:
            return new AddCommandParser().parse(arguments);

        case EditCommand.COMMAND_WORD:
            return new EditCommandParser().parse(arguments);

        case SelectCommand.COMMAND_WORD:
            return new SelectCommandParser().parse(arguments);

        case DeleteCommand.COMMAND_WORD:
            return new DeleteCommandParser().parse(arguments);

        case ClearCommand.COMMAND_WORD:
            return new ClearCommand();

        case FindCommand.COMMAND_WORD:
            return new FindCommandParser().parse(arguments);

        case ListCommand.COMMAND_WORD:
            return new ListCommand();

        case OrderCommand.COMMAND_WORD:
            return new OrderCommandParser().parse(arguments);

        case HistoryCommand.COMMAND_WORD:
            return new HistoryCommand();

        case ExitCommand.COMMAND_WORD:
            return new ExitCommand();

        case HelpCommand.COMMAND_WORD:
            return new HelpCommand();

        case UndoCommand.COMMAND_WORD:
            return new UndoCommand();

        case RedoCommand.COMMAND_WORD:
            return new RedoCommand();

        case ChangePicCommand.COMMAND_WORD:
            return new ChangePicCommand();

        case UserConfigCommand.COMMAND_WORD:
            return new UserConfigCommandParser().parse(arguments);

        case EditUserCommand.COMMAND_WORD:
            return new EditUserCommandParser().parse(arguments);

        default:
            throw new ParseException(MESSAGE_UNKNOWN_COMMAND);
        }
    }

    //@@author jaxony
    /**
     * Creates a new command object.
     * @param userInput Text input from user.
     * @return New Command object.
     * @throws IllegalArgumentException If the command in {@code userInput} is not supported.
     */
    public Command getCommand(String userInput) throws IllegalArgumentException {
        return CommandFactory.createCommand(userInput.trim());
    }
    //@@author
}
