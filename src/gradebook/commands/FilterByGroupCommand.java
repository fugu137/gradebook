package gradebook.commands;

import gradebook.MainController;
import gradebook.model.Student;
import gradebook.model.StudentGroup;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;

class FilterByGroupCommand implements UserCommand {

    private TableView<Student> table;
    private Student blankStudent;
    private StudentGroup selectedGroup;
    private ObservableList<Student> tableStudentsCopy;

    public FilterByGroupCommand(MainController mainController, ComboBox<StudentGroup>comboBox) {
        this.table = mainController.getTable();
        this.blankStudent = mainController.getBlankStudent();
        this.selectedGroup = comboBox.getSelectionModel().getSelectedItem();
        this.tableStudentsCopy = FXCollections.observableArrayList();

        tableStudentsCopy.addAll(table.getItems());
    }

    @Override
    public void execute() {
        if (selectedGroup != null) {
            table.getItems().clear();
            table.getItems().addAll(selectedGroup.getStudents());
            table.getItems().add(blankStudent);
        }
    }

    @Override
    public void undo() {
        table.getItems().clear();
        table.getItems().addAll(tableStudentsCopy);
    }

    @Override
    public void redo() {
        execute();
    }
}
