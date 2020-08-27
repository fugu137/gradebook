package gradebook.commands;

import gradebook.MainController;
import javafx.scene.control.CheckBox;

public class ShowHideAssessmentCommand implements UserCommand {

    MainController mainController;
    CheckBox checkBox;
    boolean checked;

    public ShowHideAssessmentCommand(MainController mainController, CheckBox checkBox) {
        this.mainController = mainController;
        this.checkBox = checkBox;
    }

    @Override
    public void execute() {
        checked = checkBox.isSelected();
        mainController.showHideAssessmentColumns(checkBox, true);
    }

    @Override
    public void undo() {

    }

    @Override
    public void redo() {

    }
}
