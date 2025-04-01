package mindexpander.parser;

// Commands
import mindexpander.commands.Command;
import mindexpander.commands.AddCommand;
import mindexpander.commands.DeleteCommand;
import mindexpander.commands.HelpCommand;
import mindexpander.commands.ListCommand;
import mindexpander.commands.ExitCommand;
import mindexpander.commands.SolveCommand;
import mindexpander.commands.FindCommand;

// Exceptions
import mindexpander.exceptions.IllegalCommandException;

import mindexpander.data.QuestionBank;
import mindexpander.common.Messages;

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
    public Command parseCommand (String userEntry, QuestionBank questionBank, QuestionBank lastShownQuestionBank)
            throws IllegalCommandException {
        if (ongoingCommand != null && !ongoingCommand.isCommandComplete()) {
            // Continue processing the ongoing multistep command
            return manageMultistepCommand(userEntry, questionBank, lastShownQuestionBank);
        }

        // Split into commands and details of task
        String[] stringParts = userEntry.split(" ", 2);
        String userCommand = stringParts[0];
        String taskDetails = stringParts.length > 1? stringParts[1] : "";

        // Handle commands
        return switch (userCommand.toLowerCase()) {
        case "help" -> new HelpCommand(taskDetails);
        case "exit" -> new ExitCommand();
        case "solve" -> handleSolve(taskDetails, lastShownQuestionBank);
        case "add" -> new AddCommand();
        case "list" -> {
            if (taskDetails.trim().equalsIgnoreCase("answer")) {
                yield new ListCommand(questionBank, true);
            } else if (taskDetails.trim().isEmpty()) {
                yield new ListCommand(questionBank, false);
            } else {
                throw new IllegalCommandException(Messages.UNKNOWN_COMMAND_MESSAGE);
            }
        }
        case "find" -> {
            if (taskDetails.trim().isEmpty()) {
                throw new IllegalCommandException(Messages.UNKNOWN_COMMAND_MESSAGE);
            }

            String[] parts = taskDetails.trim().split("\\s+", 2);
            String questionType;
            String keyword;
            if (parts.length == 2
                    && (parts[0].equalsIgnoreCase("mcq")
                    || parts[0].equalsIgnoreCase("fitb"))) {
                questionType = parts[0].toLowerCase();
                keyword = parts[1];
            } else if (parts.length == 1) {
                if (parts[0].equalsIgnoreCase("mcq") || parts[0].equalsIgnoreCase("fitb")) {
                    throw new IllegalCommandException(Messages.UNKNOWN_COMMAND_MESSAGE);
                }
                questionType = "all";
                keyword = taskDetails;
            } else {
                throw new IllegalCommandException(Messages.UNKNOWN_COMMAND_MESSAGE);
            }
            yield new FindCommand(questionBank, questionType, keyword);
        }
        case "delete" -> DeleteCommand.parseFromUserInput(taskDetails, questionBank, lastShownQuestionBank);
        default -> throw new IllegalCommandException(Messages.UNKNOWN_COMMAND_MESSAGE);
        };
    }

    /**
     * Feeds in the userEntry and the correct question bank to the ongoing command.
     *
     * @param userEntry the user entry to feed into the ongoing command
     * @param questionBank the full question bank for commands that need it
     * @param lastShownQuestionBank the last shown question bank for commands that need it
     * @return the ongoing command to run again
     */
    protected Command manageMultistepCommand(String userEntry, QuestionBank questionBank,
        QuestionBank lastShownQuestionBank) {
        if (ongoingCommand.isUsingLastShownQuestionBank()) {
            return ongoingCommand.handleMultistepCommand(userEntry, lastShownQuestionBank);
        }
        return ongoingCommand.handleMultistepCommand(userEntry, questionBank);
    }

    /**
     * Handles the solve command by choosing the one-step or multistep version.
     *
     * @param taskDetails the user input string after the solve command.
     * @param lastShownQuestionBank the last shown question bank.
     * @return either the multistep or one-step version of the solve command.
     */
    protected Command handleSolve(String taskDetails, QuestionBank lastShownQuestionBank) {
        if (taskDetails.isEmpty()) {
            throw new IllegalCommandException("Invalid format. Use the format 'solve [QUESTION_INDEX]'");
        }
        ongoingCommand = new SolveCommand(taskDetails, lastShownQuestionBank);
        return ongoingCommand;
    }
}
