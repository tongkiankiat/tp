package mindexpander.commands;

import mindexpander.data.QuestionBank;
import mindexpander.data.question.FillInTheBlanks;
import mindexpander.data.question.Question;
import mindexpander.data.question.QuestionType;

public class AddCommand extends Command {
    private Question toAdd;
    private String question;
    private String answer;

    private enum Step { GET_TYPE, GET_QUESTION, GET_ANSWER }
    private AddCommand.Step currentStep = AddCommand.Step.GET_TYPE;

    public AddCommand() {
        isComplete = false; // Multistep command
        updateCommandMessage("Please enter the type of the question you would like to add.");
    }

    @Override
    public Command handleMultistepCommand(String nextInput, QuestionBank questionBank) {
        switch (currentStep) {
        case GET_TYPE:
            updateCommandMessage(getQuestionType(nextInput));
            return this; // Stay in SolveCommand

        case GET_QUESTION:
            this.question = nextInput;
            currentStep = Step.GET_ANSWER;
            updateCommandMessage("Enter the answer:");
            return this;

        case GET_ANSWER:
            this.answer = nextInput;
            toAdd = new FillInTheBlanks(question, answer);
            questionBank.addQuestion(toAdd);
            updateCommandMessage(String.format("Question %1$s successfully added.", toAdd.toString()));
            isComplete = true;
            return this; // Exit multi-step mode

        default:
            return this;  // Default return, should not reach here
        }
    }

    private String getQuestionType(String nextInput) {
        if (!QuestionType.isValidType(nextInput)) {
            return "Invalid input. Please enter a correct question type.";
        }

        currentStep = Step.GET_QUESTION;
        return "Enter your question:";
    }
}

