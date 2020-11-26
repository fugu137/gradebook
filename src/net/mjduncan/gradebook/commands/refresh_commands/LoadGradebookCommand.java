package net.mjduncan.gradebook.commands.refresh_commands;

import javafx.beans.property.ObjectProperty;
import javafx.stage.Stage;
import net.mjduncan.gradebook.MainController;
import net.mjduncan.gradebook.StatisticsPane;
import net.mjduncan.gradebook.tools.FileManager;

import java.io.File;

public class LoadGradebookCommand implements RefreshCommand {

    private final MainController mainController;
    private final boolean statsPaneShowing;
    private final ObjectProperty<FileManager> fileManager;
    private final File file;
    private final Stage stage;

    public LoadGradebookCommand(MainController mainController, StatisticsPane statisticsPane, ObjectProperty<FileManager> fileManager, File file, Stage stage) {
        this.mainController = mainController;
        this.fileManager = fileManager;
        this.file = file;
        this.stage = stage;

        this.statsPaneShowing = statisticsPane != null;
    }

    @Override
    public void execute() {
        if (statsPaneShowing) {
            mainController.closeStatisticsPane();
        }
        fileManager.set(new FileManager());
        fileManager.getValue().load(file, mainController);

        mainController.setupStatisticsLabels();
        stage.setTitle("Gradebook - " + file.getName());
    }

}
