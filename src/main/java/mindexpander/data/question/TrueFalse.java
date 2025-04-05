package mindexpander.data.question;

/**
 * Represents a True/False question.
 * Only accepts answers of "true" or "false", case-insensitive.
 */
public class TrueFalse extends Question {

    public TrueFalse(String question, String answer) {
        super(question, answer.toLowerCase(), QuestionType.TF);
    }

    @Override
    public boolean checkAnswer(String userAnswer) {
        return answer.equalsIgnoreCase(userAnswer.trim());
    }

    @Override
    public String toString() {
        return "TF: " + question + " [Answer: " + answer + "]";
    }

    @Override
    public String toStringNoAnswer() {
        return "TF: " + question + " (True/False)";
    }
}
