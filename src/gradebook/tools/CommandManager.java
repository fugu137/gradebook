package gradebook.tools;

import gradebook.commands.UserCommand;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

import java.util.ArrayDeque;
import java.util.Deque;

public class CommandManager {

    Deque<UserCommand> undoStack;
    Deque<UserCommand> redoStack;
    BooleanProperty undoStackEmpty;
    BooleanProperty redoStackEmpty;

    public CommandManager() {
        undoStack = new ArrayDeque<>();
        redoStack = new ArrayDeque<>();
        undoStackEmpty = new SimpleBooleanProperty(true);
        redoStackEmpty = new SimpleBooleanProperty(true);
    }

    public void execute(UserCommand command, boolean saveToHistory) {
        command.execute();

        if (saveToHistory) {
            addToUndoStack(command);
        }
    }

    public void undo() {
        if (undoStack.size() > 0) {
            UserCommand command = getFromUndoStack();
            addToRedoStack(command);

            command.undo();
        }
    }

    public void redo() {
        if (redoStack.size() > 0) {
            UserCommand command = getFromRedoStack();
            addToUndoStack(command);

            command.redo();
        }
    }

    private void addToUndoStack(UserCommand command) {
        undoStack.push(command);
        undoStackEmpty.set(false);
    }

    private UserCommand getFromUndoStack() {
        UserCommand command = undoStack.pop();

        if (undoStack.size() == 0) {
            undoStackEmpty.set(true);
        }

        return command;
    }

    private void addToRedoStack(UserCommand command) {
        redoStack.push(command);
        redoStackEmpty.set(false);
    }

    private UserCommand getFromRedoStack() {
        UserCommand command = redoStack.pop();

        if (redoStack.size() == 0) {
            redoStackEmpty.set(true);
        }

        return command;
    }

    public BooleanProperty undoStackEmptyProperty() {
        return undoStackEmpty;
    }

    public BooleanProperty redoStackEmptyProperty() {
        return redoStackEmpty;
    }
}
