package net.mjduncan.gradebook.commands.standard_commands.edit_commands;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import net.mjduncan.gradebook.MainController;
import net.mjduncan.gradebook.commands.standard_commands.StandardCommand;
import net.mjduncan.gradebook.model.Student;
import net.mjduncan.gradebook.tools.CourseManager;
import net.mjduncan.gradebook.tools.StudentCloner;

import java.util.List;

public class DeleteCommand implements StandardCommand {

    private final MainController mainController;
    private final CourseManager courseManager;
    private final TableView<Student> table;
    private final ObservableList<Student> selectedCopy;
    private ObservableList<Student> selected;

    public DeleteCommand(MainController mainController, List<Student> selected) {
        this.mainController = mainController;
        this.courseManager = mainController.getCourseManager();
        this.table = mainController.getTable();
        this.selectedCopy = FXCollections.observableArrayList();
        this.selected = FXCollections.observableArrayList(selected);
    }

    @Override
    public void execute() {
        ObservableList<Student> studentClones = StudentCloner.run(this.selected);
        selectedCopy.clear();
        selectedCopy.addAll(studentClones);

        for (Student s : selected) {
            courseManager.removeStudent(s);
        }
        table.getItems().removeAll(selected);

        if (table.getItems().size() < 1) {
            mainController.addBlankStudent();
        }
    }

    @Override
    public void undo() {
        courseManager.newStudents(selectedCopy);
        table.getItems().clear();
        table.getItems().addAll(courseManager.getAllStudents());
        selected.clear();
        selected.addAll(selectedCopy);
    }

    @Override
    public void redo() {
        execute();
    }
}
