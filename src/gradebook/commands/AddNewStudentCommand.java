package gradebook.commands;

import gradebook.MainController;
import gradebook.model.CourseManager;
import gradebook.model.Student;
import javafx.collections.ObservableList;

public class AddNewStudentCommand implements UserCommand{

    private MainController mainController;
    private Student student;

    public AddNewStudentCommand(MainController mainController, Student student) {
        this.mainController = mainController;
        this.student = student;
    }

    @Override
    public void execute() {
        CourseManager courseManager = mainController.getCourseManager();
        courseManager.newStudent(student);
        mainController.newBlankStudent();
    }

    @Override
    public void undo() {
        CourseManager courseManager = mainController.getCourseManager();
        courseManager.removeStudent(student);
        mainController.getTable().getItems().remove(student);
    }

    @Override
    public void redo() {
        CourseManager courseManager = mainController.getCourseManager();
        ObservableList<Student> tableItems = mainController.getTable().getItems();

        courseManager.newStudent(student);
        tableItems.add(tableItems.size() - 1, student);
    }
}
