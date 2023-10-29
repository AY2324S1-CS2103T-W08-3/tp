package seedu.address.ui;

import java.util.Objects;
import java.util.logging.Logger;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Region;
import seedu.address.commons.core.LogsCenter;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * The UI component that is responsible for receiving user command inputs.
 */
public class CommandBox extends UiPart<Region> {

    public static final String ERROR_STYLE_CLASS = "error";
    private static final String FXML = "CommandBox.fxml";

    private static final Logger logger = LogsCenter.getLogger(CommandBox.class);

    private final CommandExecutor commandExecutor;
    private final AutocompleteTextField.CompletionGenerator completionGenerator;

    @FXML
    private AutocompleteTextField commandTextField;

    /**
     * Creates a {@code CommandBox} with the given {@code CommandExecutor}.
     */
    public CommandBox(
            CommandExecutor commandExecutor, AutocompleteTextField.CompletionGenerator completionGenerator
    ) {
        super(FXML);
        this.commandExecutor = commandExecutor;
        this.completionGenerator = completionGenerator;

        assert commandTextField != null;
        commandTextField.setCompletionGenerator(completionGenerator);

        commandTextField.setOnKeyPressed(this::handleKeyEvent);
        commandTextField.setOnKeyReleased(this::handleKeyEvent);
        commandTextField.setOnKeyTyped(this::handleKeyEvent);

        commandTextField.addEventFilter(KeyEvent.KEY_PRESSED, this::handleKeyFilter);
        commandTextField.addEventFilter(KeyEvent.KEY_RELEASED, this::handleKeyFilter);
        commandTextField.addEventFilter(KeyEvent.KEY_TYPED, this::handleKeyFilter);

        // calls #setStyleToDefault() whenever there is a change to the text of the command box.
        commandTextField.textProperty().addListener((unused1, unused2, unused3) -> setStyleToDefault());
    }


    /**
     * Handles the key event of a textbox.
     */
    @FXML
    private void handleKeyEvent(KeyEvent keyEvent) {

        // Note:
        //   These captures keystrokes at different KeyEvent due to weird JavaFX quirks not actually delivering
        //   all events at all situations.

        if (keyEvent.getEventType() == KeyEvent.KEY_RELEASED
                && keyEvent.getCode() == KeyCode.ENTER) {

            logger.fine("Received key ENTER");
            this.handleCommandEntered();

        } else if (keyEvent.getEventType() == KeyEvent.KEY_PRESSED
                && keyEvent.getCode() == KeyCode.TAB) {

            logger.fine("Intercepted TAB key");

            keyEvent.consume(); // consume by default
            commandTextField.requestFocus(); // revert to this focus in case (otherwise tab changes focus)
            commandTextField.end();

            this.handleCommandAutocompleted(keyEvent);

            commandTextField.refreshPopupState();
        }
    }

    /**
     * Handles the key filter of a textbox.
     */
    @FXML
    private void handleKeyFilter(KeyEvent keyEvent) {
        if (this.commandTextField.getCaretPosition() < this.commandTextField.getText().length()) {
            // Caret not at end. Autocomplete not applicable using standard keyboard intercepts.
            return;
        }

        // Note:
        //   These captures keystrokes at different KeyEvent due to weird JavaFX quirks not actually delivering
        //   all events at all situations.

        if (keyEvent.getEventType() == KeyEvent.KEY_TYPED
                && Objects.equals(keyEvent.getCharacter(), " ")) {

            logger.fine("Intercepted SPACE typed at end");
            this.handleCommandAutocompleted(keyEvent);

        } else if (keyEvent.getEventType() == KeyEvent.KEY_PRESSED
                && keyEvent.getCode() == KeyCode.BACK_SPACE) {

            logger.fine("Intercepted BACKSPACE typed at end");
            this.handleCommandUndoAutocomplete(keyEvent);

        }
    }

    /**
     * Handles the request for finalization of text input.
     */
    private void handleCommandEntered() {
        String commandText = commandTextField.getText();
        if (commandText.equals("")) {
            return;
        }

        try {
            commandExecutor.execute(commandText);
            commandTextField.setText("");
            commandTextField.requestFocus();
        } catch (CommandException | ParseException e) {
            setStyleToIndicateCommandFailure();
        }
    }

    /**
     * Handles the request for autocompletion of text input.
     */
    @FXML
    private void handleCommandAutocompleted(KeyEvent keyEvent) {
        logger.fine("User invoked auto-completion!");

        boolean hasAutocompletedResult = commandTextField.triggerImmediateAutocompletion();
        if (hasAutocompletedResult) {
            keyEvent.consume();
        }
    }

    /**
     * Handles the request for undoing autocompletion.
     */
    private void handleCommandUndoAutocomplete(KeyEvent keyEvent) {
        logger.fine("User invoked undo auto-completion!");

        boolean hasUndoneCompletion = commandTextField.undoLastImmediateAutocompletion();
        if (hasUndoneCompletion) {
            keyEvent.consume();
        }
    }

    /**
     * Sets the command box style to use the default style.
     */
    private void setStyleToDefault() {
        commandTextField.getStyleClass().remove(ERROR_STYLE_CLASS);
    }

    /**
     * Sets the command box style to indicate a failed command.
     */
    private void setStyleToIndicateCommandFailure() {
        ObservableList<String> styleClass = commandTextField.getStyleClass();

        if (styleClass.contains(ERROR_STYLE_CLASS)) {
            return;
        }

        styleClass.add(ERROR_STYLE_CLASS);
    }

    /**
     * Represents a function that can execute commands.
     */
    @FunctionalInterface
    public interface CommandExecutor {
        /**
         * Executes the command and returns the result.
         *
         * @see seedu.address.logic.Logic#execute(String)
         */
        CommandResult execute(String commandText) throws CommandException, ParseException;
    }

}
