package mindexpander.commands;

import mindexpander.data.CommandHistory;
import mindexpander.data.QuestionBank;
import mindexpander.data.question.Question;
import mindexpander.logging.ErrorLogger;

import java.util.ArrayList;
import java.util.List;

/**
 * Clears all questions from the question bank.
 * Uses multistep confirmation (Y/N).
 */
public class ClearCommand extends Command implements Traceable, Multistep {
    private final QuestionBank mainBank;
    private final CommandHistory commandHistory;
    private final List<Question> backupQuestions = new ArrayList<>();
    private boolean awaitingConfirmation = true;

    public ClearCommand(QuestionBank mainBank, CommandHistory commandHistory) {
        this.mainBank = mainBank;
        this.commandHistory = commandHistory;
        this.isComplete = false;
        updateCommandMessage("Are you sure you want to clear the entire question bank? (Y/N)");
    }

    @Override
    public Command handleMultistepCommand(String userCommand, String userInput) {
        String input = userInput.trim().toLowerCase();
        if (awaitingConfirmation) {
            switch (input) {
            case "y":
                backupQuestions.addAll(mainBank.getAllQuestions());
                mainBank.clear();
                commandHistory.add(this);
                updateCommandMessage("All questions have been cleared.");
                isComplete = true;
                break;
            case "n":
                updateCommandMessage("Clear command cancelled.");
                isComplete = true;
                break;
            default:
                ErrorLogger.logError(userCommand, "Invalid input. Please enter 'Y' or 'N':");
                updateCommandMessage("Invalid input. Please enter 'Y' or 'N':");
                break;
            }
        }
        return this;
    }

    @Override
    public void undo() {
        for (Question q : backupQuestions) {
            mainBank.addQuestion(q);
        }
    }

    @Override
    public void redo() {
        mainBank.clear();
    }

    public String undoMessage() {
        return "Cleared questions have been restored.";
    }

    public String redoMessage() {
        return "All questions have been cleared again.";
    }
}
