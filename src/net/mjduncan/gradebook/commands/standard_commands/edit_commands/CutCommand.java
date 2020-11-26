package net.mjduncan.gradebook.commands.standard_commands.edit_commands;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import net.mjduncan.gradebook.MainController;
import net.mjduncan.gradebook.commands.standard_commands.StandardCommand;
import net.mjduncan.gradebook.model.Student;
import net.mjduncan.gradebook.tools.CourseManager;

public class CutCommand implements StandardCommand {

    private final MainController mainController;
    private final CourseManager courseManager;
    private final TableView<Student> table;
    private final ObservableList<Student> clipBoardCopy;
    private final ObservableList<Student> tableItemsCopy;

    public CutCommand(MainController mainController) {
        this.mainController = mainController;
        this.courseManager = mainController.getCourseManager();
        this.table = mainController.getTable();

        this.clipBoardCopy = FXCollections.observableArrayList();
        this.tableItemsCopy = FXCollections.observableArrayList();

        tableItemsCopy.addAll(table.getItems());
    }

    @Override
    public void execute() {
        mainController.getClipBoardStudents().clear();
        ObservableList<Student> toCut = table.getSelectionModel().getSelectedItems();
        mainController.getClipBoardStudents().addAll(toCut);
        clipBoardCopy.addAll(toCut);

        for (Student s : toCut) {
            courseManager.removeStudent(s);
        }
        table.getItems().removeAll(toCut);
    }

    @Override
    public void undo() {
        courseManager.newStudents(clipBoardCopy);
        table.getItems().clear();
        table.getItems().addAll(tableItemsCopy);
    }

    @Override
    public void redo() {
        for (Student s : clipBoardCopy) {
            courseManager.removeStudent(s);
        }
        table.getItems().removeAll(clipBoardCopy);
    }
}
