package seedu.address.logic;

import java.util.logging.Logger;

import javafx.collections.ObservableList;
import seedu.address.commons.core.ComponentManager;
import seedu.address.commons.core.LogsCenter;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.AddressBookParser;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.Model;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.food.Food;

/**
 * The main LogicManager of the app.
 */
public class LogicManager extends ComponentManager implements Logic {
    private final Logger logger = LogsCenter.getLogger(LogicManager.class);

    private final Model model;
    private final CommandHistory history;
    private final AddressBookParser addressBookParser;
    private final UndoRedoStack undoRedoStack;

    public LogicManager(Model model) {
        this.model = model;
        history = new CommandHistory();
        addressBookParser = new AddressBookParser();
        undoRedoStack = new UndoRedoStack();
    }

    @Override
    public CommandResult execute(String commandText) throws CommandException, ParseException {
        logger.info("----------------[USER COMMAND][" + commandText + "]");
        try {
            //@@author jaxony
            CommandResult result;
            if (model.isUserInActiveSession()) {
                logger.info("User is in an active session with the system.");
                result = model.interpretInteractiveUserInput(commandText);
            } else if (isCommandInteractive(commandText)) {
                logger.info("Command is interactive.");
                createNewSession(commandText);
                result = startSession();
            } else {
                //@@author
                Command command = addressBookParser.parseCommand(commandText);
                command.setData(model, history, undoRedoStack);
                result = command.execute();
                undoRedoStack.push(command);
            }
            return result;
        } finally {
            history.add(commandText);
        }
    }

    @Override
    public ObservableList<Food> getFilteredFoodList() {
        return model.getFilteredFoodList();
    }

    @Override
    public ListElementPointer getHistorySnapshot() {
        return new ListElementPointer(history.getHistory());
    }

    //@@author jaxony
    @Override
    public void createNewSession(String userInput) throws IllegalArgumentException {
        Command interactiveCommand = addressBookParser.getCommand(userInput);
        interactiveCommand.setData(model, null, null);
        model.createNewSession(interactiveCommand);
    }

    @Override
    public CommandResult startSession() throws CommandException {
        return model.startSession();
    }

    @Override
    public boolean isCommandInteractive(String commandText) throws ParseException {
        return addressBookParser.isCommandInteractive(commandText);
    }

    //@@author tohcheryl
    @Override
    public ReadOnlyAddressBook getAddressBook() {
        return model.getAddressBook();
    }
}
