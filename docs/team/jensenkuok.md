# Jensen Kuok - Project Portfolio Page

## Project: MindExpander
MindExpander is a desktop application designed for Primary School students to practice Mathematics, Science and English
questions via a simple Command Line Interface (CLI).

## Overview
I was in charge of building the entire backend storage system from scratch. This includes saving and loading all 
questions (FITB and MCQ) reliably using a custom delimiter to prevent parsing errors, ensuring data persistence across 
sessions. I also implemented the delete feature end-to-end, including its integration with the parser and support for 
user feedback.

## Summary of Contributions
Given below are my contributions to the project.

* __Code contributed__:  The code contributed by me can be found in this [RepoSense Link](https://nus-cs2113-ay2425s2.github.io/tp-dashboard/?search=jensen&sort=groupTitle&sortWithin=title&timeframe=commit&mergegroup=&groupSelect=groupByRepos&breakdown=true&checkedFileTypes=docs~functional-code~test-code~other&since=2025-02-21&tabOpen=true&tabType=authorship&tabAuthor=Jensenkuok&tabRepo=AY2425S2-CS2113-F12-3%2Ftp%5Bmaster%5D&authorshipIsMergeGroup=false&authorshipFileTypes=docs~functional-code~test-code&authorshipIsBinaryFileTypeChecked=false&authorshipIsIgnoredFilesChecked=false)
* __Enhancements implemented__
    * Built the StorageFile system to handle saving and loading questions from file.
      * Designed file format and implemented parsing logic using a safe delimiter.
      * Supported both FITB and MCQ question formats, including robust error handling for malformed entries.
      * Implemented the full delete feature, including validation, parser integration, user messages, and testing.
      * Added unit tests to ensure storage and delete features work correctly.
* __Contributions to UG__
    * Wrote documentation for delete command.

