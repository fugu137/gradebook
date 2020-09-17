package net.mjduncan.gradebook.commands.standard_commands.edit_commands;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import net.mjduncan.gradebook.MainController;
import net.mjduncan.gradebook.commands.standard_commands.StandardCommand;
import net.mjduncan.gradebook.model.Student;
import net.mjduncan.gradebook.tools.CourseManager;

public class PasteCommand implements StandardCommand {

    private CourseManager courseManager;
    private TableView<Student> table;
    private ObservableList<Student> clipBoardCopy;
    private int index;

    public PasteCommand(MainController mainController) {
        this.courseManager = mainController.getCourseManager();
        this.table = mainController.getTable();
        this.clipBoardCopy = FXCollections.observableArrayList();
        this.index = table.getSelectionModel().getSelectedIndices().get(0);

        clipBoardCopy.addAll(mainController.getClipBoardStudents());
    }

    @Override
    public void execute() {
        courseManager.reAddAllStudentsAt(index, clipBoardCopy);
        table.getItems().addAll(index, clipBoardCopy);
    }

    @Override
    public void undo() {
        clipBoardCopy.forEach(s -> courseManager.removeStudent(s));
        table.getItems().removeAll(clipBoardCopy);
    }

    @Override
    public void redo() {
        execute();
    }
}
