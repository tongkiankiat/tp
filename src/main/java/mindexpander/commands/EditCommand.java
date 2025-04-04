package mindexpander.commands;

import mindexpander.data.QuestionBank;
import mindexpander.data.question.MultipleChoice;
import mindexpander.data.question.Question;
import mindexpander.exceptions.IllegalCommandException;

public class EditCommand extends Command implements Multistep {
    private final int indexToEdit;
    private final String toEdit;
    private int editOptionTracker;
    private final QuestionBank mainBank;
    private final QuestionBank lastShownBank;

    public EditCommand(int indexToEdit, String toEdit, QuestionBank mainBank, QuestionBank lastShownBank) {
        this.indexToEdit = indexToEdit - 1;
        this.toEdit = toEdit;
        this.editOptionTracker = 1;
        this.mainBank = mainBank;
        this.lastShownBank = lastShownBank;
        isComplete = false;
        updateCommandMessage(String.format("Editing %1$s \n Please enter the new %2$s:",
                lastShownBank.getQuestion(this.indexToEdit), toEdit));
    }

    @Override
    public Command handleMultistepCommand(String nextInput, QuestionBank questionBank) {
        assert lastShownBank != null : "lastShownBank must not be null";

        if (nextInput.trim().equals("")) {
            updateCommandMessage(String.format("%1$s cannot be empty!", toEdit));
            return this;
        }

        int targetIndex = mainBank.findQuestionIndex(lastShownBank.getQuestion(indexToEdit));

        if (targetIndex == -1) {
            throw new IllegalCommandException("Unable to find question in main bank to delete.");
        }

        if (toEdit.equals("question")) {
            return editQuestion(targetIndex, nextInput);
        }

        if (toEdit.equals("answer")) {
            return editAnswer(targetIndex, nextInput);
        }

        return editOptions(targetIndex, nextInput);

    }

    public Command editQuestion(int targetIndex, String nextInput) {
        mainBank.getQuestion(targetIndex).editQuestion(nextInput);
        updateCommandMessage(String.format("Question successfully edited: %1$s", mainBank.getQuestion(targetIndex)));
        isComplete = true;
        return this;
    }

    public Command editAnswer(int targetIndex, String nextInput) {
        Question q = mainBank.getQuestion(targetIndex);

        if (q.getType().getType().equalsIgnoreCase("tf")) {
            String trimmedLower = nextInput.trim().toLowerCase();
            if (!trimmedLower.equals("true") && !trimmedLower.equals("false")) {
                updateCommandMessage("Invalid answer for True/False question. Please enter 'true' or 'false'.");
                return this;
            }
            q.editAnswer(trimmedLower);
        } else {
            q.editAnswer(nextInput);
        }

        updateCommandMessage(String.format("Question successfully edited: %1$s", mainBank.getQuestion(targetIndex)));
        isComplete = true;
        return this;
    }

    public Command editOptions(int targetIndex, String nextInput) {
        Question question = mainBank.getQuestion(targetIndex);
        updateCommandMessage("Please enter the next option:");
        if (question instanceof MultipleChoice mcq && editOptionTracker < 3) {
            mcq.editOption(editOptionTracker, nextInput);
            editOptionTracker += 1;
            return this;
        }
        if (question instanceof MultipleChoice mcq) {
            mcq.editOption(editOptionTracker, nextInput);
        }
        updateCommandMessage(String.format("Question successfully edited: %1$s", mainBank.getQuestion(targetIndex)));
        isComplete = true;
        return this;
    }
}
