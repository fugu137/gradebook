package net.mjduncan.gradebook.commands.primitive_commands;

import net.mjduncan.gradebook.tools.CommandManager;

public class UndoCommand implements PrimitiveCommand {

    private CommandManager commandManager;

    public UndoCommand(CommandManager commandManager) {
        this.commandManager = commandManager;
    }

    @Override
    public void execute() {
        commandManager.undo();
    }

}
