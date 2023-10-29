package seedu.address.logic.parser;

import static seedu.address.logic.parser.CliSyntax.FLAG_CSV;
import static seedu.address.logic.parser.CliSyntax.FLAG_JSON;

import java.io.File;

import seedu.address.logic.commands.ImportCommand;
import seedu.address.logic.parser.exceptions.ParseException;

public class ImportCommandParser implements Parser<ImportCommand> {

    public ImportCommand parse(String args) throws ParseException {
        var map = ArgumentTokenizer.tokenize(args,
                ImportCommand.AUTOCOMPLETE_SUPPLIER.getAllPossibleFlags().toArray(Flag[]::new));

        var filePath = map.getValue(FLAG_CSV).or(() -> map.getValue(FLAG_JSON)).filter(s -> !s.isBlank());

        return new ImportCommand(filePath.map(File::new).orElse(null));
    }

}
