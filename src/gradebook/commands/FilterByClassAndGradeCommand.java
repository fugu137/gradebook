package gradebook.commands;

import gradebook.MainController;
import gradebook.enums.Grade;
import gradebook.model.StudentGroup;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ComboBox;

public class FilterByClassAndGradeCommand implements UserCommand {

    private MainController mainController;
    private ComboBox<StudentGroup> classListBox;
    private ComboBox<Grade> gradeListBox;

    private StudentGroup selectedGroupCopy;
    private Grade selectedGradeCopy;

    private UserCommand groupFilterCommand;
    private UserCommand gradeFilterCommand;

    public FilterByClassAndGradeCommand(MainController mainController, ComboBox<StudentGroup> classListBox, ComboBox<Grade> gradeListBox) {
        this.mainController = mainController;
        this.classListBox = classListBox;
        this.gradeListBox = gradeListBox;

        selectedGroupCopy = classListBox.getSelectionModel().getSelectedItem();
        selectedGradeCopy = gradeListBox.getSelectionModel().getSelectedItem();
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
        gradeFilterCommand.undo();
        groupFilterCommand.undo();
    }

    @Override
    public void redo() {
        EventHandler<ActionEvent> classBoxHandler = classListBox.getOnAction();
        classListBox.setOnAction(null);
        EventHandler<ActionEvent> gradeBoxHandler = gradeListBox.getOnAction();
        gradeListBox.setOnAction(null);

        classListBox.getSelectionModel().select(selectedGroupCopy);
        gradeListBox.getSelectionModel().select(selectedGradeCopy);

        classListBox.setOnAction(classBoxHandler);
        gradeListBox.setOnAction(gradeBoxHandler);
        execute();
    }
}
