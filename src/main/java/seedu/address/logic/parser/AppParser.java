package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.Messages.MESSAGE_UNKNOWN_COMMAND;

import java.util.Optional;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import seedu.address.commons.core.LogsCenter;
import seedu.address.logic.autocomplete.AutocompleteGenerator;
import seedu.address.logic.commands.AddCommand;
import seedu.address.logic.commands.ApplyCommand;
import seedu.address.logic.commands.ClearCommand;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.DeleteCommand;
import seedu.address.logic.commands.EditCommand;
import seedu.address.logic.commands.ExitCommand;
import seedu.address.logic.commands.FindCommand;
import seedu.address.logic.commands.HelpCommand;
import seedu.address.logic.commands.ImportCommand;
import seedu.address.logic.commands.ListCommand;
import seedu.address.logic.commands.SortCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Processes user input for the application.
 * It is a utility class for parsing and performing actions on command strings app-wide.
 */
public class AppParser {

    /**
     * Used for initial separation of command word and args.
     */
    private static final Pattern BASIC_COMMAND_FORMAT = Pattern.compile("(?<commandWord>\\S+)(?<arguments>.*)");
    private static final Logger logger = LogsCenter.getLogger(AppParser.class);

    /**
     * Parses user input into command for execution.
     *
     * @param userInput full user input string
     * @return the command based on the user input
     * @throws ParseException if the user input does not conform the expected format
     */
    public Command parseCommand(String userInput) throws ParseException {
        final Matcher matcher = BASIC_COMMAND_FORMAT.matcher(userInput.trim());
        if (!matcher.matches()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE));
        }

        final String commandWord = matcher.group("commandWord");
        final String arguments = matcher.group("arguments");

        // Note to developers: Change the log level in config.json to enable lower level (i.e., FINE, FINER and lower)
        // log messages such as the one below.
        // Lower level log messages are used sparingly to minimize noise in the code.
        logger.fine("Command word: " + commandWord + "; Arguments: " + arguments);

        switch (commandWord) {

        case AddCommand.COMMAND_WORD:
            return new AddCommandParser().parse(arguments);

        case EditCommand.COMMAND_WORD:
            return new EditCommandParser().parse(arguments);

        case DeleteCommand.COMMAND_WORD:
            return new DeleteCommandParser().parse(arguments);

        case ClearCommand.COMMAND_WORD:
            return new ClearCommand();

        case FindCommand.COMMAND_WORD:
            return new FindCommandParser().parse(arguments);

        case ListCommand.COMMAND_WORD:
            return new ListCommandParser().parse(arguments);

        case SortCommand.COMMAND_WORD:
            return new SortCommandParser().parse(arguments);

        case ExitCommand.COMMAND_WORD:
            return new ExitCommand();

        case HelpCommand.COMMAND_WORD:
            return new HelpCommand();

        case ApplyCommand.COMMAND_WORD:
            return new ApplyCommandParser().parse(arguments);

        case ImportCommand.COMMAND_WORD:
            return new ImportCommandParser().parse(arguments);

        default:
            logger.finer("This user input caused a ParseException: " + userInput);
            throw new ParseException(MESSAGE_UNKNOWN_COMMAND);
        }
    }

    /**
     * Parses user input into an evaluator that can be executed to obtain autocompletion results.
     *
     * @param userInput full user input string
     * @return the command based on the user input
     */
    public AutocompleteGenerator parseCompletionGenerator(String userInput) {
        final Matcher matcher = BASIC_COMMAND_FORMAT.matcher(userInput.trim());
        if (!matcher.matches()) {
            return AutocompleteGenerator.NO_RESULTS;
        }
        final String commandWord = matcher.group("commandWord");

        logger.finest("Preparing autocomplete: " + userInput);

        switch (commandWord) {
        case AddCommand.COMMAND_WORD:
            return new AutocompleteGenerator(AddCommand.AUTOCOMPLETE_SUPPLIER);

        case ApplyCommand.COMMAND_WORD:
            return new AutocompleteGenerator(ApplyCommand.AUTOCOMPLETE_SUPPLIER);

        case EditCommand.COMMAND_WORD:
            return new AutocompleteGenerator(EditCommand.AUTOCOMPLETE_SUPPLIER);

        case DeleteCommand.COMMAND_WORD:
            return new AutocompleteGenerator(DeleteCommand.AUTOCOMPLETE_SUPPLIER);

        case ListCommand.COMMAND_WORD:
            return new AutocompleteGenerator(ListCommand.AUTOCOMPLETE_SUPPLIER);

        case SortCommand.COMMAND_WORD:
            return new AutocompleteGenerator(SortCommand.AUTOCOMPLETE_SUPPLIER);

        case ImportCommand.COMMAND_WORD:
            return new AutocompleteGenerator(ImportCommand.AUTOCOMPLETE_SUPPLIER);

        case ClearCommand.COMMAND_WORD:
        case FindCommand.COMMAND_WORD:
        case ExitCommand.COMMAND_WORD:
        case HelpCommand.COMMAND_WORD:
            return AutocompleteGenerator.NO_RESULTS;

        default:
            // Not a valid command. Return autocompletion results based on all the known command names.
            return new AutocompleteGenerator(
                    Command.getCommandWords(Stream.of(
                            AddCommand.class, ApplyCommand.class, DeleteCommand.class, EditCommand.class,
                            ListCommand.class, FindCommand.class, SortCommand.class, HelpCommand.class,
                            ClearCommand.class, ExitCommand.class, ImportCommand.class
                    )).filter(Optional::isPresent).map(Optional::get)
            );

        }
    }

}
