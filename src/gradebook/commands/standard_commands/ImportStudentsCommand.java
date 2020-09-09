package gradebook.commands.standard_commands;

import gradebook.MainController;
import gradebook.model.Student;
import gradebook.tools.CourseManager;
import gradebook.tools.FileChooserWindow;
import gradebook.tools.StudentImporter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import javafx.stage.Window;

import java.io.File;
import java.util.List;

public class ImportStudentsCommand implements StandardCommand {

    private CourseManager courseManager;
    private TableView<Student> table;
    private Student blankStudent;
    private Window window;
    private ObservableList<Student> tableStudentsCopy;
    private ObservableList<Student> tableStudentsForRedo;

    public ImportStudentsCommand(MainController mainController, Window window) {
        this.courseManager = mainController.getCourseManager();
        this.table = mainController.getTable();
        this.blankStudent = mainController.getBlankStudent();
        this.window = window;
        this.tableStudentsCopy = FXCollections.observableArrayList();
    }

    @Override
    public void execute() {
        tableStudentsCopy.addAll(table.getItems());
        tableStudentsCopy.remove(blankStudent);

        List<File> files = FileChooserWindow.displayImportWindow(window, "Choose a file to import from", true);

        for (File file : files) {
            ObservableList<Student> students = StudentImporter.importStudents(file);
            courseManager.newStudents(students);

            table.getItems().addAll(students);
            table.sort();
        }
    }

    @Override
    public void undo() {
        tableStudentsForRedo = FXCollections.observableArrayList();
        tableStudentsForRedo.addAll(table.getItems());
        tableStudentsForRedo.remove(blankStudent);

        courseManager.clear();
        table.getItems().clear();

        courseManager.newStudents(tableStudentsCopy);

        table.getItems().addAll(tableStudentsCopy);
        table.getItems().add(blankStudent);
    }

    @Override
    public void redo() {
        courseManager.clear();
        courseManager.newStudents(tableStudentsForRedo);

        table.getItems().clear();
        table.getItems().addAll(tableStudentsForRedo);
        table.getItems().add(blankStudent);
    }
}
