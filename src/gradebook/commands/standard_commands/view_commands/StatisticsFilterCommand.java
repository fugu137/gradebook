package gradebook.commands.standard_commands.view_commands;

import gradebook.MainController;
import gradebook.commands.standard_commands.StandardCommand;
import gradebook.model.AssessmentColumn;
import gradebook.model.Student;
import gradebook.model.StudentGroup;
import javafx.scene.control.ComboBox;

public class StatisticsFilterCommand implements StandardCommand {

    private MainController mainController;
    private ComboBox<StudentGroup> groupListBox;
    private ComboBox<AssessmentColumn<Student, ?>> columnListBox;

    private StudentGroup oldGroup;
    private AssessmentColumn<Student, ?> oldColumn;

    private StudentGroup newGroup;
    private AssessmentColumn<Student, ?> newColumn;


    public StatisticsFilterCommand(MainController mainController, ComboBox<StudentGroup> groupListBox, ComboBox<AssessmentColumn<Student, ?>> columnListBox, StudentGroup oldGroup, AssessmentColumn<Student, ?> oldColumn) {
        this.mainController = mainController;
        this.groupListBox = groupListBox;
        this.columnListBox = columnListBox;

        this.oldGroup = oldGroup;
        this.oldColumn = oldColumn;

        this.newGroup = groupListBox.getSelectionModel().getSelectedItem();
        this.newColumn = columnListBox.getSelectionModel().getSelectedItem();
    }


    @Override
    public void execute() {
        mainController.setupStatisticsLabels();
        mainController.updateBarChart();
    }

    @Override
    public void undo() {
       removeListenersAndSetValues(oldGroup, oldColumn);
    }

    @Override
    public void redo() {
        removeListenersAndSetValues(newGroup, newColumn);
    }

    private void removeListenersAndSetValues(StudentGroup group, AssessmentColumn<Student, ?> column) {
        mainController.removeColumnsComboBoxListener();
        mainController.removeStatisticsClassBoxListener();

        groupListBox.getSelectionModel().select(group);
        columnListBox.getSelectionModel().select(column);

        execute();

        mainController.setColumnsComboBoxListener();
        mainController.setStatisticsClassBoxListener();
    }

}
