package mindexpander.commands;

import mindexpander.common.InputValidator;
import mindexpander.data.CommandHistory;
import mindexpander.data.QuestionBank;
import mindexpander.data.question.FillInTheBlanks;
import mindexpander.data.question.MultipleChoice;
import mindexpander.data.question.Question;
import mindexpander.data.question.QuestionType;
import mindexpander.data.question.TrueFalse;
import mindexpander.exceptions.IllegalCommandException;
import mindexpander.logging.QuestionLogger;

import static java.lang.Integer.parseInt;

/**
 * Represents a command to edit a specific question from the question bank.
 * This command supports editing the question text, answer, or individual options
 * for multiple choice questions, and is designed as a multistep command.
 */
public class EditCommand extends Command implements Multistep, Traceable {
    private final int indexToEdit;
    private String editedAttribute;
    private int optionIndex = 0;
    private Question questionToEdit;
    private Question originalQuestion;
    private final QuestionBank mainBank;
    private final QuestionBank lastShownBank;
    private final CommandHistory commandHistory;

    public EditCommand(int indexToEdit, String toEdit, QuestionBank mainBank,
                       QuestionBank lastShownBank, CommandHistory commandHistory) {
        this.indexToEdit = indexToEdit - 1;
        this.mainBank = mainBank;
        this.lastShownBank = lastShownBank;
        this.commandHistory = commandHistory;
        isComplete = false;
        initialiseQuestionRecord();
        updateCommandMessage(initialiseEditCommandMessage(toEdit));
    }

    private void initialiseQuestionRecord() {
        this.questionToEdit = lastShownBank.getQuestion(indexToEdit);
        if (questionToEdit instanceof FillInTheBlanks fitb) {
            this.originalQuestion = new FillInTheBlanks(fitb);
        }
        if (questionToEdit instanceof MultipleChoice mcq) {
            this.originalQuestion = new MultipleChoice(mcq);
        }
        if (questionToEdit instanceof TrueFalse tf) {
            this.originalQuestion = new TrueFalse(tf);
        }
    }

    private String initialiseEditCommandMessage(String toEdit) {
        if (!(toEdit.equals("q") || toEdit.equals("a") || toEdit.equals("o"))) {
            isComplete = true;
            return "Invalid format. Please use edit [QUESTION_IDEX] [q/a/o]" +
                    "\n" + "'q' - question content" +
                    "\n" + "'a' - answer" +
                    "\n" + "'o' for multiple choice options.";
        }
        if (toEdit.equals("q")) {
            this.editedAttribute = "question";
            return String.format("Editing %1$s \n Please enter the new question:", questionToEdit);
        }
        if (toEdit.equals("a")) {
            this.editedAttribute = "answer";
            return String.format("Editing %1$s \n Please enter the new answer:", questionToEdit);
        }
        if (questionToEdit instanceof MultipleChoice mcq) {
            this.editedAttribute = "option";
            return String.format("Editing %1$s \n Please enter the index of the option you want to edit: \n%2$s",
                    questionToEdit, mcq.getIncorrectOptions());
        }
        isComplete = true;
        return "Only multiple choice questions have options to modify.";
    }

    /**
     * Handles user input for each step of the editing process.
     *
     * @param nextInput the new input to apply to the specified field.
     * @return the updated {@code Command} instance.
     */
    @Override
    public Command handleMultistepCommand(String nextInput) {
        assert lastShownBank != null : "lastShownBank must not be null";

        if (nextInput.trim().isEmpty()) {
            updateCommandMessage(String.format("%1$s cannot be empty!", editedAttribute));
            return this;
        }

        try {
            InputValidator.validateInput(nextInput);
        } catch (IllegalCommandException e) {
            if (editedAttribute.equals("question")) {
                updateCommandMessage(
                        "Input cannot contain the reserved delimiter string! Please enter a new question:"
                );
            } else if (editedAttribute.equals("answer")) {
                updateCommandMessage(
                        "Input cannot contain the reserved delimiter string! Please enter a new answer:"
                );
            } else {
                updateCommandMessage(
                        "Input cannot contain the reserved delimiter string! Please enter a new option:"
                );
            }
            return this;
        }

        int targetIndex = mainBank.findQuestionIndex(lastShownBank.getQuestion(indexToEdit));

        if (targetIndex == -1) {
            throw new IllegalCommandException("Unable to find question in main bank to delete.");
        }

        if (editedAttribute.equals("question")) {
            return editQuestion(targetIndex, nextInput);
        }

        if (editedAttribute.equals("answer")) {
            return editAnswer(targetIndex, nextInput);
        }

        if (!(optionIndex > 0 && optionIndex <= 3)) {
            try {
                this.optionIndex = parseInt(nextInput);
                if (optionIndex > 3 || optionIndex < 1) {
                    updateCommandMessage("Please enter a valid index from 1 to 3.");
                    return this;
                }
                updateCommandMessage("Please enter the new option");
                return this;
            } catch (NumberFormatException e) {
                updateCommandMessage("Please enter a valid index from 1 to 3.");
                return this;
            }
        }

        return editOptions(targetIndex, optionIndex, nextInput);

    }

    /**
     * Applies the new question text to the specified question.
     *
     * @param targetIndex the index in the main question bank.
     * @param nextInput the new question text.
     * @return the updated {@code Command} instance.
     */
    public Command editQuestion(int targetIndex, String nextInput) {
        for (Question q : mainBank.getAllQuestions()) {
            if (q.getQuestion().equals(nextInput)) {
                updateCommandMessage("Question already exists. Terminating edit command.");
                isComplete = true;
                return this;
            }
        }
        mainBank.getQuestion(targetIndex).editQuestion(nextInput);
        QuestionLogger.logEditedQuestion(mainBank.getQuestion(targetIndex));
        updateCommandMessage(String.format("Question successfully edited: %1$s", mainBank.getQuestion(targetIndex)));
        commandHistory.add(this);
        isComplete = true;
        return this;
    }

    /**
     * Applies the new answer to the specified question.
     * Special validation is applied for True/False questions.
     *
     * @param targetIndex the index in the main question bank.
     * @param nextInput the new answer input.
     * @return the updated {@code Command} instance.
     */
    public Command editAnswer(int targetIndex, String nextInput) {
        Question q = mainBank.getQuestion(targetIndex);

        if (q instanceof MultipleChoice mcq) {
            if (mcq.getOptions().contains(nextInput)) {
                updateCommandMessage("Invalid input: Duplicate options are not allowed in multiple choice questions.");
                return this;
            }
        }

        if (q.getType().equals(QuestionType.TF)) {
            String trimmedLower = nextInput.trim().toLowerCase();
            if (!trimmedLower.equals("true") && !trimmedLower.equals("false")) {
                updateCommandMessage("Invalid answer for True/False question. Please enter 'true' or 'false'.");
                return this;
            }
            q.editAnswer(trimmedLower);
        } else {
            q.editAnswer(nextInput);
        }

        QuestionLogger.logEditedQuestion(q);
        updateCommandMessage(String.format("Question successfully edited: %1$s", mainBank.getQuestion(targetIndex)));
        commandHistory.add(this);
        isComplete = true;
        return this;
    }

    /**
     * Updates one of the options in a multiple choice question.
     *
     * @param targetIndex the index in the main question bank.
     * @param optionIndex the index in the target option to be edited.
     * @param nextInput the new option text.
     * @return the updated {@code Command} instance.
     */
    public Command editOptions(int targetIndex, int optionIndex, String nextInput) {
        Question question = mainBank.getQuestion(targetIndex);
        if (question instanceof MultipleChoice mcq) {
            if (mcq.getOptions().contains(nextInput)) {
                updateCommandMessage("Invalid input: Duplicate options are not allowed in multiple choice questions.");
                return this;
            }
            mcq.editOption(optionIndex, nextInput);
        }
        updateCommandMessage(String.format("Question successfully edited: %1$s",
                mainBank.getQuestion(targetIndex).toStringNoAnswer()));
        commandHistory.add(this);
        isComplete = true;
        return this;
    }

    public void undo() {
        mainBank.removeQuestion(indexToEdit);
        mainBank.addQuestionAt(indexToEdit, originalQuestion);
    }

    public String undoMessage() {
        return String.format("%1$s successfully restored.", originalQuestion);
    }

    public void redo() {
        mainBank.removeQuestion(indexToEdit);
        mainBank.addQuestionAt(indexToEdit, questionToEdit);
    }

    public String redoMessage() {
        return String.format("%1$s successfully restored.", questionToEdit);
    }
}
