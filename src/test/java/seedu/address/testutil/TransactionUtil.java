package seedu.address.testutil;

import static seedu.address.logic.parser.CliSyntax.PREFIX_AMOUNT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CATEGORY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATETIME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_LOCATION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TYPE;

import seedu.address.logic.commands.transaction.AddTransactionCommand;
import seedu.address.model.transaction.Transaction;

/**
 * A utility class for Transaction.
 */
public class TransactionUtil {

    /**
     * Returns an add command string for adding the {@code transaction}.
     */
    public static String getAddTransactionCommand(Transaction transaction) {
        return AddTransactionCommand.COMMAND_WORD + " " + getTransactionDetails(transaction);
    }

    /**
     * Returns the part of command string for the given {@code transaction}'s details.
     */
    public static String getTransactionDetails(Transaction transaction) {
        StringBuilder sb = new StringBuilder();
        sb.append(PREFIX_NAME + transaction.getName().fullName + " ");
        sb.append(PREFIX_TYPE + transaction.getType().type.getOriginalString() + " ");
        sb.append(PREFIX_AMOUNT + transaction.getAmount().toString() + " ");
        sb.append(PREFIX_CATEGORY + transaction.getCategory().category + " ");
        sb.append(PREFIX_DATETIME + transaction.getDateTime().originalString() + " ");
        sb.append(PREFIX_LOCATION + transaction.getLocation().location + " ");

        return sb.toString();
    }
}