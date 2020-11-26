package net.mjduncan.gradebook.commands.standard_commands.view_commands;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import net.mjduncan.gradebook.MainController;
import net.mjduncan.gradebook.commands.standard_commands.StandardCommand;
import net.mjduncan.gradebook.model.Student;
import net.mjduncan.gradebook.model.StudentGroup;

class FilterByGroupSubCommand implements StandardCommand {

    private final TableView<Student> table;
    private final Student blankStudent;
    private final StudentGroup selectedGroup;
    private final ObservableList<Student> tableStudentsCopy;

    FilterByGroupSubCommand(MainController mainController, ComboBox<StudentGroup>comboBox) {
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
