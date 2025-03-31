package mindexpander.commands;

import mindexpander.data.QuestionBank;
import mindexpander.data.question.FillInTheBlanks;
import mindexpander.data.question.MultipleChoice;
import mindexpander.data.question.Question;
import mindexpander.data.question.QuestionType;

import java.util.ArrayList;
import java.util.List;

public class AddCommand extends Command {
    private Question toAdd;
    private QuestionType type;
    private String question;
    private String answer;

    private enum Step {GET_TYPE, GET_QUESTION, GET_ANSWER, GENERATE_QUESTION}

    private AddCommand.Step currentStep = AddCommand.Step.GET_TYPE;
    private int multipleChoiceOptionNumber = 0;
    private List<String> options;

    public AddCommand() {
        isComplete = false; // Multistep command
        options = new ArrayList<>();
        updateCommandMessage("Please enter the type of the question you would like to add.");
    }

    @Override
    public Command handleMultistepCommand(String nextInput, QuestionBank questionBank) {
        if (currentStep == Step.GET_TYPE) {
            getQuestionType(nextInput);
            return this;
        }

        if (currentStep == Step.GET_QUESTION) {
            this.question = nextInput;
            currentStep = Step.GET_ANSWER;
            updateCommandMessage("Enter the answer:");
            return this;
        }

        if (currentStep == Step.GET_ANSWER) {
            this.answer = nextInput;
            currentStep = Step.GENERATE_QUESTION;
        }

        return addQuestionHandler(nextInput, questionBank);
    }

    private void getQuestionType(String nextInput) {
        if (!QuestionType.isValidType(nextInput)) {
            updateCommandMessage("Invalid input. Please enter a correct question type.");
        } else {
            this.type = QuestionType.fromString(nextInput);
            currentStep = Step.GET_QUESTION;
            updateCommandMessage("Enter your question:");
        }
    }

    private Command addQuestionHandler(String nextInput, QuestionBank questionBank) {
        if (this.type == QuestionType.FITB) {
            return addFillinTheBlank(questionBank);
        } else {
            return addMultipleChoice(nextInput, questionBank);
        }
    }

    private Command addFillinTheBlank(QuestionBank questionBank) {
        toAdd = new FillInTheBlanks(question, answer);
        questionBank.addQuestion(toAdd);
        updateCommandMessage(String.format("Question %1$s successfully added.", toAdd.toString()));
        isComplete = true;
        return this;
    }

    private Command addMultipleChoice(String nextInput, QuestionBank questionBank) {
        if (multipleChoiceOptionNumber == 0) {
            options.add(answer);
            updateCommandMessage("Enter 3 incorrect options (1/3):");
            multipleChoiceOptionNumber += 1;
            return this;
        }

        options.add(nextInput);
        multipleChoiceOptionNumber += 1;
        updateCommandMessage(String.format("Enter 3 incorrect options (%1$d/3):", multipleChoiceOptionNumber));
        if (multipleChoiceOptionNumber <= 3) {
            return this;
        }

        toAdd = new MultipleChoice(question, answer, options);
        questionBank.addQuestion(toAdd);
        updateCommandMessage(String.format("Question %1$s successfully added.", toAdd.toString()));
        isComplete = true;
        return this;
    }

}

