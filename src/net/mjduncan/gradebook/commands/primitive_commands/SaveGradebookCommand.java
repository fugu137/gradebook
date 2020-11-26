package net.mjduncan.gradebook.commands.primitive_commands;

import net.mjduncan.gradebook.MainController;
import net.mjduncan.gradebook.tools.FileManager;

public class SaveGradebookCommand implements PrimitiveCommand {

    private final MainController mainController;
    private final FileManager fileManager;

    public SaveGradebookCommand(MainController mainController, FileManager fileManager) {
        this.mainController = mainController;
        this.fileManager = fileManager;
    }

    @Override
    public void execute() {
        fileManager.save(mainController);
    }
}
