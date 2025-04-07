package mindexpander.common;

// This package contains the variables for common strings used in this program

public class Messages {
    // General messages
    public static final String LINE = "==============================";
    public static final String MINDEXPANDERLOGO = "\n" +
            "   _____  .__            .______________                                .___            \n" +
            "  /     \\ |__| ____    __| _/\\_   _____/__  ______________    ____    __| _/___________ \n" +
            " /  \\ /  \\|  |/    \\  / __ |  |    __)_\\  \\/  /\\____ \\__  \\  /    \\  / __ |/ __ \\_  __ \\\n" +
            "/    Y    \\  |   |  \\/ /_/ |  |        \\>    < |  |_> > __ \\|   |  \\/ /_/ \\  ___/|  | \\/\n" +
            "\\____|__  /__|___|  /\\____ | /_______  /__/\\_ \\|   __(____  /___|  /\\____ |\\___  >__|   \n" +
            "        \\/        \\/      \\/         \\/      \\/|__|       \\/     \\/      \\/    \\/       \n";
    public static final String TEAM_NAME = "Presented by: CS2113-F12-3";
    public static final String WELCOME_MESSAGE = "Welcome to MindExpander!";
    public static final String FAILED_MESSAGE = "Something went wrong starting MindExpander, exiting program...";
    public static final String GOODBYE_MESSAGE = "Bye! Hope to see you again soon :)";
    public static final String[] MENU_MESSAGE = {"What would you like to do today?",
        "Type <help> for a list of commands."};
    public static final String UNKNOWN_COMMAND_MESSAGE = "You have entered an unknown command." +
        " Please refer to the user guide, or type <help> to display the available commands";
    public static final String DISPLAYED_INDEX_OFFSET = ". ";
    public static final String EMPTY_INPUT = "Please enter a valid input!";
    public static final String STORAGE_DELIMITER = "%%MINDEXPANDER_DELIM%%";
    public static final String ERROR_LOGGER_DELIMITER = "|";

    // Error Messages
    public static final String LIST_ERROR_MESSAGE =
            "Invalid command! Please enter either " +
                    "`list`, `list answer`," +
                    " `list [mcq/fitb/tf]` or `list [mcq/fitb/tf] answer` to view the question bank.";
    public static final String FIND_ERROR_MESSAGE_EMPTY_BODY =
            "Invalid command! Please enter either "
                    + "`find [KEYWORD]`, `find mcq [KEYWORD]`, `find fitb [KEYWORD]` or `find tf [KEYWORD]`.";
    public static final String FIND_ERROR_MESSAGE_EMPTY_BODY_MCQ =
            "Invalid command! The correct format should be `find mcq [KEYWORD]`";
    public static final String FIND_ERROR_MESSAGE_EMPTY_BODY_FITB =
            "Invalid command! The correct format should be `find fitb [KEYWORD]`";
    public static final String FIND_ERROR_MESSAGE_EMPTY_BODY_TF =
            "Invalid command! The correct format should be `find tf [KEYWORD]`";
    public static final String UNABLE_TO_INIITIALISE_ERROR_LOGGER = "Unable to initialise the error logger.";
    public static final String SHOW_ERROR_MESSAGE_EMPTY_BODY =
            "Invalid command! The correct format should be `show [QUESTION_INDEX]`.";
    public static final String SHOW_ERROR_NOT_INTEGER =
            "Invalid command! Please enter an integer for the question index.";
    public static final String SHOW_ERROR_EMPTY_QUESTION_BANK =
            "Invalid command! There are currently no questions in the question bank.";

    // Strings with parameters
    public static final String listCommandMessage(String questionType, boolean questionBankIsEmpty) {
        if (!questionBankIsEmpty) {
            if (questionType.equals("mcq")) {
                return "Here are the MCQ questions you have currently:";
            } else if (questionType.equals("fitb")) {
                return "Here are the FITB questions you have currently:";
            } else if (questionType.equals("tf")) {
                return "Here are the TF questions you have currently:";
            } else {
                return "Here are the questions you have currently:";
            }
        } else {
            if (questionType.equals("mcq")) {
                return "You have no MCQ questions!";
            } else if (questionType.equals("fitb")) {
                return "You have no FITB questions!";
            } else if (questionType.equals("tf")) {
                return "You have no TF questions!";
            } else {
                return "You have no questions!";
            }
        }
    }

    public static final String findCommandMessage(String keyword,  String questionType, boolean questionsFound) {
        keyword = keyword.trim();
        if (questionsFound) {
            if (questionType.equals("mcq")) {
                return "Here are the MCQ questions with " + keyword + ":";
            } else if (questionType.equals("fitb")) {
                return "Here are the FITB questions with " + keyword + ":";
            } else if (questionType.equals("tf")) {
                return "Here are the TF questions with " + keyword + ":";
            } else {
                return "Here are the questions with " + keyword + ":";
            }
        } else {
            if (questionType.equals("mcq")) {
                return "No MCQ questions with " + keyword + " found!";
            } else if (questionType.equals("fitb")) {
                return "No FITB questions with " + keyword + " found!";
            } else if (questionType.equals("tf")) {
                return "No TF questions with " + keyword + " found!";
            } else {
                return "No questions with " + keyword + " found!";
            }
        }
    }

    public static final String showCommandOutOfRangeMessage(int questionBankCount) {
        return "Index is out of range! You currently have " + questionBankCount
                + " questions, please enter a question index in this range.";
    }

    public static final String showCommandMessage(int questionIndex) {
        return "Here is the answer for question " + questionIndex + ":";
    }
}
