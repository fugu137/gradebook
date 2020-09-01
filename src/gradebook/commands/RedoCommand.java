package gradebook.commands;

import gradebook.tools.CommandManager;

public class RedoCommand implements UserCommand {

    private CommandManager commandManager;

    public RedoCommand(CommandManager commandManager) {
        this.commandManager = commandManager;
    }

    @Override
    public void execute() {
        commandManager.redo();
    }

    @Override
    public void undo() {
        System.out.println("No undo functionality for redo command!");
    }

    @Override
    public void redo() {
        System.out.println("No redo functionality for redo command!");
    }
}
