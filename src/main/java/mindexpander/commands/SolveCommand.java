package mindexpander.commands;

import mindexpander.data.question.Question;

import mindexpander.data.QuestionBank;

/**
 * The {@code SolveCommand} class allows the user to solve a question
 * from the {@code QuestionBank} through a multistep interaction.
 *
 * <p>This command prompts the user to:
 * <ol>
 *   <li>Enter a question number to attempt</li>
 *   <li>Submit an answer</li>
 *   <li>Retry if the answer is incorrect (optional)</li>
 * </ol>
 *
 * The command is considered complete when the user either provides a correct answer
 * or gives up on the question.
 *
 * @author Wenyi
 * @version 1.1
 * @since 2025-03-21
 */
public class SolveCommand extends Command {
    private enum Step { GET_INDEX, GET_ANSWER, GET_TRY_AGAIN_RESPONSE }
    private Step currentStep = Step.GET_INDEX;
    private int questionIndex = -1;

    public SolveCommand() {
        isComplete = false; // Multistep command
        isUsingLastShownQuestionBank = true; // Uses the last shown one
        updateCommandMessage("Please enter the question number you would like to solve.");
    }

    /**
     * Handles user input at different stages of solving a question.
     *
     * <p>The method transitions through the following steps:
     * <ul>
     *   <li>Retrieving a valid question index</li>
     *   <li>Checking the user's answer</li>
     *   <li>Prompting for retry if the answer is incorrect</li>
     * </ul>
     *
     * @param nextInput The user's input.
     * @param questionBank The question bank containing all available questions.
     * @return The current {@code SolveCommand} instance to maintain state across steps.
     */
    @Override
    public Command handleMultistepCommand(String nextInput, QuestionBank questionBank) {
        switch (currentStep) {
        case GET_INDEX:
            updateCommandMessage(getQuestionIndex(nextInput, questionBank));
            return this; // Stay in SolveCommand

        case GET_ANSWER:
            Question question = questionBank.getQuestion(questionIndex);
            String correctAnswer = question.getAnswer();

            if (!nextInput.trim().equalsIgnoreCase(correctAnswer.trim())) {
                updateCommandMessage("Wrong answer, would you like to try again? [Y/N]");
                currentStep = Step.GET_TRY_AGAIN_RESPONSE;
                return this; // Try again
            }
            updateCommandMessage("Correct!");
            isComplete = true;
            return this; // Exit multi-step mode
        case GET_TRY_AGAIN_RESPONSE:
            if (nextInput.trim().equalsIgnoreCase("Y")) {
                updateCommandMessage("Enter your answer to try again: ");
                currentStep = Step.GET_ANSWER;
                return this; // Try again
            }
            if (nextInput.trim().equalsIgnoreCase("N")) {
                updateCommandMessage("Giving up on question.");
                isComplete = true;
                return this;
            }
            updateCommandMessage("Please enter Y or N.");
            return this;

        default:
            return this; // Default return, should not reach here
        }
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
    private String getQuestionIndex(String nextInput, QuestionBank questionBank) {
        try {
            questionIndex = Integer.parseInt(nextInput) - 1; // Convert to 0-based index

            if (questionIndex < 0 || questionIndex >= questionBank.getQuestionCount()) {
                return "Invalid question number. Please enter a valid index.";
            }

            currentStep = Step.GET_ANSWER;
            String questionDetails = questionBank.getQuestion(questionIndex).getQuestion();
            assert questionDetails != null : "There should be some question details.";
            return "Attempting question " + (questionIndex + 1) + ": " + questionDetails + "\nEnter your answer:";
        } catch (NumberFormatException e) {
            return "Invalid input. Please enter a number." ;
        }
    }
}
