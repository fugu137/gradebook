package gradebook.commands;

public interface UserCommand {

    void execute();
    void undo();
    void redo();
}
