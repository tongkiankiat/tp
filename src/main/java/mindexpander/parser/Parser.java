package mindexpander.parser;

// Commands
import mindexpander.commands.Command;
import mindexpander.commands.AddCommand;
import mindexpander.commands.DeleteCommand;
import mindexpander.commands.EditCommand;
import mindexpander.commands.HelpCommand;
import mindexpander.commands.ListCommand;
import mindexpander.commands.ExitCommand;
import mindexpander.commands.SolveCommand;
import mindexpander.commands.FindCommand;
import mindexpander.data.question.QuestionType;

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
        case "list" -> handleList(taskDetails, questionBank);
        case "find" -> handleFind(taskDetails, questionBank);
        case "edit" -> handleEdit(taskDetails, questionBank, lastShownQuestionBank);
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

    private Command handleEdit(String taskDetails, QuestionBank questionBank, QuestionBank lastShownQuestionBank) {
        try {
            String[] commandArguments = taskDetails.split(" ", 2);

            if (commandArguments.length < 2) {
                throw new IllegalCommandException(Messages.UNKNOWN_COMMAND_MESSAGE);
            }

            int indexToEdit = Integer.parseInt(commandArguments[0]);
            String toEdit = commandArguments[1];

            if (indexToEdit < 1 || indexToEdit > lastShownQuestionBank.getQuestionCount()) {
                throw new IllegalCommandException("Invalid question index.");
            }

            switch (toEdit) {
            case "q":
                return new EditCommand(indexToEdit, "question", questionBank, lastShownQuestionBank);
            case "a":
                return new EditCommand(indexToEdit, "answer", questionBank, lastShownQuestionBank);
            case "o":
                if (lastShownQuestionBank.getQuestion(indexToEdit - 1).getType() == QuestionType.MCQ) {
                    return new EditCommand(indexToEdit, "option", questionBank, lastShownQuestionBank);
                }
                throw new IllegalCommandException("Editing options is only valid for multiple choice question.");
            default:
                throw new IllegalCommandException(Messages.UNKNOWN_COMMAND_MESSAGE);
            }
        } catch (NumberFormatException e) {
            throw new IllegalCommandException("Invalid number format. Please enter a valid index.");
        }
    }

    private Command handleList(String taskDetails, QuestionBank questionBank) {
        if (taskDetails.trim().equalsIgnoreCase("answer")) {
            return new ListCommand(questionBank, true);
        } else if (taskDetails.trim().isEmpty()) {
            return new ListCommand(questionBank, false);
        } else {
            throw new IllegalCommandException(Messages.LIST_ERROR_MESSAGE);
        }
    }

    private Command handleFind(String taskDetails, QuestionBank questionBank) throws IllegalCommandException {
        if (taskDetails.trim().isEmpty()) {
            throw new IllegalCommandException(Messages.FIND_ERROR_MESSAGE_EMPTY_BODY);
        }

        String[] parts = taskDetails.trim().split("\\s+", 2);
        String questionType = "";
        String keyword = "";
        if (parts.length == 2
                && (parts[0].equalsIgnoreCase("mcq")
                || parts[0].equalsIgnoreCase("fitb")
                || parts[0].equalsIgnoreCase("tf"))) {
            questionType = parts[0].toLowerCase();
            keyword = parts[1];
        } else {
            if (parts[0].equalsIgnoreCase("mcq")) {
                throw new IllegalCommandException(Messages.FIND_ERROR_MESSAGE_EMPTY_BODY_MCQ);
            } else if (parts[0].equalsIgnoreCase("fitb")) {
                throw new IllegalCommandException(Messages.FIND_ERROR_MESSAGE_EMPTY_BODY_FITB);
            }
            questionType = "all";
            keyword = taskDetails;
        }
        return new FindCommand(questionBank, questionType, keyword);
    }
}
