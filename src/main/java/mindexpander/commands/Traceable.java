package mindexpander.commands;

/**
 * Represents an action that can be tracked and reversed (undo/redo).
 * Implementing classes should define the logic for undoing and redoing actions,
 * as well as provide meaningful messages for each operation.
 *
 */
public interface Traceable {
    void undo();
    void redo();

    String undoMessage();
    String redoMessage();
}
