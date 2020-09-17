package net.mjduncan.gradebook.commands.standard_commands;

import javafx.collections.ObservableList;
import net.mjduncan.gradebook.MainController;
import net.mjduncan.gradebook.model.Student;
import net.mjduncan.gradebook.tools.CourseManager;

public class AddNewStudentCommand implements StandardCommand {

    private MainController mainController;
    private CourseManager courseManager;
    private ObservableList<Student> tableItems;
    private Student student;

    public AddNewStudentCommand(MainController mainController, Student student) {
        this.mainController = mainController;
        this.courseManager = mainController.getCourseManager();
        this.tableItems = mainController.getTable().getItems();
        this.student = student;
    }

    @Override
    public void execute() {
        courseManager.newStudent(student);
        mainController.newBlankStudent();
    }

    @Override
    public void undo() {
        courseManager.removeStudent(student);
        tableItems.remove(student);
    }

    @Override
    public void redo() {
        courseManager.newStudent(student);
        tableItems.add(tableItems.size() - 1, student);
    }
}
