package mindexpander.commands;

import mindexpander.exceptions.IllegalCommandException;
import mindexpander.common.Messages;

/**
 * The {@code HelpCommand} class updates display message
 * to show the list of commands and their functions.
 *
 * @author Wenyi
 * @version v0.2
 * @since 2025-03-06
 */
public class HelpCommand extends Command {

    /*
     * NOTE for future devs: update the HELP_MESSAGE string with
     * whatever new features are implemented.
     */
    private static final String DEFAULT_HELP_MESSAGE = """
            Welcome to MindExpander!
            
            List of commands:
            1. help
                - Print a list of commands.
            2. add
                - Add a question to the list.
            3. list
                - List all questions added.
            4. solve
                - Attempt to solve a question.
            5. edit
                - Edit a question in the list.
            6. find
                - Find a question in the list that contains a specific keyword.
            7. delete
                - Delete a question in the list.
            8. clear
                - Clears all questions in the question bank.
            9. show
                - Show the answer to a question in the list.
            10. undo
                - Undo a previous traceable command
            11. redo
                - redo a previously undone traceable command
            12. exit
                - Exit the program.
            To get detailed information on a specific command, use `help [COMMAND]`, e.g. `help add`.""";

    private static final String HELP_HELP_MESSAGE = """
            *The `help` command*
            
            Print a full list of commands or append a specific command to get detailed information on it.
            The command can be used in two ways, shown below.
            
            Usage:
             - `help`: print the list of commands.
             - `help [COMMAND]`: print the detailed information of a specific command.
            
            Example:
             - `help`
             - `help add`""";

    private static final String ADD_HELP_MESSAGE = """
            *The `add` command*
            
            Adds a question to the question bank by following a series of steps.
            Possible question types (abbreviation in the brackets [] are how the type should be entered): 
            Fill in the Blanks - [FITB]
            Multiple Choice Question - [MCQ]
            True or False Question - [TF]
            
            Usage:
             - Follow the steps below:
               1. `add`
               2. [QUESTION_TYPE]
               3. [QUESTION_DETAILS]
               4. [QUESTION_ANSWER]
            
            Example:
             - To add a FITB question, "What is 1 + 1?", with the answer "2".
               1. add
               2. FITB
               3. What is 1+1?
               4. 2""";

    private static final String LIST_HELP_MESSAGE = """
            *The `list` command*
            
            Displays all the questions currently stored in the question bank.
            
            Usage:
             - 'list': print the list of questions currently stored in the question bank without answers.
             - 'list answer': print the list of questions currently stored in the question bank with answers.""";

    private static final String SOLVE_HELP_MESSAGE = """
            *The `solve` command*
            
            Allows the user to attempt answering a stored question in the question bank.
            The command can be used in two ways, shown below.
            
            Usage:
               1. 'solve [QUESTION_INDEX]'
               2. '[ANSWER]'
            
            Example, for "Q1. Are the developers of MindExpander cool?":
             - Multi-step:
               1. 'solve 1'
               2. 'yes'""";

    private static final String EDIT_HELP_MESSAGE = """
            *The `edit` command*
            
            Edits a question's details, including question content, answer and for multiple choice questions, their
            incorrect options.
            
            Usage:
             - 'edit [QUESTION_INDEX] q': edit the question content of a question.
             - 'edit [QUESTION_INDEX] a': edit the answer of a question.
             - 'edit [QUESTION_INDEX] o': edit the incorrect options of a multiple choice question.
            Example:
             - 'edit 1 q'
             - 'what is 3 - 2?'""";

    private static final String FIND_HELP_MESSAGE = """
            *The `find` command*
            
            Allows the user to find a question previously added to the question bank.
            The command can be used in two ways, shown below.
            
            Usage:
             - 'find [QUESTION_DETAILS]': search for all question types containing [QUESTION_DETAILS].
             - 'find mcq [QUESTION_DETAILS]': search for mcq questions containing [QUESTION_DETAILS].
             - 'find fitb [QUESTION_DETAILS]': search for fitb questions containing [QUESTION_DETAILS].
             - 'find tf [QUESTION_DETAILS]': search for tf questions containing [QUESTION_DETAILS].
            
            Example:
             - 'find hello'
             - 'find mcq hello'
             - 'find fitb hello'
             - `find tf hello`""";

    private static final String DELETE_HELP_MESSAGE = """
            *The `delete` command*
            
            Deletes a question from the question bank.
            
            Usage:
             - 'delete [QUESTION_INDEX]': Delete the question at [QUESTION_INDEX].
            
            Example:
             - 'delete 1'""";

    private static final String CLEAR_HELP_MESSAGE = """
            *The `clear` command*
            
            Clears all questions from the question bank.
            
            Usage:
             - 'clear'
             - System will ask: "Are you sure you want to clear the entire question bank? (Y/N)"
             - Enter 'Y' to proceed, 'N' to cancel (case insensitive).
            
            Example:
             - User enters: clear
             - System: Are you sure you want to clear the entire question bank? (Y/N)
             - User: y 
             - System: All questions have been cleared.""";

    private static final String SHOW_HELP_MESSAGE = """
            *The `show` command*
            
            Shows the answer to a question in the question bank.
            
            Usage:
             - `show [QUESTION_INDEX]`: Shows the answer to the question at [QUESTION_INDEX].
            
            Example:
             - `show 1`""";

    private static final String UNDO_HELP_MESSAGE = """
            *The `undo` command*
            
            Undoes the previous traceable command. These
            include add, delete, edit and clear.
            
            Usage:
             - 'undo': undo the previous traceable command.""";

    private static final String REDO_HELP_MESSAGE = """
            *The `exit` command*
            
            Redo the previous undone command.
            
            Usage:
             - 'redo': redo undone command.""";

    private static final String EXIT_HELP_MESSAGE = """
            *The `exit` command*
            
            Exits the program.
            
            Usage:
             - 'exit': exit the program.""";

    public HelpCommand(String taskDetails) {
        if (taskDetails.isEmpty()) {
            updateCommandMessage(DEFAULT_HELP_MESSAGE);
        } else {
            String helpMessage = chooseHelpMessage(taskDetails.trim());
            updateCommandMessage(helpMessage);
        }
    }

    /*
     * NOTE for future devs: when a new feature is added, add the detailed help string here.
     */

    private String chooseHelpMessage(String taskDetails) throws IllegalCommandException {
        return switch(taskDetails.toLowerCase()) {
        case "help" -> HELP_HELP_MESSAGE;
        case "add" -> ADD_HELP_MESSAGE;
        case "list" -> LIST_HELP_MESSAGE;
        case "solve" -> SOLVE_HELP_MESSAGE;
        case "edit" -> EDIT_HELP_MESSAGE;
        case "find" -> FIND_HELP_MESSAGE;
        case "delete" -> DELETE_HELP_MESSAGE;
        case "clear" -> CLEAR_HELP_MESSAGE;
        case "exit" -> EXIT_HELP_MESSAGE;
        case "show" -> SHOW_HELP_MESSAGE;
        case "undo" -> UNDO_HELP_MESSAGE;
        case "redo" -> REDO_HELP_MESSAGE;
        default -> throw new IllegalCommandException(Messages.UNKNOWN_COMMAND_MESSAGE + "\nNo help available.");
        };
    }

}
