<<<<<<< HEAD:src/main/java/mindexpander/FillInTheBlanks.java
package mindexpander;

class FillInTheBlanks extends Question {
    public FillInTheBlanks(String question, String answer) {
        super(question, answer);
    }

    @Override
    public boolean checkAnswer(String userAnswer) {
        return answer.equalsIgnoreCase(userAnswer.trim());
    }

    @Override
    public String toString() {
        return "FITB: " + question + " [Answer: " + answer + "]";
    }
}
=======
//package MindExpander;
//
//class FillInTheBlanks extends Question {
//    public FillInTheBlanks(String question, String answer) {
//        super(question, answer);
//    }
//
//    @Override
//    public boolean checkAnswer(String userAnswer) {
//        return answer.equalsIgnoreCase(userAnswer.trim());
//    }
//
//    @Override
//    public String toString() {
//        return "FITB: " + question + " [Answer: " + answer + "]";
//    }
//}
>>>>>>> 0a42c3bb5c2030118d00a06018e520a5dc68221f:src/main/java/MindExpander/FillInTheBlanks.java
