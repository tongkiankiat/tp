package mindexpander;

import mindexpander.commands.CommandResult;
import mindexpander.data.CommandHistory;
import mindexpander.parser.Parser;

import mindexpander.data.QuestionBank;
import mindexpander.exceptions.IllegalCommandException;
import mindexpander.storage.StorageFile;
import mindexpander.ui.TextUi;

import mindexpander.commands.Command;

/**
 * Entry point of Mindexpander application.
 * Initialises the application and starts the interaction with the user.
 */
public class Main {
    // Attributes
    private QuestionBank questionBank;
    private QuestionBank lastShownQuestionBank;
    private StorageFile storage;
    private TextUi ui;
    private CommandHistory commandHistory;

    // Constructor
    public static void main(String[] args) {
        new Main().run();
    }

    // Methods
    // Run MindExpander
    public void run() {
        start();
        runUserCommandUntilTermination(questionBank);
    }

    // Start function: Instantiates the TextUI class
    private void start() {
        try {
            // Initialise main business components
            this.ui = new TextUi();
            this.storage = new StorageFile();
            this.questionBank = storage.load();
            this.lastShownQuestionBank = questionBank;
            this.commandHistory = new CommandHistory();
        } catch (Exception e) {
            ui.printInitFailedMessage();
        }
    }

    // Runs the program until user enters 'exit'
    private void runUserCommandUntilTermination(QuestionBank questionBank) {
        // New command variable
        Command command;
        // Temporary state for exit, before adding in command class
        boolean isRunning = true;

        do {
            String userCommand = ui.getUserCommand();

            try {
                command = new Parser().parseCommand(userCommand, questionBank, lastShownQuestionBank, commandHistory);
                CommandResult commandResult = command.execute();
                recordResult(commandResult);
                storage.save(questionBank);
                ui.displayResults(commandResult);

                while (!command.isCommandComplete()) {
                    String input = ui.nextLine();
                    userCommand += " " + input;
                    command.handleMultistepCommand(userCommand, input);
                    storage.save(questionBank);
                    ui.displayResults(command.execute());
                }

                isRunning = command.keepProgramRunning();
            } catch (IllegalCommandException e) {
                ui.printToUser(e.getMessage());
            }

        } while (isRunning);
    }

    // Records the last shown list
    private void recordResult(CommandResult commandResult) {
        final QuestionBank questionBank = commandResult.getQuestionBank();
        boolean updateLastShownQuestionBank = commandResult.updateLastShownQuestionBank;
        if (!questionBank.isEmpty() && updateLastShownQuestionBank) {
            lastShownQuestionBank = questionBank;
        }
    }

}
