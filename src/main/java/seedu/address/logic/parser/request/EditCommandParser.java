package seedu.address.logic.parser.request;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CONDITION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.request.EditCommand;
import seedu.address.logic.commands.request.EditCommand.EditOrderDescriptor;
import seedu.address.logic.commands.request.RequestCommand;
import seedu.address.logic.parser.ArgumentMultimap;
import seedu.address.logic.parser.ArgumentTokenizer;
import seedu.address.logic.parser.Parser;
import seedu.address.logic.parser.ParserUtil;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.request.Condition;

/**
 * Parses input arguments and creates a new EditCommand object
 */
public class EditCommandParser implements Parser<RequestCommand> {
    /**
     * Parses the given {@code String} of arguments in the context of the EditCommand
     * and returns an EditCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public EditCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_PHONE , PREFIX_ADDRESS, PREFIX_DATE, PREFIX_CONDITION);

        Index index;

        try {
            index = ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (ParseException pe) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE), pe);
        }

        EditOrderDescriptor editOrderDescriptor = new EditOrderDescriptor();
        if (argMultimap.getValue(PREFIX_NAME).isPresent()) {
            editOrderDescriptor.setName(ParserUtil.parseName(argMultimap.getValue(PREFIX_NAME).get()));
        }
        if (argMultimap.getValue(PREFIX_PHONE).isPresent()) {
            editOrderDescriptor.setPhone(ParserUtil.parsePhone(argMultimap.getValue(PREFIX_PHONE).get()));
        }
        if (argMultimap.getValue(PREFIX_ADDRESS).isPresent()) {
            editOrderDescriptor.setAddress(ParserUtil.parseAddress(argMultimap.getValue(PREFIX_ADDRESS).get()));
        }
        if (argMultimap.getValue(PREFIX_DATE).isPresent()) {
            editOrderDescriptor.setDate(ParserUtil.parseDate(argMultimap.getValue(PREFIX_DATE).get()));
        }
        parseFoodForEdit(argMultimap.getAllValues(PREFIX_CONDITION)).ifPresent(editOrderDescriptor::setCondition);

        if (!editOrderDescriptor.isAnyFieldEdited()) {
            throw new ParseException(EditCommand.MESSAGE_NOT_EDITED);
        }

        return new EditCommand(index, editOrderDescriptor);
    }

    /**
     * Parses {@code Collection<String> food} into a {@code Set<Condition>} if {@code food} is non-empty.
     * If {@code food} contain only one element which is an empty string, it will be parsed into a
     * {@code Set<Condition>} containing zero food.
     */
    private Optional<Set<Condition>> parseFoodForEdit(Collection<String> food) throws ParseException {
        assert food != null;

        if (food.isEmpty()) {
            return Optional.empty();
        }

        if (food.size() == 1 && food.contains("")) {
            throw new ParseException(String.format(Condition.MESSAGE_FOOD_CONSTRAINTS));
        }

        return Optional.of(ParserUtil.parseFoods(food));
    }
}
