package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.FLAG_CSV;
import static seedu.address.logic.parser.CliSyntax.FLAG_JSON;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import javafx.stage.FileChooser;
import javafx.stage.Stage;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.autocomplete.AutocompleteSupplier;
import seedu.address.logic.autocomplete.data.AutocompleteDataSet;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.contact.NameContainsKeywordsPredicate;
import seedu.address.storage.AddressBookStorage;
import seedu.address.storage.JsonAddressBookStorage;
import seedu.address.storage.StorageManager;

public class ImportCommand extends Command {

    public static final String COMMAND_WORD = "import";

    public static final AutocompleteSupplier AUTOCOMPLETE_SUPPLIER = AutocompleteSupplier.from(
            AutocompleteDataSet.oneAmongAllOf(
                    FLAG_CSV, FLAG_JSON
            )
    ).configureValueMap(m -> m.put(null, null));

    public static Stage stage = null;
    private File file;

    public ImportCommand(File file) {
        this.file = file;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        if (file == null) {
            System.out.println("Stage: " + stage);
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open File");
            file = fileChooser.showOpenDialog(stage);
        }

        if (file == null) {
            throw new CommandException("File not provided");
        }

        if (!file.exists() || file.isDirectory()) {
            throw new CommandException("Given file does not exist");
        }

        try {
            Path sourcePath = file.toPath();
            new JsonAddressBookStorage(sourcePath).readAddressBook().ifPresent(model::setAddressBook);
        } catch (Exception e) {
            throw new CommandException("Failed");
        }

        return new CommandResult("Successfully imported data");
    }

}
