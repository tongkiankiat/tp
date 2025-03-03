package mindexpander.exceptions;

public class ExceptionHandler {
    private final static String LINE = "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!";

    /**
     * Prints message asking user for valid command.
     * */
    public static void handleBadCommand(String message) {
        System.out.println(LINE);
        System.out.println(message);
        System.out.println(LINE);
    }

    /**
     * Prints message asking user for valid task index.
     */
    public static void handleBadIndex(boolean isQuestionListEmpty) {
        System.out.println(LINE);
        if (isQuestionListEmpty) {
            System.out.println("Add a question first, then");
        }
        System.out.println("Enter a valid question index");
        System.out.println(LINE);
    }
}
