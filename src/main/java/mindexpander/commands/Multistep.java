package mindexpander.commands;

import mindexpander.data.QuestionBank;

public interface Multistep {
    public Command handleMultistepCommand(String nextInput, QuestionBank questionBank);
}
