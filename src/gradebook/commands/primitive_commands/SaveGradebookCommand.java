package gradebook.commands.primitive_commands;

import gradebook.MainController;
import gradebook.tools.FileManager;

public class SaveGradebookCommand implements PrimitiveCommand {

    private MainController mainController;
    private FileManager fileManager;

    public SaveGradebookCommand(MainController mainController, FileManager fileManager) {
        this.mainController = mainController;
        this.fileManager = fileManager;
    }

    @Override
    public void execute() {
        fileManager.save(mainController);
    }
}
