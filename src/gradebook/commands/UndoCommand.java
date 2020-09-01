package gradebook.commands;

import gradebook.tools.CommandManager;

public class UndoCommand implements UserCommand {

    private CommandManager commandManager;

    public UndoCommand(CommandManager commandManager) {
        this.commandManager = commandManager;
    }

    @Override
    public void execute() {
        commandManager.undo();
    }

    @Override
    public void undo() {
        System.out.println("No undo functionality for undo command!");
    }

    @Override
    public void redo() {
        System.out.println("No redo functionality for redo command!");
    }
}
