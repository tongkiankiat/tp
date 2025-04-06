package mindexpander.commands;

public interface Tracable {
    void undo();
    void redo();

    String undoMessage();
    String redoMessage();
}
