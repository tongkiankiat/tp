# Developer Guide

## Acknowledgements

MindExpander uses the following tools in the development process:
1. [JUnit 5](https://junit.org/junit5/) - Used for JUnit testing.
2. [Gradle](https://gradle.org/) - Used for build automation.
3. [PlantUML] (https://plantuml.com/) - Used for diagram generation in this guide.

## Design & implementation

### Overall design

The project is designed using a hybrid architecture with elements from Command Pattern in the request handling and
Layered Architectures in the organisation of code into presentation (UI), application (parser, commands) and data access.
This gives developers some flexibility in implementing more complex features such as the multistep commands or even quizzes
in the future while still maintaining some layers for scalability.

The project consists of the following main components:
1. Main: Acts as the interface between the UI, command, data and storage layers.
2. UI: Interface between the user and the program, handling printing to CLI and user input.
3. Parser: Parses the user inputs into commands for the program to execute.
4. Commands: Programme's logic for the respective commands.
5. Exceptions: Custom exception class to handle exception messages for the program.
6. Question: Objects to store data for question details and answers.
7. Question bank: Handle question storing in the list, contains the list of questions.
8. Storage handler: Handles the reading and writing to a .txt file.
9. Common: Stores all magic strings or literals to be printed to user.

The overall relations between the components and classes is as follows:

{Insert class diagram here}

The overall flow of interaction between the user and program is as follows:

![](diagrams/sequence/Main.png)

### User Interface

The MindExpander UI is implemented through the TextUi class, 
which handles user interaction via console-based input and output. 
The Main class orchestrates the application flow, 
ensuring commands are processed and displayed correctly.

The UI component,
- Displays system messages (welcome, errors, results, etc.).

- Receives and validates user input.

- Formats and prints command execution results.

### Command handling

The MindExpander application follows a structured approach to handle user input, 
process commands, and execute actions accordingly. The Parser class is responsible 
for interpreting user input and mapping it to a corresponding Command object, 
which then executes the required action.

The system consists of two primary components:

**Parser**

- Converts raw user input into command objects.

- Manages multi-step commands (e.g., SolveCommand).

- Validates input and throws IllegalCommandException for invalid commands.

**Commands**

- Serves as a base class for all command types.

- Implements execute() method, which performs the command action.

- Handles multi-step user interaction when necessary.


  ![](diagrams/class/Parser_diagram.png)

How the parsing works:
When called upon to parse a user command, the Main class creates a parser which receives user input from ui 
class to parse the user command and create a XYZCommand object (e.g., AddCommand) which the parser returns back 
to main as a Command object.

**Note**
* When adding new single line commands in the future, they should return an instance of the `CommandResult` result class
in the overridden `execute()` method.
  * The only exception to this is the `HelpCommand` class as all it has to do is print the correct help message,
  managed in its constructor.

**Multistep commands**
These are implemented through the use of a finite state machine (FSM), where the different states are defined in a command's
`handleMultistepCommand()` method. The components of the program work together in the following manner:

1. `Main`: Focuses on the loop. 
    1. Orchestrates the command loop, continues looping until the command's `isCommandComplete` flag is set.
2. `Parser`: Command creation and management
   1. If no command is ongoing, parse the new command based on the user's input.
   2. Else, manage the ongoing command by forwarding the new user input to it.
3. `Command`: Handles the command logic. 
   1. The individual commands manage their own FSM logic, handling the transition from one state to the next and when to exit the state machine.
   2. Manages the program logic of the command itself, updating the program output.

**Note**
* The FSM logic is transparent to Main and Parser, as such new steps or states can be added to multistep commands without changing
Parser or main.
* The different states are defined in an enum inside the multistep command's class.
* Multistep commands **should only override** `handleMultistepCommand()` from the `Command` class and **not the** `execute()` method.
The command's messages for the user can be updated using `updateCommandMessage()` within `handleMultistepCommand` instead of returning a `CommandResult`.
The returning of a `CommandResult` instance will be handled automatically by the parent `Command` class.

The class diagram for the example multistep command `SolveCommand`:
![](diagrams/class/CommandHandling.png)

### Data
![](diagrams/class/Data_diagram.png)
The QuestionBank component 
- is responsible for managing the storage and retrieval of data (all `Question` objects) within the MindExpander application,
- does not depend on any of the other three components (as the `QuestionBank` and `Question` represent data entities of the domain, they should make 
sense on their own without depending on other components)

The system maintains two instances of QuestionBank to efficiently manage questions and enhance user experience when retrieving and modifying data. These two instances are:

- Main `questionBank` – Stores all questions logged into the system.

- `lastShownQuestionBank` – Stores a filtered subset of questions, updated when the user invokes specific commands (e.g., list or find).

This dual-QuestionBank approach improves usability by allowing users to interact with a focused subset of questions before modifying the main dataset.

**QuestionBank Management and Modification Workflow**

- Program Initialisation

  - When the user first launches MindExpander, lastShownQuestionBank is set to questionBank.

- Command Execution and lastShownQuestionBank Updates

  - When the user executes the list command, lastShownQuestionBank is updated to contain all questions from the main QuestionBank.

  - When the user executes the find command, lastShownQuestionBank is updated to contain only the questions matching the search criteria.

- Referencing lastShownQuestionBank for Modifications

    - If a user attempts to modify (e.g., edit, delete) a question, the system first references lastShownQuestionBank.

  - This ensures that users can modify questions based on their last viewed subset without needing to manually find their index in the full question bank.

- Synchronizing Changes with the Main QuestionBank

    - Once a modification is applied (e.g., a deletion or an edit), the Command class updates the main QuestionBank accordingly.

  - This maintains data consistency and ensures that all logged questions remain up to date.
  
### Storage

The `StorageFile` class is responsible for saving and loading questions from a local `.txt` file to ensure data persistence across sessions.

#### **File Format**

* Each line in the file represents a single question, with components separated by a custom delimiter defined in `Messages.STORAGE_DELIMITER`.  

* This custom delimiter `%%MINDEXPANDER_DELIM%%` is used instead of the standard pipe `|` to prevent parsing errors if users include 
special characters like `|` in their input.
* The general format is:

    * `FITB<DELIM>QuestionText<DELIM>Answer`

    * `MCQ<DELIM>QuestionText<DELIM>Option1<DELIM>Option2<DELIM>Option3<DELIM>Option4`

* Example: 
    * `FITB%%MINDEXPANDER_DELIM%%What is the capital of France?%%MINDEXPANDER_DELIM%%Paris`
    * `MCQ%%MINDEXPANDER_DELIM%%2 + 3 = ?%%MINDEXPANDER_DELIM%%5%%MINDEXPANDER_DELIM%%1%%MINDEXPANDER_DELIM%%2%%MINDEXPANDER_DELIM%%3`

#### **Saving Logic**

* The method `save(QuestionBank questionBank)` writes all current questions in the question bank to a text file located at `./data/MindExpander.txt`.
* If the `data/` directory does not exist, it is created.
* Questions are serialised using `formatQuestionForSaving(Question q)`, which includes logic for handling `FITB` and `MCQ` formats using the common delimiter.

#### **Loading Logic**

* The method `load()` reads the `MindExpander.txt` file line-by-line and reconstructs each question.

* For MCQ:
    * The first option is always considered the correct answer.
    * The remaining 3 options are treated as distractors.

#### **Design Rationale for Custom Delimiter**

* Using the pipe symbol | caused issues when users typed it as part of their question or answer.

* A unique delimiter `%%MINDEXPANDER_DELIM%%` is now used to prevent data corruption and improve robustness against malformed input.

* This string is defined once in `Messages.java` to eliminate magic strings and ensure consistency across the codebase.

## Product scope
### Target user profile

The product is designed for younger students. 
Their level of education is at the point where examination questions come in simpler forms such as fill in the blanks or multiple choice questions.

### Value proposition

This product aims to solve the problem of students not having a convenient place to store questions they would like to practice or refer to again in the future.

## User Stories

| Version | As a ...         | I want to ...                                                                                 | So that I can ...                                                          |
|---------|------------------|-----------------------------------------------------------------------------------------------|----------------------------------------------------------------------------|
| v1.0    | new user         | view a list of commands and their uses                                                        | refer to them to understand how to use the program                         |
| v1.0    | user             | add questions into the question bank                                                          | store it for future practice                                               |
| v1.0    | user             | list the questions I have previously added in the question bank                               | check what questions I have added previously                               |
| v1.0    | user             | list the questions I have previously added in the question bank with their respective answers | check what questions I have added previously along with their answers      |
| v1.0    | user             | save my questions permanently                                                                 | the questions that i have added will not be lost                           |
| v1.0    | user             | load my saved questions when i start the program                                              | see and work on the questions even after closing and reopening the program |
| v1.0    | user             | have my answer inputs evaluated                                                               | practice the questions previously added                                    |
| v2.0    | user             | find a question in the question bank by name                                                  | locate whether I have previously added a similar question                  |
| v2.0    | experienced user | solve questions by typing everything in one command                                           | answer questions faster without going through the multiple steps           |
| v2.0    | user             | edit the questions that are currently in my question bank                                     | update outdated or incorrect question details                              |
| v2.0    | user             | delete a question from the question bank                                                      | remove outdated or incorrect questions                                     |

## Non-Functional Requirements

* Should work on any _mainstream_ OS as long as it has Java `17` or above installed
* A user with above average typing speed for regular English text should be able to complete more questions faster when solving questions in one line

(More to be added)

## Glossary

* *Multistep command* - A feature which requires the user to go through several steps to complete.

## Instructions for manual testing

### Launch and Shutdown
1. Initial launch
   1. Download the jar file.
   2. Copy the file to the folder you want to use as the _home_ folder.
   3. Open a command terminal, and `cd` into the folder you put the jar file in.
   4. Run `java -jar MindExpander.jar` to run MindExpander.
   5. The following screen should be displayed:
   ```
   ==============================
    
       _____  .__            .______________                                .___            
      /     \ |__| ____    __| _/\_   _____/__  ______________    ____    __| _/___________
     /  \ /  \|  |/    \  / __ |  |    __)_\  \/  /\____ \__  \  /    \  / __ |/ __ \_  __ \
    /    Y    \  |   |  \/ /_/ |  |        \>    < |  |_> > __ \|   |  \/ /_/ \  ___/|  | \/
    \____|__  /__|___|  /\____ | /_______  /__/\_ \|   __(____  /___|  /\____ |\___  >__|   
            \/        \/      \/         \/      \/|__|       \/     \/      \/    \/
    
    Presented by: CS2113-F12-3
    Welcome to MindExpander!
    ==============================
    ==============================
    What would you like to do today?
    Type <help> for a list of commands.
    ==============================
   ```
2. Exiting MindExpander
   1. Test case: `exit`
    
       Expected: The program exits and returns user to the command terminal.
   
### Adding a question
1. Add is a _multistep command_, below is the procedure for the test case to add a question
   1. Enter `add`
   2. Enter `mcq` or `fitb` as the question type
   3. Enter `1 + 1` as the question.
   4. Enter `2` as the answer.
      1. For `mcq`, enter 3 other incorrect options: `1`, `3`, `4`.
   
### Listing all questions
1. List all the questions currently in the question bank.
   1. Test case: `list`
   
        Expected: The list of questions in the question bank will be displayed.

### Finding questions containing a keyword
1. Finding all questions that contain a specific keyword.
   1. Prerequisites: There are already questions contained in the question bank.
   2. Test Case: `find MRT`
   
      Expected: Displays all questions (both `mcq` and `fitb`) that contain `MRT` in the question. If there are no such questions with the `MRT` keyword, UI will print `No questions with MRT found!`.
   3. Test Case: `find mcq MRT`
    
      Expected: Displays all `mcq` questions that contain `MRT` in the question. If there are no such questions with the `MRT` keyword, UI will print `No questions with MRT found!`.
    
   4. Test Case: `find fitb MRT`
    
      Expected: Displays all `fitb` questions that contain `MRT` in the question. If there are no such questions with the `MRT` keyword, UI will print `No questions with MRT found!`.