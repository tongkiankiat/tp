package mindexpander.commands;

import mindexpander.data.question.Question;
import mindexpander.data.QuestionBank;
import mindexpander.exceptions.IllegalCommandException;

/**
 * The {@code SolveCommandOneStep} class allows the user to solve a question
 * from the {@code QuestionBank} in a single step.
 *
 * <p>Expected input format:
 * <pre>
 *     /q [QUESTION_INDEX] /a [ANSWER]
 * </pre>
 * The command is considered complete after processing the input.
 *
 * @author Wenyi
 * @version 1.0
 * @since 2025-03-26
 */
public class SolveCommandOneStep extends Command {
    private int questionIndex = -1;
    private String userAnswer = "";
    private final QuestionBank questionBank;

    /**
     * Constructs a {@code SolveCommandOneStep} and parses the given input string.
     *
     * @param questionDetails A string in the format "/q [QUESTION_INDEX] /a [ANSWER]".
     */
    public SolveCommandOneStep(String questionDetails, QuestionBank questionBank) {
        isComplete = true; // Single-step command
        this.questionBank = questionBank;
        parseInput(questionDetails);
    }

    /**
     * Parses the input to extract the question index and answer.
     *
     * @param input The input string in the format "/q [QUESTION_INDEX] /a [ANSWER]".
     * @throws IllegalCommandException If the format is invalid.
     */
    private void parseInput(String input) throws IllegalCommandException {
        try {
            String[] parts = input.split("/q ");
            if (parts.length != 2 || !parts[1].contains(" /a ")) {
                throw new IllegalCommandException(
                    "Invalid command format. Use: solve /q [QUESTION_INDEX] /a [ANSWER]");
            }
            String[] subParts = parts[1].split(" /a ", 2);
            if (subParts[0].trim().isEmpty()) {
                throw new IllegalCommandException("Question index is missing. Please provide a valid index.");
            }
            if (subParts[1].trim().isEmpty()) {
                throw new IllegalCommandException("Answer is missing. Please provide an answer.");
            }
            questionIndex = Integer.parseInt(subParts[0].trim()) - 1;
            userAnswer = subParts[1].trim();
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            throw new IllegalCommandException("Invalid question number. Please enter a valid index.");
        }
    }

    /**
     * Checks the answer against the {@code QuestionBank} and updates the command message accordingly.
     */
    @Override
    public CommandResult execute() {
        if (questionIndex < 0 || questionIndex >= questionBank.getQuestionCount()) {
            throw new IllegalCommandException("Invalid question number. Please enter a valid index.");
        }

        Question question = questionBank.getQuestion(questionIndex);
        String correctAnswer = question.getAnswer();

        if (userAnswer.equalsIgnoreCase(correctAnswer.trim())) {
            return new CommandResult("Correct!");
        } else {
            return new CommandResult("Incorrect. Please try again.");
        }
    }
}

