package seedu.address.logic.parser.request;

import static org.junit.Assert.assertFalse;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_REQUEST_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import org.junit.Test;

import seedu.address.logic.commands.request.FindCommand;
import seedu.address.model.request.RequestAddressContainsKeywordPredicate;
import seedu.address.model.request.RequestConditionContainsKeywordPredicate;
import seedu.address.model.request.RequestDatePredicate;
import seedu.address.model.request.RequestNameContainsKeywordPredicate;
import seedu.address.model.request.RequestPhoneContainsKeywordPredicate;
import seedu.address.model.request.RequestStatus;
import seedu.address.model.request.RequestStatusContainsKeywordPredicate;

public class FindCommandParserTest {

    private FindCommandParser parser = new FindCommandParser();

    @Test
    public void parse_emptyArg_throwsParseException() {
        assertParseFailure(parser, "     ",
            String.format(MESSAGE_INVALID_REQUEST_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        assertParseFailure(parser, "find ",
            String.format(MESSAGE_INVALID_REQUEST_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_validArgs_returnsFindCommand() {
        FindCommand expectedNameFindCommand =
            new FindCommand(new RequestNameContainsKeywordPredicate("Alex"));
        assertParseSuccess(parser, " n/Alex", expectedNameFindCommand);

        FindCommand expectedPhoneFindCommand =
            new FindCommand(new RequestPhoneContainsKeywordPredicate(Arrays.asList("81223455")));
        assertParseSuccess(parser, " p/81223455", expectedPhoneFindCommand);

        FindCommand expectedAddressFindCommand =
            new FindCommand(new RequestAddressContainsKeywordPredicate("123, Jurong West Ave 6, #08-111"));
        assertParseSuccess(parser, " a/123, Jurong West Ave 6, #08-111", expectedAddressFindCommand);

        FindCommand expectedFoodFindCommand =
            new FindCommand(new RequestConditionContainsKeywordPredicate("physio"));
        assertParseSuccess(parser, " c/physio", expectedFoodFindCommand);

        FindCommand expectedStatusFindCommand =
            new FindCommand(new RequestStatusContainsKeywordPredicate(Arrays.asList(new RequestStatus("PENDING"))));
        assertParseSuccess(parser, " st/PENDING", expectedStatusFindCommand);

        SimpleDateFormat sf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        Date date = null;

        try {
            date = sf.parse("01-10-2018 10:00:00");
        } catch (ParseException pE) {
            assertFalse(true);
        }

        FindCommand expectedDateFindCommand =
            new FindCommand(new RequestDatePredicate(Arrays.asList(date)));
        assertParseSuccess(parser, " dt/01-10-2018 10:00:00", expectedDateFindCommand);
    }

    @Test
    public void parse_invalidArgs_throwParseException() {
        assertParseFailure(parser, "w/",
            String.format(MESSAGE_INVALID_REQUEST_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
    }
}
