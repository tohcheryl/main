package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ALLERGIES;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.commands.EditUserCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.food.allergy.Allergy;

//@@author tohcheryl
/**
 * Parses input arguments and creates a new EditUserCommand object
 */
public class EditUserCommandParser implements Parser<EditUserCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the EditCommand
     * and returns an EditCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public EditUserCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_PHONE, PREFIX_ADDRESS, PREFIX_ALLERGIES);

        EditUserCommand.EditUserDescriptor editUserDescriptor = new EditUserCommand.EditUserDescriptor();
        try {
            ParserUtil.parseName(argMultimap.getValue(PREFIX_NAME)).ifPresent(editUserDescriptor::setName);
            ParserUtil.parsePhone(argMultimap.getValue(PREFIX_PHONE)).ifPresent(editUserDescriptor::setPhone);
            ParserUtil.parseAddress(argMultimap.getValue(PREFIX_ADDRESS)).ifPresent(editUserDescriptor::setAddress);
            parseAllergiesForEdit(argMultimap.getAllValues(PREFIX_ALLERGIES))
                    .ifPresent(editUserDescriptor::setAllergies);
        } catch (IllegalValueException ive) {
            throw new ParseException(ive.getMessage(), ive);
        }

        if (!editUserDescriptor.isAnyFieldEdited()) {
            throw new ParseException(EditUserCommand.MESSAGE_NOT_EDITED);
        }

        return new EditUserCommand(editUserDescriptor);
    }

    /**
     * Parses {@code Collection<String> allergies} into a {@code Set<Allergy>} if {@code allergies} is non-empty.
     * If {@code allergies} contain only one element which is an empty string, it will be parsed into a
     * {@code Set<Allergy>} containing zero allergies.
     */
    private Optional<Set<Allergy>> parseAllergiesForEdit(Collection<String> allergies) throws IllegalValueException {
        assert allergies != null;

        if (allergies.isEmpty()) {
            return Optional.empty();
        }
        Collection<String> allergySet = allergies.size() == 1 && allergies.contains("")
                ? Collections.emptySet() : allergies;
        return Optional.of(ParserUtil.parseAllergies(allergySet));
    }
}
