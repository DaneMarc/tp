package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.PersonUtil.ADDRESS_DESC_AMY;
import static seedu.address.testutil.PersonUtil.ADDRESS_DESC_BOB;
import static seedu.address.testutil.PersonUtil.AMY;
import static seedu.address.testutil.PersonUtil.BOB;
import static seedu.address.testutil.PersonUtil.EMAIL_DESC_AMY;
import static seedu.address.testutil.PersonUtil.EMAIL_DESC_BOB;
import static seedu.address.testutil.PersonUtil.INVALID_ADDRESS_DESC;
import static seedu.address.testutil.PersonUtil.INVALID_EMAIL_DESC;
import static seedu.address.testutil.PersonUtil.INVALID_NAME_DESC;
import static seedu.address.testutil.PersonUtil.INVALID_PHONE_DESC;
import static seedu.address.testutil.PersonUtil.INVALID_TAG_DESC;
import static seedu.address.testutil.PersonUtil.NAME_DESC_AMY;
import static seedu.address.testutil.PersonUtil.NAME_DESC_BOB;
import static seedu.address.testutil.PersonUtil.PHONE_DESC_AMY;
import static seedu.address.testutil.PersonUtil.PHONE_DESC_BOB;
import static seedu.address.testutil.PersonUtil.PREAMBLE_NON_EMPTY;
import static seedu.address.testutil.PersonUtil.PREAMBLE_WHITESPACE;
import static seedu.address.testutil.PersonUtil.REMARK_DESC_AMY;
import static seedu.address.testutil.PersonUtil.TAG_DESC_COWORKER;
import static seedu.address.testutil.PersonUtil.TAG_DESC_FRIEND;
import static seedu.address.testutil.PersonUtil.VALID_ADDRESS_BOB;
import static seedu.address.testutil.PersonUtil.VALID_EMAIL_BOB;
import static seedu.address.testutil.PersonUtil.VALID_NAME_BOB;
import static seedu.address.testutil.PersonUtil.VALID_PHONE_BOB;
import static seedu.address.testutil.PersonUtil.VALID_TAG_FRIEND;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.AddCommand;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.tag.Tag;

public class AddCommandParserTest {
    private AddCommandParser parser = new AddCommandParser();

    @Test
    public void parse_allFieldsPresent_success() {
        Person expectedPerson = BOB.setTags(new Tag(VALID_TAG_FRIEND));

        // whitespace only preamble
        assertParseSuccess(parser,
            PREAMBLE_WHITESPACE + NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB + ADDRESS_DESC_BOB + TAG_DESC_FRIEND,
            new AddCommand(expectedPerson));

        // multiple names - last name accepted
        assertParseSuccess(parser,
            NAME_DESC_AMY + NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB + ADDRESS_DESC_BOB + TAG_DESC_FRIEND,
            new AddCommand(expectedPerson));

        // multiple phones - last phone accepted
        assertParseSuccess(parser,
            NAME_DESC_BOB + PHONE_DESC_AMY + PHONE_DESC_BOB + EMAIL_DESC_BOB + ADDRESS_DESC_BOB + TAG_DESC_FRIEND,
            new AddCommand(expectedPerson));

        // multiple emails - last email accepted
        assertParseSuccess(parser,
            NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_AMY + EMAIL_DESC_BOB + ADDRESS_DESC_BOB + TAG_DESC_FRIEND,
            new AddCommand(expectedPerson));

        // multiple addresses - last address accepted
        assertParseSuccess(parser,
            NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB + ADDRESS_DESC_AMY + ADDRESS_DESC_BOB + TAG_DESC_FRIEND,
            new AddCommand(expectedPerson));

        // multiple tags - all accepted
        Person expectedPersonMultipleTags = BOB;
        assertParseSuccess(parser,
            NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB + ADDRESS_DESC_BOB + TAG_DESC_COWORKER + TAG_DESC_FRIEND,
            new AddCommand(expectedPersonMultipleTags));
    }

    @Test
    public void parse_optionalFieldsMissing_success() {
        // zero tags
        Person expectedPerson = AMY.setTags();
        assertParseSuccess(parser, NAME_DESC_AMY + PHONE_DESC_AMY + EMAIL_DESC_AMY + ADDRESS_DESC_AMY + REMARK_DESC_AMY,
            new AddCommand(expectedPerson));
    }

    @Test
    public void parse_requiredFieldMissing_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE);

        // missing name prefix
        assertParseFailure(parser, VALID_NAME_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB + ADDRESS_DESC_BOB,
            expectedMessage);

        // missing phone prefix
        assertParseFailure(parser, NAME_DESC_BOB + VALID_PHONE_BOB + EMAIL_DESC_BOB + ADDRESS_DESC_BOB,
            expectedMessage);

        // missing email prefix
        assertParseFailure(parser, NAME_DESC_BOB + PHONE_DESC_BOB + VALID_EMAIL_BOB + ADDRESS_DESC_BOB,
            expectedMessage);

        // missing address prefix
        assertParseFailure(parser, NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB + VALID_ADDRESS_BOB,
            expectedMessage);

        // all prefixes missing
        assertParseFailure(parser, VALID_NAME_BOB + VALID_PHONE_BOB + VALID_EMAIL_BOB + VALID_ADDRESS_BOB,
            expectedMessage);
    }

    @Test
    public void parse_invalidValue_failure() {
        // invalid name
        assertParseFailure(parser,
            INVALID_NAME_DESC + PHONE_DESC_BOB + EMAIL_DESC_BOB + ADDRESS_DESC_BOB + TAG_DESC_COWORKER
                + TAG_DESC_FRIEND, Name.MESSAGE_CONSTRAINTS);

        // invalid phone
        assertParseFailure(parser,
            NAME_DESC_BOB + INVALID_PHONE_DESC + EMAIL_DESC_BOB + ADDRESS_DESC_BOB + TAG_DESC_COWORKER
                + TAG_DESC_FRIEND, Phone.MESSAGE_CONSTRAINTS);

        // invalid email
        assertParseFailure(parser,
            NAME_DESC_BOB + PHONE_DESC_BOB + INVALID_EMAIL_DESC + ADDRESS_DESC_BOB + TAG_DESC_COWORKER
                + TAG_DESC_FRIEND, Email.MESSAGE_CONSTRAINTS);

        // invalid address
        assertParseFailure(parser,
            NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB + INVALID_ADDRESS_DESC + TAG_DESC_COWORKER
                + TAG_DESC_FRIEND, Address.MESSAGE_CONSTRAINTS);

        // invalid tag
        assertParseFailure(parser,
            NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB + ADDRESS_DESC_BOB + INVALID_TAG_DESC + VALID_TAG_FRIEND,
            Tag.MESSAGE_CONSTRAINTS);

        // two invalid values, only first invalid value reported
        assertParseFailure(parser, INVALID_NAME_DESC + PHONE_DESC_BOB + EMAIL_DESC_BOB + INVALID_ADDRESS_DESC,
            Name.MESSAGE_CONSTRAINTS);

        // non-empty preamble
        assertParseFailure(parser,
            PREAMBLE_NON_EMPTY + NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB + ADDRESS_DESC_BOB + TAG_DESC_COWORKER
                + TAG_DESC_FRIEND, String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));
    }
}
