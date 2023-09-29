package seedu.address.logic.commands;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seedu.address.commons.core.index.Index;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.model.person.Remark;
import seedu.address.testutil.PersonBuilder;

import static seedu.address.logic.Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showPersonAtIndex;
import static seedu.address.logic.commands.RemarkCommand.MESSAGE_ADD_REMARK_SUCCESS;
import static seedu.address.logic.commands.RemarkCommand.MESSAGE_DELETE_REMARK_SUCCESS;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

class RemarkCommandTest {

    private Model model;
    private Model expectedModel;

    private final Remark TEST_REMARK = new Remark("cool lady");
    private final Remark EMPTY_REMARK = new Remark("");

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
    }

    @Test
    void execute_addRemarkUnfilteredList_success() {
        Person person = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person personToRemark = new PersonBuilder(person).withRemark(TEST_REMARK.value).build();
        expectedModel.setPerson(
            expectedModel.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased()),
            personToRemark);

        assertCommandSuccess(
            new RemarkCommand(INDEX_FIRST_PERSON, TEST_REMARK),
            model,
            String.format(MESSAGE_ADD_REMARK_SUCCESS, personToRemark),
            expectedModel);
    }

    @Test
    void execute_deleteRemarkUnfilteredList_success() {
        Person person = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person personToRemark = new PersonBuilder(person).withRemark(TEST_REMARK.value).build();
        model.setPerson(person, personToRemark);

        assertCommandSuccess(
            new RemarkCommand(INDEX_FIRST_PERSON, EMPTY_REMARK),
            model,
            String.format(MESSAGE_DELETE_REMARK_SUCCESS, personToRemark),
            expectedModel);
    }

    @Test
    public void execute_filteredList_success() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Person person = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person personToRemark = new PersonBuilder(person).withRemark(TEST_REMARK.value).build();
        expectedModel.setPerson(
            expectedModel.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased()),
            personToRemark);

        assertCommandSuccess(
            new RemarkCommand(INDEX_FIRST_PERSON, TEST_REMARK),
            model,
            String.format(MESSAGE_ADD_REMARK_SUCCESS, personToRemark),
            expectedModel);
    }

    @Test
    public void execute_invalidPersonIndexUnfilteredList_failure() {
        Index outOfBounds = Index.fromOneBased(
            model.getAddressBook().getPersonList().size() + 1);
        assertCommandFailure(
            new RemarkCommand(outOfBounds, TEST_REMARK),
            model,
            MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_invalidPersonIndexFilteredList_failure() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        assertCommandFailure(
            new RemarkCommand(INDEX_SECOND_PERSON, TEST_REMARK),
            model,
            MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }
}