# Tong Kian Kiat - Project Portfolio Page

## Project: MindExpander
MindExpander is a desktop application designed for Primary School students to practice Mathematics, Science and English
questions via a simple Command Line Interface (CLI).

Given below are my contributions to the project.

* __Code contributed__: [RepoSense Link](https://nus-cs2113-ay2425s2.github.io/tp-dashboard/?search=tongkiankiat&breakdown=true)
* __Enhancements implemented__
  * Created TextUI Class
  * Implemented commons package to store all magic strings used in Classes and methods
  * Implemented List, Find and Show Command
  * Implemented Error Logging for all commands
  * Added lastShownQuestionBank for better user experience when adding, solving, editing and deleting questions
  * Added filter for question type for Find Command
  * Added filter for question type for List Command
  * Implemented Show Command that displays the answer to the question by querying question index
    * The question index is taken from lastShownQuestionBank. However, if that is out of range, it queries the original questionBank in case the user was originally referencing it instead
* __Contributions to UG__
  * List Command
  * Find Command
  * Show Command
  * Error Logging
* __Contributions to DG__
  * Explanations for list, find, show, error logging and lastShownQuestionBank
  * User story for list, find, add and show commands
  * Sequence diagrams for list, find and show commands
  * Test Cases for launching and shutting down MindExpander, add, list, find and show commands
  * Error Logging explanation