package seedu.address.logic.parser.healthworker;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_NAME_DESC;
import static seedu.address.logic.commands.CommandTestUtil.NAME_DESC_RAJUL;
import static seedu.address.logic.commands.CommandTestUtil.PREAMBLE_WHITESPACE;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_RAJUL;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalDeliverymen.RAJUL;

import org.junit.Test;

import seedu.address.logic.commands.healthworker.AddHealthWorkerCommand;
import seedu.address.model.common.Name;
import seedu.address.model.healthworker.Healthworker;
import seedu.address.testutil.DeliverymanBuilder;

public class AddHealthWorkerCommandParserTest {
    private AddHealthWorkerCommandParser parser = new AddHealthWorkerCommandParser();

    @Test
    public void parse_allFieldsPresent_success() {
        Healthworker expectedHealthworker = new DeliverymanBuilder(RAJUL).build();

        assertParseSuccess(parser, PREAMBLE_WHITESPACE + NAME_DESC_RAJUL,
                new AddHealthWorkerCommand(expectedHealthworker));

        assertParseSuccess(parser, NAME_DESC_RAJUL,
                new AddHealthWorkerCommand(expectedHealthworker));
    }

    @Test
    public void parse_compulsoryFieldMissing_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddHealthWorkerCommand.MESSAGE_USAGE);

        //missing name prefic
        assertParseFailure(parser, VALID_NAME_RAJUL, expectedMessage);
    }

    @Test
    public void parse_invalidValue_failure() {
        assertParseFailure(parser, INVALID_NAME_DESC, Name.MESSAGE_NAME_CONSTRAINTS);
    }
}
