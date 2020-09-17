package net.mjduncan.gradebook.commands.refresh_commands;

import javafx.beans.property.ObjectProperty;
import javafx.stage.Stage;
import net.mjduncan.gradebook.MainController;
import net.mjduncan.gradebook.StatisticsPane;
import net.mjduncan.gradebook.tools.FileManager;

import java.io.File;

public class LoadGradebookCommand implements RefreshCommand {

    private MainController mainController;
    private boolean statsPaneShowing;
    private ObjectProperty<FileManager> fileManager;
    private File file;
    private Stage stage;

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

//        mainController.disableSaveMenuItem(false);
        mainController.setupStatisticsLabels();

        stage.setTitle("Gradebook - " + file.getName());
    }

}
