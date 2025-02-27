package MindExpander;

import java.util.List;

class MultipleChoice extends Question {
    private List<String> options;

    public MultipleChoice(String question, String answer, List<String> options) {
        super(question, answer);
        this.options = options;
    }

    @Override
    public boolean checkAnswer(String userAnswer) {
        return answer.equalsIgnoreCase(userAnswer.trim());
    }

    @Override
    public String toString() {
        return "MCQ: " + question + " Options: " + options + " [Answer: " + answer + "]";
    }
}