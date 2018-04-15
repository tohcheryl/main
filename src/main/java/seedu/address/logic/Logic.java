package seedu.address.logic;

import javafx.collections.ObservableList;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.food.Food;

/**
 * API of the Logic component
 */
public interface Logic {
    /**
     * Executes the command and returns the result.
     * @param commandText The command as entered by the user.
     * @return the result of the command execution.
     * @throws CommandException If an error occurs during command execution.
     * @throws ParseException If an error occurs during parsing.
     */
    CommandResult execute(String commandText) throws CommandException, ParseException;

    /** Returns an unmodifiable view of the filtered list of foods */
    ObservableList<Food> getFilteredFoodList();

    /** Returns the list of input entered by the user, encapsulated in a {@code ListElementPointer} object */
    ListElementPointer getHistorySnapshot();

    //@@author jaxony
    /**
     * Creates a new Session for chat-like interaction with system.
     * @param userInput Text input from user.
     */
    void createNewSession(String userInput);

    /**
     * Starts the active Session.
     * @return Feedback to user.
     * @throws CommandException If command execution fails.
     */
    CommandResult startSession() throws CommandException;

    /**
     * Checks if command is an interactive command.
     * @param commandText Text input from user.
     * @return Feedback to user.
     * @throws ParseException If {@code commandText} is not a valid command.
     */
    boolean isCommandInteractive(String commandText) throws ParseException;

    /**
     * Returns the current ReadOnlyAddressBook
     */
    ReadOnlyAddressBook getAddressBook();
}
