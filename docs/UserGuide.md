# User Guide

## Introduction

MindExpander is a desktop app for Primary School students to practice questions in Mathematics, Sciences and English.
It is designed for use via a Command Line Interface.

## Quick Start

{Give steps to get started quickly}

1. Ensure that you have Java 17 or above installed.
2. Down the latest version of `MindExpander` from [here](http://link.to/duke).

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

Format: `solve` | `[QUESTION INDEX]` | `[QUESTION ANSWER]`
`[QUESTION_INDEX]`: The question number of the question to be solved.
`[QUESTION_ANSWER]`: The answer to the question.

Example usage:
1. `solve`
    > Please enter the question number you would like to solve.
2. `2`
    > Attempting question 2, enter your answer:
3. `Potato`
    > Correct!

**Note**:
* It is recommended to run `list` before `solve` to check the index of the question you intend to solve.
* Entering the wrong answer will result in the following message until the correct answer is entered:
   > Wrong answer, please try again.

### Exiting the program: `exit`
Exits the program.

Format: `exit`

Example usage: `exit`

### Additional notes
* Inputting unrecognised commands will result in an error message.
* Saving and Loading: The question bank is automatically saved to a file named MindExpander.txt in the ./data/ folder. 
## FAQ

**Q**: How do I transfer my data to another computer? 

**A**: #TODO

## Command Summary

* View help sheet `help`
* Add question `add` | `[QUESTION_TYPE]` | `[QUESTION_DETAILS]` | `[QUESTION_ANSWER]`
* List question bank `list`
* Solve question `solve` | `[QUESTION_INDEX]` | `[QUESTION_ANSWER]`
* Exit program `exit`
