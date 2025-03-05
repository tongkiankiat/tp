package MindExpander;

import MindExpander.ui.TextUI;

public class Main {
    // Attributes
    private TextUI ui;

    // Constructor
    public static void main(String[] args) {
        new Main().run();
    }

    // Methods
    // Run MindExpander
    public void run() {
        start();
        runUserCommandUntilTermination();
        exit();
    }

    // Start function: Instantiates the TextUI class
    private void start() {
        try {
            this.ui = new TextUI();
            // Initialise storage and data here as well
            ui.enterMainMenu();
        } catch (Exception e) {
            ui.printInitFailedMessage();
        }
    }

    // Runs the program until user enters 'exit'
    private void runUserCommandUntilTermination() {
        // New command variable
        // Command command;
        // Temporary state for exit, before adding in command class
        boolean exit = false;
        do {
            String userCommand = ui.getUserCommand();

//             command = new Parser().parseCommand(userCommand);
//            ui.displayResults()
            // This mechanism is temporary
            if (userCommand.equals("exit")) {
                exit = true;
            }
        } while (!exit);
    }

    // Exit function: Exits the MindExpander program
    private void exit() {
        ui.printGoodbyeMessage();
        System.exit(0);
    }
}
