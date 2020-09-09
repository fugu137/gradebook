package gradebook.commands.refresh_commands;

import gradebook.MainController;

public class NewGradebookCommand implements RefreshCommand {

    MainController mainController;

    public NewGradebookCommand(MainController mainController) {
        this.mainController = mainController;
    }

    @Override
    public void execute() {
        mainController.reset();
        mainController.addBlankStudent();
        mainController.setupStatisticsLabels();
        mainController.clearFileManager();
    }
}
