package net.mjduncan.gradebook.commands.standard_commands.edit_commands;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import net.mjduncan.gradebook.commands.standard_commands.StandardCommand;
import net.mjduncan.gradebook.model.Student;

public class SelectNoneCommand implements StandardCommand {

    private final TableView<Student> table;
    private final ObservableList<Student> selectedStudentsCopy;

    public SelectNoneCommand(TableView<Student> table) {
        this.table = table;
        this.selectedStudentsCopy = FXCollections.observableArrayList();
    }

    @Override
    public void execute() {
        selectedStudentsCopy.addAll(table.getSelectionModel().getSelectedItems());

        table.getSelectionModel().clearSelection();
    }

    @Override
    public void undo() {
        selectedStudentsCopy.forEach(s -> table.getSelectionModel().select(s));
    }

    @Override
    public void redo() {
        table.getSelectionModel().clearSelection();
    }
}
