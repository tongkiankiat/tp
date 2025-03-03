//package MindExpander;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Scanner;
//
//public class MindExpander {
//    private static final ArrayList<Question> questions = new ArrayList<>();
//
//    public static void clearScreen() {
//        try {
//            if (System.getProperty("os.name").contains("Windows")) {
//                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
//            }
//            else {
//                System.out.print("\033\143");
//            }
//        } catch (IOException | InterruptedException ex) {}
//    }
//
//    private static void exit() {
//        System.out.println("____________________________________________________________");
//        System.out.println(" Bye. Hope to see you again soon!");
//        System.out.println("____________________________________________________________");
//    }
//
//    private static void menu() {
//        clearScreen();
//        System.out.println("Type <help> for a list of commands." + "\n");
//        System.out.println("this is menu." + "\n");
//    }
//
//    private static void title() {
//        System.out.println("|   MindExpander   |");
//        System.out.println("|   CS2113-F12-3   |");
//        System.out.println("Press Enter to continue...");
//    }
//
//    private static void reportUnknownCommand(String input) {
//        System.out.println("____________________________________________________________");
//        System.out.println(" <" + input + "> is an unknown command. Please Refer to the user guide.");
//        System.out.println("____________________________________________________________");
//    }
//
//    private static void help() {
//        clearScreen();
//        System.out.println("<add> Add a question to the question bank.");
//        System.out.println("<ex> Exit the program.");
//        System.out.println("<list> List all questions.");
//        System.out.println("<menu> Go back to menu screen.");
//    }
//
//    private static void addQuestionRoutine() {
//        clearScreen();
//        Scanner scanner = new Scanner(System.in);
//
//        ArrayList<String> validTypes = new ArrayList<>();
//        validTypes.add("fitb");
//        validTypes.add("mcq");
//
//        String questionType;
//        while (true) {
//            System.out.println("Please enter the question type:");
//            questionType = scanner.nextLine().trim().toLowerCase();
//
//            if (validTypes.contains(questionType)) {
//                break;
//            } else {
//                System.out.println("Invalid question type. Please enter a valid type.");
//            }
//        }
//
//        System.out.println("Please enter the question content:");
//        String question = scanner.nextLine().trim();
////        System.out.println("Please enter the answer to the question:");
////        String answer = scanner.nextLine().trim();
//
//        switch (questionType) {
//        case "fitb":
//            System.out.println("Please enter the answer to the question:");
//            String answer = scanner.nextLine().trim();
//            questions.add(new FillInTheBlanks(question, answer));
//            break;
//
//        case "mcq":
//            System.out.println("Please enter the correct answer:");
//            String correctAnswer = scanner.nextLine().trim();
//
//            List<String> options = new ArrayList<>();
//            options.add(correctAnswer);
//
//            System.out.println("Enter 3 incorrect options:");
//            for (int i = 1; i <= 3; i++) {
//                System.out.print("Incorrect option " + i + ": ");
//                options.add(scanner.nextLine().trim());
//            }
//
//            questions.add(new MultipleChoice(question, correctAnswer, options));
//            break;
//
//        default:
//
//            System.out.println("Unknown Question type.");
//        }
//
//    }
//
//    private static void list() {
//        System.out.println("____________________________________________________________");
//        if (questions.isEmpty()) {
//            System.out.println(" List is empty!");
//            System.out.println("____________________________________________________________");
//        } else {
//            System.out.println(" Here are the questions in your list:");
//            for (int i = 0; i < questions.size(); i += 1) {
//                System.out.println(" " + (i + 1) + ". " + questions.get(i));
//            }
//            System.out.println("____________________________________________________________");
//        }
//    }
//
//    public static void main(String[] args) {
//        Scanner scanner = new Scanner(System.in);
//        title();
//
//        scanner.nextLine();
//        clearScreen();
//        menu();
//
//        while (true) {
//            String input = scanner.nextLine().trim().toLowerCase();
//
//            switch (input) {
//            case "ex":
//                exit();
//                scanner.close();
//                return;
//
//            case "menu":
//                menu();
//                break;
//
//            case "help":
//                help();
//                break;
//
//            case "add":
//                addQuestionRoutine();
//                break;
//
//            case "list":
//                list();
//                break;
//
//            default:
//                reportUnknownCommand(input);
//            }
//        }
//    }
//}
