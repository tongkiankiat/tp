package MindExpander.data.question;

class FillInTheBlanks extends Question {
    public FillInTheBlanks(String question, String answer) {
        super(question, answer);
    }

    @Override
    public boolean checkAnswer(String userAnswer) {
        return answer.equalsIgnoreCase(userAnswer.trim());
    }

    public String showQuestion() {
        return "FITB: " + question;
    }

    @Override
    public String toString() {
        return "FITB: " + question + " [Answer: " + answer + "]";
    }
}
