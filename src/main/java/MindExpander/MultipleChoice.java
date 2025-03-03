package MindExpander;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class MultipleChoice extends Question {
    private final List<String> options;

    public MultipleChoice(String question, String answer, List<String> options) {
        super(question, answer);
        this.options = new ArrayList<>(options);
    }

    @Override
    public boolean checkAnswer(String userAnswer) {
        return answer.equalsIgnoreCase(userAnswer.trim());
    }

    @Override
    public String toString() {
        List<String> shuffledOptions = new ArrayList<>(options);
        Collections.shuffle(shuffledOptions); // Randomize options each time
        StringBuilder sb = new StringBuilder();
        sb.append("MCQ: ").append(question).append("\n");
        for (int i = 0; i < shuffledOptions.size(); i++) {
            sb.append((char) ('A' + i)).append(". ").append(shuffledOptions.get(i)).append("\n");
        }
        return sb.toString();
    }
}
