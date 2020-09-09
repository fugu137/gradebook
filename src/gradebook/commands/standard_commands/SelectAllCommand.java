package gradebook.commands.standard_commands;

import gradebook.model.Student;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;

public class SelectAllCommand implements StandardCommand {

    private TableView<Student> table;
    private ObservableList<Student> selectedStudentsCopy;

    public SelectAllCommand(TableView<Student> table) {
        this.table = table;
        selectedStudentsCopy = FXCollections.observableArrayList();
    }

    @Override
    public void execute() {
        selectedStudentsCopy.addAll(table.getSelectionModel().getSelectedItems());

        table.requestFocus();
        table.getSelectionModel().selectAll();
        table.getSelectionModel().clearSelection(table.getItems().size() - 1);
    }

    @Override
    public void undo() {
        table.getSelectionModel().clearSelection();
        selectedStudentsCopy.forEach(s -> table.getSelectionModel().select(s));
    }

    @Override
    public void redo() {
        selectedStudentsCopy.clear();
        execute();
    }
}
