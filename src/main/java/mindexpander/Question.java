<<<<<<< HEAD:src/main/java/mindexpander/Question.java
package mindexpander;

abstract class Question {
    protected String question;
    protected String answer;

    public Question(String question, String answer) {
        this.question = question;
        this.answer = answer;
    }

    public abstract boolean checkAnswer(String input);

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

    public abstract String toString();
}

=======
//package MindExpander;
//
//abstract class Question {
//    protected String question;
//    protected String answer;
//
//    public Question(String question, String answer) {
//        this.question = question;
//        this.answer = answer;
//    }
//
//    public abstract boolean checkAnswer(String input);
//
//    public String getQuestion() {
//        return question;
//    }
//
//    public String getAnswer() {
//        return answer;
//    }
//
//    public void editQuestion(String newQuestion) {
//        this.question = newQuestion;
//    }
//
//    public void editAnswer(String newAnswer) {
//        this.answer = newAnswer;
//    }
//
//    public abstract String toString();
//}
//
>>>>>>> 0a42c3bb5c2030118d00a06018e520a5dc68221f:src/main/java/MindExpander/Question.java
