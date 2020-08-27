package gradebook.commands;

public interface UserCommand {

    public void execute();
    public void undo();
    public void redo();
}
