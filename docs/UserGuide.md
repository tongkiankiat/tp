# MindExpander User Guide

## Table of Contents

1. **[Introduction](#introduction)**
2. **[Quick Start](#quick-start)**
3. **[Features](#features)**
    - [Viewing the help sheet: `help`](#viewing-the-help-sheet-help)
    - [Adding a question: `add`](#adding-a-question-add)
    - [Listing questions added: `list`](#listing-questions-added-list)
    - [Solving questions: `solve`](#solving-questions-solve)
        - [Multistep usage](#multistep-usage)
        - [One-step usage](#one-step-usage)
    - [Exiting the program: `exit`](#exiting-the-program-exit)
4. **[Additional Notes](#additional-notes)**
5. **[FAQ](#faq)**
6. **[Command Summary](#command-summary)**

## Introduction

MindExpander is a desktop application designed for Primary School students to practice Mathematics, Science and English
questions. It features a simple Command Line Interface (CLI), allowing users to efficiently add, list, and solve 
questions. The app provides a structured and interactive way for students to reinforce their learning through guided 
commands. With built-in saving and loading capabilities, users can seamlessly continue their practice sessions anytime.

## Quick Start

1. Ensure that you have Java 17 or above installed.
2. Down the latest version of `MindExpander` from [here](https://github.com/AY2425S2-CS2113-F12-3/tp/releases/tag/v1.0).
3. Open a command terminal
4. `cd` into the folder containing the jar file
5. Use the command `java -jar MindExpander.jar` to run the application. Upon start up, you should see:
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

## Features
The list of features and how to use them can be found below.
User input lines will be of the form `[USER_INPUT]`.
For example, an input which requires a question number from the user will be of the form `[QUESTION_NUMBER]`.
For features with multiple separate inputs, the different inputs will be separated by a | symbol. For example,
an input with 3 different input steps will be of the form `STEP 1` | `STEP 2` | `STEP 3`.

### Viewing the help sheet: `help`
Displays the list of commands, along with the format of the command, and what each command will return.

Format: `help` 

Example usage: 

`help`

### Adding a question: `add`
Adds a question to the question bank. Follows a series of steps which require separate inputs each.

Format: `add` | `[QUESTION_TYPE]` | `[QUESTION_DETAILS]` | `[QUESTION_ANSWER]`

Question types (as of this version): `FITB`

Example usage: #TODO

**Note**: This program is designed to take inputs in **Roman Alphabet** (i.e. English characters), 
please do not enter characters from other languages, for example Chinese characters.

### Listing questions added: `list`
Lists all the questions currently in the question bank.

Format: `list`

Example usage:

`list`

### Solving questions: `solve`
Solves a question that was previously added to the question bank.

**Multistep usage**
`solve` can be used in a "one command at a time" manner. This method is easier for new users and guides the user
through the process.

Format: `solve` | `[QUESTION INDEX]` | `[QUESTION ANSWER]`
`[QUESTION_INDEX]`: The question number of the question to be solved.
`[QUESTION_ANSWER]`: The answer to the question.

Example usage:
1. `solve`
    > Please enter the question number you would like to solve.
2. `2`
    > Attempting question 2: What are fries made of? Enter your answer:
3. `Potato`
    > Correct!

**Note**:
* It is recommended to run `list` before `solve` to check the index of the question you intend to solve.
* Entering the wrong answer will result in the below message, enter Y to try again and N to give up and exit:
   > Wrong answer, would you like to try again? [Y/N]

**One-step usage**
`solve` can also be used by entering all the arguments in one line, this method is faster but must follow the format
correctly.

Format: `solve /q [QUESTION_INDEX] /a [QUESTION_ANSWER]`

Example usage:
`solve /q 2 /q Potato`

**Note**:
* It is recommended to run `list` before `solve` to check the index of the question you intend to solve.
* Follow the command format as specified above and ensure that question indexes are within 1 to the number of questions,
entering otherwise will result in errors.

### Deleting a question: `delete`
Removes a question from the question bank. The question index should refer to the index displayed by the most recent `list` or `find` command.

Format: `delete [QUESTION_INDEX]`

- `[QUESTION_INDEX]`: The number of the question to be deleted, as seen in the last shown list.

Example usage:

```
list
==============================
1. FITB: 1 + 1 = __
2. FITB: The capital of France is __
==============================
delete 1
Deleted question: FITB: 1 + 1 = __
```

**Notes:**
* The question index is based on the most recently displayed question list (via `list` or `find`).
* Attempting to delete an index that does not exist in the last shown list will result in an error.
* This command is **single-step** and does not support multi-step usage.

### Exiting the program: `exit`
Exits the program.

Format: `exit`

Example usage: `exit`

### Additional notes
* Inputting unrecognised commands will result in an error message.
* Saving and Loading: The question bank is automatically saved to a file named MindExpander.txt in the ./data/ folder. 
## FAQ

**Q**: How do I transfer my data to another computer? 

**A**: Data can be transferred by copying over the `data` folder containing `MindExpander.txt` and pasting it in the
same folder where the .jar file is installed on the new computer.

## Command Summary

* View help sheet `help`
* Add question `add` | `[QUESTION_TYPE]` | `[QUESTION_DETAILS]` | `[QUESTION_ANSWER]`
* List question bank `list`
* Solve question
  * Multistep `solve` | `[QUESTION_INDEX]` | `[QUESTION_ANSWER]`
  * One-step `solve /q [QUESTION__INDEX] /a [QUESTION_ANSWER]`
* Delete question: `delete [QUESTION_INDEX]`
* Exit program `exit`
