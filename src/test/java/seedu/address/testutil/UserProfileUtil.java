package seedu.address.testutil;

import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ALLERGIES;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;

import seedu.address.logic.commands.UserConfigCommand;
import seedu.address.model.user.UserProfile;

//@@author tohcheryl
/**
 * A utility class for UserProfile.
 */
public class UserProfileUtil {
    /**
     * Returns an add command string for adding the {@code userProfile}.
     */
    public static String getUserConfigCommand(UserProfile userProfile) {
        return UserConfigCommand.COMMAND_WORD + " " + getUserDetails(userProfile);
    }

    /**
     * Returns the part of command string for the given {@code userProfile}'s details.
     */
    public static String getUserDetails(UserProfile userProfile) {
        StringBuilder sb = new StringBuilder();
        sb.append(PREFIX_NAME + userProfile.getName().fullName + " ");
        sb.append(PREFIX_PHONE + userProfile.getPhone().value + " ");
        sb.append(PREFIX_ADDRESS + userProfile.getAddress().value + " ");
        userProfile.getAllergies().stream().forEach(s -> sb.append(PREFIX_ALLERGIES + s.allergyName + " ")
        );
        return sb.toString();
    }
}
