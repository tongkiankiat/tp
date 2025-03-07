package mindexpander.parser;

import mindexpander.data.QuestionBank;
// Commands
import mindexpander.commands.Command;
import mindexpander.commands.EditQuestion;
import mindexpander.commands.Help;
import mindexpander.commands.AddQuestion;
import mindexpander.commands.List;
import mindexpander.commands.SolveQuestion;
// Exceptions
import mindexpander.exceptions.IllegalCommandException;

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

    /**
     * Parses the user's input command and returns the appropriate {@code CommandHandler} object.
     *
     * <p>This method uses a switch expression to return the corresponding command object.</p>
     *
     * @param userEntry The full command input from the user.
     * @param questionBank The {@code QuestionBank} that contains all tasks.
     * @return The appropriate {@code CommandHandler} object based on the command.
     * @throws IllegalCommandException If the command is invalid or unrecognized.
     */
    public Command parseCommand (String userEntry, QuestionBank questionBank) throws IllegalCommandException {
        // Split into commands and details of task
        String[] stringParts = userEntry.split(" ", 2);
        String userCommand = stringParts[0];
        String taskDetails = stringParts.length > 1? stringParts[1] : "";

        String ILLEGAL_COMMAND_MESSAGE = """
                Please enter a valid command.
                Use 'help' for a list of commands.
                """;

        // Handle commands
        return switch (userCommand.toLowerCase()) {
            case "help" -> new Help();
            case "add" -> new AddQuestion();
            case "list" -> new List();
            case "edit" -> new EditQuestion();
            case "solve" -> new SolveQuestion();
            // case "edit" -> new MarkCommand(userCommand.toLowerCase(), stringParts, taskDetails, taskList);
            default -> throw new IllegalCommandException(ILLEGAL_COMMAND_MESSAGE);
        };
    }
}

