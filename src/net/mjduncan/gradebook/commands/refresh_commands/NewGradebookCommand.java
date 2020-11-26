package net.mjduncan.gradebook.commands.refresh_commands;

import net.mjduncan.gradebook.MainController;

public class NewGradebookCommand implements RefreshCommand {

    MainController mainController;

    public NewGradebookCommand(MainController mainController) {
        this.mainController = mainController;
    }

    @Override
    public void execute() {
        mainController.reset();
//        mainController.addBlankStudent();
//        mainController.setupStatisticsLabels();
        mainController.clearFileManager();
        mainController.initialize();
    }
}
