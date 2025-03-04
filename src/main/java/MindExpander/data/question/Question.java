package MindExpander.data.question;

abstract class Question {
    protected String question;
    protected String answer;

    public Question(String question, String answer) {
        this.question = question;
        this.answer = answer;
    }

    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }

    public void editQuestion(String newQuestion) {
        this.question = newQuestion;
    }

    public void editAnswer(String newAnswer) {
        this.answer = newAnswer;
    }

    public abstract boolean checkAnswer(String input);

    public abstract String showQuestion();

    public abstract String toString();
}