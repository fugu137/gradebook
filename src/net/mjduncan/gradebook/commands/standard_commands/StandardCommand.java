package net.mjduncan.gradebook.commands.standard_commands;

import net.mjduncan.gradebook.commands.UserCommand;

public interface StandardCommand extends UserCommand {

    void undo();
    void redo();
}
