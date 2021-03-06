package seedu.address.logic.commands.request;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CONDITION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;

import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.request.Request;

/**
 * Adds an request to the request book.
 */
public class AddCommand extends RequestCommand {

    public static final String COMMAND_WORD = "add";

    public static final String MESSAGE_USAGE = RequestCommand.COMMAND_WORD + " " + COMMAND_WORD
            + ": Adds an request to the request book. "
            + "Parameters: "
            + PREFIX_NAME + "NAME "
            + PREFIX_PHONE + "PHONE "
            + PREFIX_ADDRESS + "ADDRESS "
            + PREFIX_DATE + "DATETIME "
            + PREFIX_CONDITION + "CONDITION...\n"
            + "Example: " + RequestCommand.COMMAND_WORD + " " + COMMAND_WORD + " "
            + PREFIX_NAME + "John Doe "
            + PREFIX_PHONE + "98765432 "
            + PREFIX_ADDRESS + "311, Clementi Ave 2, #02-25, 612344 "
            + PREFIX_CONDITION + "Physiotherapy "
            + PREFIX_CONDITION + "Cancer Treatment "
            + PREFIX_DATE + "12-10-2018 00:00:00";

    public static final String MESSAGE_SUCCESS = "New request added: %1$s";
    public static final String MESSAGE_DUPLICATE_ORDER = "This request already exists in the request book";

    private final Request toAdd;

    /**
     * Creates an AddCommand to add the specified {@code Request}
     */
    public AddCommand(Request request) {
        requireNonNull(request);
        toAdd = request;

    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        requireNonNull(model);

        if (model.hasOrder(toAdd)) {
            throw new CommandException(MESSAGE_DUPLICATE_ORDER);
        }

        model.addOrder(toAdd);
        model.commitOrderBook();
        return new CommandResult(String.format(MESSAGE_SUCCESS, toAdd));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof AddCommand // instanceof handles nulls
                && toAdd.isSameOrder(((AddCommand) other).toAdd));
    }
}
