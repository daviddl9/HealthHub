package seedu.address.logic.parser.request;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_REQUEST_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CONDITION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_STATUS;

import java.util.function.Predicate;
import java.util.stream.Stream;

import seedu.address.logic.commands.request.FindCommand;
import seedu.address.logic.commands.request.RequestCommand;
import seedu.address.logic.parser.ArgumentMultimap;
import seedu.address.logic.parser.ArgumentTokenizer;
import seedu.address.logic.parser.Parser;
import seedu.address.logic.parser.Prefix;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.request.Request;

/**
 * Parses the given {@code String} of arguments in the context of the OrderFindCommand
 * and returns an OrderFindCommand object for execution.
 * @throws ParseException if the user input does not conform the expected format
 */
public class FindCommandParser implements Parser<RequestCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the OrderFindCommand
     * and returns an OrderFindCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    @Override
    public RequestCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(
                        args, PREFIX_NAME, PREFIX_PHONE, PREFIX_ADDRESS, PREFIX_DATE, PREFIX_CONDITION, PREFIX_STATUS);

        if (!arePrefixesPresent(
                argMultimap, PREFIX_NAME, PREFIX_PHONE, PREFIX_ADDRESS, PREFIX_DATE, PREFIX_CONDITION, PREFIX_STATUS)
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_REQUEST_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        }

        Predicate<Request> suppliedPredicates = new RequestPredicateUtil().parsePredicate(argMultimap);
        return new FindCommand(suppliedPredicates);

    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).anyMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }
}
