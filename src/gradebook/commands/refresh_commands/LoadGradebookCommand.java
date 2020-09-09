package gradebook.commands.refresh_commands;

import gradebook.MainController;
import gradebook.StatisticsPane;
import gradebook.tools.FileManager;

import java.io.File;

public class LoadGradebookCommand implements RefreshCommand {

    private MainController mainController;
    private boolean statsPaneShowing;
    private FileManager fileManager;
    private File file;

    public LoadGradebookCommand(MainController mainController, StatisticsPane statisticsPane, FileManager fileManager, File file) {
        this.mainController = mainController;
        this.fileManager = fileManager;
        this.file = file;

        this.statsPaneShowing = statisticsPane != null;
    }

    @Override
    public void execute() {
        if (statsPaneShowing) {
            mainController.closeStatisticsPane();
        }
        fileManager = new FileManager();
        fileManager.load(file, mainController);

        mainController.disableSaveMenuItem(true);
        mainController.setupStatisticsLabels();
    }

}
