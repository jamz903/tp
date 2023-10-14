package seedu.address.logic.parser.transaction;
import static seedu.address.logic.UniCashMessages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.DeleteCommand;
import seedu.address.logic.commands.transaction.DeleteTransactionCommand;
import seedu.address.logic.parser.Parser;
import seedu.address.logic.parser.ParserUtil;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new DeleteTransactionCommand object
 */
public class DeleteTransactionCommandParser implements Parser<DeleteTransactionCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the
     * DeleteTransactionCommand and returns a DeleteTransactionCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public DeleteTransactionCommand parse(String args) throws ParseException {
        try {
            Index index = ParserUtil.parseIndex(args);
            return new DeleteTransactionCommand(index);
        } catch (ParseException pe) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE), pe);
        }
    }

}

