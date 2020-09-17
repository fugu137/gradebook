package net.mjduncan.gradebook.commands.standard_commands.edit_commands;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import net.mjduncan.gradebook.MainController;
import net.mjduncan.gradebook.commands.standard_commands.StandardCommand;
import net.mjduncan.gradebook.model.Student;
import net.mjduncan.gradebook.tools.CourseManager;

import java.util.List;

public class DeleteCommand implements StandardCommand {

    private CourseManager courseManager;
    private TableView<Student> table;
    private ObservableList<Student> tableItemsCopy;
    private ObservableList<Student> selected;

    public DeleteCommand(MainController mainController, List<Student> selected) {
        this.courseManager = mainController.getCourseManager();
        this.table = mainController.getTable();
        this.tableItemsCopy = FXCollections.observableArrayList();
        this.selected = FXCollections.observableArrayList(selected);

        tableItemsCopy.addAll(table.getItems());
    }

    @Override
    public void execute() {
        for (Student s : selected) {
            courseManager.removeStudent(s);
        }
        table.getItems().removeAll(selected);
    }

    @Override
    public void undo() {
        courseManager.newStudents(selected);
        table.getItems().clear();
        table.getItems().addAll(tableItemsCopy);
    }

    @Override
    public void redo() {
        execute();
    }
}
