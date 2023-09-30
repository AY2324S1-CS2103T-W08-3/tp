package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import seedu.address.model.Model;

/**
 * Format full help instructions for every command for display.
 */
public class HelloCommand extends Command {

    public static final String COMMAND_WORD = "hello";

    public static final String MESSAGE_USAGE = "Shows you that the program is live!";

    public static final String SHOWING_HELP_MESSAGE = "Hello buddy!";

    @Override
    public CommandResult execute(Model model) {
        return new CommandResult(MESSAGE_USAGE);
    }

//    @Override
//    public CommandResult execute(Model model) {
//        requireNonNull(model);
//        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
//        return new CommandResult(MESSAGE_SUCCESS);
//    }
}
