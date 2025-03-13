package mindexpander.parser;

import mindexpander.commands.ExitCommand;

// Commands
import mindexpander.commands.Command;
import mindexpander.commands.HelpCommand;
import mindexpander.commands.AddCommand;

// Exceptions
import mindexpander.commands.SolveCommand;
import mindexpander.data.question.FillInTheBlanks;
import mindexpander.exceptions.IllegalCommandException;

import mindexpander.data.QuestionBank;

import mindexpander.common.Messages;
import mindexpander.storage.StorageFile;

/**
 * The {@code Parser} class is responsible for interpreting user commands
 * and returning the corresponding {@code CommandHandler} object.
 *
 * <p>This class processes user input, extracts the command, and determines
 * the appropriate action by creating and returning a relevant command object.</p>
 *
 * @author Wenyi
 * @version 1.0
 * @since 2025-03-06
 */
public class Parser {
    private Command ongoingCommand = null; // Tracks multistep command

    /**
     * Parses the user's input command and returns the appropriate {@code CommandHandler} object.
     *
     * <p>This method uses a switch expression to return the corresponding command object.</p>
     *
     * @param userEntry The full command input from the user.
     * @return The appropriate {@code CommandHandler} object based on the command.
     * @throws IllegalCommandException If the command is invalid or unrecognized.
     */
    public Command parseCommand (String userEntry, QuestionBank questionBank, StorageFile storage)
            throws IllegalCommandException {
        if (ongoingCommand != null && !ongoingCommand.isCommandComplete()) {
            // Continue processing the ongoing multistep command
            return ongoingCommand.handleMultistepCommand(userEntry, questionBank);
        }

        // Split into commands and details of task
        String[] stringParts = userEntry.split(" ", 2);
        String userCommand = stringParts[0];
        String taskDetails = stringParts.length > 1? stringParts[1] : "";

        // Handle commands
        return switch (userCommand.toLowerCase()) {
        case "help" -> new HelpCommand();
        case "exit" -> new ExitCommand();
        case "solve" -> {
            ongoingCommand = new SolveCommand();
            yield ongoingCommand;
        }
        case "add" -> new AddCommand(storage);

        default -> throw new IllegalCommandException(Messages.UNKNOWN_COMMAND_MESSAGE);
        };
    }
}
