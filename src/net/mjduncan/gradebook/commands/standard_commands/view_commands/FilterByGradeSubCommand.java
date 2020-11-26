package net.mjduncan.gradebook.commands.standard_commands.view_commands;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import net.mjduncan.gradebook.MainController;
import net.mjduncan.gradebook.commands.standard_commands.StandardCommand;
import net.mjduncan.gradebook.enums.Grade;
import net.mjduncan.gradebook.model.Student;
import net.mjduncan.gradebook.model.StudentGroup;

class FilterByGradeSubCommand implements StandardCommand {

    private final TableView<Student> table;
    private final StudentGroup selectedGroup;
    private final Grade selectedGrade;
    private final ObservableList<Student> tableStudentsCopy;

    FilterByGradeSubCommand(MainController mainController, ComboBox<StudentGroup> classListBox, ComboBox<Grade> gradeListBox) {
        this.table = mainController.getTable();
        this.selectedGroup = classListBox.getSelectionModel().getSelectedItem();
        this.selectedGrade = gradeListBox.getSelectionModel().getSelectedItem();
        this.tableStudentsCopy = FXCollections.observableArrayList();

        tableStudentsCopy.addAll(table.getItems());
    }

    @Override
    public void execute() {

        if (selectedGrade != null && selectedGroup != null) {
            switch (selectedGrade) {
                case ANY:
                    break;
                case HD:
                    table.getItems().removeIf(s -> !selectedGroup.getTotalStatistics().getHDStudents().contains(s));
                    break;
                case D:
                    table.getItems().removeIf(s -> !selectedGroup.getTotalStatistics().getDStudents().contains(s));
                    break;
                case CR:
                    table.getItems().removeIf(s -> !selectedGroup.getTotalStatistics().getCRStudents().contains(s));
                    break;
                case P:
                    table.getItems().removeIf(s -> !selectedGroup.getTotalStatistics().getPStudents().contains(s));
                    break;
                case F:
                    table.getItems().removeIf(s -> !selectedGroup.getTotalStatistics().getFStudents().contains(s));
                    break;
            }
            table.sort();
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
