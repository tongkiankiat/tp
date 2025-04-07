package mindexpander.commands;

import mindexpander.data.CommandHistory;
import mindexpander.common.InputValidator;
import mindexpander.data.QuestionBank;
import mindexpander.data.question.FillInTheBlanks;
import mindexpander.data.question.MultipleChoice;
import mindexpander.data.question.Question;
import mindexpander.data.question.QuestionType;
import mindexpander.data.question.TrueFalse;
import mindexpander.exceptions.IllegalCommandException;
import mindexpander.logging.ErrorLogger;
import mindexpander.logging.QuestionLogger;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a command to add a new question to the question bank.
 * This command is multistep, prompting the user for the question type,
 * question text, answer, and additional data based on the type (e.g., options for MCQ).
 */
public class AddCommand extends Command implements Multistep, Traceable {
    private Question toAdd;
    private Question toAddOriginalState;
    private final int toAddIndex;
    private QuestionType type;
    private String question;
    private String answer;
    private final QuestionBank questionBank;
    private final CommandHistory commandHistory;

    private enum Step {GET_TYPE, GET_QUESTION, GET_ANSWER, GENERATE_QUESTION}

    private AddCommand.Step currentStep = AddCommand.Step.GET_TYPE;
    private int multipleChoiceOptionNumber = 0;
    private final List<String> options;

    /**
     * Constructs an {@code AddCommand} and initiates the first step
     * of the multistep process by prompting the user to enter a question type.
     */
    public AddCommand(QuestionBank questionBank, CommandHistory commandHistory) {
        isComplete = false; // Multistep command
        options = new ArrayList<>();
        this.questionBank = questionBank;
        this.commandHistory = commandHistory;
        this.toAddIndex = questionBank.getQuestionCount();
        updateCommandMessage(initialiseAddCommand());
    }

    public String initialiseAddCommand() {
        return String.format("Please enter the type of the question you would like to add. (%1$s)",
                QuestionType.allTypes());
    }

    /**
     * Handles each step of the multistep add question process based on the current input.
     *
     * @param nextInput the user input for the current step.
     * @return the updated {@code Command} instance representing the current step.
     */
    @Override
    public Command handleMultistepCommand(String userCommand, String nextInput) {
        if (nextInput.trim().equals("")) {
            ErrorLogger.logError(userCommand, "Input cannot be empty!");
            updateCommandMessage("Input cannot be empty!");
            return this;
        }

        if (currentStep == Step.GET_TYPE) {
            getQuestionType(userCommand, nextInput);
            return this;
        }

        try {
            InputValidator.validateInput(nextInput);
        } catch (IllegalCommandException e) {
            switch (currentStep) {
            case GET_QUESTION:
                ErrorLogger.logError(userCommand,
                        "Input cannot contain the reserved delimiter string! Please enter a new question:");
                updateCommandMessage(
                        "Input cannot contain the reserved delimiter string! Please enter a new question:"
                );
                break;
            case GET_ANSWER:
                ErrorLogger.logError(userCommand,
                        "Input cannot contain the reserved delimiter string! Please enter a new answer:");
                updateCommandMessage(
                        "Input cannot contain the reserved delimiter string! Please enter a new answer:"
                );
                break;
            case GENERATE_QUESTION:
                ErrorLogger.logError(userCommand,
                        "Input cannot contain the reserved delimiter string! Please enter a new option:");
                updateCommandMessage(
                        "Input cannot contain the reserved delimiter string! Please enter a new option:"
                );
                break;
            default:
                ErrorLogger.logError(userCommand, e.getMessage());
                updateCommandMessage(e.getMessage()); // fallback
            }
            return this;
        }

        if (currentStep == Step.GET_QUESTION) {
            this.question = nextInput.trim();
            for (Question q : questionBank.getAllQuestions()) {
                if (q.getQuestion().equalsIgnoreCase(question)) {
                    ErrorLogger.logError(userCommand, "Question already exists. Terminating add command.");
                    updateCommandMessage("Question already exists. Terminating add command.");
                    isComplete = true;
                    return this;
                }
            }
            currentStep = Step.GET_ANSWER;
            updateCommandMessage("Enter the answer:");
            return this;
        }

        if (currentStep == Step.GET_ANSWER) {
            this.answer = nextInput.trim();
            currentStep = Step.GENERATE_QUESTION;
            commandHistory.add(this);
        }

        return addQuestionHandler(userCommand, nextInput, questionBank);
    }

    /**
     * Validates and sets the question type based on user input.
     *
     * @param nextInput the user input representing the question type.
     */
    private void getQuestionType(String userCommand, String nextInput) {
        String trimmedInput = nextInput.trim();
        if (!QuestionType.isValidType(trimmedInput)) {
            ErrorLogger.logError(userCommand,
                    String.format("Invalid input. Please enter a correct question type. (%1$s)",
                            QuestionType.allTypes()));
            updateCommandMessage(String.format("Invalid input. Please enter a correct question type. (%1$s)",
                    QuestionType.allTypes()));
        } else {
            this.type = QuestionType.fromString(trimmedInput);
            currentStep = Step.GET_QUESTION;
            updateCommandMessage("Enter your question:");
        }
    }

    /**
     * Routes to the appropriate method based on the question type.
     *
     * @param nextInput the user input from the final step.
     * @param questionBank the {@code QuestionBank} to which the question is added.
     * @return the updated {@code Command} instance.
     */
    private Command addQuestionHandler(String userCommand, String nextInput, QuestionBank questionBank) {
        if (this.type == QuestionType.FITB) {
            return addFillInTheBlank(questionBank);
        } else if (this.type == QuestionType.MCQ) {
            return addMultipleChoice(userCommand, nextInput, questionBank);
        } else if (this.type == QuestionType.TF) {
            return addTrueFalse(userCommand, questionBank);
        }
        return this;
    }

    /**
     * Adds a Fill-In-The-Blank question to the question bank.
     *
     * @param questionBank the {@code QuestionBank} to add to.
     * @return the updated {@code Command} instance.
     */
    private Command addFillInTheBlank(QuestionBank questionBank) {
        toAdd = new FillInTheBlanks(question, answer);
        toAddOriginalState = new FillInTheBlanks((FillInTheBlanks) toAdd);
        questionBank.addQuestion(toAdd);
        QuestionLogger.logAddedQuestion(toAdd);
        updateCommandMessage(String.format("Question %1$s successfully added.", toAdd.toString()));
        isComplete = true;
        return this;
    }

    /**
     * Handles the step-by-step collection of multiple choice options and
     * creates a new {@code MultipleChoice} question when complete.
     *
     * @param nextInput the user input for an incorrect option.
     * @param questionBank the {@code QuestionBank} to add to.
     * @return the updated {@code Command} instance.
     */
    private Command addMultipleChoice(String userCommand, String nextInput, QuestionBank questionBank) {
        if (options.contains(nextInput)) {
            ErrorLogger.logError(userCommand,
                    "Invalid input: Duplicate options are not allowed in multiple choice questions.");
            updateCommandMessage("Invalid input: Duplicate options are not allowed in multiple choice questions.");
            return this;
        }

        if (multipleChoiceOptionNumber == 0) {
            options.add(answer);
            updateCommandMessage("Enter 3 incorrect options (1/3):");
            multipleChoiceOptionNumber += 1;
            return this;
        }

        options.add(nextInput);
        multipleChoiceOptionNumber += 1;
        updateCommandMessage(String.format("Enter 3 incorrect options (%1$d/3):", multipleChoiceOptionNumber));
        if (multipleChoiceOptionNumber <= 3) {
            return this;
        }

        toAdd = new MultipleChoice(question, answer, options);
        toAddOriginalState = new MultipleChoice((MultipleChoice)toAdd);
        questionBank.addQuestion(toAdd);
        QuestionLogger.logAddedQuestion(toAdd);
        updateCommandMessage(String.format("Question %1$s successfully added.", toAdd.toString()));
        isComplete = true;
        return this;
    }

    /**
     * Adds a True/False question after validating the answer input.
     *
     * @param questionBank the {@code QuestionBank} to add to.
     * @return the updated {@code Command} instance.
     */
    private Command addTrueFalse(String userCommand, QuestionBank questionBank) {
        String answerLower = answer.toLowerCase().trim();
        if (!answerLower.equals("true") && !answerLower.equals("false")) {
            ErrorLogger.logError(userCommand, "Invalid answer. Please enter 'true' or 'false'.");
            updateCommandMessage("Invalid answer. Please enter 'true' or 'false'.");
            currentStep = Step.GET_ANSWER; // Forces user to re-enter
            return this;
        }
        toAdd = new TrueFalse(question, answerLower);
        toAddOriginalState = new TrueFalse((TrueFalse)toAdd);
        questionBank.addQuestion(toAdd);
        QuestionLogger.logAddedQuestion(toAdd);
        updateCommandMessage(String.format("Question %1$s successfully added.", toAdd.toString()));
        isComplete = true;
        return this;
    }

    public void undo() {
        int indexToDelete = questionBank.findQuestionIndex(toAddOriginalState);
        questionBank.removeQuestion(indexToDelete);
    }

    public String undoMessage() {
        return String.format("%1$s successfully deleted.", toAddOriginalState);
    }

    public void redo() {
        questionBank.addQuestionAt(toAddIndex, toAddOriginalState);
    }

    public String redoMessage() {
        return String.format("%1$s successfully added.", toAddOriginalState);
    }
}
