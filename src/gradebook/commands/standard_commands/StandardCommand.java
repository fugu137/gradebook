package gradebook.commands.standard_commands;

import gradebook.commands.UserCommand;

public interface StandardCommand extends UserCommand {

    void undo();
    void redo();
}
