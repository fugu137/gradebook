package net.mjduncan.gradebook.commands.primitive_commands;

import javafx.beans.property.ObjectProperty;
import javafx.stage.Stage;
import net.mjduncan.gradebook.MainController;
import net.mjduncan.gradebook.tools.FileManager;

import java.io.File;

public class SaveGradebookAsCommand implements PrimitiveCommand {

    private final MainController mainController;
    private final ObjectProperty<FileManager> fileManager;
    private final File file;
    private final Stage stage;

    public SaveGradebookAsCommand(MainController mainController, ObjectProperty<FileManager> fileManager, File file, Stage stage) {
        this.mainController = mainController;
        this.fileManager = fileManager;
        this.file = file;
        this.stage = stage;
    }

    @Override
    public void execute() {
        fileManager.set(new FileManager());
        fileManager.getValue().saveAs(file, mainController);

        stage.setTitle("Gradebook - " + file.getName());
    }
}
