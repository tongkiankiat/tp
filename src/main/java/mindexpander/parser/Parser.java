package mindexpander.parser;

// Commands
import mindexpander.commands.HelpCommand;
import mindexpander.commands.ListCommand;
import mindexpander.commands.ExitCommand;
import mindexpander.commands.RedoCommand;
import mindexpander.commands.AddCommand;
import mindexpander.commands.EditCommand;
import mindexpander.commands.SolveCommand;
import mindexpander.commands.ShowCommand;
import mindexpander.commands.FindCommand;
import mindexpander.commands.Command;
import mindexpander.commands.DeleteCommand;
import mindexpander.commands.ClearCommand;

import mindexpander.commands.UndoCommand;
import mindexpander.data.CommandHistory;
import mindexpander.exceptions.IllegalCommandException;

import mindexpander.data.QuestionBank;
import mindexpander.common.Messages;
import mindexpander.logging.ErrorLogger;

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
    public Command parseCommand (String userEntry, QuestionBank questionBank,
                                 QuestionBank lastShownQuestionBank, CommandHistory commandHistory)
            throws IllegalCommandException {

        // Split into commands and details of task
        String[] stringParts = userEntry.split(" ", 2);
        String userCommand = stringParts[0];
        String taskDetails = stringParts.length > 1? stringParts[1] : "";

        // Handle commands
        return switch (userCommand.toLowerCase()) {
        case "help" -> new HelpCommand(taskDetails);
        case "exit" -> handleExit(userEntry, taskDetails);
        case "solve" -> handleSolve(userEntry, taskDetails, lastShownQuestionBank);
        case "add" -> handleAdd(userEntry, taskDetails, questionBank, commandHistory);
        case "list" -> handleList(userEntry, taskDetails, questionBank);
        case "find" -> handleFind(userEntry, taskDetails, questionBank);
        case "edit" -> handleEdit(userEntry, taskDetails, questionBank, lastShownQuestionBank, commandHistory);
        case "delete" -> handleDelete(userEntry, taskDetails, questionBank, lastShownQuestionBank, commandHistory);
        case "undo" -> handleUndo(userEntry, taskDetails, commandHistory);
        case "redo" -> handleRedo(userEntry, taskDetails, commandHistory);
        case "show" -> handleShow(userEntry, taskDetails, questionBank, lastShownQuestionBank);
        case "clear" -> handleClear(userEntry, taskDetails, questionBank, commandHistory);
        default -> {
            ErrorLogger.logError(userEntry, Messages.UNKNOWN_COMMAND_MESSAGE);
            throw new IllegalCommandException(Messages.UNKNOWN_COMMAND_MESSAGE);
        }
        };
    }

    private Command handleDelete(String userEntry, String taskDetails, QuestionBank questionBank,
                                 QuestionBank lastShownQuestionBank, CommandHistory commandHistory) {
        if (taskDetails.isEmpty()) {
            ErrorLogger.logError(userEntry, "Please provide a question index to delete.");
            throw new IllegalCommandException("Please provide a question index to delete.");
        }
        try {
            int index = Integer.parseInt(taskDetails.trim());
            return new DeleteCommand(index, questionBank, lastShownQuestionBank, commandHistory);
        } catch (NumberFormatException e) {
            ErrorLogger.logError(userEntry, "Invalid number format. Please enter a valid index.");
            throw new IllegalCommandException("Invalid number format. Please enter a valid index.");
        }
    }

    private Command handleClear(String userEntry, String taskDetails, QuestionBank questionBank,
                                CommandHistory commandHistory) {
        if (!taskDetails.trim().isEmpty()) {
            ErrorLogger.logError(userEntry, "The clear command does not take additional parameters.");
            throw new IllegalCommandException("The clear command does not take additional parameters.");
        }
        return new ClearCommand(questionBank, commandHistory);
    }

    private Command handleExit(String userEntry, String taskDetails) {
        if (!taskDetails.isEmpty()) {
            ErrorLogger.logError(userEntry, "Invalid format. Use 'exit' without extra parameters");
            throw new IllegalCommandException("Invalid format. Use 'exit' without extra parameters");
        }
        return new ExitCommand();
    }

    private Command handleUndo(String userEntry, String taskDetails, CommandHistory commandHistory) {
        if (!taskDetails.isEmpty()) {
            ErrorLogger.logError(userEntry, "Invalid format. Use 'undo' without extra parameters");
            throw new IllegalCommandException("Invalid format. Use 'undo' without extra parameters");
        }
        return new UndoCommand(commandHistory);
    }

    private Command handleRedo(String userEntry, String taskDetails, CommandHistory commandHistory) {
        if (!taskDetails.isEmpty()) {
            ErrorLogger.logError(userEntry, "Invalid format. Use 'redo' without extra parameters");
            throw new IllegalCommandException("Invalid format. Use 'redo' without extra parameters");
        }
        return new RedoCommand(commandHistory);
    }

    private Command handleAdd(String userEntry, String taskDetails, QuestionBank questionBank,
                              CommandHistory commandHistory) {
        if (!taskDetails.isEmpty()) {
            ErrorLogger.logError(userEntry, "Invalid format. Use 'add' without extra parameters");
            throw new IllegalCommandException("Invalid format. Use 'add' without extra parameters");
        }
        return new AddCommand(questionBank, commandHistory);
    }

    /**
     * Handles the multistep solve command.
     *
     * @param taskDetails the user input string after the solve command.
     * @param lastShownQuestionBank the last shown question bank.
     * @return the solve command.
     */
    protected Command handleSolve(String userEntry, String taskDetails, QuestionBank lastShownQuestionBank) {
        if (taskDetails.isEmpty()) {
            ErrorLogger.logError(userEntry, "Invalid format. Use the format `solve [QUESTION_INDEX]`");
            throw new IllegalCommandException("Invalid format. Use the format 'solve [QUESTION_INDEX]'");
        }
        ongoingCommand = new SolveCommand(userEntry, taskDetails, lastShownQuestionBank);
        return ongoingCommand;
    }

    /**
     * Handles the edit multistep command.
     *
     * @param taskDetails the user input string after the solve command.
     * @param questionBank the main question bank.
     * @param lastShownQuestionBank the last shown question bank.
     * @return the edit command.
     */
    private Command handleEdit(
            String userEntry,
            String taskDetails,
            QuestionBank questionBank,
            QuestionBank lastShownQuestionBank,
            CommandHistory commandHistory
    ) {
        try {
            String[] commandArguments = taskDetails.trim().split(" ", 2);

            if (commandArguments.length < 2) {
                ErrorLogger.logError(userEntry, "Invalid format. Please use edit [QUESTION_IDEX] [q/a/o]" +
                        "\n" + "'q' - question content" +
                        "\n" + "'a' - answer" +
                        "\n" + "'o' for multiple choice options.");
                throw new IllegalCommandException("Invalid format. Please use edit [QUESTION_IDEX] [q/a/o]" +
                        "\n" + "'q' - question content" +
                        "\n" + "'a' - answer" +
                        "\n" + "'o' for multiple choice options.");
            }

            int indexToEdit = Integer.parseInt(commandArguments[0].trim());
            String toEdit = commandArguments[1].trim();

            if (indexToEdit < 1 || indexToEdit > lastShownQuestionBank.getQuestionCount()) {
                ErrorLogger.logError(userEntry, "Invalid question index. Please enter a valid index.");
                throw new IllegalCommandException("Invalid question index. Please enter a valid index.");
            }
            return new EditCommand(userEntry, indexToEdit, toEdit, questionBank, lastShownQuestionBank, commandHistory);
        } catch (NumberFormatException e) {
            ErrorLogger.logError(userEntry, "Invalid number. Please enter a valid number.");
            throw new IllegalCommandException("Invalid number. Please enter a valid number.");
        }
    }

    // Helper function for handleList and handleShow
    private boolean isValidQuestionType(String questionType) {
        return questionType.equalsIgnoreCase("mcq")
                || questionType.equalsIgnoreCase("fitb")
                || questionType.equalsIgnoreCase("tf");
    }

    private Command handleList(String userEntry, String taskDetails, QuestionBank questionBank) {
        if (taskDetails.trim().isEmpty()) {
            return new ListCommand(questionBank, "all", false);
        }

        String[] parts = taskDetails.trim().split(" ");
        String questionType = "";
        boolean showAnswer = false;

        if (parts.length == 2) {
            if (parts[1].equalsIgnoreCase("answer") && isValidQuestionType(parts[0])) {
                questionType = parts[0].toLowerCase();
                showAnswer = true;
            } else {
                ErrorLogger.logError(userEntry, Messages.UNKNOWN_COMMAND_MESSAGE);
                throw new IllegalCommandException(Messages.UNKNOWN_COMMAND_MESSAGE);
            }
        } else if (parts.length == 1) {
            if (isValidQuestionType(parts[0])) {
                questionType = parts[0].toLowerCase();
            } else if (parts[0].equalsIgnoreCase("answer")) {
                questionType = "all";
                showAnswer = true;
            } else {
                ErrorLogger.logError(userEntry, Messages.LIST_ERROR_MESSAGE);
                throw new IllegalCommandException(Messages.LIST_ERROR_MESSAGE);
            }
        } else {
            ErrorLogger.logError(userEntry, Messages.LIST_ERROR_MESSAGE);
            throw new IllegalCommandException(Messages.LIST_ERROR_MESSAGE);
        }
        return new ListCommand(questionBank, questionType, showAnswer);
    }

    private Command handleFind(String userEntry, String taskDetails, QuestionBank questionBank) {
        if (taskDetails.trim().isEmpty()) {
            ErrorLogger.logError(userEntry, Messages.FIND_ERROR_MESSAGE_EMPTY_BODY);
            throw new IllegalCommandException(Messages.FIND_ERROR_MESSAGE_EMPTY_BODY);
        }

        String[] parts = taskDetails.trim().split("\\s+", 2);
        String questionType = "";
        String keyword = "";
        if (parts.length == 2 && isValidQuestionType(parts[0])) {
            questionType = parts[0].toLowerCase();
            keyword = parts[1];
        } else {
            if (parts[0].equalsIgnoreCase("mcq")) {
                ErrorLogger.logError(userEntry, Messages.FIND_ERROR_MESSAGE_EMPTY_BODY_MCQ);
                throw new IllegalCommandException(Messages.FIND_ERROR_MESSAGE_EMPTY_BODY_MCQ);
            } else if (parts[0].equalsIgnoreCase("fitb")) {
                ErrorLogger.logError(userEntry, Messages.FIND_ERROR_MESSAGE_EMPTY_BODY_FITB);
                throw new IllegalCommandException(Messages.FIND_ERROR_MESSAGE_EMPTY_BODY_FITB);
            } else if (parts[0].equalsIgnoreCase("tf")) {
                ErrorLogger.logError(userEntry, Messages.FIND_ERROR_MESSAGE_EMPTY_BODY_TF);
                throw new IllegalCommandException(Messages.FIND_ERROR_MESSAGE_EMPTY_BODY_TF);
            }
            questionType = "all";
            keyword = taskDetails;
        }
        return new FindCommand(questionBank, questionType, keyword);
    }

    // Helper function for handleShow
    private QuestionBank selectQuestionBank(String userEntry,
                                         QuestionBank questionBank,
                                         QuestionBank lastShownQuestionBank,
                                         int questionIndex) {
        if (questionBank.getQuestionCount() <= 0) {
            ErrorLogger.logError(userEntry, Messages.SHOW_ERROR_EMPTY_QUESTION_BANK);
            throw new IllegalCommandException(Messages.SHOW_ERROR_EMPTY_QUESTION_BANK);
        }
        if (questionIndex < 0) {
            ErrorLogger.logError(userEntry, Messages.showCommandOutOfRangeMessage(questionBank.getQuestionCount()));
            throw new IllegalCommandException(Messages.showCommandOutOfRangeMessage(questionBank.getQuestionCount()));
        }
        /* Search through the lastShownQuestionBank first. If the index is invalid, we search through the main
        question bank. */
        if (questionIndex >= lastShownQuestionBank.getQuestionCount() ) {
            if (questionIndex >= questionBank.getQuestionCount()) {
                ErrorLogger.logError(userEntry, Messages.showCommandOutOfRangeMessage(questionBank.getQuestionCount()));
                throw new IllegalCommandException(
                        Messages.showCommandOutOfRangeMessage(questionBank.getQuestionCount())
                );
            }
            return questionBank;
        }
        return lastShownQuestionBank;
    }

    private Command handleShow(
            String userEntry,
            String taskDetails,
            QuestionBank questionBank,
            QuestionBank lastShownQuestionBank
    ) {
        try {
            String[] parts = taskDetails.trim().split("\\s+");
            int questionIndex;

            if (parts.length == 1) {
                questionIndex = Integer.parseInt(parts[0].trim()) - 1;
                QuestionBank selectedQuestionBank = selectQuestionBank(
                        userEntry,
                        questionBank,
                        lastShownQuestionBank,
                        questionIndex
                );
                return new ShowCommand(selectedQuestionBank, questionIndex);
            } else {
                ErrorLogger.logError(userEntry, Messages.SHOW_ERROR_MESSAGE_EMPTY_BODY);
                throw new IllegalCommandException(Messages.SHOW_ERROR_MESSAGE_EMPTY_BODY);
            }
        } catch (NumberFormatException e) {
            ErrorLogger.logError(userEntry, Messages.SHOW_ERROR_NOT_INTEGER);
            throw new IllegalCommandException(Messages.SHOW_ERROR_NOT_INTEGER);
        }
    }
}
