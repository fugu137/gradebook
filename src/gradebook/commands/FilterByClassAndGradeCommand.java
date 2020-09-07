package gradebook.commands;

import gradebook.MainController;
import gradebook.enums.Grade;
import gradebook.model.StudentGroup;
import javafx.scene.control.ComboBox;

public class FilterByClassAndGradeCommand implements UserCommand {

    private MainController mainController;
    private ComboBox<StudentGroup> classListBox;
    private ComboBox<Grade> gradeListBox;

    private UserCommand groupFilterCommand;
    private UserCommand gradeFilterCommand;

    public FilterByClassAndGradeCommand(MainController mainController, ComboBox<StudentGroup> classListBox, ComboBox<Grade> gradeListBox) {
        this.mainController = mainController;
        this.classListBox = classListBox;
        this.gradeListBox = gradeListBox;
    }

    @Override
    public void execute() {
        groupFilterCommand = new FilterByGroupCommand(mainController, classListBox);
        groupFilterCommand.execute();

        gradeFilterCommand = new FilterByGradeCommand(mainController, classListBox, gradeListBox);
        gradeFilterCommand.execute();
    }

    @Override
    public void undo() {

    }

    @Override
    public void redo() {

    }
}
