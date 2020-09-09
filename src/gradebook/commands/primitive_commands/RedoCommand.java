package gradebook.commands.primitive_commands;

import gradebook.tools.CommandManager;

public class RedoCommand implements PrimitiveCommand {

    private CommandManager commandManager;

    public RedoCommand(CommandManager commandManager) {
        this.commandManager = commandManager;
    }

    @Override
    public void execute() {
        commandManager.redo();
    }

}
