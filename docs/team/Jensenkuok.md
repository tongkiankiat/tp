# Jensen Kuok - Project Portfolio Page

## Project: MindExpander
MindExpander is a desktop application designed for Primary School students to practice Mathematics, Science and English
questions via a simple Command Line Interface (CLI).

## Overview
In charge of the entire backend storage subsystem of the application, ensuring data persistence through a custom 
save/load logic and integration with all question-related commands. Also implemented critical user-facing features such 
as delete, clear, true/false question type, and input validation, with full support for undo/redo and logging.

## Summary of Contributions
Given below are my contributions to the project.

* __Code contributed__:  The code contributed by me can be found in this [RepoSense Link](https://nus-cs2113-ay2425s2.github.io/tp-dashboard/?search=jensen&sort=groupTitle&sortWithin=title&timeframe=commit&mergegroup=&groupSelect=groupByRepos&breakdown=true&checkedFileTypes=docs~functional-code~test-code~other&since=2025-02-21&tabOpen=true&tabType=authorship&tabAuthor=Jensenkuok&tabRepo=AY2425S2-CS2113-F12-3%2Ftp%5Bmaster%5D&authorshipIsMergeGroup=false&authorshipFileTypes=docs~functional-code~test-code&authorshipIsBinaryFileTypeChecked=false&authorshipIsIgnoredFilesChecked=false)
* __Enhancements implemented__
  * Created StorageFile class to save/load all questions (FITB, MCQ, TF).
    * Designed and implemented the complete storage architecture to support automatic saving and loading of FITB, MCQ, and TF questions.
    * Introduced a custom delimiter (`%%MINDEXPANDER_DELIM%%`) to prevent save file corruption from user inputs.
    * Ensured graceful handling of malformed lines during loading to maintain data integrity.
    * Integrated storage logic into the `Main` loop to trigger automatic saving upon every add, edit, delete, and clear operation.
    
  * Designed and enforced a file format that is easy to parse and robust against malformed inputs.
    * Developed a consistent file format structure for FITB, MCQ, and TF questions to ensure ease of reading and writing.
    * Built defensive loading logic that skips malformed lines to maintain application stability and prevent crashes.

  * Implemented True/False (TF) question types, including full command support.
    * Added the `TF` enum and implemented parsing logic to distinguish and load TF questions during file loading.
    * Updated commands to support TF functionality.
    * Included validation to ensure TF answers are strictly "true" or "false".

  * Developed the full logic for `DeleteCommand`.
    * Created `DeleteCommand` to delete questions based on index from the last shown list.
    * Added error handling for invalid delete attempts and invalid indices.

  * Implemented `ClearCommand` with confirmation logic and undo/redo.
    * Designed a multistep command using FSM to confirm clear action with a Y/N prompt.
    * Cleared all questions only upon user confirmation.

  * Created `InputValidator` class to reject dangerous input containing the delimiter string.
    * Centralized input validation to a utility class to maintain clean separation of concerns.
    * Prevented delimiter string (`%%MINDEXPANDER_DELIM%%`) from being entered in questions, answers, or options.
    * Threw `IllegalCommandException` when invalid input was detected and provided user-friendly prompts.

  * Built the `QuestionLogger` system to track all question-related activity.
    * Logged every question that was added, edited, or deleted into `questionLogs.txt`.
    * Included timestamps, question types, and action taken (ADD, DELETE, EDIT) to enable full traceability.
    * Avoided logging MCQ option-only changes to reduce log clutter.

  * Added unit tests to validate key functionality for storage, deletion, clear and input validation.

* __Contributions to UG__
    * Delete Command
    * Clear Command
    * Question Logger feature
    * Saving and Loading of Data
    * Examples related to T/F question types
  
* __Contributions to DG__
    * Storage: design rationale, file format, saving/loading logic, and delimiter handling.
    * DeleteCommand: explanation of the delete command, different delete cases and user flow.
    * InputValidator: integration with Add/Edit, exception management, and delimiter protection.
    * Question logging: overview, file structures, and real-world use cases.
    * Updated diagrams and wrote relevant sequences and data flow explanations to support maintainability by future developers.

* __Others__
    * Planned for milestone release
    * Reviewed and resolved duplicate issues from PE-D
    * Assigned relevant issues to team members
