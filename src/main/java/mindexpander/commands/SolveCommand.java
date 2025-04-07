package mindexpander.commands;

import mindexpander.data.question.Question;
import mindexpander.data.QuestionBank;
import mindexpander.logging.ErrorLogger;
import mindexpander.logging.SolveAttemptLogger;

/**
 * The {@code SolveCommand} class allows the user to solve a question
 * from the {@code QuestionBank} through a multistep interaction.
 *
 * <p>This command prompts the user to:
 * <ol>
 *   <li>Submit an answer</li>
 *   <li>Retry if the answer is incorrect (optional)</li>
 * </ol>
 *
 * The command is considered complete when the user either provides a correct answer
 * or gives up on the question.
 *
 * @author Wenyi
 * @version 2.0
 * @since 2025-03-21
 */

public class SolveCommand extends Command implements Multistep {
    private enum Step { GET_INDEX, GET_ANSWER, GET_TRY_AGAIN_RESPONSE }
    private Step currentStep = Step.GET_INDEX;
    private final QuestionBank questionBank;

    private int questionIndex = -1;

    public SolveCommand(String userCommand, String taskDetails, QuestionBank questionBank) {
        isComplete = false; // Multistep command
        isUsingLastShownQuestionBank = true; // Uses the last shown one, set this to ensure correct bank is fed in
        this.questionBank = questionBank;
        String solveMessage = getQuestionIndex(userCommand, taskDetails, questionBank);
        updateCommandMessage(solveMessage);
    }


    /**
     * Handles user input at different stages of solving a question.
     *
     * <p>The method transitions through the following steps:
     * <ul>
     *   <li>Checking the user's answer</li>
     *   <li>Prompting for retry if the answer is incorrect</li>
     * </ul>
     *
     * @param nextInput The user's input.
     * @return The current {@code SolveCommand} instance to maintain state across steps.
     */
    @Override
    public Command handleMultistepCommand(String userCommand, String nextInput) {
        if (currentStep == Step.GET_ANSWER) {
            Question question = questionBank.getQuestion(questionIndex);
            boolean isCorrect = question.checkAnswer(nextInput);

            // Log the attempt
            SolveAttemptLogger.logAttempt(question, isCorrect);

            if (!isCorrect) {
                updateCommandMessage("Wrong answer, would you like to try again? [Y/N]");
                currentStep = Step.GET_TRY_AGAIN_RESPONSE;
                return this;
            }
            updateCommandMessage("Correct!");
            isComplete = true;
            return this;
        }

        if (currentStep == Step.GET_TRY_AGAIN_RESPONSE) {
            if (nextInput.trim().equalsIgnoreCase("Y")) {
                updateCommandMessage("Enter your answer to try again: ");
                currentStep = Step.GET_ANSWER;
                return this;
            }
            if (nextInput.trim().equalsIgnoreCase("N")) {
                updateCommandMessage("Giving up on question.");
                isComplete = true;
                return this;
            }
            updateCommandMessage("Please enter Y or N.");
        }

        return this;
    }

    /**
     * Validates and retrieves the question index from user input.
     *
     * <p>If the input is a valid number corresponding to a question in the
     * {@code QuestionBank}, the user is prompted to enter an answer.
     * Otherwise, an appropriate error message is returned.
     *
     * @param nextInput The user's input, expected to be a numerical index.
     * @param questionBank The question bank to fetch the question from.
     * @return A message indicating the next step (either the question prompt or an error).
     */
    private String getQuestionIndex(String userCommand, String nextInput, QuestionBank questionBank) {
        try {
            questionIndex = Integer.parseInt(nextInput.trim()) - 1; // Convert to 0-based index

            if (questionBank.getQuestionCount() == 0) {
                isComplete = true;
                ErrorLogger.logError(userCommand, "Question bank is empty. Please add a question first.");
                return "Question bank is empty. Please add a question first.";
            }

            if (questionIndex < 0 || questionIndex >= questionBank.getQuestionCount()) {
                isComplete = true;
                ErrorLogger.logError(userCommand, "Invalid question number. Please enter a valid index.");
                return "Invalid question number. Please enter a valid index.";
            }

            currentStep = Step.GET_ANSWER;
            String questionDetails = questionBank.getQuestion(questionIndex).toStringNoAnswer();
            assert questionDetails != null : "There should be some question details.";
            return "Attempting question " + (questionIndex + 1) + ": " + questionDetails + "\nEnter your answer:";
        } catch (NumberFormatException e) {
            isComplete = true;
            ErrorLogger.logError(userCommand, "Invalid question number. Please enter a valid index.");
            return "Invalid question number. Please enter a valid index." ;
        }
    }
}
